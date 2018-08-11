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

    @Override
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
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + col;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + row;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cell other = (Cell) obj;
        if (col != other.col)
            return false;
        if (color != other.color)
            return false;
        if (row != other.row)
            return false;
        return true;
    }
    
}