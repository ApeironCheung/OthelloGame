package ca.yorku.eecs3311.util;

public interface Element {
	void accept(Visitor visitor);
}
