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

    private void addPieceAtCoordinate(int row, int col, ChessPiece piece) {
        ChessPosition addPosition = new ChessPosition(row, col);
        addPiece(addPosition, piece);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPosition addPosition = new ChessPosition(2, 1);

        // WHITE PIECES
        // Place Pawns
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        addPieceAtCoordinate(2, 1, whitePawn);
        addPieceAtCoordinate(2, 2, whitePawn);
        addPieceAtCoordinate(2, 3, whitePawn);
        addPieceAtCoordinate(2, 4, whitePawn);
        addPieceAtCoordinate(2, 5, whitePawn);
        addPieceAtCoordinate(2, 6, whitePawn);
        addPieceAtCoordinate(2, 7, whitePawn);
        addPieceAtCoordinate(2, 8, whitePawn);

        // Place King
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPieceAtCoordinate(1, 5, whiteKing);

        // Place Queen
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPieceAtCoordinate(1, 4, whiteQueen);

        // Place Bishops
        ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPieceAtCoordinate(1, 6, whiteBishop);
        addPieceAtCoordinate(1, 3, whiteBishop);

        // Place Knights
        ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPieceAtCoordinate(1, 7, whiteKnight);
        addPieceAtCoordinate(1, 2, whiteKnight);

        // Place Rooks
        ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPieceAtCoordinate(1, 8, whiteRook);
        addPieceAtCoordinate(1, 1, whiteRook);

        // BLACK PIECES
        // Place Pawns
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        addPieceAtCoordinate(7, 1, blackPawn);
        addPieceAtCoordinate(7, 2, blackPawn);
        addPieceAtCoordinate(7, 3, blackPawn);
        addPieceAtCoordinate(7, 4, blackPawn);
        addPieceAtCoordinate(7, 5, blackPawn);
        addPieceAtCoordinate(7, 6, blackPawn);
        addPieceAtCoordinate(7, 7, blackPawn);
        addPieceAtCoordinate(7, 8, blackPawn);

        // Place King
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPieceAtCoordinate(8, 5, blackKing);

        // Place Queen
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPieceAtCoordinate(8, 4, blackQueen);

        // Place Bishops
        ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPieceAtCoordinate(8, 6, blackBishop);
        addPieceAtCoordinate(8, 3, blackBishop);

        // Place Knights
        ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPieceAtCoordinate(8, 7, blackKnight);
        addPieceAtCoordinate(8, 2, blackKnight);

        // Place Rooks
        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPieceAtCoordinate(8, 8, blackRook);
        addPieceAtCoordinate(8, 1, blackRook);
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
