package checkerboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CheckerBoard
{
    int numberoftimes;
    int[] list = new int[13];
    MutableInt count = new MutableInt(0);
    boolean dif;
    int[] oldlist = new int[13];
    int[][] board = new int[8][8];
    Piece[] pieces = new Piece[12];

    public CheckerBoard()
    {
        /* initialized the permutation array and saves a copy */
        for (int i = 0; i < 13; i++)
        {
            /* the duplicate array is used to see if the next permutation */
            list[i] = 12 - i; /* compares for optimization */
            oldlist[i] = list[i];
            if (i < 12)
            {
                pieces[i] = new Piece();
            }
        }
        oldlist[12] = 1;
        count.val = 12;
        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                board[r][c] = -1;
            }
        }
        numberoftimes = 1;
        dif = false;
    }

    public void placepiece(int row, int column, int loc, int rot)
    {
        /* place the piece */
        for (int i = 1; i < 2 * pieces[list[loc]].number; i = i + 2)
        {
            board[row + pieces[list[loc]].rotations[rot][i]][column + pieces[list[loc]].rotations[rot][i + 1]] = list[loc];
        }
    }

    public void removepiece(int row, int column, int loc, int rot)
    {
        /* remove the piece */
        for (int i = 1; i < 2 * pieces[list[loc]].number; i = i + 2)
            board[row + pieces[list[loc]].rotations[rot][i]][column + pieces[list[loc]].rotations[rot][i + 1]] = -1;
    }

    public boolean checkpiece(int row, int column, int loc, int rot)
    {
        /* checks to see if all the */
        if ((((row + column) % 2 == 0) && (pieces[list[loc]].rotations[rot][0] != 0)) || /* squares are available */
                (((row + column) % 2 == 1) && (pieces[list[loc]].rotations[rot][0] != 1)))
            return false;
        for (int b = 1; b < (pieces[list[loc]].number * 2); b = b + 2)
        {
            int rownumber = pieces[list[loc]].rotations[rot][b];
            int columnnumber = pieces[list[loc]].rotations[rot][b + 1];
            if ((rownumber + row < 0) || (rownumber + row > 7) || (columnnumber + column < 0) || (columnnumber + column > 7)) return false;
            if (board[rownumber + row][columnnumber + column] != -1) return false;
        }
        return true;
    }

    public void locateposition(MutableInt row, MutableInt column)
    {
        /* locates the location for placing the next piece */
        for (; column.val < 8; column.val++)
        {
            if (board[row.val][column.val] == -1) return;
        }
        for (; row.val < 8; ++row.val)
        {
            for (column.val = 0; column.val < 8; column.val++)
            {
                if (board[row.val][column.val] == -1) return;
            }
        }
    }

    public void printboard()
    {
        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                String val = Integer.toString(board[r][c]);
                System.out.print(String.format("%-3s", val));
            }
            System.out.println();
        }
    }

    public void readPieces(Path path) throws IOException
    {
        try (BufferedReader infile = Files.newBufferedReader(path))
        {
            int count = 0;
            int size;
            String line;
            while ((line = infile.readLine()) != null)
            {
                line = line.trim();
                if (line.isEmpty())
                {
                    continue;
                }
                size = Integer.parseInt(line);
                pieces[count].number = size;
                for (int rot = 0; rot < 4; rot++)
                {
                    line = infile.readLine();
                    line = line.trim();
                    char[] num = line.toCharArray();
                    int ct = 0;
                    for (int h = 0; h <= size * 2; h++)
                    {
                        if (num[ct] == '-')
                        {
                            ct++;
                            pieces[count].rotations[rot][h] = Integer.parseInt(String.valueOf(num[ct])) * -1;
                        } else
                        {
                            pieces[count].rotations[rot][h] = Integer.parseInt(String.valueOf(num[ct]));
                        }
                        ct++;
                    }
                }
                count++;
            }
        } catch (IOException e)
        {
            throw e;
        }
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
            for (int i = 12; i >= count.val; i--)
            {
                if (list[i] != oldlist[i])
                {
                    dif = true;
                }
            }
            if (dif)
            {
                for (int i = 0; i <= 12; i++)
                {
                    oldlist[i] = list[i];
                }
                MutableInt loc = new MutableInt(12);
                MutableInt color = new MutableInt(0);
                count.val = 12;
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
                for (int i = 12; i >= count.val; i--)
                {
                    if (list[i] != oldlist[i])
                    {
                        dif = true;
                    }
                }
                if (dif)
                {
                    for (int i = 0; i <= 12; i++)
                    {
                        oldlist[i] = list[i];
                    }
                    MutableInt loc = new MutableInt(12);
                    MutableInt color = new MutableInt(0);
                    count.val = 12;
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
            System.out.println(numberoftimes);
            printboard();
            System.out.println();
            numberoftimes++;
        } else
        {
            locateposition(row, column);
            if (list[loc.val] == 0)
            {
                times = 2;
            } else
            {
                times = 4;
            }
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

    public static void main(String[] args) throws Exception
    {
        CheckerBoard myboard = new CheckerBoard();
        int n = 12;
        myboard.readPieces(Paths.get("C:/Users/Smeckle/Documents/Coding/cpp-projects/checker-board-puzzle/puzzle.dat"));
        long time = System.currentTimeMillis();
        myboard.generatePermutation(n);
        System.out.println("Runtime: " + (System.currentTimeMillis() - time) / 1000.0);
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
