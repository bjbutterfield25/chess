package chess;

import java.util.ArrayList;

public class PieceMoveCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece.PieceType pieceType;

    public PieceMoveCalculator(ChessBoard board, ChessPosition position, ChessPiece.PieceType pieceType){
        this.board = board;
        this.position = position;
        this.pieceType = pieceType;
    }

    public ArrayList<ChessMove> determineMoveCalculator(){
        if (pieceType == ChessPiece.PieceType.BISHOP){
            return bishopMoveCalculator(board, position);
        }
        throw new RuntimeException("Not implemented");
    }

    public ArrayList<ChessMove> moveDiagonal(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //get valid moves to the right-up diagonal
        for (int i = 1; i < (9 - position.getRow()); i ++){
            if (position.getColumn() + i < 9){
                ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() + i);
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
            }
        }
        //get valid moves to the left-up diagonal
        for (int i = 1; i < position.getRow(); i++){
            if (position.getColumn() + i < 9){
                ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() + i);
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
            }
        }
        //get valid moves to the right-down diagonal
        for (int i = 1; i < (9 - position.getRow()); i ++){
            if (position.getColumn() - i > 0){
                ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() - i);
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
            }
        }
        //get valid moves to the left-down diagonal
        for (int i = 1; i < position.getRow(); i++){
            if (position.getColumn() - i > 0){
                ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() - i);
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> bishopMoveCalculator(ChessBoard board, ChessPosition position){
        return moveDiagonal(board, position);
    }
}
