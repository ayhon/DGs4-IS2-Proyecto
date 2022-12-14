package graphic;

import control.Controller;
import java.awt.*;
import java.util.HashSet;
import javax.swing.*;
import logic.Cell;
import logic.Game;
import network.client.SocketClient;
import network.client.SocketObserver;
import network.commands.Command;
import network.commands.CommandParser;
import network.commands.PieceMovedCommand;
import network.commands.SurrenderCommand;
import network.models.Room;
import org.json.JSONObject;

public class OnlineGameWindow extends JPanel implements SocketObserver, GameObserver {
    private final Controller ctrl;
    private final SocketClient sc;
    private final Room room;
    private final String roomID;
    private final HashSet<String> localPlayers = new HashSet<>();

    private BoardPanel boardPanel;
    private OptionsPanel optionsPanel;
    private MainWindow parent;

    private Command pieceMovedCommand = new PieceMovedCommand() {

        @Override
        public void execute(JSONObject req, SocketClient connection) {
            super.execute(req, connection);
            try {
                ctrl.onlineMovePiece(x1, y1, x2, y2, playerID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Command surrenderCommand = new SurrenderCommand() {

        @Override
        public void execute(JSONObject req, SocketClient connection) {
            super.execute(req, connection);
            ctrl.surrender(getPlayerID());
        }
    };

    private CommandParser commandParser = new CommandParser() {

        @Override
        public Command[] getCommands() {
            return new Command[] { pieceMovedCommand, surrenderCommand };
        }
    };

    OnlineGameWindow(
        Controller _ctrl,
        SocketClient _sc,
        String _roomID,
        Room _room,
        MainWindow parent
    ) {
        super();
        this.sc = _sc;
        this.ctrl = _ctrl;
        this.roomID = _roomID;
        this.room = _room;
        this.parent = parent;
    }

    public void start() {
        this.initGUI();
        this.sc.addObserver(this);
        this.ctrl.addObserver(this);
    }

    protected void initGUI() {
        try {
            boardPanel = new BoardPanel(ctrl);
            optionsPanel = new OptionsPanel(ctrl);
            add(boardPanel, BorderLayout.LINE_START);
            add(optionsPanel, BorderLayout.LINE_END);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLocalPlayer(String ID) {
        this.localPlayers.add(ID);
    }

    @Override
    public void onMessage(JSONObject s) {
        String type = s.getString("type");
        try {
            Command command = commandParser.parse(type);
            command.execute(s, this.sc);
        } catch (Exception e) {}
    }

    @Override
    public void onMovedPiece(Cell from, Cell to, String playerID) {
        /// Es un movimiento hecho por un jugador local
        if (localPlayers.contains(playerID)) {
            new PieceMovedCommand(
                from.getRow(),
                from.getCol(),
                to.getRow(),
                to.getCol(),
                this.roomID,
                playerID
            )
            .send(this.sc);
        }
    }

    @Override
    public void onSurrendered(Game game, String playerID) {
        if (this.localPlayers.contains(playerID)) {
            new SurrenderCommand(playerID, roomID).send(this.sc);
        }
    }

    @Override
    public void onGameEnded(Game game) {
        this.parent.setEnabled(true);
    }

    public boolean canMove(String playerID) {
        return this.localPlayers.contains(playerID);
    }

    @Override
    public void onRegister(Game game) {
        String playerID = game.getCurrentPlayer().getId();
        setBlocker(playerID);
    }

    @Override
    public void onEndTurn(Game game) {
        String playerID = game.getCurrentPlayer().getId();
        if (!game.isFinished()) setBlocker(playerID);
    }

    private void setBlocker(String playerID) {
        if (this.canMove(playerID)) {
            this.parent.setEnabled(true);
        } else {
            this.parent.setEnabled(false);
        }
    }
}
