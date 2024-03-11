#include "ImageMatrix.h"
#include <iostream>
using namespace std;

// Default constructor
ImageMatrix::ImageMatrix(){
	data = nullptr;
	height = 0;
	width = 0;
} //DONE


// Parameterized constructor for creating a blank image of given size
ImageMatrix::ImageMatrix(int imgHeight, int imgWidth) : height(imgHeight), width(imgWidth) {
	data = new double*[height];
	for(int row {0}; row < height; row++){
		data[row] = new double [width];
		for(int col {0}; col < width; col++){
			data[row][col] = 0.0;
		}
	}
} //DONE

// Parameterized constructor for loading image from file. PROVIDED FOR YOUR CONVENIENCE
ImageMatrix::ImageMatrix(const std::string &filepath) {
    // Create an ImageLoader object and load the image
    ImageLoader imageLoader(filepath);

    // Get the dimensions of the loaded image
    height = imageLoader.getHeight();
    width = imageLoader.getWidth();

    // Allocate memory for the matrix
    data = new double*[height];
    for (int i = 0; i < height; ++i) {
        data[i] = new double[width];
    }

    // Copy data from imageLoader to data
    double** imageData = imageLoader.getImageData();
    for (int i = 0; i < height; ++i) {
        for (int j = 0; j < width; j++) {
            data[i][j] = imageData[i][j];
        }
    }
} //DONE



// Destructor
ImageMatrix::~ImageMatrix() {
	if(data != nullptr){
		for(int row {0}; row < height; row++){
			delete[] data[row];
		}
		delete[] data;
	}
}//DONE

// Parameterized constructor - direct initialization with 2D matrix
ImageMatrix::ImageMatrix(const double** inputMatrix, int imgHeight, int imgWidth){
	height = imgHeight;
	width = imgWidth;

	data = new double* [height];    //taking the image into the data variable
	for(int row {0}; row < height; row++){
		data[row] = new double [width];
		for(int col {0}; col < width; col++){
			data[row][col] = inputMatrix[row][col];
		}
	}
}//DONE

// Copy constructor
ImageMatrix::ImageMatrix(const ImageMatrix &other) : height(other.height), width(other.width){
	data = new double* [height];
	for(int row {0}; row < height; row++){
		data[row] = new double [width];
		for(int col {0}; col < width; col++){
			data[row][col] = other.data[row][col];
		}
	}

}//DONE

// Copy assignment operator
ImageMatrix& ImageMatrix::operator=(const ImageMatrix &other) {
	if (this == &other) {
		return *this; // self-assignment check
	}

//	//Deallocate old memory   //LOOK AT HERE AGAIN
	if (data != nullptr) {
		for (int row {0}; row < height; row) {
			delete[] data[row];
		}
		delete[] data;
	}

	height = other.height;
	width = other.width;

	data = new double* [height];
	for(int row {0}; row < height; row++){
		data[row] = new double [width];
		for(int col {0}; col < width; col++){
			data[row][col] = other.data[row][col];
		}
	}

	return *this;


}//DONE



// Overloaded operators

// Overloaded operator + to add two matrices
ImageMatrix ImageMatrix::operator+(const ImageMatrix &other) const {
	if(height == other.height && width == other.width){ //their size are equal (both vertically and horizontally)
		double** newData = new double* [height];
		for(int row {0}; row < height; row++){
			newData[row] = new double[width];
			for(int col {0}; col < width; col++){
				newData[row][col] = data[row][col] + other.data[row][col];

			}
		}

		ImageMatrix sumImg{(const double**)newData, height, width};
		//MAYBE WE SHOULD DO delete *this; HERE???
		for(int row {0}; row < height; row++){
			delete[] newData[row];
		}
		delete[] newData;

		return sumImg;
	}else{
		throw std::runtime_error("Image matrixes are not equal");
	}

} //DONE

// Overloaded operator - to subtract two matrices
ImageMatrix ImageMatrix::operator-(const ImageMatrix &other) const {
	if(height == other.height && width == other.width){
		ImageMatrix resultImg {other.height, other.width};
		for(int row {0}; row < height; row++){
			for(int col {0}; col < width; col++){
				double newVal = data[row][col] - other.get_data(row, col);
				resultImg.setData(row, col, newVal);
			}
		}
		return resultImg;

	}else{
		throw std::runtime_error("Matrix sizes are not equal.");
	}
} //DONE

// Overloaded operator * to multiply a matrix with a scalar
ImageMatrix ImageMatrix::operator*(const double &scalar) const {
	if(data != nullptr){
		double** newData = new double* [height];  //first copying the data
		for (int row{0}; row < height; row++) {
			newData[row] = new double [width];
			for (int col{0}; col < width; col++) {
				newData[row][col] = data[row][col]*scalar;
			}
		}

		ImageMatrix multipliedImg {(const double**) newData, height, width};

		for(int row {0}; row < multipliedImg.get_height(); row++){
			delete newData[row];
		}
		delete [] newData;

		return multipliedImg;
	}else{
		throw std::runtime_error("data is a nullptr");
	}
} //DONE


// Getter function to access the data in the matrix
double** ImageMatrix::get_data() const {
	return data;
} //DONE

// Getter function to access the data at the index (i, j)
double ImageMatrix::get_data(int i, int j) const {
	if(data){ //not nullptr
		return data[i][j];
	}else{
		throw std::runtime_error("data is nullptr");
	}
} //DONE

int ImageMatrix::get_height() const {  //gets height of the image
	return height;
}

int ImageMatrix::get_width() const { //gets width of the image
	return width;
}


void ImageMatrix::setData(int row, int col, double val) {
	data[row][col] = val;
}


void ImageMatrix::printMatrix() {
	for(int row{0}; row < height; row++){
		for(int col{0}; col < width; col++){
			cout << data[row][col] << " ";
		}
		cout << "\n";
	}
	cout << "\n";
} //DONE

