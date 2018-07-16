package checkerboard.piece;

public class Piece
{
    public static final int NUM_ROTATIONS = 4;
    public final int size;
    public final OrientedPiece[] rotations;

    public Piece(OrientedPiece orientedPiece)
    {
        this.size = orientedPiece.size;
        rotations = new OrientedPiece[NUM_ROTATIONS];
        OrientedPiece currentRotation = orientedPiece;
        for(int i = 0; i < NUM_ROTATIONS; i++)
        {
            rotations[i] = currentRotation;
            currentRotation = currentRotation.rotate();
        }
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
