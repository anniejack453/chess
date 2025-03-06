package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame{
    private TeamColor turnTeam;
    private ChessBoard currBoard;
    private boolean isInCheck;
    private boolean isInStalemate;
    private boolean isInCheckmate;

    public ChessGame() {
        currBoard = new ChessBoard();
        currBoard.resetBoard();
        turnTeam = TeamColor.WHITE;
        isInCheck = false;
        isInStalemate = false;
        isInCheckmate = false;
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

    public boolean simulateMoves(ChessMove move) {
        ChessBoard copyBoard = currBoard;
        ChessPiece mover = new ChessPiece(currBoard.getPiece(move.getStartPosition()).getTeamColor(),currBoard.getPiece(move.getStartPosition()).getPieceType());
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i,j);
                copyBoard.addPiece(position,currBoard.getPiece(position));
            }
        }
        ChessPiece enemy = copyBoard.getPiece(move.getEndPosition());
        copyBoard.addPiece(move.getEndPosition(),mover);
        copyBoard.addPiece(move.getStartPosition(),null);
        if (isInCheck(currBoard.getPiece(move.getEndPosition()).getTeamColor())) {
            copyBoard.addPiece(move.getStartPosition(),mover);
            copyBoard.addPiece(move.getEndPosition(),enemy);
            return true;
        } else {
            copyBoard.addPiece(move.getStartPosition(),mover);
            copyBoard.addPiece(move.getEndPosition(),enemy);
            return false;
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     * Piece moves, but account for putting king in check as invalid
     * !Using start position, find a piece and all its possible moves, but has to
     * account for that move putting their king in check!
     * if king in check, only include moves that put king out of check
     * make move on duplicate board, call isInCheck again, if isInCheck is
     * true, don't include move, if it isn't in check, include
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> moves;
        Collection<ChessMove> filteredMoves = new HashSet<>();
        ChessPiece piece = currBoard.getPiece(startPosition);

        if (piece != null) {
            moves = piece.pieceMoves(currBoard,startPosition);
            Iterator<ChessMove> iterator = moves.iterator();
            while (iterator.hasNext()){
                ChessMove move = iterator.next();
                if (simulateMoves(move)){
                    iterator.remove();
                } else {
                    filteredMoves.add(move);
                }
            }
            return filteredMoves;
        } else {return null;}
    }

    /**
     * Makes a move in a chess game
     * If move is in valid moves, move
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece mover = currBoard.getPiece(move.getStartPosition());
        Collection<ChessMove> moves;
        moves = validMoves(move.getStartPosition());
        if (mover == null){
            throw new InvalidMoveException("Not a piece");
        }
        if (mover.getTeamColor() != turnTeam){
            throw new InvalidMoveException("Not your turn");
        }
        if (moves.contains(move)){
            if (mover.getPieceType() == ChessPiece.PieceType.PAWN){
                if (move.getPromotionPiece() != null){
                    ChessPiece promoPiece = new ChessPiece(mover.getTeamColor(),move.getPromotionPiece());
                    currBoard.addPiece(move.getEndPosition(),promoPiece);
                    currBoard.addPiece(move.getStartPosition(),null);
                } else {
                    currBoard.addPiece(move.getEndPosition(),mover);
                    currBoard.addPiece(move.getStartPosition(),null);
                }
            } else {
                currBoard.addPiece(move.getEndPosition(),mover);
                currBoard.addPiece(move.getStartPosition(),null);
            }
        } else {throw new InvalidMoveException("Invalid move");}
        if (mover.getTeamColor() == TeamColor.BLACK) {
            turnTeam = TeamColor.WHITE;
        } else {turnTeam = TeamColor.BLACK;}
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
        ChessPosition kingPos = findKingPos(null,teamColor);
        Collection<ChessMove> moves;
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece pieceAtPosition = currBoard.getPiece(position);
                if (pieceAtPosition != null && pieceAtPosition.getTeamColor() != teamColor && kingPos != null){
                    moves = pieceAtPosition.pieceMoves(currBoard,position);
                    if (determineCheckMoves(moves,kingPos)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     * no valid moves for current team, king in check
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> moves = new HashSet<>();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i,j);
                if (currBoard.getPiece(position) != null && currBoard.getPiece(position).getTeamColor() == teamColor){
                    moves = validMoves(position);
                    if (!moves.isEmpty()){
                        return false;
                    }
                }
            }
        }
        if (isInCheck(teamColor) && moves.isEmpty()){
            isInCheckmate = true;
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     * no valid moves for current team, king not in Check
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return stalemateMoves(teamColor);
        }
        return false;
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

    private ChessPosition findKingPos(ChessPosition kingPos, TeamColor teamColor){
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece pieceAtPosition = currBoard.getPiece(position);
                if (pieceAtPosition != null) {
                    if (pieceAtPosition.getPieceType() == ChessPiece.PieceType.KING && pieceAtPosition.getTeamColor() == teamColor){
                        kingPos = position;
                    }
                }
            }
        }
        return kingPos;
    }
    
    private boolean stalemateMoves(TeamColor teamColor){
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece pieceAtPosition = currBoard.getPiece(position);
                if (pieceAtPosition != null && pieceAtPosition.getTeamColor() == teamColor && !isInCheck(teamColor)){
                    Collection<ChessMove> moves = validMoves(position);
                    if (!moves.isEmpty()){
                        return false;
                    }
                }
            }
        }
        isInStalemate = true;
        return true;
    }

    private boolean determineCheckMoves(Collection<ChessMove> moves, ChessPosition kingPos){
        for (ChessMove move : moves) {
            if (move.getEndPosition().equals(kingPos)){
                isInCheck = true;
                return true;
            }
        }
        return false;
    }
}
