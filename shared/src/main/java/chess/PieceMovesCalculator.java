package chess;

import java.util.ArrayList;

public interface PieceMovesCalculator {
    public ArrayList<ChessMove> pieceMoves();

    default public boolean onBoard(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return (1 <= row && row <= 8) && (1 <= col && col <= 8);
    }
}
