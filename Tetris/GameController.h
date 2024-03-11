#ifndef PA2_GAMECONTROLLER_H
#define PA2_GAMECONTROLLER_H

#include "BlockFall.h"

using namespace std;

class GameController {
public:
    bool play(BlockFall &game, const string &commands_file); // Function that implements the gameplay


	void printGrid(BlockFall &game, ofstream& ofstream1, bool isForClearing, bool isFinal);
	void removeBlock(BlockFall& game, const Block* block, int startingColumn);
	void addBlock(BlockFall& game, const Block* block, int newStartingCol);
	bool isOkToPlace(BlockFall&game, const Block* block, int rowControl, int colControl);
	bool pop(BlockFall& game);  // a recursive function that will pop all the filled rows and drop them down properly.
	void searchForPowerUp(BlockFall& game, ofstream& outFile);
	bool isAMatch(const BlockFall& game, int row, int col);
	bool isPoppable(const BlockFall& game);

	void applyGravitation(BlockFall& game); // everything falls down when applied.

};


#endif //PA2_GAMECONTROLLER_H
