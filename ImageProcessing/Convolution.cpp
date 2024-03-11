#include <iostream>

#include "Convolution.h"

// Default constructor 
Convolution::Convolution() { //automatic blurring setup
	kernelMatrix = new double* [3]{
		new double [3] {1.0, 1.0, 1.0},
		new double [3] {1.0, 1.0, 1.0},
		new double [3] {1.0, 1.0, 1.0}
	};
	kernelHeight = 3;
	kernelWidth = 3;
	stride = 1;
	padding = true;
	kernelMultiplier = 1.0/9;
}

// Parametrized constructor for custom kernel and other parameters
Convolution::Convolution(double** customKernel, int kh, int kw, int stride_val, bool pad){
	kernelHeight = kh;
	kernelWidth = kw;
	stride = stride_val;
	padding = pad;
	kernelMultiplier = 1.0;

	kernelMatrix = new double* [kernelHeight]; //copying the kernel
	for(int row {0}; row < kernelHeight; row++){
		kernelMatrix[row] = new double [kernelWidth];
		for(int col {0}; col < kernelWidth; col++){
			kernelMatrix[row][col] = customKernel[row][col];
		}
	} //completely copied.

}

// Destructor
Convolution::~Convolution() {
	if(kernelMatrix){ // deallocating memory
		for(int row {0}; row < kernelHeight; row++){
			delete [] kernelMatrix[row];
		}
		delete [] kernelMatrix;
	}
}

// Copy constructor
Convolution::Convolution(const Convolution &other) : kernelHeight(other.kernelHeight), kernelWidth(other.kernelWidth),
stride(other.stride), padding(other.padding), kernelMultiplier(other.kernelMultiplier){

	kernelMatrix = new double* [kernelHeight];
	for(int row{0}; row < kernelHeight; row++){
		kernelMatrix[row] = new double [kernelWidth];
		for(int col {0}; col < kernelWidth; col++){
			kernelMatrix[row][col] = other.kernelMatrix[row][col];
		}
	}

}

// Copy assignment operator
Convolution& Convolution::operator=(const Convolution &other) {
	if (this == &other) {
		return *this; // self-assignment check
	}

//	//Deallocate old memory   //LOOK AT HERE AGAIN
//	if (data != nullptr) {
//		for (int row {0}; row < height; row) {
//			delete[] data[row];
//		}
//		delete[] data;
//	}

	kernelHeight = other.kernelHeight;
	kernelWidth = other.kernelWidth;
	stride = other.stride;
	padding = other.padding;
	kernelMultiplier = other.kernelMultiplier;

	kernelMatrix = new double* [kernelHeight];
	for(int row{0}; row < kernelHeight; row++){
		kernelMatrix[row] = new double [kernelWidth];
		for(int col {0}; col < kernelWidth; col++){
			kernelMatrix[row][col] = other.kernelMatrix[row][col];
		}
	}
}


// Convolve Function: Responsible for convolving the input image with a kernel and return the convolved image.
ImageMatrix Convolution::convolve(const ImageMatrix& input_image) const {
	if(input_image.get_data()) {
		int padVal = padding ? 1 : 0;
		int outputHeight = (input_image.get_height() - kernelHeight + (2 * padVal))/stride + 1;
		int outputWidth = (input_image.get_width() - kernelWidth + (2 * padVal))/stride + 1;

		ImageMatrix workOnImg = padding ? pad(input_image) : ImageMatrix(input_image);


		double** outputData = new double* [outputHeight];
		for(int row {0}; row < outputHeight; row++){
			outputData[row] = new double [outputWidth];
		}

		for(int row {0}; (row + (kernelWidth - 1)) < workOnImg.get_height(); row += stride){
			for(int col {0}; (col + (kernelHeight - 1)) < workOnImg.get_width(); col += stride){
				//we placed the kernel, now the kernel multiplication begins.
				double sumOfMatrixMult {0};
				for(int kRow {0}; kRow < kernelHeight; kRow++){
					for(int kCol {0}; kCol < kernelWidth; kCol++){
						sumOfMatrixMult += kernelMatrix[kRow][kCol] * workOnImg.get_data()[row + kRow][col + kCol];
					}
				}

				outputData[row/stride][col/stride] = kernelMultiplier * sumOfMatrixMult;



			}
		}
		//we have got the output data for our outputImg

		ImageMatrix outputImg {(const double**)outputData, outputHeight, outputWidth};  // will use the double** outputData here.

		for(int row{0}; row < outputImg.get_height(); row++){
			delete [] outputData[row];
		}
		delete [] outputData;

		return outputImg;
	}else{
		throw std::runtime_error("Convolution failed due to null data");
	}
}

ImageMatrix Convolution::pad(const ImageMatrix &input_img) const{
	if(input_img.get_data()){
		int paddedHeight = input_img.get_height() + 2; //assumed 0 padding of size or no padding at all
		int paddedWidth = input_img.get_width() + 2;

		double** newData = new double* [paddedHeight]; // this is the new data which is padded.
		for(int row {0}; row < paddedHeight; row++){
			newData[row] = new double [paddedWidth];
			for(int col {0}; col < paddedWidth; col++){
				if(row == 0 || col == 0 || row == (paddedHeight - 1) || col == (paddedWidth - 1)){ //detecting edges
					newData[row][col] = 0.0; //padding
				}else{  //the actual data
					newData[row][col] = input_img.get_data()[row - 1][col - 1];  //copying the actual data
				}
			}
		}
		//now that we have the data we will create an ImageMatrix object.

		ImageMatrix paddedImg {(const double **)newData, paddedHeight, paddedWidth};

		for(int row {0}; row < paddedImg.get_height(); row++){
			delete [] newData[row];
		}
		delete [] newData;

		return paddedImg;

	}else{
		throw std::runtime_error("padding failed due to null data");
	}
} //DONE