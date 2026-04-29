package chess;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class RookMovesCalculator implements PieceMovesCalculator {
    // "Inheriting" from an interface is actually called "Implement" in Java
    ChessPosition position;
    ChessBoard board;

    // For constructors, you don't declare a return type
    public RookMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        this.position = currentPosition;
        this.board = board;
    }

    //    private boolean positionsDiagonal(ChessPosition pos1, ChessPosition pos2) {
    //        return abs(pos1.getRow() - pos2.getRow()) == (pos1.getColumn() - pos2.getColumn());
    //    }

    @Override
    public ArrayList<ChessMove> pieceMoves() {
        ChessPiece currentPiece = board.getPiece(this.position);
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        int i = this.position.getRow();
        int j = this.position.getColumn();

        // Up
        while (i <= 8) {
            ChessPosition positionInQuestion = new ChessPosition(i,j);
            ChessPiece pieceAtPosition = this.board.getPiece(positionInQuestion);
            if (positionInQuestion != this.position) {
                if (pieceAtPosition == null) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    i++;
                }
                else if (pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    break;
                }
                else {break;}
                // if no piece
                //     - add move; iterate
                // if piece of opposite color
                //     - add move; break;
                // if piece of same color
                //     - break

            } else {
                i++;
            }
        }

        i = this.position.getRow();
        j = this.position.getColumn();

        // Right
        while (j <= 8) {
            ChessPosition positionInQuestion = new ChessPosition(i,j);
            ChessPiece pieceAtPosition = this.board.getPiece(positionInQuestion);
            if (positionInQuestion != this.position) {
                if (pieceAtPosition == null) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    j++;
                }
                else if (pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    break;
                }
                else {break;}
                // if no piece
                //     - add move; iterate
                // if piece of opposite color
                //     - add move; break;
                // if piece of same color
                //     - break

            } else {
                j++;
            }
        }

        i = this.position.getRow();
        j = this.position.getColumn();

        // Down
        while (i >= 1) {
            ChessPosition positionInQuestion = new ChessPosition(i,j);
            ChessPiece pieceAtPosition = this.board.getPiece(positionInQuestion);
            if (positionInQuestion != this.position) {
                if (pieceAtPosition == null) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    i--;
                }
                else if (pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    break;
                }
                else {break;}
                // if no piece
                //     - add move; iterate
                // if piece of opposite color
                //     - add move; break;
                // if piece of same color
                //     - break

            } else {
                i--;
            }
        }

        i = this.position.getRow();
        j = this.position.getColumn();

        // Left
        while (j >= 1) {
            ChessPosition positionInQuestion = new ChessPosition(i,j);
            ChessPiece pieceAtPosition = this.board.getPiece(positionInQuestion);
            if (positionInQuestion != this.position) {
                if (pieceAtPosition == null) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    j--;
                }
                else if (pieceAtPosition.getTeamColor() != currentPiece.getTeamColor()) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    break;
                }
                else {break;}
                // if no piece
                //     - add move; iterate
                // if piece of opposite color
                //     - add move; break;
                // if piece of same color
                //     - break

            } else {
                j--;
            }
        }

        return validMoves;
    }
}