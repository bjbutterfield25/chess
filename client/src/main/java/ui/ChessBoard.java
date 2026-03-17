package ui;

import static ui.EscapeSequences.*;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final String EMPTY = "   ";


    public static void main(String[] args) {
        System.out.print(ERASE_SCREEN);
        drawHeaders();
        drawChessboard();
        drawHeaders();
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders() {
        setBlack();
        System.out.print("   ");
        String[] headers = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            printHeaderText(headers[boardCol]);
        }
        System.out.println();
    }

    private static void printHeaderText(String headerText) {
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.print(SET_TEXT_COLOR_GREEN);
        System.out.print(headerText);
        setBlack();
    }

    private static void drawChessboard() {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawRowOfSquares(boardRow);
        }
    }

    private static void drawRowOfSquares(int boardRow) {
        int rank = BOARD_SIZE_IN_SQUARES - boardRow;
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; squareRow++) {
            setBlack();
            System.out.print(SET_TEXT_COLOR_GREEN + " " + rank + " ");
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                if ((boardRow + boardCol) % 2 == 0) {
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
