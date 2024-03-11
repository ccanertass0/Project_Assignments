#include "EncodeMessage.h"
#include <cmath>
#include <stdexcept>
#include <iostream>
#include <typeinfo>

// Default Constructor
EncodeMessage::EncodeMessage() {

}

// Destructor
EncodeMessage::~EncodeMessage() {

}

// Function to encode a message into an image matrix
ImageMatrix EncodeMessage::encodeMessageToImage(const ImageMatrix &img, const std::string &message, const std::vector<std::pair<int, int>>& positions) {


	int asciiDecimal = {-1};
	std::string newAsciiMessage;
	for(int ind {0}; ind < message.length(); ind++){

		if(isPrime(ind)){
			asciiDecimal = static_cast<int>(message[ind]); //getting the decimal vlaue of the char
			asciiDecimal += fibonacci(ind); //updating the ascii value for encoding
			asciiDecimal = asciiDecimal > 126 ? 126 : (asciiDecimal < 33 ? asciiDecimal + 33 : asciiDecimal) ;  // clipping
			//now we got the new ascii decimal value
			char newChar = static_cast<char>(asciiDecimal);
			newAsciiMessage += newChar;

		}else{
			newAsciiMessage += message[ind];
		}
	}	//we have updated the ascii decimals of the chars at prime indexes,


	//now time for shifting
	int shiftVal = (int) (message.length() / 2);

	std::string stringToBeShifted = newAsciiMessage.substr(newAsciiMessage.length() - shiftVal, shiftVal);

	newAsciiMessage.replace((message.length() - shiftVal), shiftVal, "");
	newAsciiMessage = stringToBeShifted + newAsciiMessage;  	//we circular shifted the message by message.length()/2


	int asciiDec {-1};
	std::string binaryStr;
	for(int index {0}; index < newAsciiMessage.length(); index++){
		asciiDec = (int) newAsciiMessage[index];
		binaryStr += decTobin(asciiDec);
	}
	//we converted this ascii message into a binary message





	ImageMatrix resultImg (img);


	//Now it is time for message embedding
	int ind{0};
	std:: cout << binaryStr << " len = " << binaryStr.length() << "\n";
	for(std::pair<int, int> pixel : positions){  // the number of pixels equals binarStr.length().
		int pixelVal {-1};
		int newlsb {2};
		if(binaryStr[ind] == '1'){

			pixelVal = resultImg.get_data(pixel.first, pixel.second);
			newlsb = 1;
			pixelVal = (pixelVal & 0xFE) | newlsb;
			resultImg.setData(pixel.first, pixel.second, pixelVal);

		}else if(binaryStr[ind] == '0'){

			pixelVal = resultImg.get_data(pixel.first, pixel.second);
			newlsb = 0;
			pixelVal = (pixelVal & 0xFE) | newlsb;
			resultImg.setData(pixel.first, pixel.second, pixelVal);

		}
		ind++;
	}
	return resultImg;

}

int EncodeMessage::fibonacci(int n) {
	if(n == 0){
		return 0;
	}else if(n == 1){
		return 1;
	}

	return fibonacci(n-1) + fibonacci(n - 2);
}

bool EncodeMessage::isPrime(int num) {
	if(num <= 0 || num == 1){
		return false;
	}
	for(int k {2}; k < num; k++){
		if(num % k == 0){
			return false;
		}
	}
	return true;
}

//this function is for converting ascii characters into 7bit binary strings
//the argument is the decimal value of the char(referring to ascii)
std::string EncodeMessage::decTobin(int decimal) {

	std::string result;
	if(decimal < 33 || decimal > 126){
		throw std::runtime_error("Wrong ascii decimal value");
	}

	while (decimal != 0){
		result = std::to_string(decimal % 2) + result;
		decimal = decimal / 2;
	}

	while (result.length() != 7){
		result = "0" + result;  //padding with leading zeros if necessary
	}



	return result;
}