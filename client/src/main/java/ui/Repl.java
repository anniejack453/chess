package ui;

import java.util.Scanner;

public class Repl {
    private ChessClient client;

    public Repl(Integer port) {
        client = new ChessClient(port);
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

}
