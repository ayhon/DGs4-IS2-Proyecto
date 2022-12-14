package network.commands;

import network.models.Room;
import org.json.JSONObject;

public class RoomInfoCommand extends Command {
    private Room room;

    public RoomInfoCommand() {
        super("ROOM_INFO");
    }

    public RoomInfoCommand(Room _room) {
        this();
        this.room = _room;
    }

    @Override
    public JSONObject getData() {
        return this.room.toJSON();
    }

    @Override
    public void parseRequest(JSONObject req) {
        /// Esta petición solamente se manda del servidor al cliente
        /// Con la información de la habitación (modo del juego, jugadores conectados, etc.)
        this.room = new Room(req.getJSONObject("data"));
    }

    public Room getRoom() {
        return room;
    }
}
