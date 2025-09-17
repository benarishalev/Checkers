import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;

public class Piece {
    protected Point position;
    protected Point2D.Float drawPosition;
    protected float speed;
    protected Color color;
    protected String type;
    protected boolean isSelected = false;
    protected boolean isKing = false;
    protected boolean isCaptured = false; // Track if the piece is captured
    private Point opponentPiecePosition; // Store the position of the opponent's piece for later capture

    private static final int GRID_SIZE = 75; // Size of each square on the board

    public Piece(int x, int y, Color color, String type) {
        this.position = new Point(x, y);
        this.color = color;
        this.type = type;
        this.speed = 3;
        this.drawPosition = new Point2D.Float(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isKing() {
        return isKing;
    }

    public void makeKing() {
        this.isKing = true;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void setCaptured(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }

    public Point getOpponentPiecePosition() {
        return opponentPiecePosition;
    }

    public void setOpponentPiecePosition(Point opponentPiecePosition) {
        this.opponentPiecePosition = opponentPiecePosition;
    }

    public void draw(Graphics g, int diameter, int x, int y) {
        if (!isCaptured) { // Only draw if the piece is not captured
            // move the piece to his position
            // with smooth motion
            this.drawPosition.x += (this.position.x - this.drawPosition.x) / this.speed;
            this.drawPosition.y += (this.position.y - this.drawPosition.y) / this.speed;

            // draw a greay outline
            // and the piece color inside
            g.setColor(new Color(50, 50, 50));
            diameter += 10;
            g.fillOval(x + (int)drawPosition.x - diameter / 2, y + (int)drawPosition.y - diameter / 2, diameter, diameter);
            g.setColor(isSelected ? Color.YELLOW : color);
            diameter -= 10;
            g.fillOval(x + (int)drawPosition.x - diameter / 2, y + (int)drawPosition.y - diameter / 2, diameter, diameter);
        }
    }

    public boolean isInsideCircle(Point point, int diameter) {
        return point.distance(position) <= diameter / 2;
    }

    public List<Point> getCapturePossibleMoves(Piece[][] board, int BOARD_SIZE) {
        List<Point> captureMoves = new ArrayList<>();
        int x = position.x;
        int y = position.y;

        if (isKing) {
            addAllDirectionalMoves(captureMoves, board, x, y);
        } else {
            addDiagonalCaptureMoves(captureMoves, board, x, y);
        }

        return captureMoves;
    }

    public List<Point> getPossibleMoves(Piece[][] board, int BOARD_SIZE) {
        List<Point> possibleMoves = new ArrayList<>();
        int x = position.x;
        int y = position.y;

        if (isKing) {
            addAllDirectionalMoves(possibleMoves, board, x, y);
        } else {
            addDiagonalMoves(possibleMoves, board, x, y);
        }

        return possibleMoves;
    }

    protected void addDiagonalCaptureMoves(List<Point> possibleMoves, Piece[][] board, int x, int y) {
        if (type.equals("white")) {
            // White pieces move "down" the board
            addIfValidMove(possibleMoves, board, new Point(x - GRID_SIZE, y + GRID_SIZE)); // Left diagonal down
            addIfValidMove(possibleMoves, board, new Point(x + GRID_SIZE, y + GRID_SIZE)); // Right diagonal down
            addIfValidMove(possibleMoves, board, new Point(x - GRID_SIZE*2, y + GRID_SIZE*2)); // Left diagonal down 2
            addIfValidMove(possibleMoves, board, new Point(x + GRID_SIZE*2, y + GRID_SIZE*2)); // Right diagonal down 2

        } else if (type.equals("black")) {
            // Black pieces move "up" the board
            addIfValidMove(possibleMoves, board, new Point(x - GRID_SIZE, y - GRID_SIZE)); // Left diagonal up
            addIfValidMove(possibleMoves, board, new Point(x + GRID_SIZE, y - GRID_SIZE)); // Right diagonal up
            addIfValidMove(possibleMoves, board, new Point(x - GRID_SIZE*2, y - GRID_SIZE*2)); // Left diagonal up 2
            addIfValidMove(possibleMoves, board, new Point(x + GRID_SIZE*2, y - GRID_SIZE*2)); // Right diagonal up 2
        }
    }

    protected void addDiagonalMoves(List<Point> possibleMoves, Piece[][] board, int x, int y) {
        if (type.equals("white")) {
            // White pieces move "down" the board
            addIfValidMove(possibleMoves, board, new Point(x - GRID_SIZE, y + GRID_SIZE)); // Left diagonal down
            addIfValidMove(possibleMoves, board, new Point(x + GRID_SIZE, y + GRID_SIZE)); // Right diagonal down
        } else if (type.equals("black")) {
            // Black pieces move "up" the board
            addIfValidMove(possibleMoves, board, new Point(x - GRID_SIZE, y - GRID_SIZE)); // Left diagonal up
            addIfValidMove(possibleMoves, board, new Point(x + GRID_SIZE, y - GRID_SIZE)); // Right diagonal up
        }
    }

    protected void addAllDirectionalMoves(List<Point> possibleMoves, Piece[][] board, int x, int y) {
        // Add all possible moves for a king piece
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dy = -1; dy <= 1; dy += 2) {
                int step = 1;
                while (true) {

                    // back
                    Point move = new Point(x + step * dx * GRID_SIZE, y + step * dy * GRID_SIZE);
                    if (!isWithinBoard(move)) {
                        break;
                    }
                    if (isDestinationEmpty(board, move)) {
                        possibleMoves.add(move);
                    }
                    step++;
                }
            }
        }
    }

    protected boolean isWithinBoard(Point point) {
        return point.x >= 0 && point.x < GRID_SIZE * 8 && point.y >= 0 && point.y < GRID_SIZE * 8;
    }

    protected boolean isDestinationEmpty(Piece[][] board, Point point) {
        int gridX = point.x / GRID_SIZE;
        int gridY = point.y / GRID_SIZE;
        return board[gridY][gridX] == null;
    }

    protected void addIfValidMove(List<Point> possibleMoves, Piece[][] board, Point move) {
        if (isWithinBoard(move) && isDestinationEmpty(board, move)) {
            possibleMoves.add(move);
        }
    }
}
