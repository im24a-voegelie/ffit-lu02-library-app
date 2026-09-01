package ch.bzz;

import ch.bzz.command.AppContext;
import ch.bzz.command.Command;
import ch.bzz.command.CommandRegistry;
import ch.bzz.command.HelpCommand;
import ch.bzz.command.ImportBooksCommand;
import ch.bzz.command.ListBooksCommand;
import ch.bzz.command.QuitCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class LibraryAppMain {

    private static final Logger log = LoggerFactory.getLogger(LibraryAppMain.class);

    public static void main(String[] args) {
        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(new ListBooksCommand());
        registry.register(new ImportBooksCommand());
        registry.register(new QuitCommand());

        AppContext context = new AppContext();
        Scanner scanner = new Scanner(System.in);
        log.info("Bibliotheksapplikation gestartet");

        while (context.isRunning() && scanner.hasNextLine()) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }

            String[] parts = input.split("\\s+", 2);
            String name = parts[0];
            String argument = parts.length > 1 ? parts[1].trim() : "";

            Command command = registry.find(name);
            if (command == null) {
                log.debug("Unbekannter Befehl: {}", input);
                System.out.println("Die Eingabe wurde nicht als Befehl erkannt: " + input);
            } else {
                command.execute(context, argument);
            }
        }

        scanner.close();
        log.info("Bibliotheksapplikation beendet");
    }
}
