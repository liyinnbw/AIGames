#include "State.h"


State::State(void)
{
}


State::~State(void)
{
}
bool State::isGameOver(){
	return false;
}
double State::evaluate(){
	return 0;
}

std::vector<State> State::getNexts(){
	std::vector<State> nexts;
	return nexts;
}

void State::next(){
	//move state to next state
}

std::string State::toString(){
	return "";
}