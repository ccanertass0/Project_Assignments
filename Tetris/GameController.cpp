#include <fstream>
#include "GameController.h"
#include <sys/stat.h>
#include <ctime>

bool GameController::play(BlockFall& game, const string& commands_file){

    // TODO: Implement the gameplay here while reading the commands from the input file given as the 3rd command-line
    //       argument. The return value represents if the gameplay was successful or not: false if game over,
    //       true otherwise.
	LeaderboardEntry* currentPlayer = new LeaderboardEntry{0, 0, game.player_name};// will be revized at return states.

	//creating the output file
	ofstream outFile("some.out");
	if(!outFile.is_open()){
		cerr << "Error opening the file.\n";
	}

	ifstream commandFile(commands_file);
	if(!commandFile.is_open()){  //if we can2t open the .dat file.
		cerr << "Error opening file: " << commands_file << std::endl;
	}


	string command;
	Block* currentBlock = game.initial_block;

	int curRow = 0;   // upper left index of the block which row?
	int curCol = 0;   // upper left index of the block which column?


	bool newBlock = true;


	while (getline(commandFile, command)){
		if(!currentBlock){

			outFile << "YOU WIN!\n"
					   "No more blocks.\nFinal grid and score:\n\n";
			cout << "YOU WIN!\n"
					"No more blocks.\nFinal grid and score:\n\n";
			printGrid(game, outFile, false, true);


			currentPlayer -> score = game.current_score;
			currentPlayer -> last_played = time(nullptr);
			game.leaderboard.insert_new_entry(currentPlayer); // it will be in top ten if it can. else it does not insert it in to the list
			game.leaderboard.write_to_file(game.leaderboard_file_name);
			game.leaderboard.print_leaderboard();

			return true;
		}
		int shaperowSize = currentBlock -> shape.size();     //how many rows our block have. =  heigth
		int shapecolSize = currentBlock -> shape[0].size();  //how many columns our block have. = width


		if(newBlock) {
			// CONTROLLING AT THE START OF GETTING A NEW BLOCK IF IT IS GAME OVER. IS THERE ENOUGH SPACE FOR IT TO MOVE AT THE START? if no return false.
			for (int rc{0}; rc < shaperowSize; rc++) {
				for (int cc{0}; cc < shapecolSize; cc++) {
					if (currentBlock->shape[rc][cc] == 1 && game.grid[rc][cc] == 1) {
						outFile << "GAME OVER!\nNext block that couldn't fit:\n";
						cout << "GAME OVER!\nNext block that couldn't fit:\n";
						for(int row {0}; row < currentBlock->shape.size(); row++){
							for(int col {0}; col < currentBlock->shape[0].size(); col++){
								if(currentBlock -> shape[row][col] == 1){
									outFile << occupiedCellChar;
									cout << occupiedCellChar;
								}else{
									outFile << unoccupiedCellChar;
									cout << unoccupiedCellChar;
								}
							}
							outFile << endl;
							cout << endl;
						}

						outFile << "\nFinal grid and score:\n\n";
						cout << "\nFinal grid and score:\n\n";
						printGrid(game, outFile, false, true);

						currentPlayer -> score = game.current_score;
						currentPlayer -> last_played = time(nullptr);
						game.leaderboard.insert_new_entry(currentPlayer); // it will be in top ten if it can. else it does not insert it in to the list
						game.leaderboard.write_to_file(game.leaderboard_file_name);
						game.leaderboard.print_leaderboard();

						return false;
					}
				}
			}


			// placing the new block.
			for(int row {0}; row < shaperowSize; row++){
				for(int col {0}; col < shapecolSize; col++){
					if(currentBlock -> shape[row][col] == 1) {
						game.grid[row][col] = 1;
					}
				}
			}


			newBlock = false;

		}

		if(command == "DROP"){
			int numberOfFilledCells = 0;
			//we have the command drop so we should first delete the block in the ceil.
			//we know its position by curRow (which is always 0) and curCol.
			for(int delR {0}; delR < shaperowSize; delR++){
				int colHelper = 0;
				for(int delC {curCol}; delC < curCol + shapecolSize; delC++){
					if(currentBlock -> shape[delR][colHelper++] == 1){
						game.grid[delR][delC] = 0;
						numberOfFilledCells++;
					}
				}
			}//we deleted the block


			int rowControl = 1;   // we will start controlling the grid from the row index 1.


			while (isOkToPlace(game, currentBlock, rowControl, curCol)){
				rowControl++;
			}

			int sittingRow = rowControl - 1; // this is the row index we will put our block. upper left of out block will sit at this row.

			//we have our sitting row and column at this point.


			//now it is time to place this block
			int shpRow {0};
			for(int row {sittingRow}; row < sittingRow + shaperowSize; row++){
				int shpCol {0};
				for(int col {curCol}; col < curCol + shapecolSize; col++){ //curcol is our sitting col surely.
					if(currentBlock -> shape[shpRow][shpCol] == 1){
						game.grid[row][col] = 1;
					}
					shpCol++;
				}
				shpRow++;
			}

			// we placed the block.
			int gainedScore = sittingRow * numberOfFilledCells;  //this will be the gained score after dropping sittingRow is equal to the falling distance
			game.current_score += gainedScore;
			currentBlock = currentBlock -> next_block;
			curRow = 0;
			curCol = 0;
			newBlock = true;

			if(game.gravity_mode_on){ //we will give a gravitational effect to the newly placed block(actually the whole grid)
				applyGravitation(game);
			}




			searchForPowerUp(game, outFile);
			if(isPoppable(game)){
				outFile << "Before clearing:\n";
				cout << "Before clearing:\n";
				printGrid(game, outFile, true, false);
				while (pop(game));
			}


		}else if(command == "MOVE_RIGHT"){
			int targetCol = curCol + 1;
			bool isFree = true;
			if(targetCol + shapecolSize - 1 < game.cols){
				// we passed the first test we don't go through edges.

				for(int rowControl {0}; rowControl < shaperowSize; rowControl++){
					if(currentBlock -> shape[rowControl][shapecolSize - 1] == 1 && game.grid[rowControl][curCol + shapecolSize] == 1){
						isFree = false;
						break;
					}
				}

				if(isFree){
					removeBlock(game, currentBlock, curCol);
					addBlock(game, currentBlock, targetCol);
					curCol = targetCol;
				}

			}else{
				cout << "You can not go over the edge.\n";
			}


		}else if(command == "MOVE_LEFT"){
			//it has to come to this right for it start going left , so we can say the path is already free.
			int targetCol = curCol - 1;
			if(targetCol >= 0){
				//then it is OK.
				removeBlock(game,currentBlock,curCol);
				addBlock(game, currentBlock, targetCol);
				curCol = targetCol;
			}


		}else if(command == "ROTATE_RIGHT"){
			removeBlock(game, currentBlock, curCol);
			if(isOkToPlace(game, currentBlock -> right_rotation, 0, curCol)){
				game.active_rotation = currentBlock -> right_rotation;
				currentBlock = game.active_rotation;
			}
			addBlock(game, currentBlock, curCol);

		}else if(command == "ROTATE_LEFT"){
			removeBlock(game, currentBlock, curCol);
			if(isOkToPlace(game, currentBlock -> left_rotation, 0, curCol)){
				game.active_rotation = currentBlock -> left_rotation;
				currentBlock = game.active_rotation;
			}
			addBlock(game, currentBlock, curCol);
			
		}else if(command == "PRINT_GRID"){
			printGrid(game, outFile, false, false);
		}else if(command == "GRAVITY_SWITCH"){
			if(game.gravity_mode_on){
				game.gravity_mode_on = false;
			}else{
				game.gravity_mode_on = true;
				if(currentBlock != nullptr){
					removeBlock(game, currentBlock, curCol);
					applyGravitation(game);
					searchForPowerUp(game, outFile);
					if(isPoppable(game)){
//						outFile << "Before clearing:\n";
//						printGrid(game, outFile, true, false);
						while (pop(game));
					}
					addBlock(game, currentBlock, curCol);
				}else{
					applyGravitation(game);
					applyGravitation(game);
					searchForPowerUp(game, outFile);
					if(isPoppable(game)){
						outFile << "Before clearing:\n";
						printGrid(game, outFile, true, false);
						while (pop(game));
					}
				}



			}


		}else{
			outFile << "Unknown command: " << command << endl;
			cout << "Unknown command: " << command << endl;
		}
	}

	outFile << "GAME FINISHED!\n" << "No more commands.\n" << "Final grid and score:\n";
	cout << "GAME FINISHED!\n" << "No more commands.\n" << "Final grid and score:\n";

	outFile << endl;
	cout << endl;
	if(currentBlock != nullptr) {
		removeBlock(game, currentBlock, curCol);
	}
	printGrid(game, outFile, false, true);

	currentPlayer -> score = game.current_score;
	currentPlayer -> last_played = time(nullptr);
	game.leaderboard.insert_new_entry(currentPlayer);
	game.leaderboard.write_to_file(game.leaderboard_file_name);
	game.leaderboard.print_leaderboard();

    return true;
}



