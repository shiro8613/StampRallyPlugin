package dev.shiro8613.stamprallyplugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.shiro8613.stamprallyplugin.database.Database;
import dev.shiro8613.stamprallyplugin.database.DriverType;
import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class StampRallyPlugin extends JavaPlugin {

    private Database database;
    private static JavaPlugin instance;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        FileConfiguration configuration = getConfig();
        String type = configuration.getString("database.type");
        DriverType driverType = Objects.equals(type, "mysql") ? DriverType.MYSQL : DriverType.SQLITE;

        try {
            database = new Database(driverType,
                    configuration.getString("database.host"),
                    configuration.getString("database.user"), configuration.getString("database.database"),
                    configuration.getString("database.password"), configuration.getInt("database.port")
            );
            database.openConnection();
            database.getConn().CreateTable();

        } catch (SQLException e) {
            getLogger().warning(e.getMessage());
        } catch (ClassNotFoundException e) {
            getServer().getPluginManager().disablePlugin(this);
            throw new RuntimeException(e);
        }


        new CommandAPICommand("srp")
                .withSubcommand(new CommandAPICommand("create")
                        .withSubcommand(new CommandAPICommand("new")
                                .withArguments(new EntitySelectorArgument.ManyPlayers("player"))
                                .executes((commandSender, commandArguments) -> {
                                    //create new
                                }))
                        .withSubcommand(new CommandAPICommand("get")
                                .withArguments(new IntegerArgument("id"))
                                .executesPlayer((commandSender, commandArguments) -> {
                                    //create get
                                })))
                .withSubcommand(new CommandAPICommand("config")
                        .withSubcommand(new CommandAPICommand("pos")
                                .withSubcommand(new CommandAPICommand("gets")
                                        .executes((commandSender, commandArguments) -> {
                                            List<StampLocation> list = database.getConn().getStampLocation();
                                            if (list != null) {
                                                commandSender.sendMessage("---List---");
                                                list.forEach(l -> {
                                                    commandSender.sendMessage("[" + l.StampId + "] "+ l.WorldName + "," + l.PosX + "," + l.PosY + "," + l.PosZ);
                                                });
                                                commandSender.sendMessage("----------");
                                            } else {
                                                commandSender.sendMessage("Empty");
                                            }
                                        }))
                                .withSubcommand(new CommandAPICommand("set")
                                        .withArguments(Arrays.asList(new IntegerArgument("stampId"),
                                                                     new LocationArgument("position")))
                                        .executes((commandSender, commandArguments) -> {
                                            if (commandArguments.count() > 2) return;
                                            int stampId = (int) commandArguments.get(0);
                                            if (0 < stampId && stampId < 10) {
                                                Location location = (Location) commandArguments.get(1);
                                                assert location != null;
                                                if (database.getConn().setStampLocation(stampId, location)) {
                                                    commandSender.sendMessage("Add " + stampId + " Pos -> " + location);
                                                } else {
                                                    commandSender.sendMessage("Internal server error");
                                                }
                                            } else {
                                                commandSender.sendMessage("stampId is range 1-9");
                                            }
                                        })))
                        .withSubcommand(new CommandAPICommand("radius")
                                .withSubcommand(new CommandAPICommand("set")
                                        .withArguments(new IntegerArgument("radius"))
                                        .executes((commandSender, commandArguments) -> {
                                            //radius set
                                        }))
                                .withSubcommand(new CommandAPICommand("get")
                                        .executes((commandSender, commandArguments) -> {
                                            //radius get
                                        }))))
                .register();
    }

    @Override
    public void onDisable() {
        try {
            database.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

}
