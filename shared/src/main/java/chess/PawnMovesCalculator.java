package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator{
    private ChessPiece piece;
    private ChessPosition newPosition;
    private ChessMove move;
    private ChessPiece myPiece;


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        myPiece = board.getPiece(myPosition);
        if (myPosition.getRow() == 7 && myPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
            piece = board.getPiece(newPosition);
            if (piece == null) {
                piece = board.getPiece(new ChessPosition(newPosition.getRow() + 1, myPosition.getColumn()));
                if (piece == null) {
                    move = new ChessMove(myPosition, newPosition, null);
                    moves.add(move);
                }
            }
            blackAddMovesInBounds(board, myPosition, moves);
        } else if (myPosition.getRow() == 2 && myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
            piece = board.getPiece(newPosition);
            if (piece == null) {
                piece = board.getPiece(new ChessPosition(newPosition.getRow() - 1, myPosition.getColumn()));
                if (piece == null) {
                    move = new ChessMove(myPosition, newPosition, null);
                    moves.add(move);
                }
            }
            whiteAddMovesInBounds(board, myPosition, moves);
        } else if (myPosition.getRow() == 7 && myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            piece = board.getPiece(newPosition);
            if (piece == null) {
                addPromotionMoves(myPosition,moves);
            }
            if (myPosition.getRow() == 7 && myPosition.getColumn() + 1 <= 8 && myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                piece = board.getPiece(newPosition);
                if (piece != null && piece.getTeamColor() != myPiece.getTeamColor()) {
                    addPromotionMoves(myPosition,moves);
                }
            }
            if (myPosition.getRow() == 7 && myPosition.getColumn() - 1 > 0 && myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                piece = board.getPiece(newPosition);
                if (piece != null && piece.getTeamColor() != myPiece.getTeamColor()) {
                    addPromotionMoves(myPosition,moves);
                }
            }
        } else if (myPosition.getRow() == 2 && myPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            piece = board.getPiece(newPosition);
            if (piece == null) {
                addPromotionMoves(myPosition,moves);
            }
            if (myPosition.getRow() == 2 && myPosition.getColumn() + 1 <= 8 && myPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                piece = board.getPiece(newPosition);
                if (piece != null && piece.getTeamColor() != myPiece.getTeamColor()) {
                    addPromotionMoves(myPosition,moves);
                }
            }
            if (myPosition.getRow() == 2 && myPosition.getColumn() - 1 > 0 && myPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                piece = board.getPiece(newPosition);
                if (piece != null && piece.getTeamColor() != myPiece.getTeamColor()) {
                    addPromotionMoves(myPosition,moves);
                }
            }
        } else if (2 <= myPosition.getRow() && myPosition.getRow() < 7 && myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            whiteAddMovesInBounds(board, myPosition, moves);
        } else if (2 < myPosition.getRow() && myPosition.getRow() <= 7 && myPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            piece = board.getPiece(newPosition);
            if (piece == null) {
                move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            blackAddMovesInBounds(board, myPosition, moves);
        }
        return moves;
    }

    private void whiteAddMovesInBounds(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        if (myPosition.getColumn() + 1 <= 8) {
            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            addMove(board,myPosition,moves);
        }
        if (myPosition.getColumn() - 1 > 0) {
            newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            addMove(board,myPosition,moves);
        }
    }

    private void blackAddMovesInBounds(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        if (myPosition.getColumn() + 1 <= 8) {
            newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            addMove(board,myPosition,moves);
        }
        if (myPosition.getColumn() - 1 > 0) {
            newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            addMove(board,myPosition,moves);
        }
    }

    private void addPromotionMoves(ChessPosition myPosition, HashSet<ChessMove> moves) {
        move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN);
        moves.add(move);
        move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT);
        moves.add(move);
        move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK);
        moves.add(move);
        move = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP);
        moves.add(move);
    }

    private void addMove(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        piece = board.getPiece(newPosition);
        if (piece != null && piece.getTeamColor() != myPiece.getTeamColor()) {
            move = new ChessMove(myPosition, newPosition, null);
            moves.add(move);
        }
    }
}
