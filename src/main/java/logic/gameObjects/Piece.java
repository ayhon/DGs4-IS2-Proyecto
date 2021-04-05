package logic.gameObjects;

import exceptions.InvalidMoveException;
import exceptions.InvalidOperationException;
import exceptions.OccupiedCellException;
import exceptions.OutOfBoundsException;
import logic.Board;
import logic.Cell;
import utils.Mode;

import java.awt.Color;

import java.io.Serializable;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;


public class Piece implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    // El color es lo que hace que una ficha pertenezca a un jugador
    private final Color color;
    private Cell position;
    private Mode playMode;

    /* Constructor */

    public Piece(Cell pos, Color color, Mode playMode) throws OccupiedCellException {
	this.color = color;
	this.playMode=playMode;
	if (!pos.isEmpty())
	    throw new OccupiedCellException(pos);
	this.position = pos;
	this.position.putPiece(this);
    }

    /* Getters */

    public Color getColor() {
	return color;
    }

    public Cell getPosition() {
	return this.position;
    }

    Mode getPlayMode() {
        return playMode;
    }

    void setPlayMode(Mode playMode) {
        this.playMode = playMode;
    }

    /**
     * Comprueba si se puede llevar a cabo un determinado movimiento
     * 
     * @param targetPosition Celda a la que queremos mover la ficha
     * @throws InvalidOperationException Salta cuando el Movimiento que queremos
     *                                   llevar a cabo no es posible
     * @throws OutOfBoundsException      Salta cuando la posicion a moverse no es
     *                                   posible
     */
    public void tryToMoveTo(Cell targetPosition) throws InvalidMoveException, OutOfBoundsException {

	if (targetPosition.equals(this.position)) { // Comprobamos que la posición que queremos alcanzar no es la de
						    // partida
	    throw new InvalidMoveException();
	}

	List<Cell> neighbours = this.position.getNeighbours(); // Obtenemos los vecinos

	if (!neighbours.contains(targetPosition)) { // Si no encontramos la posición buscada entre los vecinos

	    boolean[][] checkedCells = new boolean[Board.NUM_ROW][Board.NUM_COL]; // Creamos una matriz de booleanos que
										  // utilizaremos como mar
										  // marcador para ver que piezas ya
										  // hemos estudiado
	    for (int i = 0; i < Board.NUM_ROW; ++i) { // Inicializamos la matriz
		for (int j = 0; j < Board.NUM_COL; ++j)
		    checkedCells[i][j] = false;
	    }

	    Queue<Cell> positionQueue = new LinkedList<Cell>();
	    positionQueue.add(this.position);

	    if (!recursiveTryToMoveTo(targetPosition, checkedCells, positionQueue)) {
		throw new InvalidMoveException("No se puede acceder a la casilla " + targetPosition);
	    }
	}

    }

    /**
     * Método recursivo que nos devuelve true si se puede alcancar la posición o no.
     * Para ello guardamos en una cola las posiciones que podemos alcanzar a través
     * de realizar saltos sobre otras fichas. Vamos sacando los elementos de la cola
     * y comprobando para cada una de esas posiciones los saltos que podemos
     * realizar desde cada una de ellas. Guardamos esos nuevos saltos en la cola de
     * posiciones para estudiarlas más adelante. Si no podemos realizar nuevos
     * saltos desde la posición actual, no podemos alcanzar la posición deseada
     * desde la actual posicion, devolvemos falso. El programa acaba cuando se haya
     * un camino hasta la posición deseada o cuando se han estudiado todos y ninguno
     * nos lleva a la misma.
     * 
     * @param targetPosition  Posición que queremos alcanzar
     * @param positionToCheck Posición en la que nos encontramos actualmente
     * @param checkedCells    Tablero con las Celdas que ya hemos mirado
     * @return Devuelve si se puede llevar a cabo el movimiento
     */
    private boolean recursiveTryToMoveTo(Cell targetPosition, boolean[][] checkedCells, Queue<Cell> positionQueue) {

	Cell positionToCheck = positionQueue.peek(); // Accedemos a la siguiente posición a estudiar

	if (!checkedCells[positionToCheck.getRow()][positionToCheck.getCol()]) { // Comprobamos que no la hemos
										 // estudiado ya
	    positionQueue.poll(); // Retiramos la posición de la cola
	    List<Cell> neighbours = positionToCheck.getNeighbours(); // Obtenemos sus vecinos

	    for (Cell ady : neighbours) {
		Cell newJump = positionToCheck.getCellJump(ady); // Vemos el posible salto que podemos obtener desde la
								 // posición actual y dado un vecio concreto
		if (newJump != null) {
		    if (newJump.equals(targetPosition)) // Comprobamos si el nuevo salto nos proporciona la posición
							// deseada
			return true;
		    positionQueue.add(newJump); // Añadimos la posición a la cola para estudiarla más adelante
		}
	    }

	    checkedCells[positionToCheck.getRow()][positionToCheck.getCol()] = true; // Actualizamos la matriz de
										     // comprobaciones

	    return recursiveTryToMoveTo(targetPosition, checkedCells, positionQueue); // Comprobamos las nuevas
										      // posiciones
	}

	return false; // En caso de que no podamos realizar nuevos movimientos devolvemos false
    }

    /**
     * Desplazar la pieza
     *
     * @param targetPosition posición destino
     * @throws InvalidOperationException puede ser que sea una posición ocupada o un
     *                                   movimiento inválido
     * @throws OutOfBoundsException
     */
    public void move(Cell targetPosition) throws InvalidMoveException {
	try {
	    tryToMoveTo(targetPosition); // Vemos que podemos llevar a cano el movimiento
	    this.position.removePiece(); // Actualizamos las posiciones(celdas)
	    this.position = targetPosition;
	    this.position.putPiece(this);
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	    throw new InvalidMoveException("The move is not possible.");
	}
    }

}