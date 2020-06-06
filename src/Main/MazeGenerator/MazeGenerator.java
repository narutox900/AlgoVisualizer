package Main.MazeGenerator;

import Main.Configurations.Constants;
import Main.Controller;
import Main.GraphRelated.CellState;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator extends Thread {
    private static class Point {
        public final int X;
        public final int Y;

        public Point(int x, int y) {
            this.X = x;
            this.Y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return X == point.X &&
                    Y == point.Y;
        }
    }

    private static final int[] X = { 0,1,0,-1};
    private static final int[] Y = {-1,0,1, 0};
    private static final Random random = new Random();

    private static int[] selectedDirection = new int[4];

    public static boolean inRange(int r, int c) {
        return (r >= 0 && r < Constants.ROW) && (c >= 0 && c < Constants.COL);
    }

    private static Point getNeighbor(Point current, int direction)
    {
        int x = current.X + X[direction];
        int y = current.Y + Y[direction];
        if (inRange(x, y))
            return new Point(x, y);

        return null;
    }

    private static boolean isValidNeighbor(int[][] maze, Point neighbor, int direction)
    {
        if (neighbor == null)
            return false;

        if (maze[neighbor.X][neighbor.Y] == 0)
        {
            int revertDirection = (direction + 2) % 4; // find the direction to current
            for (int i = 0; i < 4; i++)
            {
                if (i == revertDirection)
                    continue;

                Point subNeighbor = getNeighbor(neighbor, i);
                if (subNeighbor != null && maze[subNeighbor.X][subNeighbor.Y] == 1)
                    return false;
            }
            return true;
        }

        return false;
    }

    public static int[][] generateMaze()
    {

        // 0 - wall
        // 1 - path
        int[][] maze = new int[Constants.ROW][Constants.COL];
        Stack<Point> stack = new Stack<>();

        maze[0][0] = 1;
        stack.push(new Point(0,0));
        while (!stack.isEmpty())
        {
            Point current = stack.pop();
            Arrays.fill(selectedDirection, 0);

            // find valid random neighbor
            for (int i = Constants.NUM_OF_NEIGHBORS; i > 0; i--)
            {
                int direction = random.nextInt(i);

                int j = direction;
                for (int k = 0; k < Constants.NUM_OF_NEIGHBORS; k++)
                {
                    if (selectedDirection[k] == 0)
                    {
                        if (j-- == 0)
                        {
                            direction = k;
                            break;
                        }
                    }
                }

                Point neighbor = getNeighbor(current, direction);
                if (isValidNeighbor(maze, neighbor, direction))
                {
                    stack.push(current);
                    maze[neighbor.X][neighbor.Y] = 1;
                    stack.push(neighbor);
                    break;
                }
                else
                {
                    selectedDirection[direction] = 1;
                }
            }
        }

        return maze;
    }

    @Override
    public void run() {
        int[][] maze = MazeGenerator.generateMaze();

        for (int i = 0; i < Constants.ROW; i++)
            for (int j = 0; j < Constants.COL; j++)
            {
                try {
                    if (maze[i][j] == 0)
                    {
                        Controller.paintBlock(i,j,Constants.BORDER, Constants.WALL);
                        Controller.CellGrid[i][j].state = CellState.WALL;
                        Thread.sleep(Constants.THREAD_SLEEP_TIME_MAZE);
                    }
                    else
                    {
                        Controller.paintBlock(i,j,Constants.BORDER, Constants.UNVISITED);
                        Controller.CellGrid[i][j].state = CellState.UNVISITED;
                    }
                } catch (Exception e) {
                    System.out.println("Thread sleep in maze fail!");
                }
            }
    }
}
