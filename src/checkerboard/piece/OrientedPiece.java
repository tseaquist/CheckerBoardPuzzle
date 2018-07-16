package checkerboard.piece;

import java.util.Arrays;
import java.util.Comparator;

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
            int compare = Integer.compare(c1.row * c1.row, c2.row * c2.row);
            if (compare != 0)
            {
                return compare;
            } else
            {
                return Integer.compare(c1.col * c1.col, c2.col * c2.col);
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
        boolean blackSpecified = split[0].equals("b") ? true : false;
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
}