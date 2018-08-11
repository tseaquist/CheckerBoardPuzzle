package checkerboard.piece;

import java.util.Arrays;

import checkerboard.piece.Cell.Color;

public class OrientedPiece
{
    public final int size;
    public final Cell[] cells;
    
    public OrientedPiece(Cell[] cells)
    {
        this.size = cells.length;
        
        Cell referenceCell = null;
        for (Cell candidate : cells)
        {
            // Test that candidate cell is to the left and above all others
            boolean valid = true;
            for (Cell test : cells)
            {
//                These rotations will fill a chosen square on the 
//                board and will never fill above that chosen location.  
//                They will also never fill to the left in the same row as the chosen square. 
                if (test.row < candidate.row || (candidate.row == test.row && test.col < candidate.col))
                {
                    valid = false;
                    break;
                }
            }
            if (valid)
            {
                referenceCell = candidate;
                break;
            }
        }
        if (referenceCell == null)
        {
            throw new IllegalArgumentException("Piece cannot be labeled correctly");
        }

        Cell[] newCells = new Cell[size];
        for (int i = 0; i < size; i++)
        {
            Cell cell = cells[i];
            newCells[i] = new Cell(cell.row - referenceCell.row, cell.col - referenceCell.col, cell.color);
        }

        Arrays.sort(newCells, (c1, c2) -> {
            int compare = Integer.compare(c1.row , c2.row );
            if (compare != 0)
            {
                return compare;
            } else
            {
                return Integer.compare(c1.col ,  c2.col);
            }
        });

        this.cells = newCells;
    }

    protected OrientedPiece rotate()
    {
        Cell[] newCells = new Cell[size];
        for (int i = 0; i < size; i++)
        {
            Cell cell = this.cells[i];
            newCells[i] = new Cell(cell.col, -cell.row, cell.color);
        }
        return new OrientedPiece(newCells);
    }

    public static OrientedPiece parse(String string)
    {
        String[] split = string.trim().split(",");
        int size = (split.length - 1) / 2;
        boolean blackSpecified = split[0].equalsIgnoreCase("b") ? true : false;
        Cell[] cells = new Cell[size];
        boolean evenBlack = blackSpecified;
        for (int i = 1; i < split.length; i += 2)
        {
            int row = Integer.parseInt(split[i].trim());
            int col = Integer.parseInt(split[i + 1].trim());
            if (i == 1)
            {
                evenBlack = ((row + col) % 2 == 0) == blackSpecified;
            }
            Color color = evenBlack == ((row + col) % 2 == 0) ? Color.BLACK : Color.WHITE;
            Cell cell = new Cell(row, col, color);
            cells[i / 2] = cell;
        }
        return new OrientedPiece(cells);
    }

    public String toString()
    {
        return Arrays.toString(cells);
    }
    
    public String illustrate()
    {
        int minRow = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxCol = Integer.MIN_VALUE;
        for(Cell cell : cells)
        {
            if(cell.row < minRow)
            {
                minRow = cell.row;
            }
            if(cell.row > maxRow)
            {
                maxRow = cell.row;
            }
            if(cell.col < minCol)
            {
                minCol = cell.col;
            }
            if(cell.col > maxCol)
            {
                maxCol = cell.col;
            }
        }
        StringBuilder builder = new StringBuilder();
        for(int r = minRow; r < maxRow + 1; r++)
        {
            for(int c = minCol; c < maxCol + 1; c++)
            {
                String marker = "  ";
                for(Cell cell : cells)
                {
                    if(cell.row == r && cell.col == c)
                    {
                        marker = cell.color == Color.BLACK ? "X " : "O ";
                        break;
                    }
                }
                builder.append(marker);
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    
    
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(cells);
        result = prime * result + size;
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
        OrientedPiece other = (OrientedPiece) obj;
        if (!Arrays.equals(cells, other.cells))
            return false;
        if (size != other.size)
            return false;
        return true;
    }
}
