package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class PrintBoard {
    private static ChessBoard board;
    private static ChessGame.TeamColor playerColor;
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    public PrintBoard(ChessGame game, ChessGame.TeamColor playerColor) {
        this.board = game.getBoard();
        this.playerColor = playerColor;
    }


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        board = new ChessBoard();
        board.resetBoard();
        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessBoard(out);

        drawHeaders(out);
    }

    private static void drawHeaders(PrintStream out) {
        String[] headers = { " ", "a", "b", "c", "d", "e", "f", "g", "h", " " };
        if (playerColor == ChessGame.TeamColor.WHITE) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        } else {
            for (int boardCol = BOARD_SIZE_IN_SQUARES-1; boardCol >= 0 ; --boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        }
        out.print(SET_BG_COLOR_BLACK);
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print("  ");
        printHeaderText(out, headerText);
        out.print("  ");
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(player);
    }

    private static void drawChessBoard(PrintStream out) {
        for (int boardRow = 1; boardRow < BOARD_SIZE_IN_SQUARES-1; ++boardRow) {
            endRowOfSquares(out);
            if (playerColor == ChessGame.TeamColor.WHITE) {
                for (int boardCol = 0; boardCol < 1; ++boardCol) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(SET_TEXT_COLOR_BLACK);
                    out.print("  ");
                    out.print(BOARD_SIZE_IN_SQUARES-(boardRow+2));
                    out.print("  ");
                }
            } else {
                for (int boardCol = 0; boardCol < 1; ++boardCol) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(SET_TEXT_COLOR_BLACK);
                    out.print("  ");
                    out.print(boardRow);
                    out.print("  ");
                }
            }
            for (int boardCol = 1; boardCol < BOARD_SIZE_IN_SQUARES-1; ++boardCol) {
                if ((boardRow + boardCol) % 2 == 1) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                out.print("  ");
                ChessPiece piece = board.getPiece(new ChessPosition(boardRow,boardCol));
                if (piece != null){
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        out.print(SET_TEXT_COLOR_RED);
                    } else {
                        out.print(SET_TEXT_COLOR_BLUE);
                    }
                    switch (piece.getPieceType()) {
                        case KING:
                            out.print("K");
                            break;
                        case QUEEN:
                            out.print("Q");
                            break;
                        case KNIGHT:
                            out.print("N");
                            break;
                        case BISHOP:
                            out.print("B");
                            break;
                        case ROOK:
                            out.print("R");
                            break;
                        case PAWN:
                            out.print("P");
                            break;
                    }
                } else {
                    out.print(" ");
                }
                out.print("  ");
            }
            if (playerColor == ChessGame.TeamColor.WHITE) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
                out.print("  ");
                out.print(BOARD_SIZE_IN_SQUARES-(boardRow+1));
                out.print("  ");
            } else {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
                out.print("  ");
                out.print(boardRow);
                out.print("  ");
            }
        }

        out.print(RESET_BG_COLOR);

        out.println();
    }

    private static void endRowOfSquares(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawHorizontalLine(PrintStream out) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS;

        for (int lineRow = 0; lineRow < SQUARE_SIZE_IN_PADDED_CHARS; ++lineRow) {
            setLightGrey(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setWhiteAndBlue(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setLightGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }
}
