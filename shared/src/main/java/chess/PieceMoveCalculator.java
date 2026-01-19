package chess;

import java.util.ArrayList;

public class PieceMoveCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece.PieceType pieceType;
    private final ChessGame.TeamColor teamColor;

    public PieceMoveCalculator(ChessBoard board, ChessPosition position, ChessPiece.PieceType pieceType, ChessGame.TeamColor teamColor){
        this.board = board;
        this.position = position;
        this.pieceType = pieceType;
        this.teamColor = teamColor;
    }

    public ArrayList<ChessMove> determineMoveCalculator(){
        if (pieceType == ChessPiece.PieceType.BISHOP){
            return bishopMoveCalculator(board, position);
        }
        else if (pieceType == ChessPiece.PieceType.ROOK){
            return rookMoveCalculator(board, position);
        }
        else if (pieceType == ChessPiece.PieceType.QUEEN){
            return queenMoveCalculator(board, position);
        }
        else if (pieceType == ChessPiece.PieceType.KING){
            return kingMoveCalculator(board, position);
        }
        else if (pieceType == ChessPiece.PieceType.KNIGHT){
            return knightMoveCalculator(board, position);
        }
        else{
            return pawnMoveCalculator(board, position);
        }
    }

    public boolean spaceOccupiedMyPiece(ChessBoard board, ChessPosition position){
        if (board.getPiece(position) == null){
            return false;
        }
        return board.getPiece(position).getTeamColor() == teamColor;
    }

    public boolean spaceOccupiedOpponent(ChessBoard board, ChessPosition position){
        if (board.getPiece(position) == null){
            return false;
        }
        return board.getPiece(position).getTeamColor() != teamColor;
    }

    public ArrayList<ChessMove> moveDiagonal(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //get valid moves to the right-up diagonal
        for (int i = 1; i < (9 - position.getRow()); i ++){
            ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() + i);
            if (position.getColumn() + i < 9){
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                    if (spaceOccupiedOpponent(board, endPosition)) {
                        break;
                    }
                }
                else if (spaceOccupiedMyPiece(board, endPosition)){
                    break;
                }
            }
        }
        //get valid moves to the left-up diagonal
        for (int i = 1; i < position.getRow(); i++){
            ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() + i);
            if (position.getColumn() + i < 9) {
                if (!spaceOccupiedMyPiece(board, endPosition)) {
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                    if (spaceOccupiedOpponent(board, endPosition)) {
                        break;
                    }
                }
                else if (spaceOccupiedMyPiece(board, endPosition)){
                    break;
                }
            }
        }
        //get valid moves to the right-down diagonal
        for (int i = 1; i < (9 - position.getRow()); i ++){
            ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() - i);
            if (position.getColumn() - i > 0) {
                if (!spaceOccupiedMyPiece(board, endPosition)) {
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                    if (spaceOccupiedOpponent(board, endPosition)) {
                        break;
                    }
                }
                else if (spaceOccupiedMyPiece(board, endPosition)){
                    break;
                }
            }
        }
        //get valid moves to the left-down diagonal
        for (int i = 1; i < position.getRow(); i++){
            ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() - i);
            if (position.getColumn() - i > 0) {
                if (!spaceOccupiedMyPiece(board, endPosition)) {
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                    if (spaceOccupiedOpponent(board, endPosition)) {
                        break;
                    }
                }
                else if (spaceOccupiedMyPiece(board, endPosition)){
                    break;
                }
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> moveHorizontal(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //get valid moves to the left
        for (int i = 1; i < position.getColumn(); i ++){
            ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() - i);
            if (!spaceOccupiedMyPiece(board, endPosition)){
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
                if (spaceOccupiedOpponent(board, endPosition)) {
                    break;
                }
            }
            else if (spaceOccupiedMyPiece(board, endPosition)){
                break;
            }
        }
        //get valid moves to the right
        for (int i = 1; i < (9 - position.getColumn()); i++){
            ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
            if (!spaceOccupiedMyPiece(board, endPosition)) {
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
                if (spaceOccupiedOpponent(board, endPosition)) {
                    break;
                }
            } else if (spaceOccupiedMyPiece(board, endPosition)) {
                break;
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> moveVertical(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //get valid moves down
        for (int i = 1; i < position.getRow(); i ++){
            ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn());
            if (!spaceOccupiedMyPiece(board, endPosition)){
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
                if (spaceOccupiedOpponent(board, endPosition)) {
                    break;
                }
            }
            else if (spaceOccupiedMyPiece(board, endPosition)){
                break;
            }
        }
        //get valid moves up
        for (int i = 1; i < (9 - position.getRow()); i++){
            ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn());
            if (!spaceOccupiedMyPiece(board, endPosition)) {
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
                if (spaceOccupiedOpponent(board, endPosition)) {
                    break;
                }
            } else if (spaceOccupiedMyPiece(board, endPosition)) {
                break;
            }
        }
        return moves;
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

    public ArrayList<ChessMove> kingMoveCalculator(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition endPosition;
        // move up
        if (row + 1 < 9){
            endPosition = new ChessPosition(row + 1, col);
            if (!spaceOccupiedMyPiece(board, endPosition)){
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
            }
            if (col - 1 > 0){
                endPosition = new ChessPosition(row + 1, col - 1);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
            if (col + 1 < 9){
                endPosition = new ChessPosition(row + 1, col + 1);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
        }
        // move down
        if (row - 1 > 0){
            endPosition = new ChessPosition(row - 1, col);
            if (!spaceOccupiedMyPiece(board, endPosition)){
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
            }
            if (col - 1 > 0){
                endPosition = new ChessPosition(row - 1, col - 1);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
            if (col + 1 < 9){
                endPosition = new ChessPosition(row - 1, col + 1);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
        }
        //move right
        if (col + 1 < 9){
            endPosition = new ChessPosition(row, col + 1);
            if (!spaceOccupiedMyPiece(board, endPosition)){
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
            }
        }
        //move left
        if (col - 1 > 0){
            endPosition = new ChessPosition(row, col - 1);
            if (!spaceOccupiedMyPiece(board, endPosition)){
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> knightMoveCalculator(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row + 2 < 9){
            if (col + 1 < 9){
                ChessPosition endPosition = new ChessPosition(row + 2, col + 1);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
            if (col - 1 > 0){
                ChessPosition endPosition = new ChessPosition(row + 2, col - 1);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
        }
        if (row - 2 > 0){
            if (col + 1 < 9){
                ChessPosition endPosition = new ChessPosition(row - 2, col + 1);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
            if (col - 1 > 0){
                ChessPosition endPosition = new ChessPosition(row - 2, col - 1);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
        }
        if (row + 1 < 9){
            if (col + 2 < 9){
                ChessPosition endPosition = new ChessPosition(row + 1, col + 2);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
            if (col - 2 > 0){
                ChessPosition endPosition = new ChessPosition(row + 1, col - 2);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
        }
        if (row - 1 > 0){
            if (col + 2 < 9){
                ChessPosition endPosition = new ChessPosition(row - 1, col + 2);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
            if (col - 2 > 0){
                ChessPosition endPosition = new ChessPosition(row - 1, col - 2);
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> pawnMoveCalculator(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece.PieceType[] promotionPieceTypes = {ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};
        if (teamColor == ChessGame.TeamColor.WHITE){
            for(int i: new int[]{-1,1}) {
                ChessPosition endPosition = new ChessPosition(row + 1, col + i);
                if (1 <= col + i && col + i <= 8 && 1 <= row + 1 && row + 1 <= 8) {
                    if (spaceOccupiedOpponent(board, endPosition)) {
                        if (row + 1 == 8) {
                            for (ChessPiece.PieceType promotionPiece : promotionPieceTypes) {
                                ChessMove move = new ChessMove(position, endPosition, promotionPiece);
                                moves.add(move);
                            }
                        } else {
                            ChessMove move = new ChessMove(position, endPosition, null);
                            moves.add(move);
                        }
                    }
                }
            }
            if (row == 2){
                for (int i = 1; i < 3; i ++){
                    ChessPosition endPosition = new ChessPosition(row + i, col);
                    if (!spaceOccupiedMyPiece(board, endPosition) && !spaceOccupiedOpponent(board, endPosition)){
                        ChessMove move = new ChessMove(position, endPosition, null);
                        moves.add(move);
                    }
                    else if (spaceOccupiedMyPiece(board, endPosition) || spaceOccupiedOpponent(board, endPosition)){
                        break;
                    }
                }
            }
            else{
                ChessPosition endPosition = new ChessPosition(row + 1, col);
                if (!spaceOccupiedMyPiece(board, endPosition) && !spaceOccupiedOpponent(board, endPosition)) {
                    if (row + 1 == 8){
                        for (ChessPiece.PieceType promotionPiece: promotionPieceTypes){
                            ChessMove move = new ChessMove(position, endPosition, promotionPiece);
                            moves.add(move);
                        }
                    }
                    else {
                        ChessMove move = new ChessMove(position, endPosition, null);
                        moves.add(move);
                    }
                }
            }

        }
        if (teamColor == ChessGame.TeamColor.BLACK){
            for(int i: new int[]{-1,1}) {
                ChessPosition endPosition = new ChessPosition(row - 1, col + i);
                if (1 <= col + i && col + i <= 8 && 1 <= row - 1 && row - 1 <= 8) {
                    if (spaceOccupiedOpponent(board, endPosition)) {
                        if (row - 1 == 1) {
                            for (ChessPiece.PieceType promotionPiece : promotionPieceTypes) {
                                ChessMove move = new ChessMove(position, endPosition, promotionPiece);
                                moves.add(move);
                            }
                        } else {
                            ChessMove move = new ChessMove(position, endPosition, null);
                            moves.add(move);
                        }
                    }
                }
            }
            if (row == 7){
                for (int i = 1; i < 3; i ++){
                    ChessPosition endPosition = new ChessPosition(row - i, col);
                    if (!spaceOccupiedMyPiece(board, endPosition) && !spaceOccupiedOpponent(board, endPosition)){
                        ChessMove move = new ChessMove(position, endPosition, null);
                        moves.add(move);
                    }
                    else if (spaceOccupiedMyPiece(board, endPosition) || spaceOccupiedOpponent(board, endPosition)){
                        break;
                    }
                }
            }
            else {
                ChessPosition endPosition = new ChessPosition(row - 1, col);
                if (!spaceOccupiedMyPiece(board, endPosition) && !spaceOccupiedOpponent(board, endPosition)) {
                    if (row - 1 == 1){
                        for (ChessPiece.PieceType promotionPiece: promotionPieceTypes){
                            ChessMove move = new ChessMove(position, endPosition, promotionPiece);
                            moves.add(move);
                        }
                    }
                    else {
                        ChessMove move = new ChessMove(position, endPosition, null);
                        moves.add(move);
                    }
                }
            }
        }
        return moves;
    }
}
