package ca.yorku.eecs3311.othello.viewcontroller;

import ca.yorku.eecs3311.othello.controller.OthelloControllerGUI;
import ca.yorku.eecs3311.othello.model.*;
import ca.yorku.eecs3311.util.*;

public class OthelloVisitor implements Visitor, Observer{

	Othello othello;
	Reporter reporter;
	
	public OthelloVisitor() {
		this.othello = new Othello();
		this.reporter = new Reporter();
	}
	
	
	@Override
	public void visitOthello(Othello othello) {
		this.othello = othello;
		this.reporter.makeReport(othello);
	}
	public Othello getOthello() {
		return this.othello.copy();
	}
	@Override
	public void update(Observable o) {
		if(o instanceof OthelloControllerGUI) {
			this.visitOthello(othello);
		}		
	}	
	public Reporter getReporter() {
		return this.reporter;
	}
}
