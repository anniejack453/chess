package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator{
    private ChessPiece piece;
    private ChessPosition newPosition;
    private ChessMove move;
    private ChessPiece myPiece;

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        myPiece = board.getPiece(myPosition);
        if (myPosition.getRow()+1 <= 8 && myPosition.getColumn()+2 <= 8) {
            newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getRow()+2 <= 8 && myPosition.getColumn()+1 <= 8) {
            newPosition = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getRow()+2 <= 8 && myPosition.getColumn()-1 > 0) {
            newPosition = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getRow()+1 <= 8 && myPosition.getColumn()-2 > 0) {
            newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getRow()-2 > 0 && myPosition.getColumn()-1 > 0) {
            newPosition = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getRow()-1 > 0 && myPosition.getColumn()-2 > 0) {
            newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getRow()-2 > 0 && myPosition.getColumn()+1 <= 8) {
            newPosition = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1);
            addMoves(board, myPosition, moves);
        }
        if (myPosition.getRow()-1 > 0 && myPosition.getColumn()+2 <= 8) {
            newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2);
            addMoves(board, myPosition, moves);
        }
        return moves;
    }

    private void addMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        piece = board.getPiece(newPosition);
        if (piece == null || piece.getTeamColor() != myPiece.getTeamColor()) {
            move = new ChessMove(myPosition, newPosition, null);
            moves.add(move);
        }
    }
}
