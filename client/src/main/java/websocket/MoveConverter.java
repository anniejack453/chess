package websocket;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;

import java.lang.*;

public class MoveConverter {
    String startPosition;
    String endPosition;
    String pieceType;

    public MoveConverter(String startPosition, String endPosition, String pieceType) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.pieceType = pieceType;
    }

    public ChessPosition convertStartPosition(String startPosition) throws ResponseException {
        try {
            int startCol = startPosition.charAt(0);
            int startRow = startPosition.charAt(1);
            var start =  new ChessPosition(startRow-'0', startCol-'`');
            if (start.getRow() < 1 || start.getRow() > 8 || start.getColumn() < 1 || start.getColumn() > 8) {
                throw new ResponseException(400, "Not a valid position\n");
            }
            return start;
        } catch (Exception e) {
            throw new ResponseException(400, "Not a valid position\n");
        }
    }

    public ChessMove convert(String startPosition, String endPosition, String pieceType) throws ResponseException {
        try {
            int startCol = startPosition.charAt(0);
            int startRow = startPosition.charAt(1);
            ChessPiece.PieceType promo = null;
            var start = new ChessPosition(startRow-'0', startCol-'`');
            if (start.getRow() < 1 || start.getRow() > 8 || start.getColumn() < 1 || start.getColumn() > 8) {
                throw new ResponseException(400, "Not a valid move\n");
            }
            int endCol = endPosition.charAt(0);
            int endRow = endPosition.charAt(1);
            var end = new ChessPosition(endRow-'0', endCol-'`');
            if (end.getRow() < 1 || end.getRow() > 8 || end.getColumn() < 1 || end.getColumn() > 8) {
                throw new ResponseException(400, "Not a valid move\n");
            }

            if (pieceType != null) {
                if (pieceType.toUpperCase() == "ROOK") {
                    promo = ChessPiece.PieceType.ROOK;
                } else if (pieceType.toUpperCase() == "KNIGHT") {
                    promo = ChessPiece.PieceType.KNIGHT;
                } else if (pieceType.toUpperCase() == "BISHOP") {
                    promo = ChessPiece.PieceType.BISHOP;
                } else if (pieceType.toUpperCase() == "QUEEN") {
                    promo = ChessPiece.PieceType.QUEEN;
                }
            }
            return new ChessMove(start, end, promo);
        } catch (Exception e) {
            throw new ResponseException(400, "Not a valid move\n");
        }
    }
}
