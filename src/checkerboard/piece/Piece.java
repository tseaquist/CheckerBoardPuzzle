package checkerboard.piece;

import java.util.ArrayList;

public class Piece
{
    public static final int MAX_NUM_ROTATIONS = 4;
    public final int numCells;
    public final ArrayList<OrientedPiece> rotations; /* Difference between ArrayList<x> and x[] ??? */

    public Piece(OrientedPiece orientedPiece)
    {
        this.numCells = orientedPiece.size;
        rotations = new ArrayList<>(MAX_NUM_ROTATIONS);
        rotations.add(orientedPiece);
        OrientedPiece last = orientedPiece;
        for(int i = 0; i < MAX_NUM_ROTATIONS-1; i++)  /* Why not i < MAX_NUM_ROTATIONS/2  or ... Maybe these things aren't rotations by other versions of the cell. Reflections maybe??? */
        {
            last = last.rotate();
            if(!rotations.contains(last))  /* Must be possible with ArrayList. What about just a List */
            {
                rotations.add( last );
            }
        }
    }
    
    public int getNumRotations()
    {
        return rotations.size(); /* I guess only the good ones are on the list and size returns the number of objects in ArrayList ??? */
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for(OrientedPiece rotation : rotations)
        {
            builder.append(rotation.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
