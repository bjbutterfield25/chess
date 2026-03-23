package ui;

import static ui.EscapeSequences.*;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final String EMPTY = "   ";


    public static void draw(boolean isWhite) {
        System.out.print(ERASE_SCREEN);
        drawHeaders(isWhite);
        drawChessboard(isWhite);
        drawHeaders(isWhite);
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(boolean isWhite) {
        setBlack();
        System.out.print("   ");
        String[] headers = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            int col = isWhite ? i : (BOARD_SIZE_IN_SQUARES - 1 - i);
            printHeaderText(headers[col]);
        }
        System.out.println();
    }

    private static void printHeaderText(String headerText) {
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.print(SET_TEXT_COLOR_GREEN);
        System.out.print(headerText);
        setBlack();
    }

    private static void drawChessboard(boolean isWhite) {
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            int boardRow = isWhite ? i : (BOARD_SIZE_IN_SQUARES - 1 - i);
            drawRowOfSquares(boardRow, isWhite);
        }
    }

    private static void drawRowOfSquares(int boardRow, boolean isWhite) {
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
                System.out.print(EMPTY);
            }
            setBlack();
            System.out.println(SET_TEXT_COLOR_GREEN + " " + rank + " ");
        }
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
