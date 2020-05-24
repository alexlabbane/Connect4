package net.connect4Game;

public class Game {
	
	private GUI gui;
	private Board gameBoard;
	private Player player1;
	private Player player2;
	int turn = -1; //-1 for player 1's turn, 1 for player 2's turn
	
	private boolean guiEnabled;
	
	public enum AITypes {
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
		this.guiEnabled = true;
		gui = new GUI(gameBoard);
		player1 = new Player(player1Human, 1, gui, gameBoard, player1AIType, 1);
		player2 = new Player(player2Human, 2, gui, gameBoard, player2AIType, 1);
	}
	
	/**
	 * Can be used if players are already instantiated
	 */
	public Game(Player player1, Player player2, boolean guiEnabled) {
		gameBoard = new Board();
		if(guiEnabled) gui = new GUI(gameBoard);
		this.player1 = player1;
		this.player2 = player2;
		this.guiEnabled = guiEnabled;
		
		this.player1.setBoard(this.gameBoard);
		this.player1.setGUI(this.gui);
		this.player2.setBoard(this.gameBoard);
		this.player2.setGUI(this.gui);
	}
	
	public int execute() {		
		while(gameBoard.checkWin() == 0) {			
			if(turn == -1) {
				if(this.guiEnabled) {
					gui.setInputEnabled(player1.isHuman());
					gui.updateValidInputs(gameBoard);
				}
				if(player1.play(gameBoard)) turn *= -1;
			} else if (turn == 1) {
				if(this.guiEnabled) {
					gui.setInputEnabled(player2.isHuman());
					gui.updateValidInputs(gameBoard);	
				}

				if(player2.play(gameBoard)) turn *= -1;
			}
			//gameBoard.printBoard();
			if(this.guiEnabled) updateGUI(gameBoard.getLastRow(), gameBoard.getLastCol(), gameBoard.getLastColor());
		}

		if(this.guiEnabled) {
			gui.setInputEnabled(false);
			close();
		}
		
		return gameBoard.checkWin();
	}

	public void close() {
		this.gui.close();
	}
	
	public void updateGUI(int row, int col, int color) {
		gui.updateGUI(row, col, color);
	}
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public Board getBoard() {
		return this.gameBoard;
	}
}
