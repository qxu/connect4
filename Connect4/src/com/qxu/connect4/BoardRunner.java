package com.qxu.connect4;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardRunner implements Runnable {

	private Board board;
	private BoardComponent bc;

	private Player p1;
	private Player p2;

	private Player turn;

	private Deque<Integer> moves;
	
	private boolean gameEnd = true;

	public BoardRunner() {
		p1 = new Player("Gray");
		p2 = new Player("Red");

		board = new Board(8, 7);
		bc = new BoardComponent(board);
		bc.setPlayerColor(p1, Color.DARK_GRAY);
		bc.setPlayerColor(p2, Color.RED);
		
		moves = new ArrayDeque<>();
	}

	@Override
	public void run() {
		gameEnd = false;
		turn = p1;
		bc.setTurn(turn);

		BCMouseListener bcml = new BCMouseListener();
		bc.addMouseListener(bcml);
		bc.addMouseMotionListener(bcml);
	
		BCKeyListener bckl = new BCKeyListener();
		bc.setFocusable(true);
		bc.addKeyListener(bckl);

		bc.setShowColumnLabels(true);
		bc.setShowSelectedColumn(true);
		bc.setShowLineMark(false);
		bc.setShowRoundEnd(false);

		JFrame frame = new JFrame("Connect 4");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		panel.add(bc);
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	private void addPiece(int c) {
		int r = board.addPiece(c, turn);
		if (r >= 0) {
			moves.add(c);
			int mpc = getMatches(c, r, 1, 0);
			int mnc = getMatches(c, r, -1, 0);
			int mpcpr = getMatches(c, r, 1, 1);
			int mncnr = getMatches(c, r, -1, -1);
			int mpr = getMatches(c, r, 0, 1);
			int mnr = getMatches(c, r, 0, -1);
			int mncpr = getMatches(c, r, -1, 1);
			int mpcnr = getMatches(c, r, 1, -1);
//			System.out.println(mncpr + " " + mpr + " " + mpcpr);
//			System.out.println(mnc + " " + " " + " " + mpc);
//			System.out.println(mncnr + " " + mnr + " " + mpcnr);
			if (mpc + mnc >= 3) {
				endGame(c + mpc, r, c - mnc, r);
				return;
			}
			if (mpcpr + mncnr >= 3) {
				endGame(c + mpcpr, r + mpcpr, c - mncnr, r - mncnr);
				return;
			}
			if (mpr + mnr >= 3) {
				endGame(c, r + mpr, c, r - mnr);
				return;
			}
			if (mncpr + mpcnr >= 3) {
				endGame(c - mncpr, r + mncpr, c + mpcnr, r - mpcnr);
				return;
			}
			if (isFull()) {
				gameEnd = true;
				bc.setShowSelectedColumn(false);
				bc.setShowLineMark(false);
				bc.setRoundEndText("tie");
				bc.setShowRoundEnd(true);
				bc.repaint();
				return;
			}
			turn = turn.equals(p1) ? p2 : p1;
			bc.setTurn(turn);
			bc.repaint();
		}
	}
	
	private void undo() {
		if (!moves.isEmpty()) {
			if (gameEnd) {
				gameEnd = false;
				bc.setShowSelectedColumn(true);
				bc.setShowLineMark(false);
				bc.setShowRoundEnd(false);moves.removeLast();
				board.reset();
				if (moves.size() % 2 != 0)
					turn = turn.equals(p1) ? p2 : p1;
				for (int c : moves) {
					board.addPiece(c, turn);
					turn = turn.equals(p1) ? p2 : p1;
				}
				bc.repaint();
			} else {
				moves.removeLast();
				board.reset();
				if (moves.size() % 2 == 0)
					turn = turn.equals(p1) ? p2 : p1;
				for (int c : moves) {
					board.addPiece(c, turn);
					turn = turn.equals(p1) ? p2 : p1;
				}
				bc.repaint();
			}
		}
	}
	
	private void newGame() {
		gameEnd = false;
		board.reset();
		moves.clear();
		bc.setShowSelectedColumn(true);
		bc.setShowLineMark(false);
		bc.setShowRoundEnd(false);
		bc.repaint();
	}
	
	private void endGame(int c0, int r0, int c1, int r1) {
		gameEnd = true;
		bc.setShowSelectedColumn(false);
		bc.setLineMark(c0, r0, c1, r1);
		bc.setShowLineMark(true);
		bc.setRoundEndText(turn.getName() + " wins");
		bc.setShowRoundEnd(true);
		bc.repaint();
	}

	private boolean isFull() {
		int maxSize = board.getNumOfColumns() * board.getNumOfRows();
		return board.getPieceCount() >= maxSize;
	}

	private int getMatches(int c0, int r0, int dc, int dr) {
		Player p = board.getPiece(c0, r0);
		int count = 0;
		int c = c0 + dc;
		int r = r0 + dr;
		while (p.equals(checkPiece(c, r))) {
			count++;
			c += dc;
			r += dr;
		}
		return count;
	}

	private Player checkPiece(int c, int r) {
		if (c >= 0 && c < board.getNumOfColumns() && r >= 0
				&& r < board.getNumOfRows()) {
			return board.getPiece(c, r);
		}
		return null;
	}

	private class BCMouseListener extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (gameEnd) {
				newGame();
			} else {
				int cellWidth = bc.getWidth() / board.getNumOfColumns();
				int c = e.getX() / cellWidth;
				if (c >= 0 && c < board.getNumOfColumns()) {
					addPiece(c);
				}
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (!gameEnd) {
				int cellWidth = bc.getWidth() / board.getNumOfColumns();
				int c = e.getX() / cellWidth;
				if (c >= 0 && c < board.getNumOfColumns()) {
					bc.setSelectedColumn(c);
					bc.repaint();
				}
			}
		}

		public void mouseMoved(MouseEvent e) {
			if (!gameEnd) {
				int cellWidth = bc.getWidth() / board.getNumOfColumns();
				int c = e.getX() / cellWidth;
				if (c >= 0 && c < board.getNumOfColumns()) {
					bc.setSelectedColumn(c);
					bc.repaint();
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!gameEnd) {
				bc.setSelectedColumn(-1);
				bc.repaint();
			}
		}
	}

	private class BCKeyListener extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == 26) {
				undo();
			} else if (gameEnd) {
				if (e.getKeyChar() == ' ' || e.getKeyChar() == '\n') {
					newGame();
				}
			} else {
				int numOfCols = board.getNumOfColumns();
				int chNorm = e.getKeyChar() - '1';
				if (chNorm == -1)
					chNorm = 9;
				if (numOfCols <= 10 && (chNorm >= 0 && chNorm < numOfCols)) {
					addPiece(chNorm);
				}
			}
		}
	}
}
