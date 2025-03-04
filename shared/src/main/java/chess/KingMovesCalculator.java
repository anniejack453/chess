package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {
    private ChessPiece piece;
    private ChessPosition newPosition;
    private ChessMove move;
    private ChessPiece myPiece;


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        myPiece = board.getPiece(myPosition);
        if (myPosition.getColumn()-1 >= 1){
            newPosition = new ChessPosition(myPosition.getRow(),myPosition.getColumn()-1);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getRow()-1 >= 1){
            newPosition = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn());
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getColumn()+1 <= 8){
            newPosition = new ChessPosition(myPosition.getRow(),myPosition.getColumn()+1);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getRow()+1 <= 8){
            newPosition = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn());
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getColumn()-1 >= 1 && myPosition.getRow()-1 >= 1){
            newPosition = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()-1);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getColumn()+1 <= 8 && myPosition.getRow()+1 <= 8){
            newPosition = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getColumn()-1 >= 1 && myPosition.getRow()+1 <= 8){
            newPosition = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-1);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getColumn()+1 <= 8 && myPosition.getRow()-1 >= 1){
            newPosition = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+1);
            addMoves(board, myPosition, moves);
        }
        return moves;
    }

    private void addMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        piece = board.getPiece(newPosition);
        if (piece == null) {
            move = new ChessMove(myPosition,newPosition,null);
            moves.add(move);
        } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
            move = new ChessMove(myPosition,newPosition,null);
            moves.add(move);
        }
    }
}
