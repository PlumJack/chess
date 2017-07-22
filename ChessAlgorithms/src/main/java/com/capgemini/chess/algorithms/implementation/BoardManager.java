package com.capgemini.chess.algorithms.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.Path;
import com.capgemini.chess.algorithms.data.PieceCoordinate;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.data.pieces.Bishop;
import com.capgemini.chess.algorithms.data.pieces.King;
import com.capgemini.chess.algorithms.data.pieces.Knight;
import com.capgemini.chess.algorithms.data.pieces.Pawn;
import com.capgemini.chess.algorithms.data.pieces.Piece;
import com.capgemini.chess.algorithms.data.pieces.Queen;
import com.capgemini.chess.algorithms.data.pieces.Rook;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

/**
 * Class for managing of basic operations on the Chess Board.
 *
 * @author Michal Bejm
 *
 */
public class BoardManager {

	private Board board = new Board();

	public BoardManager() {
		initBoard();
	}

	public BoardManager(List<Move> moves) {
		initBoard();
		for (Move move : moves) {
			addMove(move);
		}
	}

	public BoardManager(Board board) {
		this.board = board;
	}

	/**
	 * Getter for generated board
	 *
	 * @return board
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * Performs move of the chess piece on the chess board from one field to
	 * another.
	 *
	 * @param from
	 *            coordinates of 'from' field
	 * @param to
	 *            coordinates of 'to' field
	 * @return move object which includes moved piece and move type
	 * @throws InvalidMoveException
	 *             in case move is not valid
	 */
	public Move performMove(Coordinate from, Coordinate to) throws InvalidMoveException {

		Move move = validateMove(from, to);

		addMove(move);

		return move;
	}

	/**
	 * Calculates state of the chess board.
	 *
	 * @return state of the chess board
	 */
	public BoardState updateBoardState() {

		Color nextMoveColor = calculateNextMoveColor();

		boolean isKingInCheck = isKingInCheck(nextMoveColor);
		boolean isAnyMoveValid = isAnyMoveValid(nextMoveColor);

		BoardState boardState;
		if (isKingInCheck) {
			if (isAnyMoveValid) {
				boardState = BoardState.CHECK;
			} else {
				boardState = BoardState.CHECK_MATE;
			}
		} else {
			if (isAnyMoveValid) {
				boardState = BoardState.REGULAR;
			} else {
				boardState = BoardState.STALE_MATE;
			}
		}
		this.board.setState(boardState);
		return boardState;
	}

	/**
	 * Checks threefold repetition rule (one of the conditions to end the chess
	 * game with a draw).
	 *
	 * @return true if current state repeated at list two times, false otherwise
	 */
	public boolean checkThreefoldRepetitionRule() {

		int lastNonAttackMoveIndex = findLastNonAttackMoveIndex();
		List<Move> omittedMoves = this.board.getMoveHistory().subList(0, lastNonAttackMoveIndex);
		BoardManager simulatedBoardManager = new BoardManager(omittedMoves);

		int counter = 0;
		for (int i = lastNonAttackMoveIndex; i < this.board.getMoveHistory().size(); i++) {
			Move moveToAdd = this.board.getMoveHistory().get(i);
			simulatedBoardManager.addMove(moveToAdd);
			boolean areBoardsEqual = Arrays.deepEquals(this.board.getPieces(),
					simulatedBoardManager.getBoard().getPieces());
			if (areBoardsEqual) {
				counter++;
			}
		}

		return counter >= 2;
	}

	/**
	 * Checks 50-move rule (one of the conditions to end the chess game with a
	 * draw).
	 *
	 * @return true if no pawn was moved or not capture was performed during
	 *         last 50 moves, false otherwise
	 */
	public boolean checkFiftyMoveRule() {

		if (this.board.getMoveHistory().size() < 100) {
			return false;
		}

		for (int i = this.board.getMoveHistory().size() - 1; i >= this.board.getMoveHistory().size() - 100; i--) {
			Move currentMove = this.board.getMoveHistory().get(i);
			PieceType currentPieceType = currentMove.getMovedPiece().getType();
			if (currentMove.getType() != MoveType.MOVEMENT || currentPieceType == PieceType.PAWN) {
				return false;
			}
		}

		return true;
	}

