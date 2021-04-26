package graphic;

import control.Controller;
import exceptions.OccupiedCellException;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import logic.Board;
import logic.Board.Side;
import logic.Cell;
import logic.Game;
import logic.gameObjects.Piece;
import utils.PieceColor;
import utils.Tools;

public class BoardPanel extends JPanel implements GameObserver {
    private static final long serialVersionUID = -3501731254500743354L;
    private static final int default_radius = 20;
    private static final double aspectRatio = 0.883; // w/h

    private Controller ctrl = null;
    private Map<Cell, CellLabel> cellLabels = new HashMap<>();
    private Board board;
    private int radius;

    public BoardPanel(Controller ctrl) {
        this(ctrl, default_radius, null);
    }

    /**
     * Create a BoardPanel connected to controller with the data
     * of board.
     *
     * @param ctrl		controller to connect to
     * @param radius	radius of the CellLabels
     * @param board		board to load the data from
     */
    public BoardPanel(Controller ctrl, int radius, Board board) {
        this.ctrl = ctrl;
        this.board = board;
        this.radius = radius; // Lo guardamos para ver si crece o decrece un resize

        int label_height = 2 * radius;
        int label_width = (int) (Math.sqrt(4.0 / 3) * label_height);

        initGUI(new Dimension(13 * label_width, 17 * label_height));
        if (ctrl != null) ctrl.addObserver(this);
        this.setBoard(board, radius);
    }

    private void initGUI(Dimension dim) {
        this.setLayout(null);
        this.setPreferredSize(dim);
        this.addComponentListener(
                new ComponentListener() {

                    //                    private boolean locked = false;
                    @Override
                    public void componentHidden(ComponentEvent arg0) {}

                    @Override
                    public void componentMoved(ComponentEvent arg0) {}

                    @Override
                    public void componentResized(ComponentEvent arg0) {
                        //                	if(locked) return;
                        //                	locked = true;
                        int w = BoardPanel.this.getWidth(), h = // w = 13 * label_width = 13 * 1/cos(30) * label_height = 26/cos(30) * radius
                            BoardPanel.this.getHeight(); // h = 17 * label_height = 34 * radius

                        int newRadius = (int) Math.min(
                            w * Math.cos(Math.PI / 6) / 26,
                            h / 34
                        );

                        // /*HORRIBLE HACK*/
                        // Tenemos dos posibles tamaños a los que reducirnos:
                        // Dimension(aspectRatio * h, h)
                        // Dimension(w, w /aspectRatio)
                        // Escogemos el mayor o menor dependiendo de si crecemos o encogemos
                        // Dimension newDim = new Dimension((int)(h*aspectRatio), h);
                        //
                        //  We are growing and h*aspectRatio < w or we are shrinking and h*aspectRatio > w
                        // if(newRadius > radius == h * aspectRatio < w)
                        //     newDim = new Dimension(w, (int)(w/aspectRatio));
                        //
                        // BoardPanel.this.setSize(newDim);

                        setRadius(newRadius);
                        // locked = true;
                    }

                    @Override
                    public void componentShown(ComponentEvent arg0) {}
                }
            );
    }

    public void setBoard(Board board, int radius) {
        if (board == null) return;
        cellLabels.clear();
        for (Cell cell : board) {
            CellLabel clabel = new CellLabel(this, cell, radius);
            cellLabels.put(cell, clabel); // Store in dictionary
            this.add(clabel); // Load in JPanel
        }
    }

    public void setRadius(int radius) {
        this.radius = radius;
        for (Map.Entry<Cell, CellLabel> pair : this.cellLabels.entrySet()) {
            pair.getValue().setRadius(radius);
        }
    }

    public void handleClick(Cell position) {
        this.ctrl.handleClick(position);
    }

    @Override
    public void onRegister(Game game) {
        this.setBoard(game.getBoard(), this.radius);
    }

    @Override
    public void onSelectedPiece(Piece piece) {
        CellLabel cellLabel = this.cellLabels.get(piece.getPosition());
        cellLabel.setSelected(!cellLabel.getSelected());
    }

    @Override
    public void onMovedPiece(Cell from, Piece piece) {
        CellLabel fromLabel, toLabel;

        fromLabel = this.cellLabels.get(from);
        fromLabel.setSelected(false);
        fromLabel.setColor();

        toLabel = this.cellLabels.get(piece.getPosition());
        toLabel.setColor(piece.getColor().getColor());
    }

    @Override
    public void onEndTurn(Game game) {}

    @Override
    public void onSurrendered(Game game) {}

    @Override
    public void onReset(Game game) {}

    @Override
    public void onGameEnded(Game game) {}

    public static void main(String[] args) {
        Game g = new Game();
        try {
            g.addNewPlayer(PieceColor.BLUE, Side.Down);
            g.addNewPlayer(PieceColor.RED, Side.Up);

            g.addNewPlayer(PieceColor.GREEN, Side.DownLeft);
            g.addNewPlayer(PieceColor.MAGENTA, Side.UpRight);

            g.addNewPlayer(PieceColor.ORANGE, Side.DownRight);
            g.addNewPlayer(PieceColor.YELLOW, Side.UpLeft);
        } catch (OccupiedCellException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //        for (Cell c : b) {
        //            System.out.println(c);
        //            assert (c != null);
        //        }
        Tools.showComp(new BoardPanel(null, 20, g.getBoard()));
    }
}