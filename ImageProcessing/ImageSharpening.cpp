#include <iostream>
#include "ImageSharpening.h"

// Default constructor
ImageSharpening::ImageSharpening() {
	blurringKernel = new double* [3]{
		new double [3] {1.0, 1.0, 1.0},
		new double [3] {1.0, 1.0, 1.0},
		new double [3] {1.0, 1.0, 1.0}
	};
	kerHeight = 3;
	kerWidth = 3;
	k = 2.0;
}



ImageSharpening::~ImageSharpening(){
	for(int row {0}; row < 3; row++){ //assumed blurring kernel is always 3x3 in this assignment
		delete [] blurringKernel[row];
	}
	delete [] blurringKernel;
}

ImageMatrix ImageSharpening::sharpen(const ImageMatrix& input_image, double k) {
	Convolution blurringConvolution {};  //automatically set to desired values (padding true, stride = 1 etc.)
	ImageMatrix blurredImg = blurringConvolution.convolve(input_image);
	this -> k = k;
	ImageMatrix sharpImg = input_image + (input_image - blurredImg) * (this -> k);

	for(int row {0}; row < sharpImg.get_height(); row++){
		for(int col {0}; col < sharpImg.get_width(); col++){
			if(sharpImg.get_data(row, col) < 0){
				sharpImg.setData(row, col, 0);
			}else if(sharpImg.get_data(row, col) > 255){
				sharpImg.setData(row, col, 255);
			}
		}
	}


	return sharpImg;
}
