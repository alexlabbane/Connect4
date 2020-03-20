package net.connect4Game;

public class Main {
	public static void main(String args[]) {
		
		int winner;
		int red = 0;
		int blue = 0;
		int draw = 0;
		
		for(int i = 0; i < 15; i++) {
			Game game = new Game(false, true);
			winner = game.execute();
			
			if(winner == 1) blue++;
			if(winner == 2) red++;
			if(winner == -1) draw++;
		}

		System.out.println("BLUE: " + blue);
		System.out.println("RED: " + red);
		System.out.println("DRAW: " + draw);
		
	}
}
