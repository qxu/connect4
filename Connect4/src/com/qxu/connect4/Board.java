package com.qxu.connect4;

import java.util.ArrayList;
import java.util.List;

public class Board {
	private int numOfColumns;
	private int numOfRows;

	private Player[][] board;
	
	private int pieceCount;

	public Board(int numOfColumns, int numOfRows) {
		this.numOfColumns = numOfColumns;
		this.numOfRows = numOfRows;
		this.board = new Player[numOfColumns][numOfRows];
		reset();
	}

	public int addPiece(int c, Player p) {
		for (int r = 0; r < numOfRows; r++) {
			if (board[c][r] == null) {
				board[c][r] = p;
				pieceCount++;
				return r;
			}
		}
		return -1;
	}

	public Player getPiece(int c, int r) {
		return board[c][r];
	}

	public int getNumOfColumns() {
		return numOfColumns;
	}

	public int getNumOfRows() {
		return numOfRows;
	}
	
	public int getPieceCount() {
		return pieceCount;
	}

	public void reset() {
		for (int c = 0; c < numOfColumns; c++)
			for (int r = 0; r < numOfRows; r++)
				board[c][r] = null;
		this.pieceCount = 0;
	}

	@Override
	public String toString() {
		List<StringBuilder> rowBuilders = new ArrayList<>(numOfRows);
		for (int r = 0; r < numOfRows; r++) {
			StringBuilder sb = new StringBuilder();
			sb.append("|");
			rowBuilders.add(sb);
		}
		StringBuilder headerSB = new StringBuilder(" ");
		for (int c = 0; c < numOfColumns; c++) {
			headerSB.append(c);
			int minRowSBLength = headerSB.length();

			for (int r = 0; r < numOfRows; r++) {
				StringBuilder sb = rowBuilders.get(r);
				Player p = getPiece(c, r);
				if (p != null) {
					sb.append(getPiece(c, r));
				}
				if (sb.length() > minRowSBLength) {
					minRowSBLength = sb.length();
				}
			}
			for (int r = 0; r < numOfRows; r++) {
				StringBuilder sb = rowBuilders.get(r);
				sb.ensureCapacity(minRowSBLength + 1);
				for (int i = sb.length(); i < minRowSBLength; i++) {
					sb.append(' ');
				}
				sb.append("|");
			}
			headerSB.ensureCapacity(minRowSBLength + 1);
			for (int i = headerSB.length(); i < minRowSBLength; i++) {
				headerSB.append(' ');
			}
			headerSB.append(' ');
		}
		headerSB.append("\n");
		for (int i = numOfRows - 1; i >= 0; i--) {
			headerSB.append(rowBuilders.get(i));
			headerSB.append("\n");
		}
		return headerSB.toString();
	}
}
