#ifndef PA2_BLOCK_H
#define PA2_BLOCK_H
#include <iostream>
#include <vector>
using namespace std;

class Block {
public:

    vector<vector<bool>> shape; // Two-dimensional vector corresponding to the block's shape
    Block * right_rotation = nullptr; // Pointer to the block's clockwise neighbor block (its right rotation)
    Block * left_rotation = nullptr; // Pointer to the block's counter-clockwise neighbor block (its left rotation)
    Block * next_block = nullptr; // Pointer to the next block to appear in the game

    bool operator==(const Block& other) const {
        // TODO: Overload the == operator to compare two blocks based on their shapes
		if(this -> shape == other.shape){
			return true;
		}
        return false;
    }

    bool operator!=(const Block& other) const {
        // TODO: Overload the != operator to compare two blocks based on their shapes
		if(this -> shape != other.shape){
			return true;
		}
        return false;
    }

	Block* rotate(bool isRight){
		Block* rotatedPtr = new Block;
		int newRowSize = shape[0].size();  //m
		int newColSize = shape.size();    //n
		for(int row {0}; row < newRowSize; row++){  //shape[0].size() equals the column number of the original.
			vector<bool> rotatedRow;	// so if the original grid is (n x m) , our rotated grid will be (m x n)
			for(int col {0}; col < newColSize; col++){ // shape.size() equals the row number of the original grid.
				if(isRight) {
					rotatedRow.push_back(shape[newColSize - 1 - col][row]);
				}else{
					rotatedRow.push_back(shape[col][newRowSize - 1 - row]);
				}
			}
			rotatedPtr -> shape.push_back(rotatedRow);
		}


		rotatedPtr -> next_block = this -> next_block; // so essential.


		if(isRight){
			rotatedPtr -> left_rotation = this;
		}else{
			rotatedPtr -> right_rotation = this;
		}
		return rotatedPtr;
	}

};


#endif //PA2_BLOCK_H
