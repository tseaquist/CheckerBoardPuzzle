package checkerboard;

import java.nio.file.Paths;

import checkerboard.piece.Piece;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        Piece[] pieces = CheckerBoard.generatePieces(Paths.get(args[0]));
        CheckerBoard.illustratePieces(pieces);
        System.out.println(" ");
        System.out.println("Solutions: ");
        
        CheckerBoard myboard = new CheckerBoard(pieces);
        long time = System.currentTimeMillis();
        myboard.solve();
        System.out.println("Runtime: " + (System.currentTimeMillis() - time) / 1000.0);
    }
}
