package chess;

import java.util.ArrayList;

public interface PieceMovesCalculator {
    // The following is the syntax for a "Method Signature"
    // It acts as a requirement for inheriting classes
    public ArrayList<ChessMove> pieceMoves();
}