package Main.GraphRelated;

public class Cell {
    public int x, y, weight;
    public int parent_x, parent_y;

    public CellState state;

    public Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
        state = CellState.UNVISITED;
    }

    public void setParent(int x, int y)
    {
        this.parent_x = x;
        this.parent_y = y;
    }

}
