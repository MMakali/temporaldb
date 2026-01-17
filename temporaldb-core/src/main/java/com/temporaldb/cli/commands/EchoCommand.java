package com.temporaldb.cli.commands;

import java.util.Arrays;
import java.util.List;

/**
 * Echo command.
 */
public class EchoCommand implements Command {
    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getDescription() {
        return "Echo text";
    }

    @Override
    public String getUsage() {
        return "echo <text>";
    }

    @Override
    public List<String> getExamples() {
        return Arrays.asList(
                "echo Hello",
                "echo \"Hello World\""
        );
    }


    @Override
    public boolean execute(String[] args) {
        if (args.length == 0) {
            System.out.println();
            return true;
        }

        System.out.println(String.join(" ", args));
        return true;
    }
}
