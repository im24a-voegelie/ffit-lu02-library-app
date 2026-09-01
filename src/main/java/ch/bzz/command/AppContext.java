package ch.bzz.command;

/**
 * Hält den Zustand der laufenden Anwendung, z.B. ob die Eingabeschleife
 * weiterlaufen soll. Befehle erhalten den Kontext, um diesen Zustand
 * zu verändern (z.B. beim Beenden).
 */
public class AppContext {

    private boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }
}
