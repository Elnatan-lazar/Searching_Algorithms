import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Ex1 {
    static final Map<Character, Integer> costTable = new HashMap<>();

    // Static block to initialize the cost table
    static {
        costTable.put('B', 1); // Blue
        costTable.put('G', 3); // Green
        costTable.put('R', 10); // Red
    }



    public static void main(String[] args) throws IOException {
        String fileName = "src\\input.txt";
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
                BFS(writer,board,goalBoard,openList);
                break;
            case "DFID":
                DFID(writer,board,goalBoard,openList);
                break;
            case "A*":
                A_STAR(writer,board,goalBoard,openList);
                break;
            case "IDA*":
                IDA_STAR(writer,board,goalBoard,openList);
                break;
            case "DFBnB":
                DFBnB(writer,board,goalBoard,openList);
                break;
            default:
                System.err.println("Invalid algorithm specified.");
                return;
        }
        long endTime = System.currentTimeMillis();
        long elapsedTimeMillis = endTime - startTime; // Time elapsed in milliseconds
        double elapsedTimeSeconds = elapsedTimeMillis / 1000.0; // Convert to seconds
        if(time){
            writer.write(elapsedTimeSeconds+" seconds"); // writing the time in the forth row
            writer.flush();
        }
        System.out.println(writer);





    }

    public static void BFS(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean openList) throws IOException {
        String operations = "";
        int cost = 0;
        int numberOfVertices = 0;

        // Directions for circular movement (right, down, left, up)
        final int[][] DIRECTIONS = {
                {0, 1}, // Right
                {1, 0}, // Down
                {0, -1}, // Left
                {-1, 0} // Up
        };

        // BFS setup
        Queue<State> queue = new LinkedList<>();
        Set<char[][]> closedSet = new HashSet<>(); // Closed list
        Set<char[][]> openSet = new HashSet<>(); // Open list



        // Initial state
        State initialState = new State(board, 0, "");
        queue.add(initialState);
        // if by lucky the board is also the target board finish the search
        if (Arrays.deepEquals(board, goalBoard)) {
            writer.write(operations + "\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: " +cost + "\n");
            writer.flush();
            return;
        }




        while (!queue.isEmpty()) {
            if(openList){
                System.out.println("######### open-list #####3##");
               for( State s:queue){
                   System.out.println(s);

               }
            }
            State current = queue.poll();
            openSet.remove(current.getBoard());
            // Add to closed set
            closedSet.add(current.getBoard());

           // Explore opertion for each cell
            for (int i = 0; i <3 ; i++) {
                for (int j = 0; j <3 ; j++) {
                    Point currentPosition=new Point(i,j);
                    // Skip if the target cell isn't a ball.
                    if (current.getBoard()[i][j] == 'X' ||current.getBoard()[i][j]=='_') continue;
                    Point left = new Point((i - 1 + 3) % 3, j);  // Left neighbor
                    Point right = new Point((i + 1) % 3, j);     // Right neighbor
                    Point up = new Point(i, (j + 1) % 3);        // Up neighbor
                    Point down = new Point(i, (j - 1 + 3) % 3);  // Down neighbor
                    ArrayList<Point> points=new ArrayList<>();
                    points.add(left);
                    points.add(right);
                    points.add(up);
                    points.add(down);
                    for(Point p:points)     // checking all the possible direction
                    {
                        if(current.getValue(p)=='_'){    // if the left position is open add the new state with the new calculation

                            char [][]newBoard= current.copyBoardWithSwap(currentPosition,p);

                            if(!openSet.contains(newBoard) && !closedSet.contains(newBoard)) // if the new state not in the open or close list add it
                            {
                                numberOfVertices++; // update the number of vertices
                                int newCost=current.getCost()+costTable.get(current.getValue(currentPosition));
                                String newAction= current.getActions();
                                if (newAction!="")
                                {
                                    newAction+="--";
                                }
                                newAction+="("+ (currentPosition.x+1) +","+ (currentPosition.y+1)  + "):"
                                        +current.getValue(currentPosition)
                                        +":("+ (p.x+1) +","+ (p.y+1) + ")";
                                State newState=new State(newBoard,newCost,newAction);


                                if(Arrays.deepEquals(newBoard, goalBoard))  // if we reach to the goal
                                {
                                    writer.write(newState.getActions() + "\n");
                                    writer.write("Num: " + numberOfVertices + "\n");
                                    writer.write("Cost: " + newState.getCost() + "\n");
                                    writer.flush();
                                    return;
                                }

                                openSet.add(newBoard);
                                queue.add(newState);


                                }

                            }

                        }
                    }
                }
            }


        writer.write("no path\n");
        writer.write("Num: " + numberOfVertices + "\n");
        writer.write("Cost: inf\n");
        writer.flush();

    }

    public static void DFID(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean openList) throws IOException {
        String operations="";
        int cost=0;
        int numberOfVertices=0;
        boolean noSolution=false;



    }

    public static void A_STAR(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean openList) throws IOException {
        String operations="";
        int cost=0;
        int numberOfVertices=0;
        boolean noSolution=false;



    }

    public static void IDA_STAR(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean openList) throws IOException {
        String operations="";
        int cost=0;
        int numberOfVertices=0;
        boolean noSolution=false;




    }
    public static void DFBnB(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean openList) throws IOException {
        String operations="";
        int cost=0;
        int numberOfVertices=0;
        boolean noSolution=false;



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
