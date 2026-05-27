package client;

import chess.*;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Arrays;

public class DisplayMethods {
    public static boolean whiteSpace(ChessPosition position) {
        return (position.getRow() + position.getColumn()) % 2 == 1;
    }

    public static String printPieces(ChessPiece piece) {
        String output = "";
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            output = EscapeSequences.BLACK_PAWN;
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            output = EscapeSequences.BLACK_KING;
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            output = EscapeSequences.BLACK_QUEEN;
        } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            output = EscapeSequences.BLACK_BISHOP;
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            output = EscapeSequences.BLACK_KNIGHT;
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            output = EscapeSequences.BLACK_ROOK;
        }
        return output;
    }

    public static void handlePiece(ChessPiece piece) {
        String output;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            output = EscapeSequences.SET_TEXT_COLOR_WHITE + printPieces(piece);
        } else {
            output = EscapeSequences.SET_TEXT_COLOR_BLACK + printPieces(piece);
        }
        System.out.print(output);
    }

    public static void printSpace(ChessGame currentGame, ChessBoard board, int row, int col, ChessPosition highlightPosition) {
        ChessPosition positionInQuestion = new ChessPosition(row, col);
        ChessPiece pieceAtPosition = board.getPiece(positionInQuestion);
        ArrayList<ChessPosition> validPositions = new ArrayList<>();
        if (highlightPosition != null) {
            ChessPiece highlightPiece = board.getPiece(highlightPosition);
            ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>)
                    currentGame.validMoves(highlightPosition);
            for (ChessMove move : validMoves) {
                validPositions.add(move.getEndPosition());
            }
        }
        if (whiteSpace(positionInQuestion)) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_BROWN);
            if (validPositions.contains(positionInQuestion)) {
                System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
            }
        } else {
            System.out.print(EscapeSequences.SET_BG_COLOR_BROWN);
            if (validPositions.contains(positionInQuestion)) {
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
            }
        }

        if (pieceAtPosition == null) {
            System.out.print(EscapeSequences.EMPTY);
        } else {
            handlePiece(pieceAtPosition);
        }
    }

    // Important to remember that when adding up i and j, if they are odd, it's light,
    // and if they're even, it's dark
    public static void printBoardWhite(ChessGame game, ChessPosition highlightPosition) {
        ChessBoard board = game.getBoard();
        ArrayList<String> boardLetters =
                new ArrayList<>(Arrays.asList(EscapeSequences.EMPTY, " a\u2003", " b\u2003", " c\u2003", " d\u2003",
                        " e\u2003", " f\u2003", " g\u2003", " h\u2003",
                        EscapeSequences.EMPTY,
                        EscapeSequences.RESET_BG_COLOR + "\n"));
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK);
        for (String col : boardLetters) {
            System.out.print(col);
        }
        for (int i = 8; i >= 1; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                    EscapeSequences.SET_TEXT_COLOR_BLACK + "\u2003" +
                    Integer.toString(i) + " ");
            for (int j = 1; j <= 8; j++) {
                printSpace(game, board, i, j, highlightPosition);
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                    EscapeSequences.SET_TEXT_COLOR_BLACK + "\u2003" +
                    Integer.toString(i) + " ");
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.print("\n");
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        for (String col : boardLetters) {
            System.out.print(col);
        }
    }

    public static void printBoardBlack(ChessGame game, ChessPosition highlightPosition) {
        ChessBoard board = game.getBoard();
        ArrayList<String> boardLetters =
                new ArrayList<>(Arrays.asList(EscapeSequences.EMPTY, " h\u2003", " g\u2003", " f\u2003", " e\u2003",
                        " d\u2003", " c\u2003", " b\u2003",  " a\u2003",
                        EscapeSequences.EMPTY,
                        EscapeSequences.RESET_BG_COLOR + "\n"));
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK);
        for (String col : boardLetters) {
            System.out.print(col);
        }
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                    EscapeSequences.SET_TEXT_COLOR_BLACK + "\u2003" +
                    Integer.toString(i) + " ");
            for (int j = 8; j >= 1; j--) {
                printSpace(game, board, i, j, highlightPosition);
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                    EscapeSequences.SET_TEXT_COLOR_BLACK + "\u2003" +
                    Integer.toString(i) + " ");
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.print("\n");
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        for (String col : boardLetters) {
            System.out.print(col);
        }
    }
}
