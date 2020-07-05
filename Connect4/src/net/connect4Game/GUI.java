package net.connect4Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI {
	private JFrame window;
	private JPanel gamePanel;
	private ArrayList<JButton> boardButtons;
	private ArrayList<JButton> inputs;
	private JLabel aiInfo;
	
	private bitBoard gameBoard;
	
	private int currentColor;
	private boolean enabled;
	
	public GUI(bitBoard gameBoard) {
		window = new JFrame("Connect4");
		this.gamePanel = new JPanel();
		this.gamePanel.setLayout(null);
		
		window.setSize(900, 800);
		window.setVisible(true);
		
		this.currentColor = 1;
		this.gameBoard = gameBoard;
		this.enabled = true;
		inputs = new ArrayList<JButton>();
		boardButtons = new ArrayList<JButton>();
		
		for(int i = 0; i < 7; i++) {
			JButton newButton = new JButton("");
			newButton.setBounds(112 + 100 * i, 12, 76, 76);
			
			final int index = i;
			newButton.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			    	GUI.this.gameBoard.executeMove(GUI.this.currentColor, index);
			    	GUI.this.setInputEnabled(false);
			    	System.out.println("Player plays is row " + index);
			    }
			});
			
			inputs.add(newButton);
			gamePanel.add(newButton);
		}
		
		for(int i = 0; i < 42; i++) {
			JButton newButton = new JButton("");
			newButton.setBounds(100 + 100 * (i % 7), 100 + 100 * (i / 7), 100, 100);
			newButton.setBackground(Color.WHITE);
			
			boardButtons.add(newButton);
			gamePanel.add(newButton);
		}
		
		this.aiInfo = new JLabel("<html>Nodes evaluated: <br>Nodes pruned (estimated): </html>");
		this.gamePanel.add(aiInfo);
		this.aiInfo.setBounds(100, 675, 500, 100);
		
		window.getContentPane().add(gamePanel);
		//window.revalidate();
		window.setEnabled(true);
		window.setVisible(true);
		
	}
	
	public void updateValidInputs(bitBoard gameBoard) {
		for(int i = 0; i < inputs.size(); i++) {
			boolean empty = false;
			if(gameBoard.canPlay(i))
				empty = true;
				
			inputs.get(i).setVisible(empty);
		}
	}
	
	public void setAiInfo(long nodesEvaluated, long nodesPruned) {
		this.aiInfo.setText("<html>Nodes evaluated: " + nodesEvaluated + "<br>Nodes pruned (estimated): " + nodesPruned + "</html>");
	}
	
	public boolean inputIsEnabled() {
		return this.enabled;
	}
	
	public void updateGUI(int col, int color) {
		int index = -1;
		for(int i = 5; i >= 0; i--) {
			if(boardButtons.get(7 * i + col).getBackground() != Color.BLUE
					&& boardButtons.get(7 * i + col).getBackground() != Color.RED) {
				index = 7 * i + col;
				break;
			}
		}
				
		if(index == -1)
			return;
		
		if(color == 1) {
			boardButtons.get(index).setBackground(Color.BLUE);
			this.currentColor = 2;
		} else if(color == 2) {
			boardButtons.get(index).setBackground(Color.RED);
			this.currentColor = 1;
		}
	}
	
	public void setInputEnabled(boolean enabled) {
		for(JButton button : this.inputs) {
			button.setVisible(enabled);
			if(this.currentColor == 2) button.setBackground(Color.RED);
			else if(this.currentColor == 1) button.setBackground(Color.BLUE);
		}
		
		this.enabled = enabled;
	}
	
	public void close() {
		this.window.setVisible(false);
		this.gamePanel.setVisible(false);
		window.dispose();
	}
	
}
