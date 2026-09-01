package ch.bzz;

import ch.bzz.command.AppContext;
import ch.bzz.command.Command;
import ch.bzz.command.CommandRegistry;
import ch.bzz.command.HelpCommand;
import ch.bzz.command.QuitCommand;

import java.util.Scanner;

public class LibraryAppMain {

    public static void main(String[] args) {
        CommandRegistry registry = new CommandRegistry();
        registry.register(new HelpCommand(registry));
        registry.register(new QuitCommand());

        AppContext context = new AppContext();
        Scanner scanner = new Scanner(System.in);

        while (context.isRunning()) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            Command command = registry.find(input);
            if (command == null) {
                System.out.println("Die Eingabe wurde nicht als Befehl erkannt: " + input);
            } else {
                command.execute(context);
            }
        }

        scanner.close();
    }
}
