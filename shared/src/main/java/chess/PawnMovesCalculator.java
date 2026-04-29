package chess;

import java.util.ArrayList;

public class PawnMovesCalculator implements PieceMovesCalculator {
    // "Inheriting" from an interface is actually called "Implement" in Java
    ChessPosition position;
    ChessBoard board;

    // For constructors, you don't declare a return type
    public PawnMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        this.position = currentPosition;
        this.board = board;
    }

    private ArrayList<ChessPiece.PieceType> handlePromotions(ChessPiece currentPiece, ChessPosition positionInQuestion) {
        ArrayList<ChessPiece.PieceType> validPromotions = new ArrayList<ChessPiece.PieceType>();
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (positionInQuestion.getRow() == 8) {
                validPromotions.add(ChessPiece.PieceType.QUEEN);
                validPromotions.add(ChessPiece.PieceType.ROOK);
                validPromotions.add(ChessPiece.PieceType.KNIGHT);
                validPromotions.add(ChessPiece.PieceType.BISHOP);
            }
        } else {
            if (positionInQuestion.getRow() == 1) {
                validPromotions.add(ChessPiece.PieceType.QUEEN);
                validPromotions.add(ChessPiece.PieceType.ROOK);
                validPromotions.add(ChessPiece.PieceType.KNIGHT);
                validPromotions.add(ChessPiece.PieceType.BISHOP);
            }
        }

        return validPromotions;
    }

    @Override
    public ArrayList<ChessMove> pieceMoves() {

        ChessPiece currentPiece = board.getPiece(this.position);
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ArrayList<ChessPiece.PieceType> validPromotions;
        ChessPosition positionInQuestion;
        ChessPiece pieceAtPosition;

        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            // WHITE MOVES
            // One space in front
            int i = this.position.getRow() + 1;
            int j = this.position.getColumn();
            positionInQuestion = new ChessPosition(i, j);
            if (KnightMovesCalculator.onBoard(positionInQuestion)) {
                pieceAtPosition = board.getPiece(positionInQuestion);
                if (j <= 8 && pieceAtPosition == null) {
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

            // Two spaces in front
            // Check that first space is empty first
            i = this.position.getRow() + 1;
            j = this.position.getColumn();
            positionInQuestion = new ChessPosition(i, j);
            if (KnightMovesCalculator.onBoard(positionInQuestion)) {
                pieceAtPosition = board.getPiece(positionInQuestion);
                if (i == 3 && pieceAtPosition == null) {
                    i = this.position.getRow() + 2;
                    positionInQuestion = new ChessPosition(i, j);
                    pieceAtPosition = board.getPiece(positionInQuestion);
                    if (pieceAtPosition == null) {
                        validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    }
                }
            }

            // Taking to top-left
            i = this.position.getRow() + 1;
            j = this.position.getColumn() - 1;
            positionInQuestion = new ChessPosition(i, j);
            if (KnightMovesCalculator.onBoard(positionInQuestion)) {
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
            }

            // Taking to top-right
            i = this.position.getRow() + 1;
            j = this.position.getColumn() + 1;
            positionInQuestion = new ChessPosition(i, j);
            if (KnightMovesCalculator.onBoard(positionInQuestion)) {
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
            }
        } else {
            // BLACK MOVES
            // One space in front
            int i = this.position.getRow() - 1;
            int j = this.position.getColumn();
            positionInQuestion = new ChessPosition(i, j);
            if (KnightMovesCalculator.onBoard(positionInQuestion)) {
                pieceAtPosition = board.getPiece(positionInQuestion);
                if (j >= 1 && pieceAtPosition == null) {
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

            // Two spaces in front
            // Check that first space is empty first
            i = this.position.getRow() - 1;
            j = this.position.getColumn();
            positionInQuestion = new ChessPosition(i, j);
            if (KnightMovesCalculator.onBoard(positionInQuestion)) {
                pieceAtPosition = board.getPiece(positionInQuestion);
                if (i == 6 && pieceAtPosition == null) {
                    i = this.position.getRow() - 2;
                    positionInQuestion = new ChessPosition(i, j);
                    pieceAtPosition = board.getPiece(positionInQuestion);
                    if (pieceAtPosition == null) {
                        validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    }
                }
            }

            // Taking to bottom-left
            i = this.position.getRow() - 1;
            j = this.position.getColumn() - 1;
            positionInQuestion = new ChessPosition(i, j);
            if (KnightMovesCalculator.onBoard(positionInQuestion)) {
                pieceAtPosition = board.getPiece(positionInQuestion);
                // We check that the position of i is 4, because that's the only valid 2-move row for white
                if (i >= 1 && j >= 1) {
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
            }

            // Taking to bottom-right
            i = this.position.getRow() - 1;
            j = this.position.getColumn() + 1;
            positionInQuestion = new ChessPosition(i, j);
            if (KnightMovesCalculator.onBoard(positionInQuestion)) {
                pieceAtPosition = board.getPiece(positionInQuestion);
                // We check that the position of i is 4, because that's the only valid 2-move row for white
                if (i >= 1 && j <= 8) {
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
            }
        }
        return validMoves;
    }
}
