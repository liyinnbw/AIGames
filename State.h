/*
* All state class should extend this class to 
* make use of the game tree
*/
#pragma once
#include <vector>

class State 
{
public:
	State(void);
	~State(void);
	bool isGameOver();
	double	evaluate();
	std::vector<State> getNexts();
	void next();
	std::string toString();
};

