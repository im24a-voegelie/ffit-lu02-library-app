package ch.bzz.command;

/**
 * Beendet die Eingabeschleife der Anwendung.
 */
public class QuitCommand implements Command {

    @Override
    public String getName() {
        return "quit";
    }

    @Override
    public String getDescription() {
        return "Beendet das Programm";
    }

    @Override
    public void execute(AppContext context) {
        context.stop();
    }
}
