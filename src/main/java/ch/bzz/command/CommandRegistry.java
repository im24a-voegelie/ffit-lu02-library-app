package ch.bzz.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Verwaltet alle bekannten Befehle. Neue Befehle müssen hier registriert
 * werden, damit sie über die Konsole aufgerufen werden können und
 * automatisch in der Ausgabe von "help" erscheinen.
 */
public class CommandRegistry {

    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    public Command find(String name) {
        return commands.get(name);
    }

    public Collection<Command> getCommands() {
        return commands.values();
    }
}
