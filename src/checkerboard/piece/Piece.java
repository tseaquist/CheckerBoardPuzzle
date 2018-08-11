package checkerboard.piece;

import java.util.ArrayList;

public class Piece
{
    public static final int MAX_NUM_ROTATIONS = 4;
    public final int numCells;
    public final ArrayList<OrientedPiece> rotations;

    public Piece(OrientedPiece orientedPiece)
    {
        this.numCells = orientedPiece.size;
        rotations = new ArrayList<>(MAX_NUM_ROTATIONS);
        rotations.add(orientedPiece);
        OrientedPiece last = orientedPiece;
        for(int i = 0; i < MAX_NUM_ROTATIONS-1; i++)
        {
            last = last.rotate();
            if(!rotations.contains(last))
            {
                rotations.add( last );
            }
        }
    }
    
    public int getNumRotations()
    {
        return rotations.size();
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
