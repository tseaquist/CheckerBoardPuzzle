package checkerboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import checkerboard.piece.Cell;
import checkerboard.piece.Cell.Color;
import checkerboard.piece.OrientedPiece;
import checkerboard.piece.Piece;

public class CheckerBoard
{
    protected int numberOfSolutionsFound;
    protected final int gridDimension;
    protected final int numberOfPieces;
    protected final int[] list;
    protected final int[] oldlist;
    protected MutableInt count = new MutableInt(0);
    protected boolean dif;
    protected int[][] board;
    Piece[] pieces;

    public CheckerBoard(Piece[] pieces)
    {
        int totalSize = 0;
        for(Piece piece : pieces)
        {
            totalSize += piece.size;
        }
        this.gridDimension = (int) Math.sqrt(totalSize);
        this.pieces = pieces;
        this.numberOfPieces = pieces.length;
        
        board = new int[gridDimension][gridDimension];
        
        list = new int[numberOfPieces + 1];
        oldlist = new int[numberOfPieces + 1];
        /* initialized the permutation array and saves a copy */
        for (int i = 0; i < numberOfPieces + 1; i++)
        {
            /* the duplicate array is used to see if the next permutation */
            list[i] = numberOfPieces - i; /* compares for optimization */
            oldlist[i] = list[i];
        }
        oldlist[numberOfPieces] = 1;
        count.val = numberOfPieces;
        for (int r = 0; r < gridDimension; r++)
        {
            for (int c = 0; c < gridDimension; c++)
            {
                board[r][c] = -1;
            }
        }
        numberOfSolutionsFound = 0;
        dif = false;
    }

    public void placepiece(int row, int column, int loc, int rot)
    {
        /* place the piece */
        int listLoc = list[loc];
        for (Cell cell : pieces[listLoc].rotations[rot].cells)
        {
            board[row + cell.row][column + cell.col] = listLoc;
        }
    }

    public void removepiece(int row, int column, int loc, int rot)
    {
        /* remove the piece */
        int listLoc = list[loc];
        for (Cell cell : pieces[listLoc].rotations[rot].cells)
        {
            board[row + cell.row][column + cell.col] = -1;
        }
    }

    public boolean checkpiece(int row, int column, int loc, int rot)
    {
        /* checks to see if all the */
        int listLoc = list[loc];
        Piece piece = pieces[listLoc];
        OrientedPiece rotation = piece.rotations[rot];
        Color referenceColor = rotation.cells[0].color;
        if ((((row + column) % 2 == 0) && (referenceColor != Color.WHITE)) || /* squares are available */
                (((row + column) % 2 == 1) && (referenceColor != Color.BLACK)))
        {
            return false;
        }
        for (Cell cell : piece.rotations[rot].cells)
        {
            int rownumber = cell.row;
            int columnnumber = cell.col;
            if ((rownumber + row < 0) || (rownumber + row > gridDimension - 1) || (columnnumber + column < 0) || (columnnumber + column > gridDimension - 1)) return false;
            if (board[rownumber + row][columnnumber + column] != -1) return false;
        }
        return true;
    }

    public void locateposition(MutableInt row, MutableInt column)
    {
        /* locates the location for placing the next piece */
        for (; column.val < gridDimension; column.val++)
        {
            if (board[row.val][column.val] == -1) return;
        }
        for (; row.val < gridDimension; ++row.val)
        {
            for (column.val = 0; column.val < gridDimension; column.val++)
            {
                if (board[row.val][column.val] == -1) return;
            }
        }
    }

    public void printboard()
    {
        for (int r = 0; r < gridDimension; r++)
        {
            for (int c = 0; c < gridDimension; c++)
            {
                String val = Integer.toString(board[r][c]);
                System.out.print(String.format("%-3s", val));
            }
            System.out.println();
        }
    }

    public static Piece[] generatePieces(Path path) throws IOException
    {
        List<Piece> pieces = new ArrayList<>();
        try (BufferedReader infile = Files.newBufferedReader(path))
        {
            String line;
            while ((line = infile.readLine()) != null)
            {
                line = line.trim();
                if (line.isEmpty())
                {
                    continue; /* comment */
                }
                pieces.add(new Piece(OrientedPiece.parse(line)));
            }
            return pieces.toArray(new Piece[pieces.size()]);
        } catch (IOException e)
        {
            throw e;
        }
    }
    
