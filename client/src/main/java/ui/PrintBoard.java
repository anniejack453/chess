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
    private ChessBoard board;
    private ChessGame chess;
    private static ChessGame.TeamColor playerColor;
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    public PrintBoard(ChessGame game, ChessGame.TeamColor playerColor) {
        this.board = game.getBoard();
        this.playerColor = playerColor;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out);
        drawChessBoard(out);
        drawHeaders(out);
        out.println();
        out.print(RESET_TEXT_COLOR);
    }

    private void drawHeaders(PrintStream out) {
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
        out.print(RESET_BG_COLOR);
    }

    private void drawHeader(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print("  ");
        printHeaderText(out, headerText);
        out.print("  ");
    }

    private void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(player);
    }

    private void drawChessBoard(PrintStream out) {
        for (int boardRow = 1; boardRow < BOARD_SIZE_IN_SQUARES-1; ++boardRow) {
            endRowOfSquares(out);
            if (playerColor == ChessGame.TeamColor.WHITE) {
                for (int boardCol = 0; boardCol < 1; ++boardCol) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(SET_TEXT_COLOR_BLACK);
                    out.print("  ");
                    out.print(BOARD_SIZE_IN_SQUARES-(boardRow+1));
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
                if ((boardRow + boardCol) % 2 == 0) { //check for highlight another for loop for possible moves
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                out.print("  ");
                if (playerColor == ChessGame.TeamColor.WHITE) {
                    ChessPiece piece = board.getPiece(new ChessPosition(9-boardRow,boardCol));
                    determinePiece(out, piece);
                } else {
                    ChessPiece piece = board.getPiece(new ChessPosition(boardRow,9-boardCol));
                    determinePiece(out, piece);
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

    private void determinePiece(PrintStream out, ChessPiece piece) {
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
    }

    private void endRowOfSquares(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.println();
    }
}
