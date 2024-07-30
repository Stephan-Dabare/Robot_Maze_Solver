import java.util.Scanner;   // To get inputs from the user.


// Main class.
public class Main {
    private static int getValidInput(Scanner scanner) { // Create method to validate user inputs.
        while (true) {
            try {  // Exception handling.
                return scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.next(); // Clear the invalid input from the buffer.
            }
        }
    }



    private static int[][] createGrid(Scanner scanner) {  // Create method to create a custom grid using user inputs.
        System.out.print("Enter the size of the grid (n x n) n = ");
        int size = getValidInput(scanner);   // Get the size of the grid.

        // Initialize 2D integer array called maze, array size depends on user input.
        int[][] maze = new int[size][size];

        for(int i = 0; i < maze.length; i++){
            for(int j = 0; j < maze.length; j++){
                maze[i][j] = 0; // create a 2D array without obstacles. (without 1s)
            }
        }

        int maxObstacles = size * size;  // Assign number of slots available in the grid
        System.out.print("Enter number of obstacles (<= " + maxObstacles + "): ");
        int numObstacles = getValidInput(scanner);

        while (numObstacles < 0 || numObstacles > maxObstacles) {
            System.out.print("Number of obstacles must be between 0 and " + maxObstacles + ". Please enter a valid number: ");
            numObstacles = getValidInput(scanner);
        }

        for(int count = 0; count < numObstacles; count++) {
            System.out.print("Enter X coordinate: ");
            int xObstacle = getValidInput(scanner);
            while (xObstacle < 0 || xObstacle >= size) {
                System.out.println("X coordinate out of bounds. Please enter a valid X coordinate: ");
                xObstacle = getValidInput(scanner);
            }

            System.out.print("Enter Y coordinate: ");
            int yObstacle = getValidInput(scanner);
            while (yObstacle < 0 || yObstacle >= size) {
                System.out.println("Y coordinate out of bounds. Please enter a valid Y coordinate: ");
                yObstacle = getValidInput(scanner);
            }

            if (maze[xObstacle][yObstacle] == 1) {
                System.out.println("This position is already taken as an obstacle. Please choose another position.");
                count--; // Decrement count to repeat the obstacle entry
            } else {
                maze[xObstacle][yObstacle] = 1; // Assign obstacle
            }
        }
        return maze;   // Return the grid created for the path solving algorithm.
    }



    // Breath first search algorithm.
    public static CustomLinkedList findPath(Grid grid, RobotPosition start, RobotPosition goal) {  // Path finding method.

        // Queue to store positions that need to be visited.
        CustomLinkedList positionQueue = new CustomLinkedList();

        boolean[][] visited = new boolean[grid.getRows()][grid.getCols()];
        // New 2D boolean array with same dimensions of the grid to store visited positions.

        positionQueue.add(start);
        // Starting position of the robot add to the position queue.

        while (!positionQueue.isEmpty()) {
            // Loop continue until position queue is empty. Until head = null

            RobotPosition current = positionQueue.removeFirst();
            // Mark the current position and remove it, this position will explore in the iteration.

            visited[current.getX()][current.getY()] = true;
            // Mark the current position as visited. Prevent infinite loop.

            // Check if the current position is the goal.
            if (current.getX() == goal.getX() && current.getY() == goal.getY()) {
                return reconstructPath(current);
                // If goal found reconstruct the path from start to goal.
            }

            // Exploring neighboring positions.
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            // 2D array with directions. (deltaX, deltaY)   (left, right, down, up)

            for (int[] dir : directions) {  // loop iterate through each direction in the array.
                // Add both current position plus change of the coordinate. 0 for X
                int newX = current.getX() + dir[0];
                int newY = current.getY() + dir[1];  // 1 for Y


                // Checks if the new position (newX, newY) is within the bounds of the grid, not visited before, and is not an obstacle.
                if (grid.isValidPosition(newX, newY) && !visited[newX][newY] && !grid.isObstacle(newX, newY)) {
                    RobotPosition nextPosition = new RobotPosition(newX, newY);
                    // Create a new position instance with neighboring position.
                    nextPosition.setPrePosition(current);  // Set to the previous position for reconstruct the path.
                    positionQueue.add(nextPosition);   // Add neighbor to the position queue.
                    visited[newX][newY] = true;        // Mark the neighbor as visited avoiding revisiting.
                }


            }
        }
        return null;   // If path not found return null.
    }


    // reconstruct the path from goal to starting position.
    private static CustomLinkedList reconstructPath(RobotPosition current) {
        CustomLinkedList path = new CustomLinkedList();
        // New linked list instance, which stored positions from the reconstructed path.
        while (current != null) {  // Continues until current position becomes null.
            path.add(current);  // Add positions from goal to starting point.
            current = current.getPrePosition();  // Update the current position to former position. Travel backwards.
        }
        return path;  // Return path linked list.
    }

