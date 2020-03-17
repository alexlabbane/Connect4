package net.connect4Game;

public class Game {
	
	private GUI gui;
	private Board gameBoard;
	private Player player1;
	private Player player2;
	int turn = -1; //-1 for player 1's turn, 1 for player 2's turn
	
	public Game(boolean player1Human, boolean player2Human) {
		gameBoard = new Board();
		gui = new GUI(gameBoard);
		player1 = new Player(player1Human, 1, gui);
		player2 = new Player(player2Human, 2, gui);
		
	}
	
	public void execute() {
		while(gameBoard.checkWin() == 0) {
			if(turn == -1) {
				gui.setInputEnabled(player1.isHuman());
				gui.updateValidInputs(gameBoard);
				player1.play(gameBoard);
				turn *= -1;
			} else if (turn == 1) {
				gui.setInputEnabled(player2.isHuman());
				gui.updateValidInputs(gameBoard);
				player2.play(gameBoard);
				turn *= -1;
			}

			updateGUI(gameBoard.getLastRow(), gameBoard.getLastCol(), gameBoard.getLastColor());
		}
		System.out.println("Win reached");
		//Print winning condition
		gui.setInputEnabled(false);
	}
	
	public void updateGUI(int row, int col, int color) {
		gui.updateGUI(row, col, color);
	}
}
