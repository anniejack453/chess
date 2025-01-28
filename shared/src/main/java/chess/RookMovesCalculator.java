package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class RookMovesCalculator implements PieceMovesCalculator {
    private ChessPiece piece;
    private ChessPosition newPosition;
    private ChessMove move;
    private ChessPiece myPiece;

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        myPiece = board.getPiece(myPosition);
        for (int i = myPosition.getColumn()+1;i <= 8; i++){
            newPosition = new ChessPosition(myPosition.getRow(),i);
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition,newPosition,null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition,newPosition,null);
                moves.add(move);
                break;
            } else {break;}
        }
        for (int i = myPosition.getColumn()-1;i > 0; i--){
            newPosition = new ChessPosition(myPosition.getRow(),i);
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition,newPosition,null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition,newPosition,null);
                moves.add(move);
                break;
            } else {break;}
        }
        for (int i = myPosition.getRow()+1;i <= 8; i++) {
            newPosition = new ChessPosition(i, myPosition.getColumn());
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
                break;
            } else {break;}
        }
        for (int i = myPosition.getRow()-1;i > 0; i--) {
            newPosition = new ChessPosition(i, myPosition.getColumn());
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
                break;
            } else {break;}
        }
        return moves;
    }

//    public internalMove(ChessBoard board, ChessPosition myPosition, int i) {
//        List<ChessMove> moves = new ArrayList<>();
//        boolean capture = false;
//        newPosition = new ChessPosition(myPosition.getRow(),i);
//        piece = board.getPiece(newPosition);
//        if (piece == null) {
//            move = new ChessMove(myPosition,newPosition,null);
//            moves.add(move);
//            break;
//        } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
//            move = new ChessMove(myPosition,newPosition,null);
//            moves.add(move);
//            capture = true;
//            break;
//        }
//        if (capture){
//            break;
//        }
//    }
}
