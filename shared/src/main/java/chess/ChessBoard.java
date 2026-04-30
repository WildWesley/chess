package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    private void addPieceAtCoordinates(int row, int col, ChessPiece piece) {
        ChessPosition position = new ChessPosition(row,col);
        addPiece(position, piece);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        squares = new ChessPiece[8][8];

        // WHITE PIECES
        // Pawns
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPieceAtCoordinates(2, 1, whitePawn);
        addPieceAtCoordinates(2, 2, whitePawn);
        addPieceAtCoordinates(2, 3, whitePawn);
        addPieceAtCoordinates(2, 4, whitePawn);
        addPieceAtCoordinates(2, 5, whitePawn);
        addPieceAtCoordinates(2, 6, whitePawn);
        addPieceAtCoordinates(2, 7, whitePawn);
        addPieceAtCoordinates(2, 8, whitePawn);

        // King
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPieceAtCoordinates(1, 5, whiteKing);

        // Queen
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPieceAtCoordinates(1, 4, whiteQueen);

        // Bishops
        ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPieceAtCoordinates(1, 6, whiteBishop);
        addPieceAtCoordinates(1, 3, whiteBishop);

        // Knights
        ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPieceAtCoordinates(1, 7, whiteKnight);
        addPieceAtCoordinates(1, 2, whiteKnight);

        // Rooks
        ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPieceAtCoordinates(1, 8, whiteRook);
        addPieceAtCoordinates(1, 1, whiteRook);


        // BLACK PIECES
        // Pawns
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPieceAtCoordinates(7, 1, blackPawn);
        addPieceAtCoordinates(7, 2, blackPawn);
        addPieceAtCoordinates(7, 3, blackPawn);
        addPieceAtCoordinates(7, 4, blackPawn);
        addPieceAtCoordinates(7, 5, blackPawn);
        addPieceAtCoordinates(7, 6, blackPawn);
        addPieceAtCoordinates(7, 7, blackPawn);
        addPieceAtCoordinates(7, 8, blackPawn);

        // King
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPieceAtCoordinates(8, 5, blackKing);

        // Queen
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPieceAtCoordinates(8, 4, blackQueen);

        // Bishops
        ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPieceAtCoordinates(8, 6, blackBishop);
        addPieceAtCoordinates(8, 3, blackBishop);

        // Knights
        ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPieceAtCoordinates(8, 7, blackKnight);
        addPieceAtCoordinates(8, 2, blackKnight);

        // Rooks
        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPieceAtCoordinates(8, 8, blackRook);
        addPieceAtCoordinates(8, 1, blackRook);

    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessBoard that)) {
            return false;
        }
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
