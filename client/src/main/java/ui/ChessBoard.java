package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final String EMPTY = "   ";


    public static void draw(boolean isWhite, ChessGame game) {
        chess.ChessBoard board = game.getBoard();
        System.out.print(ERASE_SCREEN);
        drawHeaders(isWhite);
        drawChessboard(isWhite, board);
        drawHeaders(isWhite);
        System.out.print(RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR);
        System.out.println();
    }

    private static void drawHeaders(boolean isWhite) {
        setBlack();
        System.out.print(EMPTY);
        String[] headers = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            int col = isWhite ? i : (BOARD_SIZE_IN_SQUARES - 1 - i);
            printHeaderText(headers[col]);
        }
        System.out.print(EMPTY);
        System.out.print(RESET_BG_COLOR);
        System.out.println();
    }

    private static void printHeaderText(String headerText) {
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.print(SET_TEXT_COLOR_GREEN);
        System.out.print(headerText);
    }

    private static void drawChessboard(boolean isWhite, chess.ChessBoard board) {
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            int boardRow = isWhite ? i : (BOARD_SIZE_IN_SQUARES - 1 - i);
            drawRowOfSquares(boardRow, isWhite, board);
        }
    }

    private static void drawRowOfSquares(int boardRow, boolean isWhite, chess.ChessBoard board) {
        int rank = BOARD_SIZE_IN_SQUARES - boardRow;
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; squareRow++) {
            setBlack();
            System.out.print(SET_TEXT_COLOR_GREEN + " " + rank + " ");
            for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
                int col = isWhite ? i : (BOARD_SIZE_IN_SQUARES - 1 - i);
                if ((boardRow + col) % 2 == 0) {
                    setWhite();
                } else {
                    setBlack();
                }
                ChessPiece piece = board.getPiece(new ChessPosition(rank, col + 1));
                System.out.print(renderPiece(piece));
            }
            setBlack();
            System.out.print(SET_TEXT_COLOR_GREEN + " " + rank + " ");
            System.out.print(RESET_BG_COLOR);
            System.out.println();
        }
    }

    private static String renderPiece(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        String color = (piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                ? SET_TEXT_COLOR_RED
                : SET_TEXT_COLOR_BLUE;
        String type = switch (piece.getPieceType()) {
            case KING -> " K ";
            case QUEEN -> " Q ";
            case BISHOP -> " B ";
            case KNIGHT -> " N ";
            case ROOK -> " R ";
            case PAWN -> " P ";
        };
        return color + type;
    }

    private static void setWhite() {
        System.out.print(SET_BG_COLOR_WHITE);
        System.out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack() {
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.print(SET_TEXT_COLOR_BLACK);
    }
}
