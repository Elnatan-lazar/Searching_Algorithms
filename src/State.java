import java.awt.*;
import java.util.Arrays;

public class State {
    static int genrealIndex=0;
    private char[][] board; // Current board state
    private int cost; // Total cost to reach this state
    private String actions; // Sequence of actions
    private int index;
    private boolean isOut;
    State(char[][] board, int cost, String actions) {
        this.board = copyBoard(board);
        this.cost = cost;
        this.actions = actions;
        genrealIndex++;
        this.index=genrealIndex;
        this.isOut=false;
    }
    public boolean getIsOut(){
        return isOut;
    }
    public void setOut(){
        isOut=true;
    }

    // Helper to copy the board
    private char[][] copyBoard(char[][] original) {
        char[][] copy = new char[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }
    public char[][] copyBoardWithSwap(Point p1,Point p2) {
        char[][] copy = new char[this.board.length][this.board[0].length];
        for (int i = 0; i < this.board.length; i++) {
            System.arraycopy(this.board[i], 0, copy[i], 0, this.board[i].length);
        }
        char temp=copy[p1.x][p1.y];
        copy[p1.x][p1.y]=copy[p2.x][p2.y];
        copy[p2.x][p2.y]=temp;

        return copy;
    }


    public void swap(Point p1,Point p2){
        char temp=this.getValue(p1);
        this.setValue(p1,this.getValue(p2));
        this.setValue(p2,this.getValue(p1));
    }
    public void setValue(Point p,char a){
        board[p.x][p.y]=a;
    }

    public char getValue(Point p){
        return getValue(p.x, p.y);
    }
    public char getValue(int i, int j){
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
        String boardString="";
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
        return "------------state numner "+this.index+" ----------"+"\n"+ this.actions + "\n"+"Cost: "+cost+"\n"+ boardString;
    }

    @Override
    public int hashCode() {

        return Arrays.deepHashCode(this.board);
    }
    public char[][] getBoard(){
        return board;
    }

    public int getCost() {
        return cost;
    }

    public String getActions() {
        return actions;
    }

    public static String boardToString(char[][] board) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : board) {
            for (char cell : row) {
                sb.append(cell);
            }
        }
        return sb.toString();
    }
}



