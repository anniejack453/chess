package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {
    private ChessPiece piece;
    private ChessPosition newPosition;
    private ChessMove move;
    private ChessPiece myPiece;


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        myPiece = board.getPiece(myPosition);
        if (myPosition.getColumn()-1 >= 1 && myPosition.getRow()-1 >= 1 && myPosition.getColumn()+1 <= 8 && myPosition.getRow()+1 <= 8) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i==0 && j==0) {
                        continue;
                    }
                    newPosition = new ChessPosition(myPosition.getColumn()+i,myPosition.getRow()+j);
                    piece = board.getPiece(newPosition);
                    if (piece == null) {
                        move = new ChessMove(myPosition,newPosition,myPiece.getPieceType());
                        moves.add(move);
                    }
                }
            }
        }
        return moves;
    }
}
