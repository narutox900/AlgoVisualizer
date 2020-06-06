package Main.Algorithms;

import Main.Configurations.Constants;
import Main.Controller;
import Main.GraphRelated.Cell;
import Main.GraphRelated.CellState;

import java.util.LinkedList;

// Similar Part For All Algorithm
// Inherited from thread for performance improvement
public class Algorithm extends Thread {
    // offset value for a particular cell (all its neighbors)
    public static int[] X = { 0,1,0,-1};
    public static int[] Y = {-1,0,1, 0};

    protected Cell source;
    protected Cell target;

    protected boolean pathFound;
    protected int shortestPath;

    public void initialize(Cell source, Cell target)
    {
        this.source = source;
        this.target = target;

        pathFound = false;
        shortestPath = Integer.MAX_VALUE;
    }

    public boolean inRange(int r, int c) {
        return (r >= 0 && r < Constants.ROW) && (c >= 0 && c < Constants.COL);
    }

    protected void colorPath(LinkedList<Cell> shortestPath, String color, boolean isShortest)
    {
        Cell previous = null, current = null;

        for (Cell cell : shortestPath) {
            current = cell;

            if (!isShortest) { // change its state to visited state
                Controller.paintBlock(current.x, current.y, Constants.BORDER, color);
                Controller.CellGrid[current.x][current.y].state = CellState.VISITED;

                previous = current;
            } else {
                Constants.stopButton.setDisable(true);
                if (previous != null) { //make the source box run from source to actual target
                    if(previous.weighted)
                        Controller.paintBlock(previous.x, previous.y, Constants.BORDER, Constants.WEIGHT_IN_PATH);
                    else Controller.paintBlock(previous.x, previous.y, Constants.BORDER, color);
                }
//                if(current.weighted)
//                    Controller.paintBlock(current.x, current.y, Constants.BORDER, Constants.WEIGHT_IN_PATH);
//                else
                Controller.paintBlock(current.x, current.y, Constants.BORDER, Constants.SOURCE);
                current.state = CellState.SHORTEST;

                previous = current;
                try {
                    Controller.CellGrid[current.x][current.y].state = CellState.SHORTEST;
                    Thread.sleep(Constants.THREAD_SLEEP_TIME);
                    while (Constants.isPause)
                    {
                        try{
                            Thread.sleep(Constants.THREAD_PAUSE_TIME);
                        }
                        catch (Exception e) { System.out.println("Thread sleep fail"); }
                    }
                } catch (Exception e) {
                    System.out.println("Thread sleep fail");
                }
            }
        }

        //repaint the last one to target
        if(previous != null) Controller.paintBlock(previous.x, previous.y, Constants.BORDER, Constants.TARGET);
        Constants.stopButton.setDisable(false);
    }

    public void killThread() {
        System.out.println("Thread killed");
        pathFound=true;
        this.interrupt();

        Constants.currentThread = null;

    }


}
