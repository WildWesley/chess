package chess;

import java.util.ArrayList;

public class KnightMovesCalculator implements PieceMovesCalculator {
    // "Inheriting" from an interface is actually called "Implement" in Java
    ChessPosition position;
    ChessBoard board;

    // For constructors, you don't declare a return type
    public KnightMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        this.position = currentPosition;
        this.board = board;
    }

    // TODO: Ask why this is static?
    // Checks if a position is on the board
    public static boolean onBoard(ChessPosition positionInQuestion) {
        int i = positionInQuestion.getRow();
        int j = positionInQuestion.getColumn();
        return (1 <= i && i <= 8) && (1 <= j && j <= 8);
    }

    @Override
    public ArrayList<ChessMove> pieceMoves() {

        ChessPiece currentPiece = board.getPiece(this.position);
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ArrayList<ChessPiece.PieceType> validPromotions;

        // Top Moves
        int i = this.position.getRow() + 2;
        int j = this.position.getColumn() - 1;
        ChessPosition positionInQuestion = new ChessPosition(i, j);
        ChessPiece pieceAtPosition = board.getPiece(positionInQuestion);
        if (onBoard(positionInQuestion)) {
            if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(this.position, positionInQuestion, null));
            }
        }

        i = this.position.getRow() + 2;
        j = this.position.getColumn() + 1;
        positionInQuestion = new ChessPosition(i, j);
        pieceAtPosition = board.getPiece(positionInQuestion);
        if (onBoard(positionInQuestion)) {
            if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(this.position, positionInQuestion, null));
            }
        }


        // Right Moves
        i = this.position.getRow() + 1;
        j = this.position.getColumn() + 2;
        positionInQuestion = new ChessPosition(i, j);
        pieceAtPosition = board.getPiece(positionInQuestion);
        if (onBoard(positionInQuestion)) {
            if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(this.position, positionInQuestion, null));
            }
        }

        i = this.position.getRow() - 1;
        j = this.position.getColumn() + 2;
        positionInQuestion = new ChessPosition(i, j);
        pieceAtPosition = board.getPiece(positionInQuestion);
        if (onBoard(positionInQuestion)) {
            if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(this.position, positionInQuestion, null));
            }
        }

        // Bottom Moves
        i = this.position.getRow() - 2;
        j = this.position.getColumn() + 1;
        positionInQuestion = new ChessPosition(i, j);
        pieceAtPosition = board.getPiece(positionInQuestion);
        if (onBoard(positionInQuestion)) {
            if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(this.position, positionInQuestion, null));
            }
        }

        i = this.position.getRow() - 2;
        j = this.position.getColumn() - 1;
        positionInQuestion = new ChessPosition(i, j);
        pieceAtPosition = board.getPiece(positionInQuestion);
        if (onBoard(positionInQuestion)) {
            if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(this.position, positionInQuestion, null));
            }
        }

        // Left Moves
        i = this.position.getRow() - 1;
        j = this.position.getColumn() - 2;
        positionInQuestion = new ChessPosition(i, j);
        pieceAtPosition = board.getPiece(positionInQuestion);
        if (onBoard(positionInQuestion)) {
            if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(this.position, positionInQuestion, null));
            }
        }

        i = this.position.getRow() + 1;
        j = this.position.getColumn() - 2;
        positionInQuestion = new ChessPosition(i, j);
        pieceAtPosition = board.getPiece(positionInQuestion);
        if (onBoard(positionInQuestion)) {
            if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(this.position, positionInQuestion, null));
            }
        }

        return validMoves;
    }
}
