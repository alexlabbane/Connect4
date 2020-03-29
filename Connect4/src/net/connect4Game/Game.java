package net.connect4Game;

public class Game {
	
	private GUI gui;
	private Board gameBoard;
	private Player player1;
	private Player player2;
	int turn = -1; //-1 for player 1's turn, 1 for player 2's turn
	
	enum AITypes {
		MINIMAX(1),
		RANDOM(2),
		HYBRID(3);
		
		private final int type;

	    AITypes(int type) {
	        this.type = type;
	    }
	    
	    public int getType() {
	        return this.type;
	    }
	}
	
	public Game(boolean player1Human, int player1AIType, boolean player2Human, int player2AIType) {
		gameBoard = new Board();
		gui = new GUI(gameBoard);
		player1 = new Player(player1Human, 1, gui, gameBoard, player1AIType, 1);
		player2 = new Player(player2Human, 2, gui, gameBoard, player2AIType, 1);
	}
	
	public int execute() {		
		while(gameBoard.checkWin() == 0) {			
			if(turn == -1) {
				gui.setInputEnabled(player1.isHuman());
				gui.updateValidInputs(gameBoard);
				if(player1.play(gameBoard)) turn *= -1;
			} else if (turn == 1) {
				gui.setInputEnabled(player2.isHuman());
				gui.updateValidInputs(gameBoard);
				if(player2.play(gameBoard)) turn *= -1;
			}
			gameBoard.printBoard();
			updateGUI(gameBoard.getLastRow(), gameBoard.getLastCol(), gameBoard.getLastColor());
		}
		System.out.println("Game Over.");
		gui.setInputEnabled(false);
		
		return gameBoard.checkWin();
	}

	public void close() {
		this.gui.close();
	}
	
	public void updateGUI(int row, int col, int color) {
		gui.updateGUI(row, col, color);
	}
}
