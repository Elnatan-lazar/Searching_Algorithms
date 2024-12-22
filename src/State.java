import java.awt.*;
import java.util.Arrays;

public class State implements Comparable<State>{
    static int genrealIndex=0;
    private char[][] board; // Current board state
    private int cost; // Total cost to reach this state
    private String actions; // Sequence of actions
    private int index;
    private int F=0;
    private int hursticValue=0;
    private boolean isOut;
    private String[] lastAction;


    State(char[][] board, int cost, String actions) {
        this.board = copyBoard(board);
        this.cost = cost;
        this.actions = actions;
        genrealIndex++;
        this.index=genrealIndex;
        this.isOut=false;
        lastAction=new String[3];
    }

    public void setLastAction(int i,int j,Point To){
        // the array will remember the origin location the destination and the collor of the last action
        lastAction[0]=Character.toString(this.board[To.x][To.y]);
        lastAction[1]=Integer.toString(i)+","+Integer.toString(j);
        lastAction[2]=Integer.toString(To.x)+","+Integer.toString(To.y);
    }
    public boolean isReversedAction(int i,int j,Point to){
        //c == color
        //f == from
        //t == to
        String c=Character.toString(this.board[i][j]);
        String f=lastAction[1]=Integer.toString(i)+","+Integer.toString(j);
        String t=lastAction[1]=Integer.toString(to.x)+","+Integer.toString(to.y);

        // if the color of this action is the same as the last action
        // & the origin location of this action is the same as the distinction of the last action
        // & the destination of this action is the same as the origin location of the last action
        // it mean this is a reversed action
        return c.equals(lastAction[0])  && f.equals(lastAction[2])&&t.equals(lastAction[1]) ;


    }

    public int getF(){
        return  F;
    }
    public void setF(int f){
        F=f;
    }
    public int getHursticValue(){
        return  hursticValue;
    }
    public void setHursticValue(int hursticValue){
        this.hursticValue=hursticValue;
        setF(this.hursticValue+this.cost);
    }
    public boolean getIsOut(){
        return isOut;
    }
    public void setOut(){
        isOut=true;
    }
    public void setNotOut(){
        isOut=false;
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

    @Override
    public int compareTo(State o) {
        return Integer.compare(this.F,o.F);
    }
}



