package checkerboard.piece;

public class Piece
{
    public static final int NUM_ROTATIONS = 4;
    public final int numberofrots;
    public final int size;
    public final OrientedPiece[] rotations;

    public Piece(OrientedPiece orientedPiece)
    {
    	int nrots = 4;
        this.size = orientedPiece.size;
        rotations = new OrientedPiece[NUM_ROTATIONS];
        OrientedPiece currentRotation = orientedPiece;
        for(int i = 0; i < NUM_ROTATIONS; i++)
        {
            rotations[i] = currentRotation;
            currentRotation = currentRotation.rotate();
        }
        if (rotations[0].equal(rotations[2])) nrots = 2;
        if (rotations[0].equal(rotations[1])) nrots = 1;
        
        this.numberofrots = nrots;
    }

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
