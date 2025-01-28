package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator{
    private ChessPiece piece;
    private ChessPosition newPosition;
    private ChessMove move;
    private ChessPiece myPiece;

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        myPiece = board.getPiece(myPosition);
        for (int i = myPosition.getColumn()+1, j = myPosition.getRow()+1; i<=8 && j<=8; i++, j++){
            newPosition = new ChessPosition(j,i);
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
        for (int i = myPosition.getColumn()-1, j = myPosition.getRow()-1; i>0 && j>0; i--, j--){
            newPosition = new ChessPosition(j,i);
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
        for (int i = myPosition.getColumn()+1, j = myPosition.getRow()-1; i<=8 && j>0; i++, j--){
            newPosition = new ChessPosition(j,i);
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
        for (int i = myPosition.getColumn()-1, j = myPosition.getRow()+1; i>0 && j<=8; i--, j++){
            newPosition = new ChessPosition(j,i);
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
        return moves;
    }
}
