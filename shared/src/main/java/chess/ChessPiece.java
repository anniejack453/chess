package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moveList = new HashSet<>();
    if (board.getPiece(myPosition).getPieceType() == PieceType.KING){
        KingMovesCalculator moves = new KingMovesCalculator();
        moveList = moves.pieceMoves(board, myPosition);
    } else if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN){
        BishopMovesCalculator bishopMoves = new BishopMovesCalculator();
        RookMovesCalculator rookMoves = new RookMovesCalculator();
        moveList = bishopMoves.pieceMoves(board, myPosition);
        moveList.addAll(rookMoves.pieceMoves(board, myPosition));
    } else if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP){
        BishopMovesCalculator moves = new BishopMovesCalculator();
        moveList = moves.pieceMoves(board, myPosition);
    } else if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT){
        KnightMovesCalculator moves = new KnightMovesCalculator();
        moveList = moves.pieceMoves(board, myPosition);
    } else if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK){
        RookMovesCalculator moves = new RookMovesCalculator();
        moveList = moves.pieceMoves(board, myPosition);
    } else if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN){
        PawnMovesCalculator moves = new PawnMovesCalculator();
        moveList = moves.pieceMoves(board, myPosition);
    }
    return moveList;
    }
}
