package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn;
    ChessBoard board;
    // Populate this every single time maybe in a helper function
    ArrayList<ChessPosition> whitePositions = new ArrayList<>();
    ArrayList<ChessPosition> blackPositions = new ArrayList<>();
    ChessPosition whiteKingPosition = new ChessPosition();
    ChessPosition blackKingPosition = new ChessPosition();


    public ChessGame() {
        this.board = new ChessBoard();
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
        ChessPiece pieceInQuestion = board.getPiece(startPosition);
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        if (pieceInQuestion == null) {return null;}
        else {
            // Loop through whole board, populate all positions, save them
            // Grab all possible moves with pieceMoves();
            // Use movePiece() to actually move the piece
            // Check if isInCheck() is true, if so, remove from possibleMoves, reset the board (function), and iterate
            return pieceInQuestion.pieceMoves(board, startPosition);}
    }

    public void movePiece(ChessMove move) {
        // Moving the piece on the board
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // Grab the piece at the start position
        // Grab all valid moves with validMoves()
        // Check if move is in validMoves
        // if so, movePiece
        // else throw error
        // Change team turn
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");

        // Loop through all positions, populate pieces, loop through opponent positions, check if king is attacked
        // Populate all team positions AND find king position
        // Loop through pieces on opposing team, and use pieceMoves() to get attacked spaces
        // Check if at any time a possibly attacked space is the king position, if so, return true
        // else return false
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        throw new RuntimeException("Not implemented");

        // Loop through all opposing pieces and see if any of the pieces can attack king, if so break
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // Check if isInCheck() is true
        // Loop through the whole board and grab all piece positions
        // Loop through white pieces and check if each piece has any valid moves
        // If so, return false
        // else return true
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // Check if isInCheck() is false
        // Loop through the whole board and grab all piece positions
        // Loop through white pieces and check if each piece has any valid moves
        // If so, return false
        // else return true
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
        throw new RuntimeException("Not implemented");
    }
}
