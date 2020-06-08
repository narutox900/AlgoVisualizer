package Main.MazeGenerator;

import Main.Configurations.Constants;
import Main.Controller;
import Main.GraphRelated.CellState;

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

    private static int[] validDirections = new int[4];

    // percentage of changing direction
    private static final int windingPercent = 100;

    public static boolean inRange(int r, int c) {
        return (r >= 1 && r < Constants.ROW - 1) && (c >= 1 && c < Constants.COL - 1);
    }

    private static Point getNeighbor(Point current, int direction)
    {
        int x = current.X + X[direction] * 2;
        int y = current.Y + Y[direction] * 2;
        if (inRange(x, y))
            return new Point(x, y);

        return null;
    }

    private static boolean isValidNeighbor(int[][] maze, Point neighbor)
    {
        if (neighbor == null)
            return false;

        return maze[neighbor.X][neighbor.Y] == 0;
//        if (maze[neighbor.X][neighbor.Y] == 0)
//        {
//            int revertDirection = (direction + 2) % 4; // find the direction to current
//            for (int i = 0; i < 4; i++)
//            {
//                if (i == revertDirection)
//                    continue;
//
//                Point subNeighbor = getNeighbor(neighbor, i);
//                if (subNeighbor != null && maze[subNeighbor.X][subNeighbor.Y] == 1)
//                    return false;
//            }
//            return true;
//        }

//        return false;
    }

    public static int[][] generateMaze()
    {

        // 0 - wall
        // 1 - path
        int[][] maze = new int[Constants.ROW][Constants.COL];
        Stack<Point> stack = new Stack<>();

        int lastDirection = -1;

        maze[1][1] = 1;
        stack.push(new Point(1,1));
        while (!stack.isEmpty())
        {
            Point current = stack.peek();
            Arrays.fill(validDirections, -1);

            // find valid neighbor
            int validDirectionCount = 0;
            boolean lastDirectionValid = false;
            for (int i = 0; i < Constants.NUM_OF_NEIGHBORS; i++)
            {
                Point neighbor = getNeighbor(current, i);
                if (isValidNeighbor(maze, neighbor))
                {
                    validDirections[validDirectionCount++] = i;

                    if (lastDirection == i)
                        lastDirectionValid = true;
                }
            }

            if (validDirectionCount > 0)
            {
                int chosenDirection;
                // get random neighbor
                if (lastDirection != -1 && lastDirectionValid && random.nextInt(101) > windingPercent)
                {
                    chosenDirection = lastDirection;
                }
                else
                {
                    chosenDirection = validDirections[random.nextInt(validDirectionCount)];
                }

                Point tmp = getNeighbor(current, chosenDirection);
                maze[tmp.X - X[chosenDirection]][tmp.Y - Y[chosenDirection]] = 1;
                maze[tmp.X][tmp.Y] = 1;

                lastDirection = chosenDirection;
                stack.push(tmp);
            }
            else
            {
                stack.pop();
                lastDirection = -1;
            }
        }

        return maze;
    }

    @Override
    public void run() {
        Constants.mazeButton.setDisable(true);
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
        Constants.mazeButton.setDisable(false);
    }
}
