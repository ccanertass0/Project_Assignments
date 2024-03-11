// EdgeDetector.cpp

#include "EdgeDetector.h"
#include <cmath>
using namespace std;

// Default constructor
EdgeDetector::EdgeDetector() {
	sobelX = new double* [3]{ // for detecting horizontal edges
			new double[3]{-1.0, 0.0, 1.0},
			new double[3]{-2.0, 0.0, 2.0},
			new double[3]{-1.0, 0.0, 1.0}
	};

	sobelY = new double* [3] {
			new double[3]{-1.0, -2.0, -1.0},
			new double[3]{-0.0, 0.0, 0.0},
			new double[3]{1.0, 2.0, 1.0}
	};

	threshold = 0;  // it will stay as 0 untill detectEdges function is executed.



}

// Destructor
EdgeDetector::~EdgeDetector() {
	for(int row {0}; row < 3; row++){ //deallocating memory
		delete [] sobelX[row];
		delete [] sobelY[row];
	}
	delete [] sobelX;
	delete [] sobelY;

}

// Detect Edges using the given algorithm
std::vector<std::pair<int, int>> EdgeDetector::detectEdges(const ImageMatrix& input_image) {
	Convolution sobelXConvolution {sobelX, 3, 3, 1, true};
	ImageMatrix imageX = sobelXConvolution.convolve(input_image);

	Convolution sobelYConvolution {sobelY, 3, 3, 1, true};
	ImageMatrix imageY = sobelYConvolution.convolve(input_image);


	//below we are calculating the average of gradient magnitudes which will be the threshold.
	for(int row {0}; row < input_image.get_height(); row++){
		for(int col {0}; col < input_image.get_width(); col++){
			threshold += sqrt(pow(imageX.get_data(row, col), 2) + pow(imageY.get_data(row, col), 2));
		}
	}
	threshold /= input_image.get_height() * input_image.get_width();

	vector<pair<int, int>> edgePixels;
	for(int row {0}; row < input_image.get_height(); row++){
		for(int col {0}; col < input_image.get_width(); col++){
			if (sqrt(pow(imageX.get_data(row, col), 2) + pow(imageY.get_data(row, col), 2)) > threshold){
				edgePixels.push_back(make_pair(row, col));
			}
		}
	}

	return edgePixels;
}

