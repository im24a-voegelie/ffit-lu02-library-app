package ch.bzz.command;

/**
 * Ein Befehl, der über die Konsole aufgerufen werden kann.
 * Neue Befehle implementieren dieses Interface und werden
 * in der {@link CommandRegistry} registriert.
 */
public interface Command {

    String getName();

    String getDescription();

    /**
     * Führt den Befehl aus.
     *
     * @param context  Zustand der Anwendung
     * @param argument alles, was hinter dem Befehlsnamen eingegeben wurde
     *                 (z.B. ein Dateipfad); leer, wenn kein Argument angegeben wurde
     */
    void execute(AppContext context, String argument);
}
