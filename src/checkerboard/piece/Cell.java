package checkerboard.piece;

public class Cell
{
	public final int row;
    public final int col;
    public final Cell.Color color;

    public Cell(int row, int col, Cell.Color color)
    {
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public String toString()
    {
        return "(" + row + "," + col + "," + color + ")";
    }
    
    public enum Color
    {
        BLACK, WHITE;
        public String toString()
        {
            if (this == BLACK)
            {
                return "B";
            } else
            {
                return "W";
            }
        }
    }
    
    public boolean equal(Cell c)
    {
    	if (c.row == this.row && c.col == this.col && c.color == this.color) return true;
    	return false;
    }
}