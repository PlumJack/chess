package com.capgemini.chess.algorithms.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.capgemini.chess.algorithms.data.Bishop;
import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.King;
import com.capgemini.chess.algorithms.data.Knight;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.Path;
import com.capgemini.chess.algorithms.data.Pawn;
import com.capgemini.chess.algorithms.data.Piece;
import com.capgemini.chess.algorithms.data.PieceCoordinate;
import com.capgemini.chess.algorithms.data.Queen;
import com.capgemini.chess.algorithms.data.Rook;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
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
		//this.board = board.generateCopy();
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

		// there is no need to check moves that where before last capture/en
		// passant/castling
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

		// for this purpose a "move" consists of a player completing his turn
		// followed by his opponent completing his turn
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

	// PRIVATE

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
		
		//move.getMovedPiece().setMovedThisGame(true);

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
		//rook.setMovedThisGame(true);
	}

	private void addEnPassant(Move move) {
		Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
		this.board.setPieceAt(null, lastMove.getTo());
	}

	private Move validateMove(Coordinate from, Coordinate to) throws InvalidMoveException, KingInCheckException {

		initialPieceValidation(from,to);
		
		Piece piece = board.getPieceAt(from);
		Piece fromCoordinateTo = board.getPieceAt(to);
		
		if(fromCoordinateTo==null){
			//movement
			
			ArrayList<Path> initialPossibleMovePaths = piece.getMovePaths();
			//ArrayList<Path> initialPossibleMovePaths = pieceFromCoordinateFrom.getMovePaths();
			ArrayList<Coordinate> initialPossibleMoveCoordinates = calculateInitialPossibleMoveCoordinates(from,to,initialPossibleMovePaths);				
			
		//contains
			Move initialMove;
		if(initialPossibleMoveCoordinates.contains(to)){
			initialMove = new Move(from,to,MoveType.MOVEMENT,board.getPieceAt(from));
			
			if(!moveCausesSelfCheck(initialMove)){
				return initialMove;
			}
			
		} else {
			if(piece.getType()==PieceType.PAWN){
				//doubleMove
				Pawn pawn = ((Pawn)piece);
				
				if(piece.isMovedThisGame()==false){
					//Pawn pawn = ((Pawn)piece);
					if(pawn.possibleStartCoordinates(calculateNextMoveColor()).contains(from)){
						initialPossibleMovePaths = pawn.getDoubleMovePaths();
						initialPossibleMoveCoordinates = calculateInitialPossibleMoveCoordinates(from,to,initialPossibleMovePaths);
						if(initialPossibleMoveCoordinates.size()==2){
							initialMove = new Move(from,to,MoveType.MOVEMENT,board.getPieceAt(from));
							
							if(!moveCausesSelfCheck(initialMove)){
								return initialMove;
							}
						}	
					}
					
				}

				//enPassant
				if(board.getMoveHistory().size()>0){
					Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
					if(lastMove.getMovedPiece().getType() == PieceType.PAWN){
						if(Math.abs((lastMove.getTo().getY()-lastMove.getFrom().getY())) == 2){
							if(board.getPieceAt(to)==null){
								if(Math.abs((lastMove.getTo().getX()-from.getX())) == 1){
									if(to.getX() == lastMove.getTo().getX() && to.getY()==(lastMove.getTo().getY()+lastMove.getFrom().getY())/2){
										initialMove = new Move(from,to,MoveType.EN_PASSANT,board.getPieceAt(from));
										if(!moveCausesSelfCheck(initialMove)){
											return initialMove;
										}
									}
								}
							}				
						}
					}
				}	
				
				/*
				initialPossibleMovePaths = pawn.getDoubleMovePaths();
				initialPossibleMoveCoordinates = calculateInitialPossibleMoveCoordinates(from,to,initialPossibleMovePaths);
				*/
				
				
				/*
				 * if(board.getMoveHistory().size()>0){
					Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
					if(lastMove.getMovedPiece().getType() == PieceType.PAWN){
						if(Math.abs((lastMove.getTo().getY()-lastMove.getFrom().getY())) == 2){
							if(board.getPieceAt(to)==null){
								if(Math.abs((lastMove.getTo().getX()-from.getX())) == 1){
									if(to.getX() == lastMove.getTo().getX() && to.getY()==(lastMove.getTo().getY()+lastMove.getFrom().getY())/2){
										Move enPassant = new Move(from,to,MoveType.EN_PASSANT,board.getPieceAt(from));
										return enPassant;
									}
								}
							}				
						}
					}
				}
				 */
				
			} //else if (piece.getType()==PieceType.KING){
				
			//}
			
			
			
			
			//castling
			//else:
			
			throw new InvalidMoveException("You can't move this piece there.");
		}		
		} else {
			// failed capture
			if (fromCoordinateTo.getColor()==calculateNextMoveColor()){
				throw new InvalidMoveException("You can't capture your own pieces.");
			} else {
				//capture
				ArrayList<Path> initialPossibleCapturePaths = piece.getCapturePaths();
				//ArrayList<Path> initialPossibleCapturePaths = pieceFromCoordinateFrom.getCapturePaths();
				ArrayList<Coordinate> initialPossibleCaptureCoordinates = calculateInitialPossibleCaptureCoordinates(from,to,initialPossibleCapturePaths,calculateNextMoveColor());
				
				//contains
				if(initialPossibleCaptureCoordinates.contains(to)){
					Move initialMove = new Move(from,to,MoveType.CAPTURE,board.getPieceAt(from));
				
					if(!moveCausesSelfCheck(initialMove)){
						return initialMove;
					}			
				//capture end
				}
			}	
			
		}
		
		throw new InvalidMoveException("Something went wrong.");
	}

	private boolean isKingInCheck(Color kingColor) {
		
		ArrayList<PieceCoordinate> piecesWithCoordinates = getAllPiecesOfColor(getEnemyColor(kingColor));
		boolean kingChecked = false;
		
		Coordinate to = getKingCoordinate(kingColor);
		if(to == null){
			return false;
		}
		
		for (int i = 0; i < piecesWithCoordinates.size() && !kingChecked; i++) {
			PieceCoordinate pc = piecesWithCoordinates.get(i);
			Coordinate from = pc.getPosition();
			
			ArrayList<Path> initialPossibleCapturePaths = pc.getPiece().getCapturePaths();
			ArrayList<Coordinate> initialPossibleCaptureCoordinates = calculateInitialPossibleCaptureCoordinates(from,to,initialPossibleCapturePaths,getEnemyColor(kingColor));
			
			if(initialPossibleCaptureCoordinates.contains(to)){
				kingChecked = true;
			}					
			//validateMove(pc.getPosition(), getKingCoordinate(calculateNextMoveColor()));
		}
		
		//return false;
		return kingChecked;
	}

	private boolean isAnyMoveValid(Color nextMoveColor) {
		
		ArrayList<PieceCoordinate> piecesWithCoordinates = getAllPiecesOfColor(nextMoveColor);
		boolean anyMoveValid = false;
		
		for (int i = 0; i < piecesWithCoordinates.size() && !anyMoveValid; i++) {
			
			for(int x=0; x<Board.SIZE && !anyMoveValid; x++){
				for(int y=0; y<Board.SIZE && !anyMoveValid; y++){
					try {
						Move move = validateMove(piecesWithCoordinates.get(i).getPosition(), new Coordinate(x,y));
						if(move != null){
							anyMoveValid = true;
							return true;
						}
					} catch (KingInCheckException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					} catch (InvalidMoveException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
					
				}
			}
			
			
		}

		return anyMoveValid;
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
		return coordinate.getX()>=0 && coordinate.getX() < board.SIZE &&
			   coordinate.getY()>=0 && coordinate.getY() < board.SIZE;
	}
	
	private ArrayList<Coordinate> calculateInitialPossibleMoveCoordinates(Coordinate from, Coordinate to, ArrayList<Path> initialPossibleMovePaths){
		//ArrayList<Path> initialPossibleMovePaths = pieceFromCoordinateFrom.getMovePaths();
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

	private ArrayList<Coordinate> calculateInitialPossibleCaptureCoordinates(Coordinate from, Coordinate to, ArrayList<Path> initialPossibleCapturePaths, Color attackerColor){
		//ArrayList<Path> initialPossibleCapturePaths = pieceFromCoordinateFrom.getCapturePaths();
		ArrayList<Coordinate> initialPossibleCaptureCoordinates = new ArrayList<Coordinate>();
			
		for(Path path: initialPossibleCapturePaths){
			Coordinate lastCoordinate = from;
			
			
			boolean cancelLoop = false;
			while(!cancelLoop){
				Coordinate nextCoordinate = lastCoordinate.nextFromPath(path);
				if(isInsideBoard(nextCoordinate)){
					if(board.getPieceAt(nextCoordinate) == null){
						
						//initialPossibleCaptureCoordinates.add(nextCoordinate);
						lastCoordinate = nextCoordinate;
						if(!path.isRepeat()){
							cancelLoop = true;							
						}
					} else {	
						if(board.getPieceAt(nextCoordinate).getColor()==attackerColor){
						//if(board.getPieceAt(nextCoordinate).getColor()==calculateNextMoveColor()){
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
		//Piece[][] wholeBoard = board.getPieces();
		ArrayList<PieceCoordinate> piecesWithCoords = new ArrayList<PieceCoordinate>();
		
		for(int i=0; i<board.SIZE; i++){
			for(int j=0; j<board.SIZE; j++){
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
		
		for(int i=0; i<board.SIZE; i++){
			for(int j=0; j<board.SIZE; j++){
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
	
	private boolean moveCausesSelfCheck(Move moveToTest) throws KingInCheckException {
		//ArrayList<Move> movesCopy = board.getMoveHistoryCopy();
		Board newBoard = board.generateCopy();
		//System.out.println("a");
		//(newBoard.getMoveHistory()).add(moveToTest);
		//movesCopy.add(moveToTest);
		//System.out.println("b");
		BoardManager testBoardManager = new BoardManager(newBoard);
		testBoardManager.addMove(moveToTest);;
		//System.out.println("c");
		
		if(testBoardManager.isKingInCheck(calculateNextMoveColor())){
			throw new KingInCheckException();
		} else {
			return false;
		}
	}
}