	private void initBoard() {

		this.board.setPieceAt(new Rook(Color.BLACK), new Coordinate(0, 7));
		this.board.setPieceAt(new Knight(Color.BLACK), new Coordinate(1, 7));
		this.board.setPieceAt(new Bishop(Color.BLACK), new Coordinate(2, 7));
		this.board.setPieceAt(new Queen(Color.BLACK), new Coordinate(3, 7));
		this.board.setPieceAt(new King(Color.BLACK), new Coordinate(4, 7));
		this.board.setPieceAt(new Bishop(Color.BLACK), new Coordinate(5, 7));
		this.board.setPieceAt(new Knight(Color.BLACK), new Coordinate(6, 7));
		this.board.setPieceAt(new Rook(Color.BLACK), new Coordinate(7, 7));

		for (int x = 0; x < Board.SIZE; x++) {
			this.board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(x, 6));
		}

		this.board.setPieceAt(new Rook(Color.WHITE), new Coordinate(0, 0));
		this.board.setPieceAt(new Knight(Color.WHITE), new Coordinate(1, 0));
		this.board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(2, 0));
		this.board.setPieceAt(new Queen(Color.WHITE), new Coordinate(3, 0));
		this.board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		this.board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(5, 0));
		this.board.setPieceAt(new Knight(Color.WHITE), new Coordinate(6, 0));
		this.board.setPieceAt(new Rook(Color.WHITE), new Coordinate(7, 0));

		for (int x = 0; x < Board.SIZE; x++) {
			this.board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(x, 1));
		}
	}

	private void addMove(Move move) {

		addRegularMove(move);

		if (move.getType() == MoveType.CASTLING) {
			addCastling(move);
		} else if (move.getType() == MoveType.EN_PASSANT) {
			addEnPassant(move);
		}
		
		if(move.getMovedPiece()!=null){
			move.getMovedPiece().setMovedThisGame(true);
		}
		
		this.board.getMoveHistory().add(move);
	}

	private void addRegularMove(Move move) {
		Piece movedPiece = this.board.getPieceAt(move.getFrom());
		movedPiece.setMovedThisGame(true);		
		this.board.setPieceAt(null, move.getFrom());
		this.board.setPieceAt(movedPiece, move.getTo());

		performPromotion(move, movedPiece);
	}

	private void performPromotion(Move move, Piece movedPiece) {
		if (movedPiece.getType() == PieceType.PAWN && movedPiece.getColor() == Color.WHITE && 
			move.getTo().getY() == (Board.SIZE - 1)) {
			this.board.setPieceAt(new Queen(Color.WHITE), move.getTo());
		}
		if (movedPiece.getType() == PieceType.PAWN && movedPiece.getColor() == Color.BLACK && 
				move.getTo().getY() == 0) {
			this.board.setPieceAt(new Queen(Color.BLACK), move.getTo());
		}
	}

	private void addCastling(Move move) {
		Piece rook;
		if (move.getFrom().getX() > move.getTo().getX()) {
			rook = this.board.getPieceAt(new Coordinate(0, move.getFrom().getY()));
			this.board.setPieceAt(null, new Coordinate(0, move.getFrom().getY()));
			this.board.setPieceAt(rook, new Coordinate(move.getTo().getX() + 1, move.getTo().getY()));
		} else {
			rook = this.board.getPieceAt(new Coordinate(Board.SIZE - 1, move.getFrom().getY()));
			this.board.setPieceAt(null, new Coordinate(Board.SIZE - 1, move.getFrom().getY()));
			this.board.setPieceAt(rook, new Coordinate(move.getTo().getX() - 1, move.getTo().getY()));
		}
		rook.setMovedThisGame(true);
	}

	private void addEnPassant(Move move) {
		Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
		this.board.setPieceAt(null, lastMove.getTo());
	}

	private Move validateMove(Coordinate from, Coordinate to) throws InvalidMoveException, KingInCheckException {

		initialPieceValidation(from,to);	
		Piece fromCoordinateTo = board.getPieceAt(to);
		
		if(fromCoordinateTo==null){			
			return checkForMovementMove(from, to);		
		} else {		
			if(validateCapture(from,to)){
				Move initialMove = new Move(from,to,MoveType.CAPTURE,board.getPieceAt(from));
				return checkForSelfCheck(initialMove);
			}			
		}
		
		throw new InvalidMoveException();
	}

	private boolean isKingInCheck(Color kingColor) {
		
		Coordinate to = getKingCoordinate(kingColor);
		if(to == null){
			return false;
		}
		
		return isFieldUnderAttack(to, getEnemyColor(kingColor));		
	}

	private boolean isAnyMoveValid(Color nextMoveColor) {
		
		ArrayList<PieceCoordinate> piecesWithCoordinates = getAllPiecesOfColor(nextMoveColor);
		
		for (int i = 0; i < piecesWithCoordinates.size(); i++) {
			
			for(int x=0; x<Board.SIZE; x++){
				for(int y=0; y<Board.SIZE; y++){
					try {
						validateMove(piecesWithCoordinates.get(i).getPosition(), new Coordinate(x,y));
						return true;
					} catch (KingInCheckException e) {
						continue;
					} catch (InvalidMoveException e) {
						continue;
					}				
				}
			}			
		}
		return false;
	}

	private Color calculateNextMoveColor() {
		if (this.board.getMoveHistory().size() % 2 == 0) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}
	
	private Color getEnemyColor(Color color) {
		if (color == Color.WHITE) {
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}
	}

	private int findLastNonAttackMoveIndex() {
		int counter = 0;
		int lastNonAttackMoveIndex = 0;

		for (Move move : this.board.getMoveHistory()) {
			if (move.getType() != MoveType.MOVEMENT) {
				lastNonAttackMoveIndex = counter;
			}
			counter++;
		}

		return lastNonAttackMoveIndex;
	}
	
	private boolean isInsideBoard(Coordinate coordinate){
		return coordinate.getX()>=0 && coordinate.getX() < Board.SIZE &&
			   coordinate.getY()>=0 && coordinate.getY() < Board.SIZE;
	}
	
	private ArrayList<Coordinate> calculateInitialPossibleMoveCoordinates(Coordinate from, Coordinate to, ArrayList<Path> initialPossibleMovePaths){
		ArrayList<Coordinate> initialPossibleMoveCoordinates = new ArrayList<Coordinate>();
			
		for(Path path: initialPossibleMovePaths){
			Coordinate lastCoordinate = from;
				
			boolean cancelLoop = false;
			while(!cancelLoop){
				Coordinate nextCoordinate = lastCoordinate.nextFromPath(path);
				if(isInsideBoard(nextCoordinate)){
					if(board.getPieceAt(nextCoordinate) == null){				
						initialPossibleMoveCoordinates.add(nextCoordinate);
						lastCoordinate = nextCoordinate;
						if(!path.isRepeat()){
							cancelLoop = true;							
						}
					} else {
						cancelLoop = true;
					}	
				} else{
					cancelLoop = true;
				}												
			}				
		}	
		
		return initialPossibleMoveCoordinates;
	}

	private ArrayList<Coordinate> calculateInitialPossibleCaptureCoordinates
	(Coordinate from, Coordinate to, ArrayList<Path> initialPossibleCapturePaths, Color attackerColor){
		
		ArrayList<Coordinate> initialPossibleCaptureCoordinates = new ArrayList<Coordinate>();
			
		for(Path path: initialPossibleCapturePaths){
			Coordinate lastCoordinate = from;
					
			boolean cancelLoop = false;
			while(!cancelLoop){
				Coordinate nextCoordinate = lastCoordinate.nextFromPath(path);
				if(isInsideBoard(nextCoordinate)){
					if(board.getPieceAt(nextCoordinate) == null){
						lastCoordinate = nextCoordinate;
						if(!path.isRepeat()){
							cancelLoop = true;							
						}
					} else {	
						if(board.getPieceAt(nextCoordinate).getColor()==attackerColor){
							cancelLoop = true;
						} else {
							initialPossibleCaptureCoordinates.add(nextCoordinate);
							lastCoordinate = nextCoordinate;
							cancelLoop = true;	
						}				
					}	
				} else{
					cancelLoop = true;
				}												
			}				
		}	
		return initialPossibleCaptureCoordinates;
	}
	
	private ArrayList<PieceCoordinate> getAllPiecesOfColor(Color color){
		ArrayList<PieceCoordinate> piecesWithCoords = new ArrayList<PieceCoordinate>();
		
		for(int i=0; i<Board.SIZE; i++){
			for(int j=0; j<Board.SIZE; j++){
				Coordinate currentCoordinate = new Coordinate(i,j);
				Piece currentPiece = board.getPieceAt(currentCoordinate);
				if(currentPiece != null && currentPiece.getColor() == color){
					piecesWithCoords.add(new PieceCoordinate(currentPiece,currentCoordinate));
				}
			}
		}
		return piecesWithCoords;
	}
	
	private Coordinate getKingCoordinate(Color color){
		Coordinate kingCoordinate = null;
		
		for(int i=0; i<Board.SIZE; i++){
			for(int j=0; j<Board.SIZE; j++){
				Coordinate currentCoordinate = new Coordinate(i,j);
				Piece currentPiece = board.getPieceAt(currentCoordinate);
				if(currentPiece != null && currentPiece.getType() == PieceType.KING && currentPiece.getColor() == color){
					kingCoordinate = currentCoordinate;
				}
			}
		}
		return kingCoordinate;
	}
	
	private void initialPieceValidation(Coordinate from, Coordinate to) throws InvalidMoveException {
		if(!isInsideBoard(from)){
			throw new InvalidMoveException("Incorrect start position.");
		}
		if(!isInsideBoard(to)){
			throw new InvalidMoveException("Incorrect final position.");
		}
		Piece piece = board.getPieceAt(from);
		if(piece == null){
			throw new InvalidMoveException("There is no piece at the start position.");
		}
		if(piece.getColor() != calculateNextMoveColor()){
			throw new InvalidMoveException("This is not your piece.");
		}	
	}
	
	private boolean moveCausesSelfCheck(Move moveToTest){
		Board newBoard = board.generateCopy();
		BoardManager testBoardManager = new BoardManager(newBoard);
		testBoardManager.addMove(moveToTest);
		
		if(testBoardManager.isKingInCheck(calculateNextMoveColor())){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean validateCastling(Coordinate kingStart, Coordinate kingDestination){
		
		King king = ((King)board.getPieceAt(kingStart));
		
		if(isKingInCheck(calculateNextMoveColor())){
			return false;
		}
		
		if(king.isMovedThisGame()){
			return false;
		}
				
		ArrayList<Path> initialPossibleCastlingPaths = king.getCastlingPaths();
		ArrayList<Coordinate> initialPossibleCastlingCoordinates = 
				calculateInitialPossibleMoveCoordinates(kingStart, kingDestination, initialPossibleCastlingPaths);
				
		if(!initialPossibleCastlingCoordinates.contains(kingDestination)){
			return false;
		}
		
		int rookX = 0;
		int rookY = 0;
		int direction = -1;
		
		if(kingStart.getX()<kingDestination.getX()){
			rookX= Board.SIZE-1;	
			direction = 1;
		}
		
		if(king.getColor() == Color.BLACK){
			rookY = Board.SIZE-1;
		} 
		
		Coordinate rookCoordinate = new Coordinate(rookX,rookY);
		
		Piece piece = board.getPieceAt(rookCoordinate);
		if (piece == null){
			return false;
		}
		if(piece.getType() != PieceType.ROOK){
			return false;
		}
		if(piece.isMovedThisGame()){
			return false;
		}
		
		for(int x=kingStart.getX()+direction;x<rookX;x+=direction){
			if(board.getPieceAt(new Coordinate(x,rookY)) != null){
				return false;
			}
		}
		
		if(isFieldUnderAttack(new Coordinate((kingStart.getX()+kingDestination.getX())/2,rookY),getEnemyColor(king.getColor()))){
			return false;
		}
		
		return true;
	}
	
	private boolean isFieldUnderAttack(Coordinate field, Color attackerColor) {
		
		if(field == null){
			return false;
		} 
		
		Piece piece = board.getPieceAt(field);
			
		if(piece != null && piece.getColor() == attackerColor){
			return false;
		}
						
		ArrayList<PieceCoordinate> piecesWithCoordinates = getAllPiecesOfColor(attackerColor);	
		boolean fieldAttacked = false;
							
		for (int i = 0; i < piecesWithCoordinates.size() && !fieldAttacked; i++) {
			PieceCoordinate pc = piecesWithCoordinates.get(i);
			Coordinate from = pc.getPosition();
			ArrayList<Path> paths;
			ArrayList<Coordinate> coordinates;
			if(piece == null){	
				paths = pc.getPiece().getMovePaths();
				coordinates = calculateInitialPossibleMoveCoordinates(from, field, paths);
			} else {
				paths = pc.getPiece().getCapturePaths();
				coordinates = calculateInitialPossibleCaptureCoordinates(from,field,paths,attackerColor);
			}		
			
			if(coordinates.contains(field)){
				fieldAttacked = true;
			}					

		}	
		
		return fieldAttacked;
	}
	
	private boolean validateEnPassant(Coordinate from, Coordinate to){
		if(board.getMoveHistory().size()==0){
			return false;
		}
		
		Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
		if(lastMove.getMovedPiece().getType() != PieceType.PAWN){
			return false;
		}
			
		if(Math.abs((lastMove.getTo().getY()-lastMove.getFrom().getY())) != 2){
			return false;			
		}
		
		if(board.getPieceAt(to)!=null){
			return false;
		}	
		
		if(Math.abs((lastMove.getTo().getX()-from.getX())) != 1){
			return false;
		}
		
		if(!(to.getX() == lastMove.getTo().getX() && to.getY()==(lastMove.getTo().getY()+lastMove.getFrom().getY())/2)){
			return false;
		}
		
		return true;
	}
	
	private boolean validatePawnDoubleMove(Coordinate from, Coordinate to){
		
		Piece piece = board.getPieceAt(from);
		Pawn pawn = ((Pawn)piece);
		
		if(piece.isMovedThisGame()){
			return false;		
		}
		
		if(!pawn.possibleStartCoordinates(calculateNextMoveColor()).contains(from)){
			return false;
		}
		
		ArrayList<Path> initialPossibleMovePaths = pawn.getDoubleMovePaths();
		ArrayList<Coordinate> initialPossibleMoveCoordinates = calculateInitialPossibleMoveCoordinates(from,to,initialPossibleMovePaths);
		if(initialPossibleMoveCoordinates.size()!=2){
			return false;
		}	
				
		return true;
	}
	
	private Move checkForSelfCheck(Move moveToCheck) throws KingInCheckException{
		if(moveCausesSelfCheck(moveToCheck)){
			throw new KingInCheckException();
		} else {
			return moveToCheck;	
		}
	}
	
	private boolean validateCapture(Coordinate from, Coordinate to) throws InvalidMoveException{
		Piece fromCoordinateTo = board.getPieceAt(to);
		if (fromCoordinateTo.getColor()==calculateNextMoveColor()){
			throw new InvalidMoveException("You can't capture your own pieces.");
		} else {
			Piece piece = board.getPieceAt(from);
			ArrayList<Path> initialPossibleCapturePaths = piece.getCapturePaths();
			ArrayList<Coordinate> initialPossibleCaptureCoordinates = 
					calculateInitialPossibleCaptureCoordinates(from,to,initialPossibleCapturePaths,calculateNextMoveColor());
			
			if(initialPossibleCaptureCoordinates.contains(to)){
				return true;
			} else {
				return false;
			}
		}	
	}
	
	private boolean validateNormalMovement(Coordinate from, Coordinate to) {
		Piece piece = board.getPieceAt(from);
		ArrayList<Path> initialPossibleMovePaths = piece.getMovePaths();
		ArrayList<Coordinate> initialPossibleMoveCoordinates = 
				calculateInitialPossibleMoveCoordinates(from,to,initialPossibleMovePaths);				

		if(initialPossibleMoveCoordinates.contains(to)){	
			return true;		
		}
		return false;
	}
	
	private Move checkForMovementMove(Coordinate from, Coordinate to) throws InvalidMoveException, KingInCheckException{
		if(validateNormalMovement(from,to)){
			Move initialMove = new Move(from,to,MoveType.MOVEMENT,board.getPieceAt(from));			
			return checkForSelfCheck(initialMove);				
		} else {
			Piece piece = board.getPieceAt(from);
			if(piece.getType()==PieceType.PAWN){
				if(validatePawnDoubleMove(from,to)){
					Move initialMove = new Move(from,to,MoveType.MOVEMENT,board.getPieceAt(from));
					return checkForSelfCheck(initialMove);
				}
				
				if(validateEnPassant(from, to)){
					Move initialMove = new Move(from,to,MoveType.EN_PASSANT,board.getPieceAt(from));
					return checkForSelfCheck(initialMove);
				}		
			} 						
			if (piece.getType()==PieceType.KING){	
				if(validateCastling(from, to)){
					Move initialMove = new Move(from,to,MoveType.CASTLING,board.getPieceAt(from));
					return checkForSelfCheck(initialMove);
				}	
			}		
			throw new InvalidMoveException("You can't move this piece there.");
		}	
	}
	
}
