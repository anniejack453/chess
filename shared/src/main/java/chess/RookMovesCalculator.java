package chess;

import java.util.Collection;
import java.util.HashSet;

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
            if (addColMoves(board, myPosition, moves, i)) {
                break;
            }
        }
        for (int i = myPosition.getColumn()-1;i > 0; i--){
            if (addColMoves(board, myPosition, moves, i)) {
                break;
            }
        }
        for (int i = myPosition.getRow()+1;i <= 8; i++) {
            if (getRowMoves(board, myPosition, moves, i)) {
                break;
            }
        }
        for (int i = myPosition.getRow()-1;i > 0; i--) {
            if (getRowMoves(board, myPosition, moves, i)) {
                break;
            }
        }
        return moves;
    }

    private boolean getRowMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves, int i) {
        newPosition = new ChessPosition(i, myPosition.getColumn());
        piece = board.getPiece(newPosition);
        if (piece == null) {
            move = new ChessMove(myPosition, newPosition, null);
            moves.add(move);
        } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
            move = new ChessMove(myPosition, newPosition, null);
            moves.add(move);
            return true;
        } else {
            return true;
        }
        return false;
    }

    private boolean addColMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves, int i) {
        newPosition = new ChessPosition(myPosition.getRow(),i);
        piece = board.getPiece(newPosition);
        if (piece == null) {
            move = new ChessMove(myPosition,newPosition,null);
            moves.add(move);
        } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
            move = new ChessMove(myPosition,newPosition,null);
            moves.add(move);
            return true;
        } else {
            return true;
        }
        return false;
    }

}
