package chess;

import java.util.ArrayList;

public interface PieceMovesCalculator {
    // The following is the syntax for a "Method Signature"
    // It acts as a requirement for inheriting classes
    public ArrayList<ChessMove> pieceMoves();

    // If you add the default key word, you can have a function that is inherited from an interface like normal
}