package ca.yorku.eecs3311.util;

import ca.yorku.eecs3311.othello.model.Othello;

public interface Visitor {
	void visitOthello(Othello othello);
}
