package com.temporaldb.cli.commands;

import com.temporaldb.TemporalDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for all available commands.
 */
public class CommandRegistry {
    private static final Logger logger = LoggerFactory.getLogger(CommandRegistry.class);

    private final Map<String, Command> commands = new ConcurrentHashMap<>();
    private final TemporalDBServer server;

    /**
     * Create command registry.
     */
    public CommandRegistry(TemporalDBServer server) {
        this.server = server;
        registerCommands();
    }

    /**
     * Register all available commands.
     */
    private void registerCommands() {
        // Connection command
        registerCommand(new StatusCommand(server));

        // Data manipulation commands
        registerCommand(new CreateTableCommand(server));
        registerCommand(new DropTableCommand(server));
        registerCommand(new InsertCommand(server));
        registerCommand(new SelectCommand(server));
        registerCommand(new UpdateCommand(server));
        registerCommand(new DeleteCommand(server));

        // Temporal commands
        registerCommand(new ShowVersionCommand(server));
        registerCommand(new TimeTravelQuery(server));
        registerCommand(new CreateSnapshotCommand(server));
        registerCommand(new ListSnapshotsCommand(server));

        // Index commands
        registerCommand(new CreateIndexCommand(server));
        registerCommand(new DropIndexCommand(server));
        registerCommand(new ListIndexesCommand(server));

        // Replication commands
        registerCommand(new AddReplicaCommand(server));
        registerCommand(new ListReplicasCommand(server));
        registerCommand(new CheckReplicationCommand(server));

        // Backup commands
        registerCommand(new CreateBackupCommand(server));
        registerCommand(new ListBackupsCommand(server));
        registerCommand(new RestoreBackupCommand(server));

        // Schema commands
        registerCommand(new DescribeTableCommand(server));
        registerCommand(new ListTablesCommand(server));
        registerCommand(new ShowColumnsCommand(server));

        // Monitoring commands
        registerCommand(new ShowMetricsCommand(server));
        registerCommand(new ShowHealthCommand(server));
        registerCommand(new ShowStatsCommand(server));

        // Utility commands
        registerCommand(new HelpCommand());
        registerCommand(new ExitCommand());
        registerCommand(new ClearCommand());
        registerCommand(new EchoCommand());

        logger.info("Registered {} commands", commands.size());
    }

    /**
     * Register a command.
     */
    public void registerCommand(Command command) {
        commands.put(command.getName().toLowerCase(), command);
        logger.debug("Registered command: {}", command.getName());
    }

    /**
     * Get command by name.
     */
    public Command getCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    /**
     * Get all commands.
     */
    public List<Command> getAllCommands() {
        List<Command> cmds = new ArrayList<>(commands.values());
        cmds.sort(Comparator.comparing(Command::getName));
        return cmds;
    }

    /**
     * Get command count.
     */
    public int getCommandCount() {
        return commands.size();
    }
}
