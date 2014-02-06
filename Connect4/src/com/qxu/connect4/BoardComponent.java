package com.qxu.connect4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

public class BoardComponent extends JComponent {

	private Board board;

	private Map<Player, Color> playerColorMap;

	private boolean showTurn = false;
	private Player turn;
	
	private boolean showColumnLabels = false;
	private Color columnLabelColor = Color.BLACK;

	private boolean showSelectedColumn = false;
	private int selectedColumn;
	private Color selectedColumnColor = Color.BLACK;

	private boolean showLineMark = false;
	private int lineMarkC0;
	private int lineMarkR0;
	private int lineMarkC1;
	private int lineMarkR1;
	private Color lineMarkColor = Color.BLACK;

	private boolean showRoundEnd = false;
	private String roundEndText = null;
	private Color roundEndTextColor;

	public BoardComponent(Board b) {
		super();
		this.board = b;
		this.playerColorMap = new HashMap<>();

		Dimension prefSize = new Dimension(b.getNumOfColumns() * 80,
				b.getNumOfRows() * 80);
		setPreferredSize(prefSize);
		setSize(prefSize);
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	public Color getPlayerColor(Player p) {
		return playerColorMap.get(p);
	}

	public void setPlayerColor(Player p, Color c) {
		playerColorMap.put(p, c);
		repaint();
	}

	public boolean getShowTurn() {
		return showTurn;
	}

	public void setShowTurn(boolean show) {
		this.showTurn = show;
	}
	
	public boolean getShowColumnLabels() {
		return showColumnLabels;
	}
	
	public void setShowColumnLabels(boolean show) {
		this.showColumnLabels = show;
	}

	public Color getShowColumnLabelColor() {
		return columnLabelColor;
	}

	public void setShowColumnLabelColor(Color c) {
		this.columnLabelColor = c;
	}

	public boolean getShowSelectedColumn() {
		return showSelectedColumn;
	}

	public void setShowSelectedColumn(boolean show) {
		this.showSelectedColumn = show;
	}

	public Color getSelectedColumnColor() {
		return selectedColumnColor;
	}

	public void setSelectedColumnColor(Color c) {
		this.selectedColumnColor = c;
	}

	public Color getLineMarkColor() {
		return lineMarkColor;
	}

	public void setLineMarkColor(Color c) {
		this.lineMarkColor = c;
	}

	public Color getRoundEndTextColor() {
		return roundEndTextColor;
	}

	public void setRoundEndTextColor(Color c) {
		this.roundEndTextColor = c;
	}

	public Player getTurn() {
		return turn;
	}

	public void setTurn(Player p) {
		this.turn = p;
	}

	public int getSelectedColumn() {
		return selectedColumn;
	}

	public void setSelectedColumn(int c) {
		this.selectedColumn = c;
	}

	public boolean getShowLineMark() {
		return showLineMark;
	}

	public void setShowLineMark(boolean show) {
		this.showLineMark = show;
	}

	public int[] getLineMark() {
		return new int[] { lineMarkC0, lineMarkR0, lineMarkC1, lineMarkR1 };
	}

	public void setLineMark(int c0, int r0, int c1, int r1) {
		this.lineMarkC0 = c0;
		this.lineMarkR0 = r0;
		this.lineMarkC1 = c1;
		this.lineMarkR1 = r1;
	}

	public boolean getShowRoundEnd() {
		return showRoundEnd;
	}

	public void setShowRoundEnd(boolean show) {
		this.showRoundEnd = show;
	}

	public String getRoundEndText() {
		return roundEndText;
	}

	public void setRoundEndText(String text) {
		this.roundEndText = text;
	}

	@Override
	protected void paintComponent(Graphics g0) {
		Graphics2D g = (Graphics2D) g0;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (board != null) {
			int width = getWidth();
			int height = getHeight();

			int cellWidth = width / board.getNumOfColumns();
			int cellHeight = height / board.getNumOfRows();

			width = cellWidth * board.getNumOfColumns();
			height = cellHeight * board.getNumOfRows();

			for (int c = 0; c < board.getNumOfColumns(); c++) {
				for (int r = 0; r < board.getNumOfRows(); r++) {
					Player p = board.getPiece(c, r);
					if (p != null) {
						Color color = playerColorMap.get(p);
						if (color == null)
							color = Color.BLACK;
						g.setColor(color);
						g.fillOval(c * cellWidth,
								height - (r + 1) * cellHeight, cellWidth,
								cellHeight);
					}
				}
			}

			if (turn != null) {
				Color turnColor = playerColorMap.get(turn);
				if (turnColor != null) {
					g.setColor(turnColor);
					int triWidth = Math.max(cellWidth / 6, 4);
					int triHeight = Math.max(cellHeight / 6, 4);
					g.fillPolygon(new int[] { 0, triWidth, 0 }, new int[] { 0, 0,
							triHeight }, 3);
				}
			}

			if (showColumnLabels) {
				g.setColor(columnLabelColor);
				int fontSize = Math.max(height / 40, 10);
				g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, fontSize));
				FontMetrics fm = g.getFontMetrics();
				int labelHeight = fm.getHeight();
				for (int c = 0; c < board.getNumOfColumns(); c++) {
					String labelText = String.valueOf(c + 1);
					int labelWidth = fm.stringWidth(labelText);
					int x = c * cellWidth + cellWidth / 2 - labelWidth / 2;
					g.drawString(labelText, x, labelHeight);
				}
			}

			if (showSelectedColumn) {
				g.setColor(selectedColumnColor);
				int triCX = selectedColumn * cellWidth + cellWidth / 2;
				int halfTriWidth = Math.max(cellWidth / 12, 2);
				int triHeight = Math.max(cellHeight / 12, 2);
				g.fillPolygon(new int[] { triCX - halfTriWidth, triCX,
						triCX + halfTriWidth }, new int[] { 0, triHeight, 0 },
						3);
			}

			if (showLineMark) {
				g.setColor(lineMarkColor);
				int lineWidth = Math.max((cellWidth + cellHeight) / (2 * 16), 2);
				g.setStroke(new BasicStroke(lineWidth));
				int x0 = lineMarkC0 * cellWidth + cellWidth / 2;
				int y0 = height - (lineMarkR0 + 1) * cellHeight + cellHeight
						/ 2;
				int x1 = lineMarkC1 * cellWidth + cellWidth / 2;
				int y1 = height - (lineMarkR1 + 1) * cellHeight + cellHeight
						/ 2;
				g.drawLine(x0, y0, x1, y1);
			}

			if (showRoundEnd) {
				int bgRGB = getBackground().getRGB() & 0xffffff;
				g.setColor(new Color(bgRGB + (0x80 << 24), true));
				g.fillRect(0, 0, width, height);
				g.setColor(new Color(~bgRGB));
				int fontSize = Math.max(width / 10, 12);
				g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, fontSize));
				FontMetrics fm = g.getFontMetrics();
				g.drawString(roundEndText, 0, height - fm.getDescent());
			}
		}
	}

	private static final long serialVersionUID = 4482665250190261947L;
}
