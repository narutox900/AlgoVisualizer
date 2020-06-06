package Main.Algorithms;

import Main.Configurations.Constants;
import Main.Controller;
import Main.GraphRelated.Cell;
import Main.GraphRelated.CellState;

import java.util.LinkedList;

public class DepthFirst extends Algorithm {
    private LinkedList<Cell> previousPath = null; //path lead from source to target but not guarantee the shortest

    private void DFS(Cell current)
    {
        // Control the speed of the algorithm
        try {
            Thread.sleep(Constants.THREAD_SLEEP_TIME);
        }
        catch (Exception e) { System.out.println("Can not make thread sleep!"); }

        if (!Constants.isPause)
        {
            if (current.state != CellState.SHORTEST && current.state != CellState.TARGET && current.state != CellState.SOURCE)
            {
                Controller.paintBlock(current.x,current.y,Constants.BORDER,Constants.VISITED);
            }
            for (int i = 0; i < Constants.NUM_OF_NEIGHBORS && !pathFound && current.weight < shortestPath; i++)
            {
                if (inRange(current.x + X[i], current.y + Y[i]))
                {
                    Cell next = Controller.CellGrid[current.x + X[i]][current.y + Y[i]];

                    if (current.weight + 1 < next.weight)
                    {
                        next.weight = current.weight + 1;
                        next.setParent(current.x, current.y); // set parent for later trace back path

                        if (next.state != CellState.TARGET) // if not found
                        {
                            Controller.paintBlock(next.x,next.y,Constants.BORDER,Constants.NEXT_VISIT);
                            DFS(next); //recursively search for next child
                        }
                        else { //target reached
                            if (next.weight < shortestPath) // new path shorter is found, change to this path
                            {
                                shortestPath = next.weight;
                                if (previousPath != null) //reset the path
                                {
                                    colorPath(previousPath,Constants.VISITED, false);
                                }
                                tracePath(next);
                            }

                            return;
                        }
                    }
                }
            }

            // reverse if not found
            if (current.state != CellState.SHORTEST && current.state != CellState.TARGET && current.state != CellState.SOURCE)
            {
                Controller.paintBlock(current.x,current.y,Constants.BORDER,Constants.UNVISITED);
                current.state = CellState.UNVISITED;
            }
        }
        else
        {
            try {
                Thread.sleep(Constants.THREAD_PAUSE_TIME);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Thread sleep fail"); }
            finally {
                DFS(current);
            }
        }
    }

    public void tracePath(Cell cell)
    {
        LinkedList<Cell> shortestPath = new LinkedList<Cell>();
        while (cell.state != CellState.SOURCE)
        {
            shortestPath.addFirst(cell);
            cell = Controller.CellGrid[cell.parent_x][cell.parent_y];
        }

        previousPath = shortestPath;
        colorPath(shortestPath,Constants.SHORTEST,true);
        killThread(); // fix since DFS do not guarantee best path
    }

    @Override
    public void run() {
        source.weight = 0;
        DFS(source);

        Constants.currentThread = null;
        if(!pathFound) {
            System.out.println("No path found!");
            killThread();
        }
        System.out.println("Depth-First Search Algorithm Finish");
    }
}
