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

    private boolean onBoard(ChessPosition positionInQuestion) {
        int i = positionInQuestion.getRow();
        int j = positionInQuestion.getColumn();
        if ((1 <= i && i <= 8) && (1 <= j && j <= 8)) {
            return true;
        } else {
            return false;
        }
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
        if (1 <= i <= 8 && 1 <= j <= 8) {
            validMoves.add(new ChessMove(this.position, positionInQuestion, null));
        }

        // Two spaces in front
        i = this.position.getRow() + 2;
        j = this.position.getColumn();
        positionInQuestion = new ChessPosition(i, j);
        pieceAtPosition = board.getPiece(positionInQuestion);
        // We check that the position of i is 4, because that's the only valid 2-move row for white
        if (i == 4 && pieceAtPosition == null) {
            validMoves.add(new ChessMove(this.position, positionInQuestion, null));
        }

        // Taking to top-left
        i = this.position.getRow() + 1;
        j = this.position.getColumn() - 1;
        positionInQuestion = new ChessPosition(i, j);
        pieceAtPosition = board.getPiece(positionInQuestion);
        // We check that the position of i is 4, because that's the only valid 2-move row for white
        if (i <= 8 && j >= 1) {
            // Has to be a piece there
            if (pieceAtPosition != null) {
                // Has to be other team's piece
                if (pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                    validPromotions = handlePromotions(currentPiece, positionInQuestion);
                    if (validPromotions.isEmpty()) {
                        validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    } else {
                        for (ChessPiece.PieceType promotion : validPromotions) {
                            validMoves.add(new ChessMove(this.position, positionInQuestion, promotion));
                        }
                    }
                }
            }
        }

        // Taking to top-right
        i = this.position.getRow() + 1;
        j = this.position.getColumn() + 1;
        positionInQuestion = new ChessPosition(i, j);
        pieceAtPosition = board.getPiece(positionInQuestion);
        // We check that the position of i is 4, because that's the only valid 2-move row for white
        if (i <= 8 && j <= 8) {
            // Has to be a piece there
            if (pieceAtPosition != null) {
                // Has to be other team's piece
                if (pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                    validPromotions = handlePromotions(currentPiece, positionInQuestion);
                    if (validPromotions.isEmpty()) {
                        validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    } else {
                        for (ChessPiece.PieceType promotion : validPromotions) {
                            validMoves.add(new ChessMove(this.position, positionInQuestion, promotion));
                        }
                    }
                }
            }
        }

        return validMoves;
    }
}