void GameController::printGrid(BlockFall &game, ofstream& outFile, bool isForClearing, bool isFinal) {
	if(!isForClearing) {
		outFile << "Score: " << game.current_score << endl;
		cout << "Score: " << game.current_score << endl;
		if(game.leaderboard.head_leaderboard_entry) {
			outFile << "High Score: " << game.leaderboard.head_leaderboard_entry->score << endl;
			cout << "High Score: " << game.leaderboard.head_leaderboard_entry->score << endl;
		}else{
			outFile << "High Score: " << game.current_score << "\n";
			cout << "High Score: " << game.current_score << "\n";
		}
	}
	for(int row {0}; row < game.grid.size(); row++){
		for(int col {0}; col < game.grid[row].size(); col++){
			if(game.grid[row][col] == 1){
				outFile << occupiedCellChar;
				cout << occupiedCellChar;
			}else{
				outFile << unoccupiedCellChar;
				cout << unoccupiedCellChar;
			}
		}
		outFile << endl;
		cout << endl;
	}
	if(isFinal){
		outFile << "\n";
		cout << "\n";
	}else{
		outFile << "\n\n";
		cout << "\n\n";
	}
}

//tries to remove block directly, be cauitous about errors
void GameController::removeBlock(BlockFall& game, const Block* blockPtr, int startingcolumn) {
	for(int row{0}; row < blockPtr -> shape.size(); row++){
		int colHelper {0};
		for(int col {startingcolumn}; col < startingcolumn + blockPtr -> shape[0].size(); col++){
			if(blockPtr -> shape[row][colHelper++] == 1){
				game.grid[row][col] = 0;
			}
		}
	}

}

