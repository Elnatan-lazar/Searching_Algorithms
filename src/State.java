import java.awt.*;
import java.util.Arrays;

public class State   {
    static int genrealIndex = 0;
    private char[][] board; // Current board state
    private int cost; // Total cost to reach this state
    private int index;
    private boolean isOut;
    private String[] lastAction;
    private State perent;


    State(char[][] board, int cost, State perent) {
        this.board = copyBoard(board);
        this.cost = cost;
        this.perent = perent;
        genrealIndex++;
        this.index = genrealIndex;
        this.isOut = false;
        lastAction = new String[3];
    }

    State(char[][] board, int cost) {
        this.board = copyBoard(board);
        this.cost = cost;
        this.perent = null;
        genrealIndex++;
        this.index = genrealIndex;
        this.isOut = false;
        lastAction = new String[3];
    }

    public int getIndex() {
        return this.index;
    }


    public String getPathString() {
        String ans = this.getActions();
        State temp = this.perent;
        while (temp.perent != null) {
            ans = temp.getActions() + "--" + ans;
            temp = temp.perent;
        }
        return ans;

    }

    public String getActions() {
        return "(" + lastAction[1] + "):" + lastAction[0] + ":(" + lastAction[2] + ")";
    }

    public void setLastAction(int i, int j, Point To) {
        // the array will remember the origin location the destination and the collor of the last action
        lastAction[0] = Character.toString(this.board[To.x][To.y]);
        lastAction[1] = Integer.toString(i + 1) + "," + Integer.toString(j + 1);
        lastAction[2] = Integer.toString(To.x + 1) + "," + Integer.toString(To.y + 1);
    }

    public boolean isReversedAction(int i, int j, Point to) {
        //c == color
        //f == from
        //t == to
        String c = Character.toString(this.board[i][j]);
        String f = Integer.toString(i + 1) + "," + Integer.toString(j + 1);
        String t = Integer.toString(to.x + 1) + "," + Integer.toString(to.y + 1);

        // if the color of this action is the same as the last action
        // & the origin location of this action is the same as the distinction of the last action
        // & the destination of this action is the same as the origin location of the last action
        // it mean this is a reversed action
        return c.equals(lastAction[0]) && f.equals(lastAction[2]) && t.equals(lastAction[1]);


    }


    public boolean getIsOut() {
        return isOut;
    }

    public void setOut() {
        isOut = true;
    }

    public void setNotOut() {
        isOut = false;
    }

    // Helper to copy the board
    private char[][] copyBoard(char[][] original) {
        char[][] copy = new char[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

    public char[][] copyBoardWithSwap(Point p1, Point p2) {
        char[][] copy = new char[this.board.length][this.board[0].length];
        for (int i = 0; i < this.board.length; i++) {
            System.arraycopy(this.board[i], 0, copy[i], 0, this.board[i].length);
        }
        char temp = copy[p1.x][p1.y];
        copy[p1.x][p1.y] = copy[p2.x][p2.y];
        copy[p2.x][p2.y] = temp;

        return copy;
    }


    public char getValue(Point p) {
        return getValue(p.x, p.y);
    }

    public char getValue(int i, int j) {
        return board[i][j];
    }

    // Check if two boards are equal
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State other)) return false;
        return Arrays.deepEquals(this.board, other.board);
    }

    @Override
    public String toString() {
        String boardString = "";
        for (char[] row : this.board) {
            for (char cell : row) {
                switch (cell) {
                    case 'R': // Red
                        boardString += "\u001B[31m0\u001B[0m"; // ANSI escape code for red
                        break;
                    case 'G': // Green
                        boardString += "\u001B[32m0\u001B[0m"; // ANSI escape code for green
                        break;
                    case 'B': // Blue
                        boardString += "\u001B[34m0\u001B[0m"; // ANSI escape code for blue
                        break;
                    case 'X': // Black
                        boardString += "\u001B[30m0\u001B[0m"; // ANSI escape code for black
                        break;
                    case '_': // Empty space
                        boardString += " ";
                        break;
                }
                boardString += " "; // Add spacing between cells
            }
            boardString += "\n"; // Move to the next row
        }
        return "------------state numner " + this.index + " ----------" + "\n" + this.getActions() + "\n" + "Cost: " + cost + "\n" + boardString;
    }

    @Override
    public int hashCode() {

        return Arrays.deepHashCode(this.board);
    }

    public char[][] getBoard() {
        return board;
    }

    public int getCost() {
        return cost;
    }
    public State getPerent(){
        return  this.perent;
    }


}



