import java.awt.*;
import java.util.Arrays;

public class State {
    private char[][] board; // Current board state
    private int cost; // Total cost to reach this state
    private String actions; // Sequence of actions

    State(char[][] board, int cost, String actions) {
        this.board = copyBoard(board);
        this.cost = cost;
        this.actions = actions;
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
}



