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
import net.train.Chromosome;
import net.train.GA;

public class Main {
	public static void main(String args[]) throws InterruptedException {
		
		GA g = new GA(10, 100);
		//g.train();
		//System.exit(0);
		
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
			
			Object difficulty = JOptionPane.showInputDialog(null, "Select AI Difficulty", "Input", 0, null, new Object[] {"1", "2", "3", "4", "5", "6", "7", "8"}, 0);
			
			int[] weights = new int[] {1, 8, 5, 16, 21, 70};
			
			Player player1 = new Player(playerFirst, 1, null, null, Game.AITypes.MINIMAX.getType(), weights, Integer.parseInt(difficulty.toString()));
			Player player2 = new Player(!playerFirst, 2, null, null, Game.AITypes.MINIMAX.getType(), 1, Integer.parseInt(difficulty.toString()));

			
			game = new Game(player1, player2, true);
			game.execute();
		
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
