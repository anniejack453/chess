package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
        }
        if (myPosition.getRow()+2 <= 8 && myPosition.getColumn()+1 <= 8) {
            newPosition = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1);
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
        }
        if (myPosition.getRow()+2 <= 8 && myPosition.getColumn()-1 > 0) {
            newPosition = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1);
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
        }
        if (myPosition.getRow()+1 <= 8 && myPosition.getColumn()-2 > 0) {
            newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2);
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
        }
        if (myPosition.getRow()-2 > 0 && myPosition.getColumn()-1 > 0) {
            newPosition = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1);
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
        }
        if (myPosition.getRow()-1 > 0 && myPosition.getColumn()-2 > 0) {
            newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2);
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
        }
        if (myPosition.getRow()-2 > 0 && myPosition.getColumn()+1 <= 8) {
            newPosition = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1);
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
        }
        if (myPosition.getRow()-1 > 0 && myPosition.getColumn()+2 <= 8) {
            newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2);
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            } else if (piece.getTeamColor() != myPiece.getTeamColor()) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
        }
        return moves;
    }
}
