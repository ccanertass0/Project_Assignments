#include "ImageProcessor.h"

ImageProcessor::ImageProcessor() = default;

ImageProcessor::~ImageProcessor() = default;


std::string ImageProcessor::decodeHiddenMessage(const ImageMatrix &img) {
	ImageSharpening imageSharpening;
	ImageMatrix sharpImg = imageSharpening.sharpen(img, 2);

	EdgeDetector edgeDetector;
	std::vector<std::pair<int, int>> edgePixels = edgeDetector.detectEdges(sharpImg);

	DecodeMessage decodeMessage;
	std::string decodedMessage = decodeMessage.decodeFromImage(sharpImg, edgePixels);

	return decodedMessage;
}

ImageMatrix ImageProcessor::encodeHiddenMessage(const ImageMatrix &img, const std::string &message) {
	ImageSharpening imageSharpening;
	ImageMatrix sharpImg = imageSharpening.sharpen(img, 2);

	EdgeDetector edgeDetector;
	std::vector<std::pair<int, int>> edgePixels = edgeDetector.detectEdges(sharpImg);

	EncodeMessage encodeMessage;
	ImageMatrix encodedImage = encodeMessage.encodeMessageToImage(img, message, edgePixels);

	return encodedImage;
}

