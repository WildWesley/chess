package chess;

import java.util.ArrayList;

public class KnightMovesCalculator implements PieceMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;

    public KnightMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public static boolean onBoard(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return (1 <= row && row <= 8) && (1 <= col && col <= 8);
    }

    @Override
    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(position);
        ChessPosition positionInQuestion;
        ChessPiece pieceInQuestion;

        // Up
        int i = position.getRow() + 2;
        int j = position.getColumn() - 1;
        positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null || pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
            }
        }

        i = position.getRow() + 2;
        j = position.getColumn() + 1;
        positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null || pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
            }
        }

        // Right
        i = position.getRow() + 1;
        j = position.getColumn() + 2;
        positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null || pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
            }
        }

        i = position.getRow() - 1;
        j = position.getColumn() + 2;
        positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null || pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
            }
        }

        // Down
        i = position.getRow() - 2;
        j = position.getColumn() + 1;
        positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null || pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
            }
        }

        i = position.getRow() - 2;
        j = position.getColumn() - 1;
        positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null || pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
            }
        }

        // Left
        i = position.getRow() - 1;
        j = position.getColumn() - 2;
        positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null || pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
            }
        }

        i = position.getRow() + 1;
        j = position.getColumn() - 2;
        positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null || pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
            }
        }

        return validMoves;
    }
}
