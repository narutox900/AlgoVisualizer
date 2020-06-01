package Main.GraphRelated;

import com.jfoenix.validation.IntegerValidator;

public class Cell {
    public int x, y, weight;
    public int distance;
    public int parent_x, parent_y;
    public String direction;
    public boolean weighted;
    public int count;

    public CellState state;

    public Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
        state = CellState.UNVISITED;
        this.distance = Integer.MAX_VALUE;
        //this.update = true;
        this.weighted = false;
        this.count = 0;
    }

    public void setParent(int x, int y)
    {
        this.parent_x = x;
        this.parent_y = y;
    }

}
