package Main.Algorithms;

import Main.Configurations.Constants;
import Main.Controller;
import Main.GraphRelated.Cell;
import Main.GraphRelated.CellState;

import java.text.Collator;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class Dijkstra extends Algorithm {

    public distanceAndDir getDistance(Cell currNode, Cell nextNode) {

        int x1, x2, y1, y2;
        x1 = currNode.x;
        x2 = nextNode.x;
        y1 = currNode.y;
        y2 = nextNode.y;
        //distanceAndDir retval;

        if( x2 < x1) {
            if(currNode.direction == "up")

                return new distanceAndDir(1, "up");
            else if (currNode.direction == "right")
                return new distanceAndDir(2, "up");
            else if (currNode.direction == "left")
                return new distanceAndDir(2, "up");
            else if (currNode.direction =="down")
                return new distanceAndDir(3, "up");
        }
        else if( x2 > x1)
        {
            if(currNode.direction == "up")
                return  new distanceAndDir(3, "down");
            else if (currNode.direction == "right")
                return new distanceAndDir(2, "down");
            else if (currNode.direction == "left")
                return new distanceAndDir(2, "down");
            else if (currNode.direction =="down")
                return new distanceAndDir(1, "down");
        }

        if(y2 < y1) {
            if(currNode.direction == "up")
                return  new distanceAndDir(2, "left");
            else if (currNode.direction == "right")
            {
                //System.out.println("+1");
                return new distanceAndDir(1, "left");
            }

            else if (currNode.direction == "left")
                return new distanceAndDir(3, "left");
            else if (currNode.direction =="down")
                return new distanceAndDir(2, "left");
        }

        else if( y2 > y1) {
            if(currNode.direction == "up")
                return  new distanceAndDir(2, "right");
            else if (currNode.direction == "right")
                return new distanceAndDir(1, "right");
            else if (currNode.direction == "left")
                return new distanceAndDir(3, "right");
            else if (currNode.direction =="down")
                return new distanceAndDir(2, "right");
        }
        return null;

    }
    public Cell updateNode(Cell currNode, Cell nextNode) {


        int x1, x2, y1, y2;
        int distance = 0;
        String direction = null;
        x1 = currNode.x;
        x2 = nextNode.x;
        y1 = currNode.y;
        y2 = nextNode.y;
        //distanceAndDir retval;
        //System.out.println(currNode.direction);

        if( x2 < x1) {
            if(currNode.direction == "up")
            {
                distance = 1;
                direction = "up";
            }
            else if (currNode.direction == "right")
            {
                distance = 2;
                direction = "up";
            }
            else if (currNode.direction == "left")
            {
                distance = 2;
                direction = "up";
            }
            else if (currNode.direction =="down")
            {
                distance = 3;
                direction = "up";
            }
        }
        else if( x2 > x1)
        {
            if(currNode.direction == "up")
            {
                distance = 3;
                direction = "down";
            }
            else if (currNode.direction == "right")
            {
                distance = 2;
                System.out.println(distance);
                direction = "down";
            }
            else if (currNode.direction == "left")
            {
                distance = 2;
                direction = "down";
            }
            else if (currNode.direction =="down")
            {
                distance = 1;
                direction = "down";
            }
        }

        if(y2 < y1) {
            if(currNode.direction == "up")
            {
                distance = 2;
                direction = "left";
            }
            else if (currNode.direction == "right")
            {
                distance = 1;
                direction = "left";
            }

            else if (currNode.direction == "left")
            {
                distance = 3;
                direction = "left";
            }
            else if (currNode.direction =="down")
            {
                distance = 2;
                direction = "left";
            }
        }

        else if( y2 > y1) {
            if(currNode.direction == "up")
            {
                distance = 2;
                direction = "right";
            }
            else if (currNode.direction == "right")
            {
                distance = 1;
                direction = "right";
            }
            else if (currNode.direction == "left")
            {
                distance = 3;
                direction = "right";
            }
            else if (currNode.direction =="down")
            {
                distance = 2;
                direction = "right";
            }
        }





        //getDistance(currNode, nextNode, dis_and_dir);

        System.out.printf("\ndistance = ", distance);

        if(currNode == source)
            nextNode.distance = 999999999;
        int distanceToCompare = currNode.distance + distance;
        if (distanceToCompare < nextNode.distance) {
            nextNode.distance = distanceToCompare;
            nextNode.parent_x = currNode.x;
            nextNode.parent_y = currNode.y;
            nextNode.direction = direction;
        }
        return nextNode;
    }

    public Cell closestNode(LinkedList<Cell> queue) {
        Cell currentCloset = null;
        currentCloset = queue.peekFirst();
        queue.clear();
        return currentCloset;
    }
    @Override
    public void run() {
        LinkedList<Cell> queue = new LinkedList<>();
        Cell current, tmp;

        source.distance = 0;
        source.direction = "right";
        queue.add(source);
        try {
            // pop the waiting queue
            while (!queue.isEmpty() && !pathFound) {
                Thread.sleep(Constants.THREAD_SLEEP_TIME);
                if (!Constants.isPause)
                {
                    current = queue.poll();
                    Cell min_dis_cell = null;
                    int min_dis = 9999999;
                    if (current.state != CellState.SOURCE)
                        Controller.paintBlock(current.x, current.y, Constants.BORDER, Constants.VISITED);
                    //Go to all neighbors of the current state and push into queue if path not found

                    for (int i = 0; i < Constants.NUM_OF_NEIGHBORS && !pathFound; i++) {
                        if (inRange(current.x + X[i], current.y + Y[i])) {

                            tmp = Controller.CellGrid[current.x + X[i]][current.y + Y[i]];
                            tmp = updateNode(current, tmp);

                            if (tmp.state == CellState.TARGET || tmp.state == CellState.UNVISITED) {

                                // Update distance based on weighted cell.

                                //tmp.weight = current.weight + 1;
                                //tmp.setParent(current.x, current.y);

                                //tmp = updateNode(current, tmp);
                                if (min_dis > tmp.distance)
                                {
                                    min_dis = tmp.distance;
                                    min_dis_cell = tmp;

                                }


                                if (tmp.state != CellState.TARGET )
                                    Controller.paintBlock(tmp.x, tmp.y, Constants.BORDER, Constants.NEXT_VISIT);
                                else {
                                    tracePath(tmp);
                                    shortestPath = tmp.distance;
                                    pathFound = true;
                                    break;
                                }

                                Controller.CellGrid[tmp.x][tmp.y].state = CellState.VISITED;
                                queue.add(tmp);
                            }
                        }
                    }

                    //queue.addFirst(min_dis_cell);
                    for(int j = 0; j < queue.size(); j++)
                        System.out.printf("%d  ",queue.get(j).distance);
                }
                else
                    Thread.sleep(Constants.THREAD_PAUSE_TIME);
            }
        } catch (Exception e) {
            System.out.println("Thread interrupted while sleeping");
        }
    }

    public void tracePath(Cell cell) {
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
