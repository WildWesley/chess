package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn;
    ChessBoard board;

    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece originalPiece = board.getPiece(startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        if (originalPiece == null) {return null;}
        else {
            ArrayList<ChessPosition> whitePositions;
            ArrayList<ChessPosition> blackPositions;
            ArrayList<ArrayList<ChessPosition>> container;
            ChessPiece pieceTaken;
            ChessPosition kingPosition = new ChessPosition(1,1);
            ArrayList<ChessMove> pieceMoves;
            container = populatePieces();
            whitePositions = container.get(0);
            blackPositions = container.get(1);

            // TODO: Ask about addAll with collection and ArrayList
            // Grab all possible valid moves
            validMoves.addAll(originalPiece.pieceMoves(board, startPosition));

            // Loop through all possible valid moves
            for (ChessMove move : validMoves) {
                pieceTaken = movePiece(move);
                if (isInCheck(teamTurn)) {
                    validMoves.remove(move);
                }

                // Reset board
                resetPositions(originalPiece, pieceTaken, startPosition, move.getEndPosition());
            }

            // Loop through whole board, populate all positions, save them
            // Grab all possible valid moves with pieceMoves();
            // Use movePiece() to actually move the piece
            // Check if isInCheck() is true, if so, remove from possibleMoves, reset the board (function), and iterate

            // Two options for reset board: keep track of OG piece, keep track of pieces taken, make move, check
            // isInCheck(), if so, remove position from possibleMoves, and put OG piece back at start and pieceTaken
            // back at endPosition
            return validMoves;
        }
    }

    public ChessPiece movePiece(ChessMove move) {
        // Makes move regardless of whether the move is valid
        ChessPiece originalPiece = board.getPiece(move.getStartPosition());
        ChessPiece pieceTaken = board.getPiece(move.getEndPosition());
        board.addPiece(move.getEndPosition(), originalPiece);
        return pieceTaken;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece originalPiece = board.getPiece(move.getStartPosition());
        ChessPiece pieceTaken;
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
        if (validMoves == null) {
            throw new InvalidMoveException();
        }
        if (validMoves.contains(move)) {
           pieceTaken = movePiece(move);
           if (teamTurn == TeamColor.WHITE) {
               teamTurn = TeamColor.BLACK;
           } else {
               teamTurn = TeamColor.WHITE;
           }
        } else {
            throw new InvalidMoveException();
        }
        // Grab the piece at the start position
        // Grab all valid moves with validMoves()
        // Check if move is in validMoves
        // if so, movePiece
        // else throw error
        // Change team turn
    }

    /**
     * @return the ArrayLists for white and then black pieces in an ArrayList
     */
    private ArrayList<ArrayList<ChessPosition>> populatePieces() {
        ChessPosition positionInQuestion;
        ChessPiece pieceInQuestion;
        ArrayList<ChessPosition> whitePositions = new ArrayList<ChessPosition>();
        ArrayList<ChessPosition> blackPositions = new ArrayList<ChessPosition>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                positionInQuestion = new ChessPosition(i, j);
                pieceInQuestion = board.getPiece(positionInQuestion);
                if (pieceInQuestion == null) {
                    continue;
                } else if (pieceInQuestion.getTeamColor() == TeamColor.WHITE) {
                    whitePositions.add(positionInQuestion);
                } else {
                    blackPositions.add(positionInQuestion);
                }
            }
        }
        // You can initialize an ArrayList with values using the following:
        // ArrayList<Integer> list = new ArrayList<>(java.util.Arrays.asList(1, 2));
        return new ArrayList<ArrayList<ChessPosition>>(java.util.Arrays.asList(whitePositions, blackPositions));
    }
    
    private void resetPositions(ChessPiece originalPiece, ChessPiece pieceTaken,
                                ChessPosition startPosition, ChessPosition endPosition) {
        board.addPiece(startPosition, originalPiece);
        board.addPiece(endPosition, pieceTaken);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ArrayList<ChessPosition> whitePositions;
        ArrayList<ChessPosition> blackPositions;
        ArrayList<ArrayList<ChessPosition>> container;
        ChessPiece pieceInQuestion;
        ChessPosition kingPosition = new ChessPosition(1,1);
        ArrayList<ChessMove> pieceMoves;
        container = populatePieces();
        whitePositions = container.get(0);
        blackPositions = container.get(1);

        // Find king position using teamColor
        if (teamColor == TeamColor.WHITE) {
            for (ChessPosition position : whitePositions) {
                pieceInQuestion = board.getPiece(position);
                if (pieceInQuestion.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = position;
                    break;
                }
            }
        } else {
            for (ChessPosition position : blackPositions) {
                pieceInQuestion = board.getPiece(position);
                if (pieceInQuestion.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = position;
                    break;
                }
            }
        }

        // Loop through opposing pieces
        if (teamColor == TeamColor.WHITE) {
            for (ChessPosition position : blackPositions) {
                pieceInQuestion = board.getPiece(position);
                pieceMoves = (ArrayList<ChessMove>) pieceInQuestion.pieceMoves(board, position);
                for (ChessMove move : pieceMoves) {
                    if (move.getEndPosition().equals(kingPosition)) {
                        return true;
                    }
                }
            }
        } else {
            for (ChessPosition position : whitePositions) {
                pieceInQuestion = board.getPiece(position);
                pieceMoves = (ArrayList<ChessMove>) pieceInQuestion.pieceMoves(board, position);
                for (ChessMove move : pieceMoves) {
                    if (move.getEndPosition().equals(kingPosition)) {
                        return true;
                    }
                }
            }
        }
        // No piece attacking king position
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
   public boolean isInCheckmate(TeamColor teamColor) {
       if (isInCheck(teamColor)) {
           ArrayList<ChessPosition> whitePositions;
           ArrayList<ChessPosition> blackPositions;
           ArrayList<ArrayList<ChessPosition>> container;
           ChessPiece originalPiece;
           ChessPiece pieceTaken;
           ChessPosition kingPosition = new ChessPosition(1,1);
           ArrayList<ChessMove> validMoves;
           container = populatePieces();
           whitePositions = container.get(0);
           blackPositions = container.get(1);

           // Loop through team positions depending on color
           if (teamColor == TeamColor.WHITE) {
               for (ChessPosition position : whitePositions) {
                   validMoves = (ArrayList<ChessMove>) validMoves(position);
                   if (!validMoves.isEmpty()) {
                       return false;
                   }
               }
           } else {
               for (ChessPosition position : blackPositions) {
                   validMoves = (ArrayList<ChessMove>) validMoves(position);
                   if (!validMoves.isEmpty()) {
                       return false;
                   }
               }
           }
           return true;
       }
        // Check if isInCheck() is true
        // Loop through the whole board and grab all piece positions
        // Loop through white pieces and check if each piece has any valid moves
        // If so, return false
        // else return true
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            ArrayList<ChessPosition> whitePositions;
            ArrayList<ChessPosition> blackPositions;
            ArrayList<ArrayList<ChessPosition>> container;
            ChessPiece originalPiece;
            ChessPiece pieceTaken;
            ChessPosition kingPosition = new ChessPosition(1,1);
            ArrayList<ChessMove> validMoves;
            container = populatePieces();
            whitePositions = container.get(0);
            blackPositions = container.get(1);

            // Loop through team positions depending on color
            if (teamColor == TeamColor.WHITE) {
                for (ChessPosition position : whitePositions) {
                    validMoves = (ArrayList<ChessMove>) validMoves(position);
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            } else {
                for (ChessPosition position : blackPositions) {
                    validMoves = (ArrayList<ChessMove>) validMoves(position);
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", board=" + board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return getTeamTurn() == chessGame.getTeamTurn() && Objects.equals(getBoard(), chessGame.getBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTeamTurn(), getBoard());
    }
}
