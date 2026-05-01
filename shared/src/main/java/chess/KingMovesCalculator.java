package chess;

import java.sql.Array;
import java.util.ArrayList;

public class KingMovesCalculator implements PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;

    public KingMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    @Override
    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(position);
        ChessPosition positionInQuestion;
        ChessPiece pieceInQuestion;

        for (int i = position.getRow() - 1; i <= position.getRow() + 1; i++) {
            for (int j = position.getColumn() - 1; j <= position.getColumn() + 1; j++) {
                positionInQuestion = new ChessPosition(i, j);
                if (onBoard(positionInQuestion)) {
                    pieceInQuestion = board.getPiece(positionInQuestion);
                    if (pieceInQuestion == null || pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                        validMoves.add(new ChessMove(position, positionInQuestion, null));
                    }
                }
            }
        }

        return validMoves;
    }
}
