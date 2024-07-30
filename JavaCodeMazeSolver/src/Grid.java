// Custom Grid class.
public class Grid {
    private int[][] grid;   // 2-dimensional array of integers.
    private int rows;
    private int cols;

    // constructor for Grid class
    public Grid(int[][] grid) {
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
    }

    public boolean isValidPosition(int x, int y) {  //This method ensures that the provided coordinates (x, y) are within the bounds of the grid.

        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    public boolean isObstacle(int x, int y) {
        return grid[x][y] == 1;  // If there is an obstacle return True, otherwise false.
    }

    // Getter methods
    public int getRows() {

        return rows;
    }
    public int getCols() {

        return cols;
    }
}
