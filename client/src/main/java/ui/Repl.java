package ui;

public class Repl {
    private ChessClient client;

    public Repl(Integer port) {
        client = new ChessClient(port);
    }

    public void run() {
        System.out.println("Welcome to the Chess Website.");
        System.out.print(client.help());
    }
}