    public static void solveMaze(int[][] maze) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Y\\X   ");
        for (int i = 0; i < maze[0].length; i++) {
            String twoDigitNum = String.format("%02d", i);
            System.out.print(twoDigitNum + "   "); // Print the indices of the first row.
        }

        System.out.println();
        for (int i = 0; i < maze.length; i++) {  // Iterates over the rows.
            String twoDigitNum = String.format("%02d", i);
            System.out.print(twoDigitNum + "  | ");    // Label each row.
            for (int j = 0; j < maze[0].length; j++) {   // Iterate over each column of the current row.
                System.out.print(maze[i][j] + "    ");     // Prints the content.
            }
            System.out.println();
        }

        Grid grid = new Grid(maze); // Passed the maze array as an argument.

        RobotPosition start = new RobotPosition(0, 0);  // Initializes the starting position of the robot.

        System.out.print("\nEnter goal X coordinate: ");
        int goalX = getValidInput(scanner);
        System.out.print("\nEnter goal Y coordinate: ");
        int goalY = getValidInput(scanner);

        RobotPosition goal = new RobotPosition(goalY, goalX);  // Initializes the goal position using user inputs.

        CustomLinkedList path = findPath(grid, start, goal);  // Call to the algorithm to find the shortest path

        if (path != null) {
            System.out.println("\nPath found:");
            CustomLinkedList.Node currentNode = path.getHead(); // Access the node inner class and find the head of the list, Name it current Node
            while (currentNode != null) {
                RobotPosition position = currentNode.data;  // Access the coordinate data from the custom linked list
                maze[position.getX()][position.getY()] = 2; // Use get method to get coordinates, and Mark path with 2
                currentNode = currentNode.next; // Access next node.
            }

            System.out.print("Y\\X    ");
            for (int i = 0; i < maze[0].length; i++) {
                String twoDigitNum = String.format("%02d", i);
                System.out.print(twoDigitNum + "   "); // Print the indices of the first row.
            }
            System.out.println();
            for (int i = 0; i < maze.length; i++) {
                String twoDigitNum = String.format("%02d", i);
                System.out.print(twoDigitNum + "  | ");
                for (int j = 0; j < maze[0].length; j++) {
                    if (maze[i][j] == 2) {
                        System.out.print("\u001B[102m" + "\u001B[30m" + "  .  \u001B[0m");    // Change background color for path found.
                    } else if (maze[i][j] == 1) {
                        System.out.print("\u001B[40m" + "\u001B[37m" + "     \u001B[0m");     // Change background color for obstacles.
                    } else {
                        System.out.print("\u001B[104m" + "\u001B[30m" + "     \u001B[0m");    // Change background color for free paths.
                    }
                    if (maze[i][j] == 2){
                        maze[i][j] = 0;
                    }
                }
                System.out.println();
            }
            System.out.println("\nRobot current position:" + "(" + goalX + "," + goalY + ")");
            System.out.println("Robot says, \"I found the GOAL!!\"");
            System.out.print("\u001B[102m" + "\u001B[30m" + ".  \u001B[0m" + " = Path," + " " + "\u001B[40m" + "\u001B[37m" + "   \u001B[0m" + " = Obstacles," + " " + "\u001B[104m" + "\u001B[30m" + "   \u001B[0m" + " = Open area");
            System.out.println("\n--------------------------------------------------------------");
        } else {
            System.out.println("Robot says, \"Sorry! Path not found :\\\"");  // If path list is empty. No possible path available.
            System.out.println("--------------------------------------------------------------");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isProceed = true;
        int[][] maze = {
                {0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1},
                {0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1},
                {0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1},
                {0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0},
                {1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0}
        }; // Default fixed grid

        while (isProceed) {
            System.out.println("\nRobot says, \"I'm ready to find the path!!\"");
            System.out.println("Please say which option should I need to use");
            System.out.println("1 <--- Find Path");
            System.out.println("2 <--- Create a Custom Grid and Find Path");
            System.out.println("3 <--- Exit");

            System.out.print("Enter Number: ");
            int choice = getValidInput(scanner);

            switch (choice) {
                case 1:
                    System.out.println("\nRobot says, \"I'm in (0,0) position. Now say where should I need to go!\"");
                    System.out.println("0 = Open positions, 1 = Obstacles");
                    System.out.println("Robot current position (0,0)");
                    solveMaze(maze); // Pass the default grid to find the path for robot.
                    break;
                case 2:
                    System.out.println("Robot says, \"Let's create a grid with obstacles.\"");
                    int[][] maze1 = createGrid(scanner); // Create custom grid.
                    System.out.println("\nRobot says, \"I'm in (0,0) position. Now say where should I need to go!\"");
                    System.out.println("Robot current position (0,0)");
                    solveMaze(maze1);   // Use the custom grid to find the path for robot.
                    break;
                case 3:
                    System.out.println("Exiting Maze Solver...");
                    isProceed = false;   // Exit from the loop and the program.
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}