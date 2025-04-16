package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

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

    public String getEndPosition() {
        return endPosition;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public ChessPosition convertStartPosition(String startPosition) {
        int startCol = startPosition.charAt(0);
        int startRow = startPosition.charAt(1);
        return new ChessPosition(startRow-'0', startCol-'`');
    }

    public ChessMove convert(String startPosition, String endPosition, String pieceType) {
        int startCol = startPosition.charAt(0);
        int startRow = startPosition.charAt(1);
        ChessPiece.PieceType promo = null;
        var start = new ChessPosition(startRow-'0', startCol-'`');

        int endCol = endPosition.charAt(0);
        int endRow = endPosition.charAt(1);
        var end = new ChessPosition(endRow-'0', endCol-'`');

        if (pieceType.toUpperCase() == "ROOK") {
            promo = ChessPiece.PieceType.ROOK;
        } else if (pieceType.toUpperCase() == "KNIGHT") {
            promo = ChessPiece.PieceType.KNIGHT;
        } else if (pieceType.toUpperCase() == "BISHOP") {
            promo = ChessPiece.PieceType.BISHOP;
        } else if (pieceType.toUpperCase() == "QUEEN") {
            promo = ChessPiece.PieceType.QUEEN;
        }
        return new ChessMove(start, end, promo);
    }
}
