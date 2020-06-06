package Main.Algorithms;

import Main.Configurations.Constants;
import Main.Controller;
import Main.GraphRelated.Cell;
import Main.GraphRelated.CellState;
import Main.GraphRelated.Direction;

import java.util.LinkedList;

public class Dijkstra extends Algorithm {


    public Cell updateNode(Cell currNode, Cell nextNode) {


        int x1, x2, y1, y2;
        int distance = 0;
        Direction direction = null;
        x1 = currNode.x;
        x2 = nextNode.x;
        y1 = currNode.y;
        y2 = nextNode.y;

        if (x2 < x1) {
            switch (currNode.direction)
            {
                case UP:
                    distance = 1;
                    break;
                case RIGHT:
                    distance = 2;
                    break;
                case LEFT:
                    distance = 2;
                    break;
                case DOWN:
                    distance = 3;
                    break;
            }
            direction = Direction.UP;
        } else if (x2 > x1) {
            switch (currNode.direction)
            {
                case UP:
                    distance = 3;
                    break;
                case RIGHT:
                    distance = 2;
                    break;
                case LEFT:
                    distance = 2;
                    break;
                case DOWN:
                    distance = 1;
                    break;
            }
            direction = Direction.DOWN;
        }

        if (y2 < y1) {
            switch (currNode.direction)
            {
                case UP:
                    distance = 2;
                    break;
                case RIGHT:
                    distance = 1;
                    break;
                case LEFT:
                    distance = 3;
                    break;
                case DOWN:
                    distance = 2;
                    break;
            }
            direction = Direction.LEFT;
        } else if (y2 > y1) {
            switch (currNode.direction)
            {
                case UP:
                    distance = 2;
                    break;
                case RIGHT:
                    distance = 1;
                    break;
                case LEFT:
                    distance = 3;
                    break;
                case DOWN:
                    distance = 2;
                    break;
            }
            direction = Direction.RIGHT;
        }

        if (currNode == source)
            nextNode.distance = Integer.MAX_VALUE;
        int distanceToCompare = currNode.distance + distance;
        if(currNode.weighted) distanceToCompare += Constants.WEIGHT_COUNT;
        if (distanceToCompare < nextNode.distance) {
            nextNode.distance = distanceToCompare;
            nextNode.parent_x = currNode.x;
            nextNode.parent_y = currNode.y;
            nextNode.direction = direction;
        }
        return nextNode;
    }


    @Override
    public void run() {
        LinkedList<Cell> queue = new LinkedList<>();
        Cell current, tmp;
        queue.clear();
        source.distance = 0;
        source.direction = Direction.RIGHT;
        queue.add(source);
        try {
            // pop the waiting queue
            while (!queue.isEmpty() && !pathFound) {
                Thread.sleep(Constants.THREAD_SLEEP_TIME);
                if (!Constants.isPause) {
                    current = queue.poll();
                    if (current.count > 0 && current.state != CellState.WALL) {
                        current.distance += 2;
                        current.count -= 1;
                        queue.add(current);
                    } else {
                        if (current.state != CellState.SOURCE) {
                            if (current.weighted) {
                                Controller.paintBlock(current.x, current.y, Constants.BORDER, Constants.WEIGHT);
                                current.distance += Constants.WEIGHT_COUNT;
                            } else
                                Controller.paintBlock(current.x, current.y, Constants.BORDER, Constants.VISITED);
                        }

                        //Go to all neighbors of the current state and push into queue if path not found

                        for (int i = 0; i < Constants.NUM_OF_NEIGHBORS && !pathFound; i++) {
                            if (inRange(current.x + X[i], current.y + Y[i])) {
                                tmp = Controller.CellGrid[current.x + X[i]][current.y + Y[i]];

                                if (tmp.state != CellState.WALL) {
                                    tmp = updateNode(current, tmp);
                                    if (tmp.state == CellState.TARGET || tmp.state == CellState.UNVISITED
                                            || tmp.state == CellState.WEIGHT) {

                                        if (tmp.count == 0 || tmp.count == Constants.WEIGHT_COUNT) {

                                            if (tmp.state != CellState.TARGET) {
                                                Controller.paintBlock(tmp.x, tmp.y, Constants.BORDER, Constants.NEXT_VISIT);
                                                if (tmp.weighted) {
                                                    Controller.paintBlock(tmp.x, tmp.y, Constants.BORDER, Constants.WEIGHT);
                                                }
                                            } else {
                                                Cell tmp_parent;
                                                int min_dist = Integer.MAX_VALUE;
                                                for (int j = 0; j < Constants.NUM_OF_NEIGHBORS && !pathFound; j++) {
                                                    if (inRange(tmp.x + X[j], tmp.y + Y[j])) {
                                                        tmp_parent = Controller.CellGrid[tmp.x + X[j]][tmp.y + Y[j]];
                                                        if (tmp_parent.distance < min_dist) {
                                                            min_dist = tmp_parent.distance;
                                                            tmp.parent_x = tmp_parent.x;
                                                            tmp.parent_y = tmp_parent.y;
                                                        }
                                                    }
                                                }
                                                tracePath(tmp);
                                                //shortestPath = tmp.distance;
                                                pathFound = true;
                                                break;
                                            }

                                            Controller.CellGrid[tmp.x][tmp.y].state = CellState.VISITED;
                                            queue.add(tmp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else
                    Thread.sleep(Constants.THREAD_PAUSE_TIME);
            } if (!pathFound) {
                System.out.println("No path available!\nDijkstra Search Algorithm Finish\n");
                killThread();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Thread interrupted while sleeping");
        }
    }

    public void tracePath(Cell cell) {
        //System.out.println("tracing...");
        LinkedList<Cell> shortestPath = new LinkedList<Cell>();

        while (cell.state != CellState.SOURCE) {
            shortestPath.addFirst(cell);
            cell = Controller.CellGrid[cell.parent_x][cell.parent_y];
        }
        colorPath(shortestPath, Constants.SHORTEST, true);

        System.out.println("Dijkstra Search Algorithm Finished.");
        killThread();
    }
}
