package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable{
    private TeamColor turnTeam;
    private ChessBoard currBoard;
    private ChessPiece mover;

    public ChessGame() {
        currBoard = new ChessBoard();
        currBoard.resetBoard();
        turnTeam = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turnTeam = team;

    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * Piece moves, but account for putting king in check as invalid
     *
     *
     * if king in check, only include moves that put king out of check
     * make move on duplicate board, call isInCheck again, if isInCheck is
     * true, don't include move, if it isn't in check, include
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard copyBoard = new ChessBoard();
        Collection<ChessMove> moves;
        Collection<ChessMove> middleMoves;
        ChessPosition kingPos = null;
        ChessPosition otherKingPos = null;
        Collection<ChessMove> finalMoves = new HashSet<>();
        ChessGame chess = new ChessGame();
        chess.setBoard(currBoard);
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i,j);
                copyBoard.addPiece(position,currBoard.getPiece(position));
                if (copyBoard.getPiece(position).getPieceType() == ChessPiece.PieceType.KING && chess.getTeamTurn() == turnTeam){
                    kingPos = position;
                }
                if (copyBoard.getPiece(position).getPieceType() == ChessPiece.PieceType.KING && chess.getTeamTurn() != turnTeam){
                    otherKingPos = position;
                }
            }
        }
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = new ChessPiece(copyBoard.getPiece(position).getTeamColor(),copyBoard.getPiece(position).getPieceType());
                if (piece.getPieceType() != null) {
                    middleMoves = piece.pieceMoves(copyBoard,position);
                    for (ChessMove move : middleMoves) {
                        if (move.getEndPosition() == otherKingPos){
                            if (piece.getTeamColor() == TeamColor.BLACK) {
                                isInCheck(TeamColor.WHITE);
                            } else {isInCheck(TeamColor.BLACK);}
                        }
                    }
                }

//                ChessPiece mover = new ChessPiece(turnTeam, copyBoard.getPiece(startPosition).getPieceType());
//                moves = mover.pieceMoves(copyBoard,position);

            }
        }

        return finalMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * If move is in valid moves, move
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard copyBoard = new ChessBoard();
        ChessPiece mover = new ChessPiece(turnTeam,currBoard.getPiece(move.getStartPosition()).getPieceType());
        ChessGame chess = new ChessGame();
        Collection<ChessMove> moves;
        chess.setBoard(currBoard);
        moves = chess.validMoves(move.getStartPosition());
        ChessPiece copyMover = new ChessPiece(turnTeam,copyBoard.getPiece(move.getStartPosition()).getPieceType());
        if (moves.contains(move)){
            currBoard.addPiece(move.getEndPosition(),mover);
            currBoard.addPiece(move.getStartPosition(),null);
        }
        if (mover.getTeamColor() == TeamColor.BLACK) {
            turnTeam = TeamColor.WHITE;
        } else {turnTeam = TeamColor.BLACK;};
    }

    /**
     * Determines if the given team is in check
     * Calculate valid moves for every piece on team, if one contains opposing
     * king's position, king is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> moves = new HashSet<>();
        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * no valid moves for current team, king in check
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * no valid moves for current team, king not in Check
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currBoard;
    }
}
