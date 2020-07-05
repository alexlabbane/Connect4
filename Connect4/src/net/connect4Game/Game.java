package net.connect4Game;

public class Game {
	
	private GUI gui;
	private bitBoard gameBoard;
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
	
	public Game(boolean player1Human, int player1AIType, boolean player2Human, int player2AIType, int depth) {
		gameBoard = new bitBoard();
		this.guiEnabled = true;
		gui = new GUI(gameBoard);
		player1 = new Player(player1Human, 1, gui, gameBoard, player1AIType, 1, depth);
		player2 = new Player(player2Human, 2, gui, gameBoard, player2AIType, 1, depth);
	}
	
	/**
	 * Can be used if players are already instantiated
	 */
	public Game(Player player1, Player player2, boolean guiEnabled) {
		gameBoard = new bitBoard();
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
		int currentColor = 1;
		
		while(gameBoard.checkWin(currentColor) == 0) {		
			System.out.println(gameBoard.checkWin(currentColor));
			System.out.println(gameBoard);
			if(turn == -1) {
				currentColor = player1.getColor();
				if(this.guiEnabled) {
					gui.setInputEnabled(player1.isHuman());
					gui.updateValidInputs(gameBoard);
				}
				if(player1.play(gameBoard)) turn *= -1;
			} else if (turn == 1) {
				currentColor = player2.getColor();
				if(this.guiEnabled) {
					gui.setInputEnabled(player2.isHuman());
					gui.updateValidInputs(gameBoard);	
				}

				if(player2.play(gameBoard)) turn *= -1;
			}
			//gameBoard.printBoard();
			if(this.guiEnabled) 
				updateGUI(gameBoard.getLastCol(), currentColor);
		}

		if(this.guiEnabled) {
			gui.setInputEnabled(false);
			close();
		}
		System.out.println(gameBoard.checkWin(currentColor));
		System.out.println(gameBoard);

		return gameBoard.checkWin(currentColor);
	}

	public void close() {
		this.gui.close();
	}
	
	public void updateGUI(int col, int color) {
		gui.updateGUI(col, color);
	}
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public bitBoard getBoard() {
		return this.gameBoard;
	}
}
