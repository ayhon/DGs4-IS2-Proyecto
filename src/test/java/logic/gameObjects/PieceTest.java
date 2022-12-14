package logic.gameObjects;

import static org.junit.jupiter.api.Assertions.fail;

import exceptions.InvalidMoveException;
import exceptions.OccupiedCellException;
import logic.Board;
import logic.Cell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Mode;
import utils.PieceColor;

class PieceTest {
    Piece piece, piece2, piece3;
    Cell posAux = null;
    Board board = null;
    PieceColor color = null;

    @BeforeEach
    void init() {
        try {
            board = new Board();
            color = PieceColor.BLUE;
            piece = new Piece(new Cell(8, 6, board), color);

            piece2 = new Piece(piece.getPosition().getUpperLeft(), color);
            piece3 =
                new Piece(
                    piece.getPosition().getUpperLeft().getUpperLeft().getUpperRight(),
                    color
                );
        } catch (OccupiedCellException e) {
            fail("Middle cell wasn't empty in initialization.");
        }
    }

    @Test
    void tryToMoveToCell() { // TODO: Expand in all directions, more movement
        // Guardamos la posicion en la auxiliar
        Assertions.assertDoesNotThrow(
            () -> {
                posAux = piece.getPosition();
            }
        );
        // Intentamos movernos a nuestra propia posición por lo que no podemos y nos
        // salta una excepción
        Assertions.assertThrows(
            InvalidMoveException.class,
            () -> {
                piece.tryToMove(posAux);
            }
        );
        // Nos movemos a una posición adyacente
        Assertions.assertDoesNotThrow(
            () -> {
                posAux = posAux.getUpperRight();

                piece.tryToMove(posAux);
            }
        );
        // Nos movemos realizando un salto
        Assertions.assertDoesNotThrow(
            () -> {
                piece.tryToMove(piece2.getPosition().getUpperLeft());
            }
        );
        // Nos movemos realizando un doble salto
        Assertions.assertDoesNotThrow(
            () -> {
                piece.tryToMove(piece3.getPosition().getUpperRight());
            }
        );
        // Tratamos de movernos a una posición inválida por estar ocupada por otra pieza
        Assertions.assertThrows(
            InvalidMoveException.class,
            () -> {
                piece.tryToMove(piece3.getPosition());
            }
        );
        // Tratamos de movernos a una posición inválida por no ser adyacente a ninguna
        // otra pieza
        Assertions.assertThrows(
            InvalidMoveException.class,
            () -> {
                piece.tryToMove(piece2.getPosition().getLowerLeft(2));
            }
        );
    }

    @Test
    void move() {
        // Tratamos de movernos a una posición en la que debemos dar un salto
        Assertions.assertDoesNotThrow(
            () -> {
                posAux = piece.getPosition();
                piece.move(piece2.getPosition().getUpperLeft(), Mode.Traditional);
            }
        );
        // Tratamos de movernos a una posición inválida por no tener ninguna pieza
        // adyacente
        /// Aquí lanza una excepción de null pointer, porque no existe ninguna pieza en esa celda
        Assertions.assertThrows(
            InvalidMoveException.class,
            () -> {
                piece.move(piece2.getPosition().getLowerLeft(2), Mode.Traditional);
            }
        );
        // Comprobamos que se han actualizado correctamente las variables(se han vaciado
        // la celda anterior y la nueva tiene la pieza
        Assertions.assertDoesNotThrow(
            () -> {
                assert (piece.getPosition().equals(piece2.getPosition().getUpperLeft()));
                assert (posAux.isEmpty());
            }
        );
    }
}
