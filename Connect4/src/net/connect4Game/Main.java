package net.connect4Game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import javax.swing.JOptionPane;

import net.AI.AIAgent;
import net.launcher.Launcher;
import net.train.Chromosome;
import net.train.GA;

public class Main {
	public static void main(String args[]) throws InterruptedException, IOException {
		
		//testBitBoard();
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
			
			Object difficulty = JOptionPane.showInputDialog(null, "Select AI Difficulty (only difficulty 8 is tracked for wins/losses)", "Input", 0, null, new Object[] {"11", "9", "8", "7", "6", "5", "4", "3", "2", "1"}, 0);
			
			int[] weights = new int[] {1, 8, 5, 16, 21, 70};
			
			Player player1 = new Player(playerFirst, 1, null, null, Game.AITypes.MINIMAX.getType(), weights, Integer.parseInt(difficulty.toString()));
			Player player2 = new Player(!playerFirst, 2, null, null, Game.AITypes.MINIMAX.getType(), 1, Integer.parseInt(difficulty.toString()));

			
			game = new Game(player1, player2, true);
			game.execute();
		
			int winner = game.execute();
			
			if(winner == 1) {
				JOptionPane.showMessageDialog(null, "Blue won the game");
				if(player1.isHuman() && Integer.parseInt(difficulty.toString()) == 8) {
					System.out.println("LOSSES: " + addAILoss());
				} else {
					System.out.println("WINS: " + addAIWin());
				}
					
			} else if(winner == 2) {
				JOptionPane.showMessageDialog(null, "Red won the game.");
				if(player2.isHuman() && Integer.parseInt(difficulty.toString()) == 8) {
					System.out.println("LOSSES: " + addAILoss());
				} else {
					System.out.println("WINS: " + addAIWin());
				}
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
		bitBoard board = new bitBoard();
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
	
	public static int addAIWin() {
		int numWins = -1;
		
		try {
			//API documentation at countapi.xyz
			URL aiWins = new URL("https://api.countapi.xyz/hit/alexlabbane.com/connect4wins");
			HttpURLConnection con = (HttpURLConnection) aiWins.openConnection();
			
	        con.addRequestProperty("User-Agent", "Mozilla");
			con.setRequestMethod("GET");
			
			int status = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String response = in.readLine();
			numWins = Integer.parseInt(response.substring(response.indexOf(':') + 1, response.length() - 1));
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //AI Wins

		return numWins;
	}
	
	public static int addAILoss() {
		int numLosses = -1;
		try {
			//API documentation at countapi.xyz
			URL aiLosses = new URL("https://api.countapi.xyz/hit/alexlabbane.com/connect4losses");
			HttpURLConnection con = (HttpURLConnection) aiLosses.openConnection();
	        
			con.addRequestProperty("User-Agent", "Mozilla");
			con.setRequestMethod("GET");
			
			int status = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String response = in.readLine();
			numLosses = Integer.parseInt(response.substring(response.indexOf(':') + 1, response.length() - 1));
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //AI Wins

		return numLosses;
	}
	
	public static void testBitBoard() {
		bitBoard test = new bitBoard();
		test.executeMove(2, 0);
		//test.executeMove(1, 1);
		//test.executeMove(1, 2);
		test.executeMove(1, 3);
		test.executeMove(1, 4);
		//test.executeMove(1, 4);
		//test.executeMove(1, 1);
		//test.executeMove(1, 1);

		System.out.println(test.countThreeSeq(1)[0] + " " + test.countThreeSeq(1)[1]);
	}
}
