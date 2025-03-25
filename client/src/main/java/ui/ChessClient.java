package ui;

public class ChessClient {
    private final ServerFacade server;
    private State state = State.PRELOGIN;

    public ChessClient(Integer port) {
        server = new ServerFacade(port);
    }

    public String help() {
        if (state == State.PRELOGIN) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL> - create account
                    - login <USERNAME> <PASSWORD>
                    - help - list possible options
                    - quit - exit program
                    """;
        } else if (state == State.POSTLOGIN) {
            return """
                    - create <NAME> - create a game
                    - join <ID> [WHITE|BLACK] - join a game
                    - list - list games
                    - observe <ID> - watch a game
                    - logout
                    - help - list possible options
                    - quit - exit program
                    """;
        }
        return """
                
                """;
    }
}
