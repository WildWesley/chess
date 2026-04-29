package chess;

import java.util.ArrayList;

public class KingMovesCalculator implements PieceMovesCalculator {
    // "Inheriting" from an interface is actually called "Implement" in Java
    ChessPosition position;
    ChessBoard board;

    // For constructors, you don't declare a return type
    public KingMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        this.position = currentPosition;
        this.board = board;
    }

    @Override
    public ArrayList<ChessMove> pieceMoves() {

        ChessPiece currentPiece = board.getPiece(this.position);
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        for (int i=this.position.getRow() - 1; i <= this.position.getRow() + 1; i++) {
            for (int j=this.position.getColumn() - 1; j <= this.position.getColumn() + 1; j++) {
                // To start, I create a new position and grab the piece at that position
                ChessPosition positionInQuestion = new ChessPosition(i, j);
                ChessPiece pieceAtPosition = board.getPiece(positionInQuestion);
                if (this.position != positionInQuestion) {
                    // Check if nothing at the position
                    if (pieceAtPosition == null) {
                        validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    } else {
                        if (pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                            validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                        }
                    }
                }
            }
        }
        // Check edge of board and return possible moves
        // Also make sure to use getPiece() to check if there is already a piece there
        //     - if(moveLocationOnBoard() && (notPieceAtLocation() || pieceAtLocationOtherTeam()){...}
        // Don't worry about putting the king in check with the moves
        return validMoves;
    }
}
