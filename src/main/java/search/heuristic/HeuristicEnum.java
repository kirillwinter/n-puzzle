package search.heuristic;

public enum HeuristicEnum {
	SIMPLE, // фишки не на своих местах
	MANHATTAN, // манхеттанское растояние
	MANHATTAN_AND_LINEAR_CONFLICT,
	MANHATTAN_AND_LAST_MOVE,
	MANHATTAN_LC_LM
}
