package chess;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class BishopMovesCalculator implements PieceMovesCalculator {
    // "Inheriting" from an interface is actually called "Implement" in Java
    ChessPosition position;
    ChessBoard board;

    // For constructors, you don't declare a return type
    public BishopMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
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
        // Up-right
        while (i <= 8 && j <= 8) {
            ChessPosition positionInQuestion = new ChessPosition(i,j);
            ChessPiece pieceAtPosition = this.board.getPiece(positionInQuestion);
            if (positionInQuestion != this.position) {
                if (pieceAtPosition == null) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    i++;
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
                i++;
                j++;
            }
        }

        i = this.position.getRow();
        j = this.position.getColumn();
        // Down-right
        while (i >= 1 && j <= 8) {
            ChessPosition positionInQuestion = new ChessPosition(i,j);
            ChessPiece pieceAtPosition = this.board.getPiece(positionInQuestion);
            if (positionInQuestion != this.position) {
                if (pieceAtPosition == null) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    i--;
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
                i--;
                j++;
            }
        }

        i = this.position.getRow();
        j = this.position.getColumn();
        // Down-left
        while (i >= 1 && j >= 1) {
            ChessPosition positionInQuestion = new ChessPosition(i,j);
            ChessPiece pieceAtPosition = this.board.getPiece(positionInQuestion);
            if (positionInQuestion != this.position) {
                if (pieceAtPosition == null) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    i--;
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
                i--;
                j--;
            }
        }

        i = this.position.getRow();
        j = this.position.getColumn();
        // Up-left
        while (i <= 8 && j >= 1) {
            ChessPosition positionInQuestion = new ChessPosition(i,j);
            ChessPiece pieceAtPosition = this.board.getPiece(positionInQuestion);
            if (positionInQuestion != this.position) {
                if (pieceAtPosition == null) {
                    validMoves.add(new ChessMove(this.position, positionInQuestion, null));
                    i++;
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
                i++;
                j--;
            }
        }

        return validMoves;
    }
}