#include <fstream>
#include <sstream>
#include <iostream>
#include "BlockFall.h"
using namespace std;

BlockFall::BlockFall(string grid_file_name, string blocks_file_name, bool gravity_mode_on, const string &leaderboard_file_name,
					 const string &player_name) : gravity_mode_on(
        gravity_mode_on), leaderboard_file_name(leaderboard_file_name), player_name(player_name) {

    initialize_grid(grid_file_name);
    read_blocks(blocks_file_name);
    leaderboard.read_from_file(leaderboard_file_name);  // we created infosOfLeaders and by using this vector .
}

BlockFall::BlockFall(){
}

void BlockFall::read_blocks(const string &input_file) {
// TODO: Read the blocks from the input file and initialize "initial_block" and "active_rotation" member variables
	string blocksPath = input_file;
	ifstream blockFile(blocksPath);
	if(!blockFile.is_open()){  //if we can2t open the .dat file.
		cerr << "Error opening file: " << blocksPath << std::endl;
		return;
	}


	/* This is a vector that consist of pairs. First item is the complete string of the block
	for example if our block is represented in the dat format like this
	[001
	 110
	 010]
	 we will concatanate the rows one by one in order (001 + 110 + 010)
	 so our string is going to be 001110010 in this specific case
	 int part is the number of rows : it will be useful when creating a 2d vector.
	 */
	vector<pair<string, int>> stringBlocks; // has ever block in it including the power shape which is the last block.

	string line;
	int rowNum {0};
	string shapeStr;  // will be string of each pair in the vector stringBlocks
	while (getline(blockFile, line)) {

		if(line[0] == '['){  //adding a new block here.
			if(line[line.length() - 1] == ']'){ // we have row num = 1, column num = n block here. and we rech the END of the block
				shapeStr = line.substr(1, line.length() - 2);
				rowNum = 1;
				stringBlocks.push_back({shapeStr, rowNum}); // pushing our block
				rowNum = 0;
				shapeStr = "";


			}else{  // we have row num >= 2, column num = n block here.  we are still on first row.
				shapeStr += line.substr(1, line.length() - 1);
				rowNum++;
			}
		}else if (line != ""){ //then it is row >= 2 . we are in somewhere of the block
			rowNum++;
			if(line[line.length() - 1] == ']'){ // we reach the END of the block.
				shapeStr += line.substr(0, line.length() - 1);
				stringBlocks.push_back({shapeStr, rowNum});  //pushing our block
				rowNum = 0;
				shapeStr = "";
			}else{ // we are sure row num >= 3.
				shapeStr += line;

			}
		}
	}





	// SINGLY LINKED LIST CREATION
	for(int index {0}; index < stringBlocks.size() - 1; index++){
		pair<string, int> pai = stringBlocks.at(index);
		Block* newBlockPtr = new Block;

		string blockStr = pai.first;
		int row = pai.second; // how many rows we have
		int col = blockStr.length() / row; // how many columns we have

		for(int r {0}; r < row; r++){
			vector<bool> shapeRow; // we are creating the row part.
			for(int c {0}; c < col; c++){
				shapeRow.push_back(blockStr[r*col + c] == '1');
			}
			newBlockPtr -> shape.push_back(shapeRow);
		}

		//we have a new shape for a different block now creating the block ptr
		//putting in to the sll

		if(!initial_block){  //if sll length is 0
			initial_block = newBlockPtr;
		}else{  //if not
			Block* temp = initial_block;
			while (temp -> next_block){ //going until the end of the linked list
				temp = temp -> next_block;
			}
			temp -> next_block = newBlockPtr; // finally appending.
		}

	}
	//We created the linked list, whose pointer to its head node is called initial_block.

	active_rotation = initial_block;


	//assigning the powerupshape
	pair<string, int> lastPair = stringBlocks.at(stringBlocks.size() - 1);

	string blockStr = lastPair.first;
	int row = lastPair.second; // how many rows we have

	int col = blockStr.length() / row; // how many columns we have

	vector<vector<bool>> lastShape;
	for(int r {0}; r < row; r++){
		vector<bool> lastShapeRow; // we are creating the row part.
		for(int c {0}; c < col; c++){
			lastShapeRow.push_back(blockStr[r*col + c] == '1');
		}
		lastShape.push_back(lastShapeRow);
	}
	power_up = lastShape;
	//we created the power up


    // TODO: For every block, generate its rotations and properly implement the multilevel linked list structure
    //       that represents the game blocks, as explained in the PA instructions.
	//Now we are implementing the doubly linked list structure.

	Block* blockPtr = initial_block;  // iterator pointer
	int blockNum2 = 0;
	while (blockPtr){
		Block* rBlock = blockPtr -> rotate(true);
		if(*rBlock == *blockPtr){  // it only takes one rotation for it come back (the direction of the rotation does not matter).

			blockPtr -> right_rotation = blockPtr; //the block right rotation is pointing to is itself.
			blockPtr -> left_rotation = blockPtr;  //the block left rotation is pointing to is itself.

		}else{
			Block* rrBlock = rBlock -> rotate(true); // rrBlock means the original block 180 degree rotated. (rotated twice)

			if(*rrBlock == *blockPtr){

				blockPtr -> right_rotation = rBlock;    // □ -> □
				rBlock -> left_rotation = blockPtr;     // □ -> <- □

				rBlock -> right_rotation = blockPtr;   // -> □ -> <- □ ->(this goes to the head)
				blockPtr -> left_rotation = rBlock;    // -> <-(this goes to the end) □ -> <- □ -> <-(this goes to the head)
			}else{
				//Now I assume there is no block whose rotation to the right three times can result in a block
				// that is the same as itself. This assumption comes from my simple 10 iq brain.



				/////////////////////////////////////////////////////////////////////////////
				// all not handled connections are handled here. CAUTION CAUTION CAUTION!!!!
				blockPtr -> right_rotation = rBlock;
				rBlock -> left_rotation = blockPtr;

				rBlock -> right_rotation = rrBlock;
				rrBlock -> left_rotation = rBlock;

				Block* lBlock = blockPtr -> rotate(false);
				lBlock -> right_rotation = blockPtr;
				blockPtr -> left_rotation = lBlock;

				//it is always the case that after rotating 4 times a shape comes to itself. So,

				rrBlock -> right_rotation = lBlock;
				lBlock -> left_rotation = rrBlock;
				//END OF THE CAUTION ALERT.
				////////////////////////////////////////////////////////////////////////////////

			}

		}
		blockNum2++;
		blockPtr = blockPtr -> next_block;

	}

	delete blockPtr;


    // TODO: Initialize the "power_up" member variable as the last block from the input file (do not add it to the linked list!)
}

void BlockFall::initialize_grid(const string &input_file) {
    // TODO: Initialize "rows" and "cols" member variables
    // TODO: Initialize "grid" member variable using the command-line argument 1 in main

	ifstream gridFile(input_file);
	if(!gridFile.is_open()){
		cerr << "Error opening file: " << input_file << std::endl;
		return;
	}

	string line;
	while (getline(gridFile, line)){
		istringstream iss(line);
		vector<int> row;
		int square;
		while (iss >> square){
			row.push_back(square);
		}
		grid.push_back(row);
	}
	rows = grid.size();
	cols = grid[0].size();

}




BlockFall::~BlockFall() {
    // TODO: Free dynamically allocated memory used for storing game blocks
	Block* current = initial_block;
	while (current){
		Block* next = current -> next_block;
		Block* dll = current -> right_rotation;
		while (dll != current){
			Block* dllNext = dll -> right_rotation;
			delete dll;
			dll = dllNext;
		}
		delete current;
		current = next;
	}
}