    public void solve() 
    {
        generatePermutation(numberOfPieces);
    }

    public void generatePermutation(int n)
    {
        /* Generates the permutations */
        int c = 1;
        int t;
        if (n > 2)
        {
            generatePermutation(n - 1);
        } else
        {
            dif = false;
            for (int i = numberOfPieces; i >= count.val; i--)
            {
                if (list[i] != oldlist[i])
                {
                    dif = true;
                }
            }
            if (dif)
            {
                for (int i = 0; i <= numberOfPieces; i++)
                {
                    oldlist[i] = list[i];
                }
                MutableInt loc = new MutableInt(numberOfPieces);
                MutableInt color = new MutableInt(0);
                count.val = numberOfPieces;
                solveit(loc, 0, 0, color, count);
            }
        }
        while (c < n)
        {
            if (n % 2 == 0)
            {
                t = list[n];
                list[n] = list[c];
                list[c] = t;
            } else
            {
                t = list[n];
                list[n] = list[1];
                list[1] = t;
            }
            c = c + 1;
            if (n > 2)
            {
                generatePermutation(n - 1);
            } else
            {
                dif = false;
                for (int i = numberOfPieces; i >= count.val; i--)
                {
                    if (list[i] != oldlist[i])
                    {
                        dif = true;
                    }
                }
                if (dif)
                {
                    for (int i = 0; i <= numberOfPieces; i++)
                    {
                        oldlist[i] = list[i];
                    }
                    MutableInt loc = new MutableInt(numberOfPieces);
                    MutableInt color = new MutableInt(0);
                    count.val = numberOfPieces;
                    solveit(loc, 0, 0, color, count);
                }
            }
        }
    }

    void solveit(MutableInt loc, int rowIn, int columnIn, MutableInt color, MutableInt count)
    {
        MutableInt row = new MutableInt(rowIn);
        MutableInt column = new MutableInt(columnIn);
        /* recursive procedure to */
        /* find solutions */
        int times;
        if (loc.val == 0)
        {
            System.out.println(++numberOfSolutionsFound);
            printboard();
            System.out.println();
        } else
        {
            locateposition(row, column);
            int listLoc = list[loc.val];
            Piece piece = pieces[listLoc];
            times = piece.numberofrots;
            for (int rot = 0; rot < times; rot++)
            {
                if (checkpiece(row.val, column.val, loc.val, rot))
                {
                    placepiece(row.val, column.val, loc.val, rot);
                    loc.val--;
                    if (count.val > loc.val) count.val = loc.val;
                    solveit(loc, row.val, column.val, color, count);
                    loc.val++;
                    removepiece(row.val, column.val, loc.val, rot);
                }
            }
        }
    }
    
    public static void printOldFormat(Piece[] pieces)
    {
        for(Piece piece : pieces)
        {
            System.out.println(piece.size);
            for(OrientedPiece rotation : piece.rotations)
            {
                System.out.print(rotation.cells[0].color == Color.BLACK ? 1 : 0);
                for(Cell cell : rotation.cells)
                {
                    System.out.print(cell.row);
                    System.out.print(cell.col);
                }
                System.out.println();
            }
        }
    }
    
    public static void illustratePieces(Piece[] pieces)
    {
        int i = 0;
        for(Piece piece : pieces)
        {
            System.out.println("Piece " + (i) + ":");
            System.out.println("Piece " + (i) + " size: " + (piece.size) + " Number of Rotations: " + (piece.numberofrots));    
            System.out.println(piece.rotations[0].illustrate());           	     	


            for (int j = 0; j < piece.numberofrots; j++)
            {
            	System.out.println("Piece " + (i) + "  Rotation " + (j) + ":");
            	System.out.println(piece.rotations[j].illustrate());
            	for (int k=0; k < piece.rotations[j].size; k++) {
            		System.out.println("(" + (piece.rotations[j].cells[k].row) + "," + (piece.rotations[j].cells[k].col) + ")");
            	}
            		
            }
          	i++;     
        }
    }

    protected static class MutableInt
    {
        public int val;

        public MutableInt(int val)
        {
            this.val = val;
        }
    }
}
