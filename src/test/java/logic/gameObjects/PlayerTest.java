package logic.gameObjects;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.OccupiedCellException;
import logic.Board;
import logic.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.PieceColor;

public class PlayerTest {
    HumanPlayer test;
    Board board;

    @BeforeEach
    void init() {
        try {
            board = new Board();

            test = new HumanPlayer(PieceColor.BLUE, Board.Side.Down, "0");
        } catch (OccupiedCellException e) {
            fail("Player start Cell previously occupied");
        }
    }

    @Test
    void selectedPiece() throws Exception {
        assertFalse(test.hasSelectedPiece());

        assertFalse(test.selectPiece(new Piece(new Cell(6, 6, board), PieceColor.BLUE)));

        assertFalse(test.hasSelectedPiece());

        assertTrue(test.selectPiece(board.getCell(15, 6).getPiece()));

        assertTrue(test.hasSelectedPiece());

        test.deselectPiece();

        assertFalse(test.hasSelectedPiece());
    }
}
