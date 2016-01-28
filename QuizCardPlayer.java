package com.persistent.training.serialization;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class QuizCardPlayer {

	public JTextArea display;
	public JTextArea answer;
	public ArrayList<QuizCard> cardList;
	public QuizCard currentCard;
	public int currentCardIndex;
	public JFrame frame;
	public JButton nextButton;
	public boolean isShowAnswer;

	public static void main(String[] args) {
		QuizCardPlayer reader = new QuizCardPlayer();
		reader.go();
	}

	private void go() {
		//build gui

		frame = new JFrame("Quiz Card Player");
		JPanel mainPanel = new JPanel();
		Font bigFont = new Font("sanserif", Font.BOLD, 24);

		display = new JTextArea(10,20);
		display.setFont(bigFont);
		display.setLineWrap(true);
		display.setEditable(false);

		JScrollPane qScroller = new JScrollPane(display);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		nextButton = new JButton("Show Question");
		mainPanel.add(qScroller);
		mainPanel.add(nextButton);
		nextButton.addActionListener(new NextCardListener());

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem loadMenuItem = new JMenuItem("Load card set");

		loadMenuItem.addActionListener(new OpenMenuListener());

		fileMenu.add(loadMenuItem);

		menuBar.add(fileMenu);

		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(640, 500);
		frame.setVisible(true);

	}
	public class NextCardListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {

			if(isShowAnswer) {
				display.setText(currentCard.getAnswer());
				nextButton.setText("Next card");
				isShowAnswer = false;
			} else {
				if (currentCardIndex < cardList.size()) {
					showNextCard();
				} else {
					display.setText("That was the last card");
					nextButton.setEnabled(false);
				}
			}

		}

		private void showNextCard() {

			currentCard = cardList.get(currentCardIndex);
			currentCardIndex++;
			display.setText(currentCard.getQuestion());
			nextButton.setText("Show Answer");
			isShowAnswer = true;
		}

	}

	public class OpenMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(frame);
			loadFile(fileOpen.getSelectedFile());

		}

		private void loadFile(File file) {
			cardList = new ArrayList<QuizCard>();
			try {
				BufferedReader reader = new BufferedReader(new FileReader (file));
				String line = null;
				while((line = reader.readLine())!=null) {
					makeCard(line);
					
				}
				reader.close();
			} catch(Exception ex) {
				System.out.println("Couldn't read the card file");
				ex.printStackTrace();
			}
			
			showNextCard();
		}

		private void showNextCard() {
			currentCard = cardList.get(currentCardIndex);
			currentCardIndex++;
			display.setText(currentCard.getQuestion());
			nextButton.setText("Show Answer");
			isShowAnswer = true;
			
		}

		private void makeCard(String line) {
			String lineToParse = null;
			String[] result = lineToParse.split("/");
			QuizCard card = new QuizCard(result[0], result[1]);
			cardList.add(card);
			System.out.println("Made a card");
			
		}

	}

}
