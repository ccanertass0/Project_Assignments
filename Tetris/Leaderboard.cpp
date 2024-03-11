#include <sstream>
#include "Leaderboard.h"
#include <vector>
void Leaderboard::insert_new_entry(LeaderboardEntry * new_entry) {  //will insert if it deserves to be in top ten else not.
    // TODO: Insert a new LeaderboardEntry instance into the leaderboard, such that the order of the high-scores
    //       is maintained, and the leaderboard size does not exceed 10 entries at any given time (only the
    //       top 10 all-time high-scores should be kept in descending order by the score).
	LeaderboardEntry* temp = head_leaderboard_entry;
	LeaderboardEntry* prev = nullptr;
	if(temp == nullptr){  // empty list
		head_leaderboard_entry = new_entry;
	}else{ // not empty
		while (temp){
			if(new_entry -> score > temp -> score){ //before temp
				if(prev == nullptr){ // inserting at the head of the list
					new_entry -> next_leaderboard_entry = temp;
					head_leaderboard_entry = new_entry;
				}else {
					prev->next_leaderboard_entry = new_entry;
					new_entry->next_leaderboard_entry = temp;
				}
				break;

			}else if(new_entry -> score == temp -> score){
				if(prev == nullptr){ // at the head of the list
					if(new_entry -> last_played >= temp -> score){ // after temp
						new_entry -> next_leaderboard_entry = temp -> next_leaderboard_entry;
						temp -> next_leaderboard_entry = new_entry;
					}else{ // before temp. (adding to very front)
						new_entry -> next_leaderboard_entry = temp;
						head_leaderboard_entry = new_entry;
					}
				}else{
					if(new_entry -> last_played >= temp -> score){ // AFTER TEMP
						new_entry -> next_leaderboard_entry = temp -> next_leaderboard_entry;
						temp -> next_leaderboard_entry = new_entry;
					}else{
						new_entry -> next_leaderboard_entry = temp;
						prev -> next_leaderboard_entry = new_entry;
					}
				}
				break;

			}else if(new_entry -> score < temp -> score){
				if(temp -> next_leaderboard_entry == nullptr){ // adding to the last
					temp -> next_leaderboard_entry = new_entry;
					break;
				}

			}

			//CAUTION
			prev = temp;
			temp = temp -> next_leaderboard_entry;
		}


		int count {0};
		LeaderboardEntry* temp2 = head_leaderboard_entry;
		while (temp2){
			count++;
			temp2 = temp2 -> next_leaderboard_entry;
		}

		// we know how many elements we have

		//removing the last one
		LeaderboardEntry* temp3 = head_leaderboard_entry -> next_leaderboard_entry;
		LeaderboardEntry* prev3 = head_leaderboard_entry;
		if(count == 11){
			while (temp3 -> next_leaderboard_entry){
				prev3 = temp3;
				temp3 = temp3 -> next_leaderboard_entry;
			}
			prev3 -> next_leaderboard_entry = nullptr;
			delete temp3;
		}


	}
}

void Leaderboard::write_to_file(const string& filename) {
    // TODO: Write the latest leaderboard status to the given file in the format specified in the PA instructions
	ofstream outFile(filename);
	if(outFile.is_open()){
		LeaderboardEntry* temp = head_leaderboard_entry;
		while (temp){
			outFile << temp -> score << " " << temp -> last_played << " " << temp -> player_name << endl;
			temp = temp -> next_leaderboard_entry;
		}
	}else{
		cout << "Error while writing on the leaderboard.txt";
	}

}

void Leaderboard::read_from_file(const string& filename) {
    // TODO: Read the stored leaderboard status from the given file such that the "head_leaderboard_entry" member
    //       variable will point to the highest all-times score, and all other scores will be reachable from it
    //       via the "next_leaderboard_entry" member variable pointer.
	ifstream ldrboardFile(filename);
	if(!ldrboardFile.is_open()){
		cerr << "Error opening file: " << filename << std::endl;
		return;
	}
	string line;

	while (getline(ldrboardFile, line)){
		istringstream iss(line);
		vector<string> infos;
		string info;
		while(iss >> info){
			infos.push_back(info);
		}
		infosOfLeaders.push_back(infos);
	}
//
//	for(int row {0}; row < infosOfLeaders.size(); row++){
//		for(int col {0}; col < infosOfLeaders[0].size(); col++){
//			cout << infosOfLeaders[row][col] << " ";
//		}
//		cout << endl;
//	}

	// at this point we have all the initial information in leaders.txt
	// now it is time to create the linked list with all informations in their proper place.
	for(vector<string> information : infosOfLeaders){ // each information belongs to one scorer
		unsigned int score = stoi(information[0]);

		time_t timestamp = static_cast<time_t>(std::stoll(information[1]));


		LeaderboardEntry* newLeaderEntryPtr = new LeaderboardEntry{score, timestamp, information[2]};
		if(head_leaderboard_entry == nullptr){
			head_leaderboard_entry = newLeaderEntryPtr;
		}else{
			LeaderboardEntry* temp = head_leaderboard_entry;
			while (temp -> next_leaderboard_entry != nullptr){
				temp = temp -> next_leaderboard_entry;
			}//we reached the end of the linked list.
			temp -> next_leaderboard_entry = newLeaderEntryPtr;
		}
	}

	//now we initialized our leaderboard linked list from the leaderboard.txt


}


void Leaderboard::print_leaderboard() {
    // TODO: Print the current leaderboard status to the standard output in the format specified in the PA instructions

	//let's print
	cout << "Leaderboard\n-----------\n";
	LeaderboardEntry* temp = head_leaderboard_entry;
	while (temp){

		// Convert time_t to tm struct for local time
		tm *timeinfo = localtime(&temp -> last_played);

		// Buffer to hold the formatted date and time
		char formattedStr[80];

		// Define your desired format
		std::string format = "%H:%M:%S/%d.%m.%Y";

		// Format the timeinfo struct
		strftime(formattedStr, sizeof(formattedStr), format.c_str(), timeinfo);

		cout << temp -> player_name << " " << temp -> score << " " << formattedStr << "\n";
		temp = temp -> next_leaderboard_entry;
	}
}

Leaderboard::~Leaderboard() {
    // TODO: Free dynamically allocated memory used for storing leaderboard entries
	LeaderboardEntry* current = head_leaderboard_entry;
	while (current != nullptr) {
		LeaderboardEntry* next = current->next_leaderboard_entry;
		delete current;
		current = next;
	}
}
