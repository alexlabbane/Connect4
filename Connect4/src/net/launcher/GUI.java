package net.launcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.connect4Game.Game;

public class GUI {

	private JFrame window;
	private JPanel launcherPanel;
	private JButton aiai;
	private JButton playerai;
	private JButton playerplayer;
	
	Game game;
	
	public GUI() {
		this.window = new JFrame("Connect4 Launcher");
		this.window.setSize(300, 375);
		this.launcherPanel = new JPanel();
		
		this.aiai = new JButton("AI vs AI");
		this.playerai = new JButton("Player vs AI");
		this.playerplayer = new JButton("Player vs Player");
		
		this.launcherPanel.setLayout(null);
		this.launcherPanel.setBounds(0, 0, 300, 375);
		
		this.playerai.setBounds(50, 25, 200, 75);
		this.playerplayer.setBounds(50, 125, 200, 75);
		this.aiai.setBounds(50, 225, 200, 75);
		
		this.launcherPanel.add(this.playerai);
		this.launcherPanel.add(this.playerplayer);
		this.launcherPanel.add(this.aiai);
		
		this.window.getContentPane().add(this.launcherPanel);
		this.window.setDefaultCloseOperation(2);
		this.window.setResizable(false);
		this.window.add(launcherPanel);
		this.window.setVisible(true);
				
		//initializeListeners();
	}
	
	/*private void initializeListeners() {
		
		this.aiai.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				Game game = new Game(false, false);
				
				int winner = game.execute();
				
				switch(winner) {
					case 1:
						JOptionPane.showMessageDialog(null, "Red wins!");
					case 2:
						JOptionPane.showMessageDialog(null, "Blue wins!");
					case 3:
						JOptionPane.showMessageDialog(null, "It's a tie!");
				}
			}
		});
		
		this.playerai.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game game = null;
				
				Object selected = JOptionPane.showInputDialog(null, "Who should go first?", "Input", 0, null, new Object[]{"AI", "Player"}, 0);
				
				if(selected == null) return;
				
				if(selected.toString().equals("AI")) {
					System.out.println("AI First");
					game = new Game(false, true);
				}
				else {
					System.out.println("Player First");
					game = new Game(true, false);
				}
				
				int winner = game.execute();
				
				switch(winner) {
					case 1:
						JOptionPane.showMessageDialog(null, "Red wins!");
					case 2:
						JOptionPane.showMessageDialog(null, "Blue wins!");
					case 3:
						JOptionPane.showMessageDialog(null, "It's a tie!");
				}
			}
		});
		
		this.playerplayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game game = new Game(true, true);
				
				int winner = game.execute();
				
				switch(winner) {
					case 1:
						JOptionPane.showMessageDialog(null, "Red wins!");
					case 2:
						JOptionPane.showMessageDialog(null, "Blue wins!");
					case 3:
						JOptionPane.showMessageDialog(null, "It's a tie!");
				}
			}
		});
		
	}*/
}
