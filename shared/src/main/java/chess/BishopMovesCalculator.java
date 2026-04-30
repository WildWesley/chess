package chess;

import java.util.ArrayList;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;

    public BishopMovesCalculator(ChessBoard board, ChessPosition position) {
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

        // Up-Right
        int i = position.getRow() + 1;
        int j = position.getColumn() + 1;
        positionInQuestion = new ChessPosition(i, j);
        while (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                i++;
                j++;
                positionInQuestion = new ChessPosition(i, j);
            } else if (pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                break;
            } else {
                break;
            }
        }

        // Down-Right
        i = position.getRow() - 1;
        j = position.getColumn() + 1;
        positionInQuestion = new ChessPosition(i, j);
        while (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                i--;
                j++;
                positionInQuestion = new ChessPosition(i, j);
            } else if (pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                break;
            } else {
                break;
            }
        }


        // Down-Left
        i = position.getRow() - 1;
        j = position.getColumn() - 1;
        positionInQuestion = new ChessPosition(i, j);
        while (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                i--;
                j--;
                positionInQuestion = new ChessPosition(i, j);
            } else if (pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                break;
            } else {
                break;
            }
        }


        // Up-Left
        i = position.getRow() + 1;
        j = position.getColumn() - 1;
        positionInQuestion = new ChessPosition(i, j);
        while (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null) {
                validMoves.add(new ChessMove(position, positionInQuestion, null));
                i++;
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
