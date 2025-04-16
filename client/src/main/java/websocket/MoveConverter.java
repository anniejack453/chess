package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.lang.*;

public class MoveConverter {
    String startPosition;
    String endPosition;
    //ChessGame chess;

    public MoveConverter(String startPosition, String endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        //this.chess = chess;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public ChessMove convert(String startPosition, String endPosition) {
        int startCol = startPosition.charAt(0);
        int startRow = startPosition.charAt(1);
        var start = new ChessPosition(startRow-'0', startCol-'`');

        int endCol = endPosition.charAt(0);
        int endRow = endPosition.charAt(1);
        var end = new ChessPosition(endRow-'0', endCol-'`');

//        var chessPiece = chess.getBoard().getPiece(start).getPieceType();
//        if (chessPiece == ChessPiece.PieceType.PAWN) {
//            return new ChessMove(start, end, )
//        }
        return new ChessMove(start, end, null);


    }
}
