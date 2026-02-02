package chess;

import java.util.ArrayList;

public class PieceMoveCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece.PieceType type;
    private final ChessGame.TeamColor color;

    public PieceMoveCalculator(ChessBoard board, ChessPosition position, ChessPiece.PieceType type, ChessGame.TeamColor color){
        this.board = board;
        this.position = position;
        this.type = type;
        this.color = color;
    }

    public ArrayList<ChessMove> determineMoveCalculator(){
        if (type == ChessPiece.PieceType.BISHOP){
            return bishopMoveCalculator(board, position);
        }
        else if (type == ChessPiece.PieceType.ROOK){
            return rookMoveCalculator(board, position);
        }
        else if (type == ChessPiece.PieceType.QUEEN){
            return queenMoveCalculator(board, position);
        }
        else if (type == ChessPiece.PieceType.KNIGHT){
            return knightMoveCalculator(board, position);
        }
        else if (type == ChessPiece.PieceType.KING){
            return kingMoveCalculator(board, position);
        }
        return pawnMoveCalculator(board, position);
    }

    public ArrayList<ChessMove> moveDiagonal(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //move up-right
        for(int i = 1; position.getRow() + i < 9; i++){
            if (position.getColumn() + i < 9){
                ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() + i);
                if(spaceOccupiedOpponent(board, endPosition)){
                    moves.add(new ChessMove(position, endPosition, null));
                    break;
                }
                if(!spaceOccupiedMyPiece(board, endPosition)){
                    moves.add(new ChessMove(position, endPosition, null));
                }
                else{
                    break;
                }
            }
        }
        //move up-left
        for(int i = 1; position.getRow() + i < 9; i++){
            if (position.getColumn() - i > 0){
                ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() - i);
                if(spaceOccupiedOpponent(board, endPosition)){
                    moves.add(new ChessMove(position, endPosition, null));
                    break;
                }
                if(!spaceOccupiedMyPiece(board, endPosition)){
                    moves.add(new ChessMove(position, endPosition, null));
                }
                else{
                    break;
                }
            }
        }
        // move down-right
        for(int i = 1; position.getRow() - i > 0; i++){
            if (position.getColumn() + i < 9){
                ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() + i);
                if(spaceOccupiedOpponent(board, endPosition)){
                    moves.add(new ChessMove(position, endPosition, null));
                    break;
                }
                if(!spaceOccupiedMyPiece(board, endPosition)){
                    moves.add(new ChessMove(position, endPosition, null));
                }
                else{
                    break;
                }
            }
        }
        //move down-left
        for(int i = 1; position.getRow() - i > 0; i++){
            if (position.getColumn() - i > 0){
                ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() - i);
                if(spaceOccupiedOpponent(board, endPosition)){
                    moves.add(new ChessMove(position, endPosition, null));
                    break;
                }
                if(!spaceOccupiedMyPiece(board, endPosition)){
                    moves.add(new ChessMove(position, endPosition, null));
                }
                else{
                    break;
                }
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> moveHorizontal(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //move right
        for (int i = 1; position.getColumn() + i < 9; i++){
            ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
            if(spaceOccupiedOpponent(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
                break;
            }
            if(!spaceOccupiedMyPiece(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
            }
            else {
                break;
            }
        }
        //move left
        for (int i = 1; position.getColumn() - i > 0; i++){
            ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() +- i);
            if(spaceOccupiedOpponent(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
                break;
            }
            if(!spaceOccupiedMyPiece(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
            }
            else {
                break;
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> moveVertical(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //move up
        for (int i = 1; position.getRow() + i < 9; i++){
            ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn());
            if(spaceOccupiedOpponent(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
                break;
            }
            if(!spaceOccupiedMyPiece(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
            }
            else {
                break;
            }
        }
        //move down
        for (int i = 1; position.getRow() - i > 0; i++){
            ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn());
            if(spaceOccupiedOpponent(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
                break;
            }
            if(!spaceOccupiedMyPiece(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
            }
            else {
                break;
            }
        }
        return moves;
    }

    public boolean spaceOccupiedMyPiece(ChessBoard board, ChessPosition position){
        if (board.getPiece(position) == null){
            return false;
        }
        return color == board.getPiece(position).getTeamColor();
    }

    public boolean spaceOccupiedOpponent(ChessBoard board, ChessPosition position){
        if (board.getPiece(position) == null){
            return false;
        }
        return color != board.getPiece(position).getTeamColor();
    }

    public ArrayList<ChessMove> bishopMoveCalculator(ChessBoard board, ChessPosition position){
        return moveDiagonal(board, position);
    }

    public ArrayList<ChessMove> rookMoveCalculator(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = moveHorizontal(board, position);
        moves.addAll(moveVertical(board, position));
        return moves;
    }

    public ArrayList<ChessMove> queenMoveCalculator(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = moveHorizontal(board, position);
        moves.addAll(moveVertical(board, position));
        moves.addAll(moveDiagonal(board, position));
        return moves;
    }

    public ArrayList<ChessMove> knightMoveCalculator(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        for(int i: new int[]{-1,1}){
            if(position.getColumn() + i < 9 && position.getColumn() + i > 0){
                if(position.getRow() + 2 < 9){
                    ChessPosition endPosition = new ChessPosition(position.getRow() + 2, position.getColumn() + i);
                    if(!spaceOccupiedMyPiece(board, endPosition)){
                        moves.add(new ChessMove(position, endPosition, null));
                    }
                }
                if(position.getRow() - 2 > 0){
                    ChessPosition endPosition = new ChessPosition(position.getRow() - 2, position.getColumn() + i);
                    if(!spaceOccupiedMyPiece(board, endPosition)){
                        moves.add(new ChessMove(position, endPosition, null));
                    }
                }
            }
        }
        for(int i: new int[]{-1,1}){
            if(position.getRow() + i < 9 && position.getRow() + i > 0){
                if(position.getColumn() + 2 < 9){
                    ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() + 2);
                    if(!spaceOccupiedMyPiece(board, endPosition)){
                        moves.add(new ChessMove(position, endPosition, null));
                    }
                }
                if(position.getColumn() - 2 > 0){
                    ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() - 2);
                    if(!spaceOccupiedMyPiece(board, endPosition)){
                        moves.add(new ChessMove(position, endPosition, null));
                    }
                }
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> kingMoveCalculator(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        for (int i: new int[]{-1,0,1}){
            if(position.getRow() + 1 < 9){
                if(position.getColumn() + i < 9 && position.getColumn() + i > 0){
                    ChessPosition endPosition = new ChessPosition(position.getRow() + 1, position.getColumn() + i);
                    if(!spaceOccupiedMyPiece(board, endPosition)){
                        moves.add(new ChessMove(position, endPosition, null));
                    }
                }
            }
        }
        for (int i: new int[]{-1,0,1}){
            if(position.getRow() - 1 > 0){
                if(position.getColumn() + i < 9 && position.getColumn() + i > 0){
                    ChessPosition endPosition = new ChessPosition(position.getRow() - 1, position.getColumn() + i);
                    if(!spaceOccupiedMyPiece(board, endPosition)){
                        moves.add(new ChessMove(position, endPosition, null));
                    }
                }
            }
        }
        if(position.getColumn() + 1 < 9){
            ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() + 1);
            if(!spaceOccupiedMyPiece(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
            }
        }
        if(position.getColumn() - 1 > 0){
            ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() - 1);
            if(!spaceOccupiedMyPiece(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> pawnMoveCalculator(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece.PieceType[] promotionPieceType = {
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP
        };
        int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow  = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promoRow  = (color == ChessGame.TeamColor.WHITE) ? 8 : 1;
        for(int i: new int[]{-1,1}){
            if(position.getColumn() + i > 8 || position.getColumn() + i < 1) {
                continue;
            }
            ChessPosition endPosition = new ChessPosition(position.getRow() + direction, position.getColumn() + i);
            if(position.getRow() + direction == promoRow){
                for(ChessPiece.PieceType promotionPiece: promotionPieceType){
                    if(spaceOccupiedOpponent(board, endPosition)){
                        moves.add(new ChessMove(position, endPosition, promotionPiece));
                    }
                }
            }
            else if(spaceOccupiedOpponent(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
            }
        }
        if(position.getRow() == startRow){
            for(int i = 1; i < 3; i++){
                ChessPosition endPosition = new ChessPosition(position.getRow() + i * direction, position.getColumn());
                if(!spaceOccupiedOpponent(board, endPosition) && !spaceOccupiedMyPiece(board, endPosition)){
                    moves.add(new ChessMove(position, endPosition, null));
                }
                else{
                    break;
                }
            }
        }
        else if (position.getRow() + direction == promoRow){
            ChessPosition endPosition = new ChessPosition(position.getRow() + direction, position.getColumn());
            if(!spaceOccupiedOpponent(board, endPosition) && !spaceOccupiedMyPiece(board, endPosition)){
                for(ChessPiece.PieceType promotionPiece: promotionPieceType){
                    moves.add(new ChessMove(position, endPosition, promotionPiece));
                }
            }
        }
        else{
            ChessPosition endPosition = new ChessPosition(position.getRow() + direction, position.getColumn());
            if(!spaceOccupiedOpponent(board, endPosition) && !spaceOccupiedMyPiece(board, endPosition)){
                moves.add(new ChessMove(position, endPosition, null));
            }
        }
        return moves;
    }
}