// this directly tries to add a block to the grid, be cautious.
void GameController::addBlock(BlockFall &game, const Block* blockPtr, int newStartingCol) {
	//be careful about errors
	for(int row {0}; row < blockPtr -> shape.size(); row++){
		int colHelper {0};
		for(int col {newStartingCol}; col < newStartingCol + blockPtr -> shape[0].size(); col++){
			if(blockPtr -> shape[row][colHelper++] == 1){
				game.grid[row][col] = 1;
			}
		}
	}
}

bool GameController::isOkToPlace(BlockFall &game, const Block *block, int rowControl, int colControl) {

	if(rowControl + block -> shape.size() - 1 >= game.rows || colControl + block -> shape[0].size() - 1 >= game.cols){
		return false;
	}
	int shapeRow = 0;

	for(int row {rowControl}; row < rowControl + block -> shape.size(); row++){
		int shapeCol = 0;
		for(int col {colControl}; col < colControl + block -> shape[0].size(); col++){
			if(game.grid[row][col] == 1 && block -> shape[shapeRow][shapeCol] == 1){
				return false;
			}
			shapeCol++;
		}
		shapeRow++;
	}
	return true;
}

bool GameController::pop(BlockFall &game) { //pops if it can.
	for(int row {game.rows - 1}; row >= 0; row--){
		int onesCount {0};
		for(int col {0}; col < game.cols; col++){
			if(game.grid[row][col] == 1){
				onesCount++;
			}
		}
		if(onesCount == game.cols){ // IT IS POPPABLE. the current row is full of 1s.
			game.current_score += game.cols;

			//patlatmaca
			for(int col {0}; col < game.cols; col++){
				game.grid[row][col] = 0;
			}
			//dropping everything down below
			//now, we will shift every row above this current row one place down. we will arrange the row 0 later.
			for(int row2 {row - 1}; row2 >= 1; row2--){
				for(int col {0}; col < game.cols; col++){
					game.grid[row2 + 1][col] = game.grid[row2][col];
				}
			}
			for(int col {0}; col < game.cols; col++){
				game.grid[0][col] = 0;
			}//we arranged the row 0.
			//we popped and returned true. There might be other poppable rows.
			return true;
		}

	}
	return false;
}

