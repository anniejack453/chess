package ui;

import chess.ChessGame;
import websocket.ServerMessageHandler;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements ServerMessageHandler {
    private ChessClient client;

    public Repl(Integer port){
        client = new ChessClient(port, this);
    }

    public void run() {
        System.out.println("Welcome to the Chess Website.");
        System.out.print(client.help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    @Override
    public void notify(ServerMessage message) { //TODO: make message formats
        if (message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            var loadGameMessage = (LoadGameMessage) message;
            var game = loadGameMessage.game();
            new PrintBoard(game,((LoadGameMessage) message).getTeamColor());
        }
    }
}
