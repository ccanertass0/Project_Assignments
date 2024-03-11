// DecodeMessage.cpp

#include "DecodeMessage.h"

// Default constructor
DecodeMessage::DecodeMessage() = default;

// Destructor
DecodeMessage::~DecodeMessage() = default;

std::string DecodeMessage::decodeFromImage(const ImageMatrix &image, const std::vector<std::pair<int, int>> &edgePixels) {
	std::string binaryString;


	for (const auto &pair: edgePixels) {
		int lsb = static_cast<int>(image.get_data(pair.first, pair.second)) & 1;  //getting the lsb
		binaryString += std::to_string(lsb);   //concatenating
	}

	while (binaryString.length() % 7 != 0) {
		binaryString = "0" + binaryString;  //padding with 0s from the front
	}

	std::string decodedMessage;
	for (int i = 0; i < binaryString.length(); i += 7) {
		std::string subStr = binaryString.substr(i, 7);
		int decValue = std::stoi(subStr, nullptr, 2);
		decValue = decValue <= 32 ? (decValue + 33) : (decValue == 127 ? 126 : decValue);
		decodedMessage += static_cast<char>(decValue);
	}

	return decodedMessage;
}