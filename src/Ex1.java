import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Ex1 {
    static final Map<Character, Integer> costTable = new HashMap<>();

    // Static block to initialize the cost table
    static {
        costTable.put('B', 1); // Blue
        costTable.put('G', 3); // Green
        costTable.put('R', 10); // Red
    }



    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\elnatan\\IdeaProjects\\Ex1\\src\\input.txt";
        List<String> lines = null;

        // Read the input file
        try {
            lines = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return; // Exit if the file cannot be read
        }

        // Extract input details
        String algo = lines.get(0);
        Boolean time = lines.get(1).equals("with time");
        Boolean openList = lines.get(2).equals("with open");

        // Parse the current board state
        char[][] board = new char[3][3];
        String[] line1 = lines.get(3).split(",");
        String[] line2 = lines.get(4).split(",");
        String[] line3 = lines.get(5).split(",");

        for (int i = 0; i < 3; i++) {
            board[0][i] = line1[i].charAt(0);
            board[1][i] = line2[i].charAt(0);
            board[2][i] = line3[i].charAt(0);
        }

        // Parse the goal state board
        char[][] goalBoard = new char[3][3];
        String[] goalLine1 = lines.get(7).split(",");
        String[] goalLine2 = lines.get(8).split(",");
        String[] goalLine3 = lines.get(9).split(",");

        for (int i = 0; i < 3; i++) {
            goalBoard[0][i] = goalLine1[i].charAt(0);
            goalBoard[1][i] = goalLine2[i].charAt(0);
            goalBoard[2][i] = goalLine3[i].charAt(0);
        }
        String outputFile = "output.txt";



        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        long startTime = System.currentTimeMillis();
        // Handle algorithm selection
        switch (algo) {
            case "BFS":
                System.out.println("Running BFS...");
                // Add BFS implementation here
                break;
            case "DFID":
                System.out.println("Running DFID...");
                // Add DFID implementation here
                break;
            case "A*":
                System.out.println("Running A*...");
                // Add A* implementation here
                break;
            case "IDA*":
                System.out.println("Running IDA*...");
                // Add IDA* implementation here
                break;
            case "DFBnB":
                System.out.println("Running DFBnB...");
                // Add DFBnB implementation here
                break;
            default:
                System.err.println("Invalid algorithm specified.");
                return;
        }
        long endTime = System.currentTimeMillis();
        long elapsedTimeMillis = endTime - startTime; // Time elapsed in milliseconds
        double elapsedTimeSeconds = elapsedTimeMillis / 1000.0; // Convert to seconds
        if(time){
            writer.write("\n"+elapsedTimeSeconds+" seconds"); // writing the time in the forth row
            writer.flush();
        }





    }

    public static void BFS(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean openList) throws IOException {
        String operations="";
        int cost=0;
        int numberOfVertices=0;
        boolean noSolution=false;


        if (noSolution){
            writer.write(operations + "\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: " + cost);
            writer.flush();
        }
        else{
            writer.write("no path\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: inf");
            writer.flush();
        }

    }

    public static void DFID(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean openList) throws IOException {
        String operations="";
        int cost=0;
        int numberOfVertices=0;
        boolean noSolution=false;


        if (noSolution){
            writer.write(operations + "\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: " + cost);
            writer.flush();
        }
        else{
            writer.write("no path\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: inf");
            writer.flush();
        }

    }

    public static void A_STAR(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean openList) throws IOException {
        String operations="";
        int cost=0;
        int numberOfVertices=0;
        boolean noSolution=false;


        if (noSolution){
            writer.write(operations + "\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: " + cost);
            writer.flush();
        }
        else{
            writer.write("no path\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: inf");
            writer.flush();
        }
    }

    public static void IDA_STAR(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean openList) throws IOException {
        String operations="";
        int cost=0;
        int numberOfVertices=0;
        boolean noSolution=false;


        if (noSolution){
            writer.write(operations + "\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: " + cost);
            writer.flush();
        }
        else{
            writer.write("no path\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: inf");
            writer.flush();
        }
    }
    public static void DFBnB(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean openList) throws IOException {
        String operations="";
        int cost=0;
        int numberOfVertices=0;
        boolean noSolution=false;


        if (noSolution){
            writer.write(operations + "\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: " + cost);
            writer.flush();
        }
        else{
            writer.write("no path\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: inf");
            writer.flush();
        }
    }


    public static void printBoard(char[][] board) {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
