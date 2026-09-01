package ch.bzz.command;

/**
 * Ein Befehl, der über die Konsole aufgerufen werden kann.
 * Neue Befehle implementieren dieses Interface und werden
 * in der {@link CommandRegistry} registriert.
 */
public interface Command {

    String getName();

    String getDescription();

    void execute(AppContext context);
}
