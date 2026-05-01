package chess;

import java.util.ArrayList;

public class RookMovesCalculator implements PieceMovesCalculator{

    private final ChessBoard board;
    private final ChessPosition position;

    public RookMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }
    
    @Override
    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(position);
        ChessPosition positionInQuestion;
        ChessPiece pieceInQuestion;

        // Up
        int i = position.getRow() + 1;
        int j = position.getColumn();
        positionInQuestion = new ChessPosition(i, j);
        while (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                i++;
                positionInQuestion = new ChessPosition(i, j);
            } else if (pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                break;
            } else {
                break;
            }
        }

        // Right
        i = position.getRow();
        j = position.getColumn() + 1;
        positionInQuestion = new ChessPosition(i, j);
        while (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                j++;
                positionInQuestion = new ChessPosition(i, j);
            } else if (pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                break;
            } else {
                break;
            }
        }


        // Down
        i = position.getRow() - 1;
        j = position.getColumn();
        positionInQuestion = new ChessPosition(i, j);
        while (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                i--;
                positionInQuestion = new ChessPosition(i, j);
            } else if (pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                break;
            } else {
                break;
            }
        }


        // Left
        i = position.getRow();
        j = position.getColumn() - 1;
        positionInQuestion = new ChessPosition(i, j);
        while (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                j--;
                positionInQuestion = new ChessPosition(i, j);
            } else if (pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                break;
            } else {
                break;
            }
        }


        return validMoves;
    }
}
