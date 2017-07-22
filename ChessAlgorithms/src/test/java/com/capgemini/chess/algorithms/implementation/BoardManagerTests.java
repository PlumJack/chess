package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
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
 * Test class for testing {@link BoardManager}
 * 
 * @author Michal Bejm
 *
 */
public class BoardManagerTests {

	@Test
	public void shouldGenerateBoardInitialPosition() {
		// given
		List<Move> moves = new ArrayList<>();
		
		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		for (int x = 0; x < Board.SIZE; x++) {
			for (int y = 0; y < Board.SIZE; y++) {
				if (y > 1 && y < 6) {
					assertNull(boardManager.getBoard().getPieceAt(new Coordinate(x, y)));
				}
				else {
					assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(x, y)));
				}
			}
		}
		assertEquals(new Pawn(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(5, 1)));
		assertEquals(new King(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(4, 0)));
		assertEquals(new Bishop(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(5, 0)));
		assertEquals(new Rook(Color.BLACK), boardManager.getBoard().getPieceAt(new Coordinate(0, 7)));
		assertEquals(new Knight(Color.BLACK), boardManager.getBoard().getPieceAt(new Coordinate(1, 7)));
		assertEquals(new Queen(Color.BLACK), boardManager.getBoard().getPieceAt(new Coordinate(3, 7)));
		assertEquals(32, calculateNumberOfPieces(boardManager.getBoard()));
	}
	
	@Test
	public void shouldMakeMovement() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new Move();
		move.setFrom(new Coordinate(5, 1));
		move.setTo(new Coordinate(5, 3));
		move.setType(MoveType.MOVEMENT);
		moves.add(move);
		
		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(5, 1)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(5, 3)));
		assertEquals(32, calculateNumberOfPieces(boardManager.getBoard()));
	}
	
	@Test
	public void shouldMakeCapture() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new Move();
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 6));
		move.setType(MoveType.CAPTURE);
		moves.add(move);
		
		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(0, 0)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(0, 6)));
		assertEquals(31, calculateNumberOfPieces(boardManager.getBoard()));
	}
	
	@Test
	public void shouldMakeCastling() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new Move();
		move.setFrom(new Coordinate(4, 0));
		move.setTo(new Coordinate(2, 0));
		move.setType(MoveType.CASTLING);
		moves.add(move);
		
		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(4, 0)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 0)));
		assertEquals(new King(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(2, 0)));
		assertEquals(new Rook(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(3, 0)));
	}
	
	@Test
	public void shouldMakeEnPassant() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move1 = new Move();
		move1.setFrom(new Coordinate(1, 1));
		move1.setTo(new Coordinate(1, 4));
		move1.setType(MoveType.MOVEMENT);
		moves.add(move1);
		Move move2 = new Move();
		move2.setFrom(new Coordinate(2, 6));
		move2.setTo(new Coordinate(2, 4));
		move2.setType(MoveType.MOVEMENT);
		moves.add(move2);
		Move move3 = new Move();
		move3.setFrom(new Coordinate(1, 4));
		move3.setTo(new Coordinate(2, 5));
		move3.setType(MoveType.EN_PASSANT);
		moves.add(move3);
		
		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 4)));
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(1, 4)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 5)));
		assertEquals(new Pawn(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(2, 5)));
		assertEquals(31, calculateNumberOfPieces(boardManager.getBoard()));
	}
	
	@Test
	public void shouldMakePromotionForBlackPawn() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new Move();
		move.setFrom(new Coordinate(1, 6));
		move.setTo(new Coordinate(1, 0));
		move.setType(MoveType.CAPTURE);
		moves.add(move);

		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertEquals(new Queen(Color.BLACK), boardManager.getBoard().getPieceAt(new Coordinate(1, 0)));
	}
	
	@Test
	public void shouldMakePromotionForWhitePawn() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new Move();
		move.setFrom(new Coordinate(1, 1));
		move.setTo(new Coordinate(1, 7));
		move.setType(MoveType.CAPTURE);
		moves.add(move);

		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertEquals(new Queen(Color.WHITE), boardManager.getBoard().getPieceAt(new Coordinate(1, 7)));
	}
	
	@Test
	public void shouldPerformMoveBishopMovement() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(0, 6));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(0, 6), new Coordinate(6, 0));
		
		// then
		assertEquals(MoveType.MOVEMENT, move.getType());
		assertEquals(new Bishop(Color.WHITE), move.getMovedPiece());
	}
	
	@Test
	public void shouldPerformMovePawnMovement() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(4, 6));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 6), new Coordinate(4, 4));
		
		// then
		assertEquals(MoveType.MOVEMENT, move.getType());
		assertEquals(new Pawn(Color.BLACK), move.getMovedPiece());
	}

	@Test
	public void shouldPerformMoveKingMovement() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 0), new Coordinate(4, 1));
		
		// then
		assertEquals(MoveType.MOVEMENT, move.getType());
		assertEquals(new King(Color.WHITE), move.getMovedPiece());
	}
	
	@Test
	public void shouldPerformMoveKnightCapture() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Knight(Color.BLACK), new Coordinate(3, 4));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(2, 6));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(3, 4), new Coordinate(2, 6));
		
		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(new Knight(Color.BLACK), move.getMovedPiece());
	}
	
	@Test
	public void shouldPerformMoveQueenCapture() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(new Queen(Color.WHITE), new Coordinate(5, 0));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(7, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(5, 0), new Coordinate(7, 2));
		
		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(new Queen(Color.WHITE), move.getMovedPiece());
	}
	
	@Test
	public void shouldPerformMoveRookCapture() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Rook(Color.BLACK), new Coordinate(1, 4));
		board.setPieceAt(new Knight(Color.WHITE), new Coordinate(5, 4));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(1, 4), new Coordinate(5, 4));
		
		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(new Rook(Color.BLACK), move.getMovedPiece());
	}
	
	@Test
	public void shouldPerformMoveCastling() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(7, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		
		// then
		assertEquals(MoveType.CASTLING, move.getType());
		assertEquals(new King(Color.WHITE), move.getMovedPiece());
	}
	
	@Test
	public void shouldPerformMoveEnPassant() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 4));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(2, 6));
		boardManager.performMove(new Coordinate(2, 6), new Coordinate(2, 4));
		
		// when
		Move move = boardManager.performMove(new Coordinate(1, 4), new Coordinate(2, 5));
		
		// then
		assertEquals(MoveType.EN_PASSANT, move.getType());
		assertEquals(new Pawn(Color.WHITE), move.getMovedPiece());
	}
	
	@Test
	public void shouldGetExceptionFromIncorrectXPerformMoveEnPassant() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 4));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(2, 6));
		boardManager.performMove(new Coordinate(2, 6), new Coordinate(2, 4));
		
		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 4), new Coordinate(3, 5));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
			
		// then
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionFromIncorrectYPerformMoveEnPassant() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 4));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(2, 6));
		boardManager.performMove(new Coordinate(2, 6), new Coordinate(2, 4));
		
		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 4), new Coordinate(2, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
			
		// then
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionFromIncorrectColumnPerformMoveEnPassant() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 4));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(3, 6));
		boardManager.performMove(new Coordinate(3, 6), new Coordinate(3, 4));
		
		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 4), new Coordinate(3, 5));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
			
		// then
		assertTrue(exceptionThrown);
	}
	
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidIndexOutOfBound() {
		// given
		BoardManager boardManager = new BoardManager();
		
		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(8, 6), new Coordinate(7, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidMoveOrder() {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.BLACK), new Coordinate(0, 7));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(0, 7), new Coordinate(1, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidEmptySpot() {
		// given
		Board board = new Board();
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(0, 7), new Coordinate(1, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidSameSpot() {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(0, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(0, 0), new Coordinate(0, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidPawnBackwardMove() {
		// given
		Board board = new Board();
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(1, 1));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidPawnAttackDestination() {
		// given
		Board board = new Board();
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(0, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidPawnAttackDistance() {
		// given
		Board board = new Board();
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(1, 4));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidPawnCaptureDestination() {
		// given
		Board board = new Board();
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(1, 2));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(1, 3));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(1, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidKingDistance() {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(4, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidKnightDestination() {
		// given
		Board board = new Board();
		board.setPieceAt(new Knight(Color.WHITE), new Coordinate(1, 1));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 1), new Coordinate(3, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidBishopDestination() {
		// given
		Board board = new Board();
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(1, 1));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 1), new Coordinate(1, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidQueenLeapsOver() {
		// given
		Board board = new Board();
		board.setPieceAt(new Queen(Color.WHITE), new Coordinate(1, 1));
		board.setPieceAt(new Pawn(Color.BLACK), new Coordinate(4, 4));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 1), new Coordinate(6, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidRookLeapsOver() {
		// given
		Board board = new Board();
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(3, 0));
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(3, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(3, 0), new Coordinate(3, 7));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidOwnPieceCapture() {
		// given
		Board board = new Board();
		board.setPieceAt(new Knight(Color.WHITE), new Coordinate(5, 6));
		board.setPieceAt(new Pawn(Color.WHITE), new Coordinate(3, 5));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(5, 6), new Coordinate(3, 5));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidCastlingPiecesMoved() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(7, 0));
		boardManager.performMove(new Coordinate(4, 0), new Coordinate(3, 0));
		board.getMoveHistory().add(createDummyMove(board));
		boardManager.performMove(new Coordinate(3, 0), new Coordinate(4, 0));
		board.getMoveHistory().add(createDummyMove(board));
		
		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidCastlingWithPiecesBetween() {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(7, 0));
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(5, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenFieldBetweenUnderAttack() {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(7, 0));
		board.setPieceAt(new Rook(Color.BLACK), new Coordinate(5, 7));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidCastlingKingUnderCheck() {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new King(Color.WHITE), new Coordinate(7, 0));
		board.setPieceAt(new Rook(Color.BLACK), new Coordinate(5, 7));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldGetExceptionForPerformMoveWhenInvalidKingWouldBeChecked() {
		// given
		Board board = new Board();
		board.setPieceAt(new King(Color.WHITE), new Coordinate(4, 0));
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(4, 5));
		board.setPieceAt(new Rook(Color.BLACK), new Coordinate(4, 7));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 5), new Coordinate(7, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = e instanceof KingInCheckException;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldUpdateBoardStateRegular() throws InvalidMoveException {
		// given
		BoardManager boardManager = new BoardManager();
		
		// when
		BoardState boardState = boardManager.updateBoardState();
		
		// then
		assertEquals(BoardState.REGULAR, boardState);
	}
	
	@Test
	public void shouldUpdateBoardStateCheck() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Bishop(Color.WHITE), new Coordinate(1, 3));
		board.setPieceAt(new King(Color.BLACK), new Coordinate(4, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();
		
		// then
		assertEquals(BoardState.CHECK, boardState);
	}
	
	@Test
	public void shouldUpdateBoardStateCheckMate() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(0, 1));
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(1, 0));
		board.setPieceAt(new King(Color.BLACK), new Coordinate(4, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();
		
		// then
		assertEquals(BoardState.CHECK_MATE, boardState);
	}
	
	@Test
	public void shouldUpdateBoardStateStaleMate() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(new King(Color.BLACK), new Coordinate(7, 0));
		board.setPieceAt(new Queen(Color.WHITE), new Coordinate(5, 1));
		board.setPieceAt(new King(Color.WHITE), new Coordinate(6, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();
		
		// then
		assertEquals(BoardState.STALE_MATE, boardState);
	}
	
	@Test
	public void shouldCheckThreefoldRepetitionRuleSuccessful() {
		// given
		List<Move> moves = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			Move move1 = new Move();
			move1.setFrom(new Coordinate(5, 1));
			move1.setTo(new Coordinate(5, 3));
			move1.setType(MoveType.MOVEMENT);
			moves.add(move1);
			
			Move move2 = new Move();
			move2.setFrom(new Coordinate(5, 6));
			move2.setTo(new Coordinate(5, 4));
			move2.setType(MoveType.MOVEMENT);
			moves.add(move2);
			
			Move move3 = new Move();
			move3.setFrom(new Coordinate(5, 3));
			move3.setTo(new Coordinate(5, 1));
			move3.setType(MoveType.MOVEMENT);
			moves.add(move3);
			
			Move move4 = new Move();
			move4.setFrom(new Coordinate(5, 4));
			move4.setTo(new Coordinate(5, 6));
			move4.setType(MoveType.MOVEMENT);
			moves.add(move4);
		}
		BoardManager boardManager = new BoardManager(moves);
		
		// when
		boolean isThreefoldRepetition = boardManager.checkThreefoldRepetitionRule();
		
		// then
		assertTrue(isThreefoldRepetition);
	}
	
	@Test
	public void shouldCheckThreefoldRepetitionRuleUnsuccessful() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move1 = new Move();
		move1.setFrom(new Coordinate(5, 1));
		move1.setTo(new Coordinate(5, 3));
		move1.setType(MoveType.MOVEMENT);
		moves.add(move1);
		
		Move move2 = new Move();
		move2.setFrom(new Coordinate(5, 6));
		move2.setTo(new Coordinate(5, 4));
		move2.setType(MoveType.MOVEMENT);
		moves.add(move2);
		
		Move move3 = new Move();
		move3.setFrom(new Coordinate(5, 3));
		move3.setTo(new Coordinate(5, 1));
		move3.setType(MoveType.MOVEMENT);
		moves.add(move3);
		
		Move move4 = new Move();
		move4.setFrom(new Coordinate(5, 4));
		move4.setTo(new Coordinate(5, 6));
		move4.setType(MoveType.MOVEMENT);
		moves.add(move4);
		BoardManager boardManager = new BoardManager(moves);
		
		// when
		boolean isThreefoldRepetition = boardManager.checkThreefoldRepetitionRule();
		
		// then
		assertFalse(isThreefoldRepetition);
	}
	
	@Test
	public void shouldCheckFiftyMoveRuleSuccessful() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		for (int i = 0; i < 100; i++) {
			board.getMoveHistory().add(createDummyMove(board));
		}
			
		// when
		boolean areFiftyMoves = boardManager.checkFiftyMoveRule();
		
		// then
		assertTrue(areFiftyMoves);
	}
	
	@Test
	public void shouldCheckFiftyMoveRuleUnsuccessfulWhenNotEnoughMoves() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		for (int i = 0; i < 99; i++) {
			board.getMoveHistory().add(createDummyMove(board));
		}
			
		// when
		boolean areFiftyMoves = boardManager.checkFiftyMoveRule();
		
		// then
		assertFalse(areFiftyMoves);
	}
	
	@Test
	public void shouldCheckFiftyMoveRuleUnsuccessfulWhenPawnMoved() {
		// given
		BoardManager boardManager = new BoardManager(new Board());
		
		Move move = new Move();
		boardManager.getBoard().setPieceAt(new Pawn(Color.WHITE), new Coordinate(0, 0));
		move.setMovedPiece(new Pawn(Color.WHITE));
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 0));
		move.setType(MoveType.MOVEMENT);
		boardManager.getBoard().setPieceAt(null, new Coordinate(0, 0));
		boardManager.getBoard().getMoveHistory().add(move);
		
		for (int i = 0; i < 99; i++) {
			boardManager.getBoard().getMoveHistory().add(createDummyMove(boardManager.getBoard()));
		}
			
		// when
		boolean areFiftyMoves = boardManager.checkFiftyMoveRule();
		
		// then
		assertFalse(areFiftyMoves);
	}
	
	private Move createDummyMove(Board board) {
		
		Move move = new Move();
		
		if (board.getMoveHistory().size() % 2 == 0) {
			board.setPieceAt(new Rook(Color.WHITE), new Coordinate(0, 0));
			move.setMovedPiece(new Rook(Color.WHITE));
		}
		else {
			board.setPieceAt(new Rook(Color.BLACK), new Coordinate(0, 0));
			move.setMovedPiece(new Rook(Color.BLACK));
		}
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 0));
		move.setType(MoveType.MOVEMENT);
		board.setPieceAt(null, new Coordinate(0, 0));
		return move;
	}

	private int calculateNumberOfPieces(Board board) {
		int counter = 0;
		for (int x = 0; x < Board.SIZE; x++) {
			for (int y = 0; y < Board.SIZE; y++) {
				if (board.getPieceAt(new Coordinate(x, y)) != null) {
					counter++;
				}
			}
		}
		return counter;
	}

	
}
