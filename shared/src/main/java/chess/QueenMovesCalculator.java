package chess;

import java.util.ArrayList;

public class QueenMovesCalculator implements PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;

    public QueenMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    @Override
    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(position);
        ChessPosition positionInQuestion;
        ChessPiece pieceInQuestion;

        // ROOK MOVES
        RookMovesCalculator RookCalculator = new RookMovesCalculator(board, position);
        validMoves.addAll(RookCalculator.pieceMoves());

        // BISHOP MOVES
        // Up-Right
        BishopMovesCalculator BishopCalculator = new BishopMovesCalculator(board, position);
        validMoves.addAll(BishopCalculator.pieceMoves());
        
        return validMoves;
    }
}
