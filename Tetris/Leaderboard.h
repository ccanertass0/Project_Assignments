#ifndef PA2_LEADERBOARD_H
#define PA2_LEADERBOARD_H

#include <ctime>
#include <string>
#include <fstream>
#include "LeaderboardEntry.h"
#include <iostream>
#include <vector>

#define MAX_LEADERBOARD_SIZE 10

using namespace std;

class Leaderboard {
public:
    LeaderboardEntry* head_leaderboard_entry = nullptr;
    void read_from_file(const string &filename);
    void write_to_file(const string &filename);
    void print_leaderboard();
    void insert_new_entry(LeaderboardEntry* new_entry);
    virtual ~Leaderboard();

	vector<vector<string>> infosOfLeaders;  //is in form {{score, time_t, name}, {score, time_t, name}...}  and used only in initialization.


};


#endif //PA2_LEADERBOARD_H
