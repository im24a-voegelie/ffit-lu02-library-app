package ch.bzz.command;

/**
 * Listet alle registrierten Befehle mit ihrer Beschreibung auf.
 * Die Liste muss nicht manuell gepflegt werden, sondern ergibt sich
 * automatisch aus der {@link CommandRegistry}.
 */
public class HelpCommand implements Command {

    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Zeigt alle verfügbaren Befehle an";
    }

    @Override
    public void execute(AppContext context, String argument) {
        System.out.println("Verfügbare Befehle:");
        for (Command command : registry.getCommands()) {
            System.out.println("  " + command.getName() + " - " + command.getDescription());
        }
    }
}
