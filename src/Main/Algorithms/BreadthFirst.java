package Main.Algorithms;

import Main.Configurations.Constants;
import Main.Controller;
import Main.GraphRelated.Cell;
import Main.GraphRelated.CellState;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirst extends Algorithm {

    @Override
    public void run() {
        Queue<Cell> queue = new LinkedList<>();
        Cell current, tmp;

        source.weight = 0;
        queue.add(source);
        try {
            // pop the waiting queue
            while (!queue.isEmpty() && !pathFound) {
                Thread.sleep(Constants.THREAD_SLEEP_TIME);
                if (!Constants.isPause)
                {
                    current = queue.poll();

                    if (current.state != CellState.SOURCE)
                        Controller.paintBlock(current.x, current.y, Constants.BORDER, Constants.VISITED);
                    //Go to all neighbors of the current state and push into queue if path not found
                    for (int i = 0; i < Constants.NUM_OF_NEIGHBORS && !pathFound; i++) {
                        if (inRange(current.x + X[i], current.y + Y[i])) {

                            tmp = Controller.CellGrid[current.x + X[i]][current.y + Y[i]];

                            if (tmp.state == CellState.TARGET || tmp.state == CellState.UNVISITED) {
                                tmp.weight = current.weight + 1;
                                tmp.setParent(current.x, current.y);


                                if (tmp.state != CellState.TARGET)
                                    Controller.paintBlock(tmp.x, tmp.y, Constants.BORDER, Constants.NEXT_VISIT);
                                else {
                                    tracePath(tmp);
                                    shortestPath = tmp.weight;
                                    pathFound = true;
                                    break;
                                }

                                Controller.CellGrid[tmp.x][tmp.y].state = CellState.VISITED;
                                queue.add(tmp);
                            }
                        }
                    }
                }
                else
                    Thread.sleep(Constants.THREAD_PAUSE_TIME);
            } if (!pathFound) {
                System.out.println("No path available!\nBreadth-First Search Algorithm Finish!");
                killThread();
            }
        } catch (Exception e) {
            System.out.println("Thread interrupted while sleeping");
        }

    }

    //trace back and print out the best path
    public void tracePath(Cell cell) {
        LinkedList<Cell> shortestPath = new LinkedList<Cell>();
        while (cell.state != CellState.SOURCE) {
            shortestPath.addFirst(cell);
            cell = Controller.CellGrid[cell.parent_x][cell.parent_y];
        }
        colorPath(shortestPath, Constants.SHORTEST, true);

        System.out.println("Breadth-First Search Algorithm Finish");
        killThread();
    }
}