void GameController::searchForPowerUp(BlockFall &game, ofstream& outFile) {
	for(int row {0}; row < game.rows; row++){
		for(int col {0}; col < game.cols; col++){
			//if my powerUp matrix matches the matrix at this index i have a power up
			//r, c indicates the coordinates of the upperleft index of current moving matrix

			if(isAMatch(game, row, col)){
				outFile << "Before clearing:\n";
				cout << "Before clearing:\n";
				printGrid(game, outFile, true, false);
				//boom delete everything
				game.current_score += 1000;
				int oneCount {0};
				for(int row2 {0}; row2 < game.rows; row2++){
					for(int col2 {0}; col2 < game.cols; col2++){
						if(game.grid[row2][col2] == 1) {
							oneCount++;
							game.grid[row2][col2] = 0;
						}
					}
				}
				game.current_score += oneCount;

				return;
			}

		}
	}
}

bool GameController::isAMatch(const BlockFall &game, int row, int col) {
	//we will start controlling our grid at the given coordinates row, col.
	// row col indicates the upper left index of the current moving lens.
	int powerUpRowSize = game.power_up.size();
	int powerUpColSize = game.power_up[0].size();

	if(row + powerUpRowSize - 1 >= game.rows || col + powerUpColSize - 1 >= game.cols) {
		return false;
	}

	for(int r {0}; r < powerUpRowSize; r++){
		for(int c {0}; c < powerUpColSize; c++){
			if(game.grid[row + r][col + c] != game.power_up[r][c]){
				return false;
			}
		}
	}

	return true;
}

bool GameController::isPoppable(const BlockFall &game) {
	for(int row {game.rows - 1}; row >= 0; row--){
		int onesCount {0};
		for(int col {0}; col < game.cols; col++){
			if(game.grid[row][col] == 1){
				onesCount++;
			}
		}
		if(onesCount == game.cols){ // IT IS POPPABLE. the current row is full of 1s.
			return true;
		}
	}
	return false;
}

void GameController::applyGravitation(BlockFall &game) {
	int oneCounter {0};
	for(int col {0}; col < game.cols; col++){
		for(int row{game.rows - 1}; row >= 0; row--){
			if(game.grid[row][col] == 1){
				oneCounter++;
				game.grid[row][col] = 0;
			}
		}
		//at this point we counted all the ones in a column.
		// now it is time to put them back for the same column.
		int rowHelper {0};
		while (oneCounter > 0){
			game.grid[game.rows - 1 - rowHelper++][col] = 1;
			oneCounter--;
		}
	}

}