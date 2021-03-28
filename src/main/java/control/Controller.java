package control;

import control.options.Option;
import control.options.OptionGenerator;
import logic.Game;

import java.util.Scanner;

public class Controller {
    private Game game;
    private Scanner scanner;

    public Controller(Game game, Scanner scanner) {
        this.game = game;
        this.scanner = scanner;
    }

    public void printGame() {
        System.out.println(this.game);
    }

    public void run() {
        boolean refreshDisplay = true;

        while (!game.isFinished()) {
            if (refreshDisplay) printGame();
            refreshDisplay = false;

            OptionGenerator.printOptions();

            try {
                Option option = OptionGenerator.parse(scanner);
                System.out.format("[DEBUG]: Se ha seleccionado la opción: [%s] \n\n", option.title);
                refreshDisplay = option.execute(game, scanner);
                if (refreshDisplay) game.advance();
            } catch (Exception ex) {
                System.out.format(ex.getMessage() + "%n%n");
            }
        }

        if (refreshDisplay) printGame();
        System.out.println("[GAME OVER]");
    }
}
