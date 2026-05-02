package chess;

import javax.swing.*;
import java.util.ArrayList;

public class PawnMovesCalculator implements PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;

    public PawnMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    private ArrayList<ChessMove> normalPromotions(ChessPosition originalPosition, ChessPosition endPosition) {
        ArrayList<ChessMove> promotions = new ArrayList<>();
        promotions.add(new ChessMove(originalPosition, endPosition, ChessPiece.PieceType.QUEEN));
        promotions.add(new ChessMove(originalPosition, endPosition, ChessPiece.PieceType.ROOK));
        promotions.add(new ChessMove(originalPosition, endPosition, ChessPiece.PieceType.BISHOP));
        promotions.add(new ChessMove(originalPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        return promotions;
    }

    private ArrayList<ChessMove> handlePromotions(ChessPosition positionInQuestion, ChessPiece currentPiece) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        // White Promotions
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (positionInQuestion.getRow() == 8) {
                validMoves.addAll(normalPromotions(position, positionInQuestion));
            }
        } else {
            if (positionInQuestion.getRow() == 1) {
                validMoves.addAll(normalPromotions(position, positionInQuestion));
            }
        }

        return validMoves;
    }

    private ArrayList<ChessMove> whiteForward() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(new ChessPosition(position.getRow(), position.getColumn()));
        int i = position.getRow() + 1;
        int j = position.getColumn();
        ChessPosition positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            ChessPiece pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null) {
                ArrayList<ChessMove> validPromotions = handlePromotions(positionInQuestion, currentPiece);
                if (validPromotions.isEmpty()) {
                    validMoves.add(new ChessMove(position, positionInQuestion, null));
                    if (positionInQuestion.getRow() == 3) {
                        i = position.getRow() + 2;
                        j = position.getColumn();
                        positionInQuestion = new ChessPosition(i, j);
                        pieceInQuestion = board.getPiece(positionInQuestion);
                        if (pieceInQuestion == null) {
                            validMoves.add(new ChessMove(position, positionInQuestion, null));
                        }
                    }
                } else {
                    validMoves.addAll(validPromotions);
                    validPromotions.clear();
                }
            }
        }
        return validMoves;
    }

    private ArrayList<ChessMove> blackForward() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ArrayList<ChessMove> validPromotions = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(new ChessPosition(position.getRow(), position.getColumn()));
        ChessPiece pieceInQuestion;
        int i = position.getRow() - 1;
        int j = position.getColumn();
        ChessPosition positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion == null) {
                validPromotions = handlePromotions(positionInQuestion, currentPiece);
                if (validPromotions.isEmpty()) {
                    validMoves.add(new ChessMove(position, positionInQuestion, null));
                    if (positionInQuestion.getRow() == 6) {
                        i = position.getRow() - 2;
                        j = position.getColumn();
                        positionInQuestion = new ChessPosition(i,j);
                        pieceInQuestion = board.getPiece(positionInQuestion);
                        if (pieceInQuestion == null) {
                            validMoves.add(new ChessMove(position, positionInQuestion, null));
                        }
                    }
                } else {
                    validMoves.addAll(validPromotions);
                    validPromotions.clear();
                }
            }
        }
        return validMoves;
    }

    private ArrayList<ChessMove> addMovesAtAttackCoordinate(int i, int j) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ArrayList<ChessMove> validPromotions;
        ChessPiece currentPiece = board.getPiece(new ChessPosition(position.getRow(), position.getColumn()));
        ChessPiece pieceInQuestion;
        ChessPosition positionInQuestion = new ChessPosition(i,j);
        if (onBoard(positionInQuestion)) {
            pieceInQuestion = board.getPiece(positionInQuestion);
            if (pieceInQuestion != null && pieceInQuestion.getTeamColor() != currentPiece.getTeamColor()) {
                validPromotions = handlePromotions(positionInQuestion, currentPiece);
                if (validPromotions.isEmpty()) {
                    validMoves.add(new ChessMove(position, positionInQuestion, null));
                } else {
                    validMoves.addAll(validPromotions);
                    validPromotions.clear();
                }
            }
        }
        return validMoves;
    }

    private ArrayList<ChessMove> attack(ChessGame.TeamColor color) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int j = position.getColumn() - 1;
        int i;
        if (color == ChessGame.TeamColor.BLACK) {
            i = position.getRow() - 1;
            validMoves.addAll(addMovesAtAttackCoordinate(i, j));
        } else {
            i = position.getRow() + 1;
            validMoves.addAll(addMovesAtAttackCoordinate(i, j));
        }

        j = position.getColumn() + 1;
        if (color == ChessGame.TeamColor.BLACK) {
            i = position.getRow() - 1;
            validMoves.addAll(addMovesAtAttackCoordinate(i, j));
        } else {
            i = position.getRow() + 1;
            validMoves.addAll(addMovesAtAttackCoordinate(i, j));
        }

        return validMoves;
    }

    @Override
    public ArrayList<ChessMove> pieceMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(position);
        ChessPosition positionInQuestion;
        ChessPiece pieceInQuestion;
        ArrayList<ChessMove> validPromotions = new ArrayList<>();

        // WHITE MOVES
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            // Forward
            validMoves.addAll(whiteForward());

            // Attack
            validMoves.addAll(attack(ChessGame.TeamColor.WHITE));

        } else {
            // Forward
            validMoves.addAll(blackForward());

            // Attack
            validMoves.addAll(attack(ChessGame.TeamColor.BLACK));
        }

        return validMoves;
    }
}
