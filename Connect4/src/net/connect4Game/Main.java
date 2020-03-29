package net.connect4Game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import javax.swing.JOptionPane;

import net.AI.AIAgent;
import net.launcher.Launcher;

public class Main {
	public static void main(String args[]) throws InterruptedException {
		
		//test();
		
		//Launcher launcher = new Launcher();
		boolean playAgain = true;
		Game game = null;
		while(playAgain) {
			Random AIGenerator = new Random();
			int type = Math.abs(AIGenerator.nextInt());
			boolean playerFirst = true;
			
			Object selected = JOptionPane.showInputDialog(null, "Who should go first?", "Input", 0, null, new Object[]{"AI", "Player"}, 0);
			if(selected == null) System.exit(0);;
			
			if(selected.toString() == "AI") playerFirst = false;

			
			if(type % 2 == 0 || true) {
				System.out.println("Type 1 Game");
				game = new Game(playerFirst, Game.AITypes.MINIMAX.getType(), !playerFirst, Game.AITypes.MINIMAX.getType());
			} else if(type % 2 == 1) {
				System.out.println("Type 2 Game");
				game = new Game(playerFirst, Game.AITypes.RANDOM.getType(), !playerFirst, Game.AITypes.RANDOM.getType());
			} else if(type % 3 == 2 ) {
				System.out.println("HYBRID GAME");
				game = new Game(playerFirst, Game.AITypes.HYBRID.getType(), !playerFirst, Game.AITypes.HYBRID.getType());
			}
			
			int winner = game.execute();
			
			if(winner == 1) {
				JOptionPane.showMessageDialog(null, "Blue won the game");
			} else if(winner == 2) {
				JOptionPane.showMessageDialog(null, "Red won the game.");
			} else {
				JOptionPane.showMessageDialog(null, "The game ended in a draw.");
			}
			
			Object again = JOptionPane.showConfirmDialog(null, "Play again?");
			System.out.println(again.toString());
			if(!again.toString().equals("0")) playAgain = false;
			
			//Write results
			
			
			game.close();
		}

		System.exit(0);
	}
	
	public static void test() {
		Board board = new Board();
		AIAgent blue = new AIAgent(board, 8, 1, Game.AITypes.MINIMAX.getType());
		try {
			int move = blue.getBookMove(new BufferedReader(new FileReader("C:/Users/scien/Desktop/Connect Four V2/ConnectFourData/AIFirst/board.txt")));
			System.out.println(move);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(0);
	}
}
