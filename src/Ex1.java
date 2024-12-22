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
        String fileName = "input.txt";
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

        // in case we got lucky and the input is the answer
        if (Arrays.deepEquals(board, goalBoard)) {
            writer.write("\n");
            writer.write("Num: " + 0 + "\n");
            writer.write("Cost: " +0 + "\n");
            writer.flush();
            return;
        }


        // Handle algorithm selection
        switch (algo) {
            case "BFS" -> BFS(writer, board, goalBoard, openList);
            case "DFID" -> DFID(writer, board, goalBoard, openList);
            case "A*" -> A_STAR(writer, board, goalBoard, openList);
            case "IDA*" -> IDA_STAR(writer, board, goalBoard, openList);
            case "DFBnB" -> DFBnB(writer, board, goalBoard, openList);
            default -> {
                System.err.println("Invalid algorithm specified.");
                return;
            }
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

    public static int hurstic(char[][] board,char[][] goalBoard){
        int cost=0;
        for (int i = 0; i < goalBoard.length ; i++) {
            for (int j = 0; j <goalBoard[0].length ; j++) {
                if (goalBoard[i][j]!='X' && goalBoard[i][j]!='_'&&goalBoard[i][j]!=board[i][j]){

                    cost+=costTable.get(goalBoard[i][j]);

                }
            }
        }
        return  cost;
    }

    public static void BFS(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean withoOpenList) throws IOException {
        int numberOfVertices = 0;
        // BFS setup
        Queue<State> queue = new LinkedList<>();
        Set<State> closedSet = new HashSet<>(); // Closed list
        Set<State> openSet = new HashSet<>(); // Open list


        // Initial state
        State initialState = new State(board, 0, "");
        queue.add(initialState);



        while (!queue.isEmpty()) {

            if(withoOpenList){
                System.out.println("######### open-list ########");
               for( State s:queue){
                   System.out.println(s);

               }
            }
            State currentState = queue.poll();
            openSet.remove(currentState);

            // Add to closed set
            closedSet.add(currentState);

           // Explore opertion for each cell
            for (int i = 0; i <3 ; i++) {
                for (int j = 0; j <3 ; j++) {
                    Point currentPosition=new Point(i,j);

                    // Skip if the target cell isn't a marble.
                    if (currentState.getBoard()[i][j] == 'X' ||currentState.getBoard()[i][j]=='_')
                    {
                        continue;
                    }

                    Point left = new Point((i - 1 + 3) % 3, j);  // Left neighbor
                    Point right = new Point((i + 1) % 3, j);     // Right neighbor
                    Point up = new Point(i, (j + 1) % 3);        // Up neighbor
                    Point down = new Point(i, (j - 1 + 3) % 3);  // Down neighbor
                    ArrayList<Point> points=new ArrayList<>();
                    points.add(left);
                    points.add(up);
                    points.add(right);
                    points.add(down);

                    for(Point p:points)     // checking all the possible direction for each coordinate in the board
                    {
                        if(currentState.getValue(p)!='_'){
                            continue;
                        }

                        //check if it a revers action if it skip the action
                        if(currentState.isReversedAction(i,j,p)){
                            continue;
                        }
                        // if the position in point p (left/right/upd/down to (i,j) is open add the new state with the new calculation
                        char [][]newBoard= currentState.copyBoardWithSwap(currentPosition,p);
                        // update the number of vertices
                        int newCost=currentState.getCost()+costTable.get(currentState.getValue(currentPosition));
                        String newAction= currentState.getActions();
                        if (newAction!="")
                        {
                            newAction+="--";
                        }
                        newAction+="("+ (currentPosition.x+1) +","+ (currentPosition.y+1)  + "):"
                                +currentState.getValue(currentPosition)
                                +":("+ (p.x+1) +","+ (p.y+1) + ")";
                        State newState=new State(newBoard,newCost,newAction);
                        newState.setLastAction(i,j,p);
                        numberOfVertices++;

                        if(!openSet.contains(newState) && !closedSet.contains(newState)) // if the new state not in the open or close list add it
                        {

                            if(Arrays.deepEquals(newBoard, goalBoard))  // if we reach to the goal
                            {
                                writer.write(newState.getActions() + "\n");
                                writer.write("Num: " + numberOfVertices + "\n");
                                writer.write("Cost: " + newState.getCost() + "\n");
                                writer.flush();
                                return;
                            }

                            openSet.add(newState);
                            queue.add(newState);


                        }

                    }
                    }
                }
            }
        // if we reach here there is no selution and no more pisoobole move that we havent try.
        writer.write("no path\n");
        writer.write("Num: " + numberOfVertices + "\n");
        writer.write("Cost: inf\n");
        writer.flush();

    }

    public static void DFID(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean withoOpenList) throws IOException {
        int numberOfVertices=0;

        for (int depth = 1; depth <Integer.MAX_VALUE ; depth++)
        {

            State initialState = new State(board, 0, "");
            Set<State> HashTable=new HashSet<>();

            //0 == cutoff
            //1 == no solution
            //2 == solution found
            int result=limited_DFS(writer,initialState,goalBoard,depth,HashTable,withoOpenList);

            //if the answer is a path or a fail stop the fucntion (the solution will be flash in the limited_DFS function
            if(result != 0){
                if(result==1){
                    writer.write("no path\n");
                    writer.write("Num: " + (State.genrealIndex-1) + "\n");
                    writer.write("Cost: inf\n");
                    writer.flush();
                }
                return;
            }


        }

    }
    public static int limited_DFS(BufferedWriter writer,State currentState,char[][] goalBoard,int limit,Set<State> HashTable,boolean withoOpenList) throws IOException{

        if(Arrays.deepEquals(currentState.getBoard(), goalBoard))  // if we reach to the goal
        {
            writer.write(currentState.getActions() + "\n");
            writer.write("Num: " + (State.genrealIndex-1) + "\n");
            writer.write("Cost: " + currentState.getCost() + "\n");
            writer.flush();
            return 2;
        }
        if (limit==0){
            return 0;
        }
        HashTable.add(currentState);
        boolean isCutoff = false;
        if(withoOpenList){
            System.out.println("######### open-list ########");
            for( State s:HashTable){
                System.out.println(s);

            }
        }

        // Explore opertion for each cell
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                Point currentPosition=new Point(i,j);

                // Skip if the target cell isn't a marble.
                if (currentState.getBoard()[i][j] == 'X' ||currentState.getBoard()[i][j]=='_')
                {
                    continue;
                }

                Point left = new Point((i - 1 + 3) % 3, j);  // Left neighbor
                Point right = new Point((i + 1) % 3, j);     // Right neighbor
                Point up = new Point(i, (j + 1) % 3);        // Up neighbor
                Point down = new Point(i, (j - 1 + 3) % 3);  // Down neighbor
                ArrayList<Point> points=new ArrayList<>();
                points.add(left);
                points.add(up);
                points.add(right);
                points.add(down);

                for(Point p:points)     // checking all the possible direction for each coordinate in the board
                {
                    if(currentState.getValue(p)!='_'){
                        continue;
                    }

                    //check if it a revers action if it skip the action
                    if(currentState.isReversedAction(i,j,p)){
                        continue;
                    }
                    // if the position in point p (left/right/upd/down to (i,j) is open add the new state with the new calculation
                    char [][]newBoard= currentState.copyBoardWithSwap(currentPosition,p);
                    // update the number of vertices
                    int newCost=currentState.getCost()+costTable.get(currentState.getValue(currentPosition));
                    String newAction= currentState.getActions();
                    if (newAction!="")
                    {
                        newAction+="--";
                    }
                    newAction+="("+ (currentPosition.x+1) +","+ (currentPosition.y+1)  + "):"
                            +currentState.getValue(currentPosition)
                            +":("+ (p.x+1) +","+ (p.y+1) + ")";
                    State newState=new State(newBoard,newCost,newAction);
                    newState.setLastAction(i,j,p);

                    if(HashTable.contains(newState))
                    {
                        continue;
                    }

                    //0 == cutoff
                    //1 == no solution
                    //2 == solution found
                    int result=limited_DFS( writer,newState,goalBoard,limit-1,HashTable,withoOpenList);

                    if(result ==0){
                        isCutoff=true;
                    }
                    if(result ==2){
                        return  2;
                    }

                }
            }
        }
        HashTable.remove(currentState);
        if(isCutoff == true){
            return 0;
        }
        else{

            return 0;
        }



    }



    public static void A_STAR(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean withoOpenList) throws IOException {
        int numberOfVertices = 0;

        Comparator<State> compere=new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return Integer.compare(o1.getF(),o2.getF());
            }
        };

        Queue<State> queue=new PriorityQueue<>(compere);
        HashMap<State, State> closeList = new HashMap<>();
        HashMap<State, State> openList= new HashMap<>();

        State initialState = new State(board, 0, "");
        initialState.setHursticValue(hurstic(board,goalBoard));
        queue.add(initialState);
        openList.put(initialState,initialState);
        while (!queue.isEmpty()) {
            if(withoOpenList){
                System.out.println("######### open-list ########");
                for( State s:queue){
                    System.out.println(s);

                }
            }

            State currentState=queue.poll();
            openList.remove(currentState);
            closeList.put(currentState,currentState);
            if(Arrays.deepEquals(currentState.getBoard(),goalBoard)){
                writer.write(currentState.getActions()+"\n");
                writer.write("Num: " + numberOfVertices + "\n");
                writer.write("Cost: "+currentState.getCost()+"\n");
                writer.flush();
                return;
            }

                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            Point currentPosition = new Point(i, j);

                            // Skip if the target cell isn't a marble.
                            if (currentState.getBoard()[i][j] == 'X' || currentState.getBoard()[i][j] == '_') {
                                continue;
                            }

                            Point left = new Point((i - 1 + 3) % 3, j);  // Left neighbor
                            Point right = new Point((i + 1) % 3, j);     // Right neighbor
                            Point up = new Point(i, (j + 1) % 3);        // Up neighbor
                            Point down = new Point(i, (j - 1 + 3) % 3);  // Down neighbor
                            ArrayList<Point> points = new ArrayList<>();
                            points.add(left);
                            points.add(up);
                            points.add(right);
                            points.add(down);

                            for (Point p : points)     // checking all the possible direction for each coordinate in the board
                            {
                                if (currentState.getValue(p) != '_') {
                                    continue;
                                }

                                //check if it a revers action if it skip the action
                                if (currentState.isReversedAction(i, j, p)) {
                                    continue;
                                }
                                // if the position in point p (left/right/upd/down to (i,j) is open add the new state with the new calculation
                                char[][] newBoard = currentState.copyBoardWithSwap(currentPosition, p);
                                // update the number of vertices
                                int newCost = currentState.getCost() + costTable.get(currentState.getValue(currentPosition));
                                String newAction = currentState.getActions();
                                if (newAction != "") {
                                    newAction += "--";
                                }
                                newAction += "(" + (currentPosition.x + 1) + "," + (currentPosition.y + 1) + "):"
                                        + currentState.getValue(currentPosition)
                                        + ":(" + (p.x + 1) + "," + (p.y + 1) + ")";
                                State newState = new State(newBoard, newCost, newAction);
                                newState.setLastAction(i, j, p);
                                newState.setHursticValue(hurstic(newState.getBoard(), goalBoard));
                                numberOfVertices++;

                                if(closeList.containsKey(newState) ){
                                    continue;
                                }

                                if(!openList.containsKey(newState)){
                                    openList.put(newState,newState);
                                }
                                else if(newState.getCost()>= openList.get(newState).getCost()){
                                    continue;
                                }
                                openList.remove(newState,newState);
                                openList.put(newState,newState);
                                queue.add(newState);




                            }
                        }
                    }


        }

        writer.write("no path\n");
        writer.write("Num: " + numberOfVertices + "\n");
        writer.write("Cost: inf\n");
        writer.flush();
    }



    public static void IDA_STAR(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean withoOpenList) throws IOException {
        int numberOfVertices = 0;
        Stack<State> stack = new Stack<>();
        HashMap<State, State> hashMap = new HashMap<>();
        int thresHold = hurstic(goalBoard, goalBoard);
        State initialState = new State(board, 0, "");
        initialState.setHursticValue(hurstic(board,goalBoard));
        while (thresHold != Integer.MAX_VALUE) {

            if(withoOpenList){
                System.out.println("######### open-list ########");
                for( State s:stack){
                    System.out.println(s);

                }
            }
            initialState.setNotOut();
            stack.add(initialState);
            hashMap.put(initialState, initialState);
            int minF = Integer.MAX_VALUE;
            while (!stack.isEmpty()) {
                State currentState = stack.pop();
                if (currentState.getIsOut()) {
                    hashMap.remove(currentState, currentState);
                }
                else {
                    currentState.setOut();
                    stack.add(currentState);
                    ArrayList<State> nodes = new ArrayList<>();
                    // Explore opertion for each cell
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            Point currentPosition = new Point(i, j);

                            // Skip if the target cell isn't a marble.
                            if (currentState.getBoard()[i][j] == 'X' || currentState.getBoard()[i][j] == '_') {
                                continue;
                            }

                            Point left = new Point((i - 1 + 3) % 3, j);  // Left neighbor
                            Point right = new Point((i + 1) % 3, j);     // Right neighbor
                            Point up = new Point(i, (j + 1) % 3);        // Up neighbor
                            Point down = new Point(i, (j - 1 + 3) % 3);  // Down neighbor
                            ArrayList<Point> points = new ArrayList<>();
                            points.add(left);
                            points.add(up);
                            points.add(right);
                            points.add(down);

                            for (Point p : points)     // checking all the possible direction for each coordinate in the board
                            {
                                if (currentState.getValue(p) != '_') {
                                    continue;
                                }

                                //check if it a revers action if it skip the action
                                if (currentState.isReversedAction(i, j, p)) {
                                    continue;
                                }
                                // if the position in point p (left/right/upd/down to (i,j) is open add the new state with the new calculation
                                char[][] newBoard = currentState.copyBoardWithSwap(currentPosition, p);
                                // update the number of vertices
                                int newCost = currentState.getCost() + costTable.get(currentState.getValue(currentPosition));
                                String newAction = currentState.getActions();
                                if (newAction != "") {
                                    newAction += "--";
                                }
                                newAction += "(" + (currentPosition.x + 1) + "," + (currentPosition.y + 1) + "):"
                                        + currentState.getValue(currentPosition)
                                        + ":(" + (p.x + 1) + "," + (p.y + 1) + ")";
                                State newState = new State(newBoard, newCost, newAction);
                                newState.setLastAction(i, j, p);
                                newState.setHursticValue(hurstic(newState.getBoard(), goalBoard));
                                numberOfVertices++;
                                nodes.add(newState);

                            }
                        }
                    }

                    for (State node:nodes) {

                        if (node.getF() > thresHold) {
                            minF=Integer.min(minF,node.getF());
                            continue;
                        }
                        if (hashMap.containsKey(node) && hashMap.get(node).getIsOut()) {
                            continue;
                        } else if (hashMap.containsKey(node) && !hashMap.get(node).getIsOut()) {
                            if (hashMap.get(node).getF()> node.getF()) {
                                stack.remove(hashMap.get(node));
                                hashMap.remove(node, node);
                            } else {
                                continue;

                            }
                        }
                        if (Arrays.deepEquals(node.getBoard(), goalBoard)) {
                            writer.write(node.getActions() + "\n");
                            writer.write("Num: " + numberOfVertices + "\n");
                            writer.write("Cost: " + node.getCost() + "\n");
                            writer.flush();
                            return;
                        }
                        stack.add(node);
                        hashMap.put(node,node);


                    }

                }
            }
            thresHold=minF;
            if(thresHold==Integer.MAX_VALUE){
                System.out.println("123");
            }
            }

            writer.write("no path\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: inf\n");
            writer.flush();
        }






    public static void DFBnB(BufferedWriter writer,char[][] board,char[][] goalBoard,boolean withoOpenList) throws IOException {
        int numberOfVertices=0;
        Stack<State> stack=new Stack<>();
        HashMap<State,State> hashMap=new HashMap<>();
        int thresHold=Integer.MAX_VALUE;
        State result=null;
        State initialState = new State(board, 0, "");
        stack.add(initialState);
        hashMap.put(initialState,initialState);
        while (!stack.isEmpty())
        {
            if(withoOpenList){
                System.out.println("######### open-list ########");
                for( State s:stack){
                    System.out.println(s);

                }
            }
            State currentState=stack.pop();
            if(currentState.getIsOut()){
                hashMap.remove(currentState,currentState);
            }
            else{
                currentState.setOut();
                stack.add(currentState);
                ArrayList<State> nodes=new ArrayList<>();
                // Explore opertion for each cell
                for (int i = 0; i <3 ; i++) {
                    for (int j = 0; j <3 ; j++) {
                        Point currentPosition=new Point(i,j);

                        // Skip if the target cell isn't a marble.
                        if (currentState.getBoard()[i][j] == 'X' ||currentState.getBoard()[i][j]=='_')
                        {
                            continue;
                        }

                        Point left = new Point((i - 1 + 3) % 3, j);  // Left neighbor
                        Point right = new Point((i + 1) % 3, j);     // Right neighbor
                        Point up = new Point(i, (j + 1) % 3);        // Up neighbor
                        Point down = new Point(i, (j - 1 + 3) % 3);  // Down neighbor
                        ArrayList<Point> points=new ArrayList<>();
                        points.add(left);
                        points.add(up);
                        points.add(right);
                        points.add(down);

                        for(Point p:points)     // checking all the possible direction for each coordinate in the board
                        {
                            if(currentState.getValue(p)!='_'){
                                continue;
                            }

                            //check if it a revers action if it skip the action
                            if(currentState.isReversedAction(i,j,p)){
                                continue;
                            }
                            // if the position in point p (left/right/upd/down to (i,j) is open add the new state with the new calculation
                            char [][]newBoard= currentState.copyBoardWithSwap(currentPosition,p);
                            // update the number of vertices
                            int newCost=currentState.getCost()+costTable.get(currentState.getValue(currentPosition));
                            String newAction= currentState.getActions();
                            if (newAction!="")
                            {
                                newAction+="--";
                            }
                            newAction+="("+ (currentPosition.x+1) +","+ (currentPosition.y+1)  + "):"
                                    +currentState.getValue(currentPosition)
                                    +":("+ (p.x+1) +","+ (p.y+1) + ")";
                            State newState=new State(newBoard,newCost,newAction);
                            newState.setLastAction(i,j,p);
                            newState.setHursticValue(hurstic(newState.getBoard(),goalBoard));
                            numberOfVertices++;
                            nodes.add(newState);

                            }
                        }
                    }
                Comparator<State> compere=new Comparator<State>() {
                    @Override
                    public int compare(State o1, State o2) {
                        return Integer.compare(o1.getF(),o2.getF());
                    }
                };
                nodes.sort(compere);
                int i=0;
                while (i<nodes.size()){
                    State node=nodes.get(i);

                    if(node.getF()>=thresHold){
                        nodes.subList(i, nodes.size()).clear();
                    }
                    else if(hashMap.containsKey(node) && hashMap.get(node).getIsOut()){
                        nodes.remove(node);
                        i--;
                    }
                    else if(hashMap.containsKey(node) && !hashMap.get(node).getIsOut()){
                        if(hashMap.get(node).getF()<=node.getF()){
                            nodes.remove(node);
                            i--;
                        }
                        else {
                            stack.remove(hashMap.get(node));
                            hashMap.remove(node,node);
                        }
                    }
                    else if(Arrays.deepEquals(node.getBoard(),goalBoard)){
                        result=new State(node.getBoard(),node.getCost(),node.getActions());
                        thresHold=node.getF();
                        nodes.clear();
                    }
                    i++;

                }
                List<State> reversed=nodes.reversed();
                for(State node:reversed){
                    stack.add(node);
                    hashMap.put(node,node);
                }

            }
        }
        if(result==null){
            writer.write("no path\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: inf\n");
            writer.flush();
        }
        else {
            writer.write(result.getActions()+"\n");
            writer.write("Num: " + numberOfVertices + "\n");
            writer.write("Cost: "+result.getCost() +"\n");
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
