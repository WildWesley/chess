package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator calculator;
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP) {
            calculator = new BishopMovesCalculator(myPosition, board);
        } else if (piece.getPieceType() == PieceType.ROOK) {
            calculator = new RookMovesCalculator(myPosition, board);
        } else if (piece.getPieceType() == PieceType.QUEEN) {
            calculator = new QueenMovesCalculator(myPosition, board);
        } else if (piece.getPieceType() == PieceType.KING) {
            calculator = new KingMovesCalculator(myPosition, board);
        } else if (piece.getPieceType() == PieceType.KNIGHT) {
            calculator = new KnightMovesCalculator(myPosition, board);
        } else if (piece.getPieceType() == PieceType.PAWN) {
            calculator = new PawnMovesCalculator(myPosition, board);
        }
        else {
            return new ArrayList<ChessMove>();
        }
        return calculator.pieceMoves();
    }

    @Override
    public String toString() {
        // If you include a string in the addition, it will automatically cast to a string
        return type + " " + pieceColor;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
