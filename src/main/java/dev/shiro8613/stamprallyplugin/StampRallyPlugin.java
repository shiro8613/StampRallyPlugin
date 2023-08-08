package dev.shiro8613.stamprallyplugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.shiro8613.stamprallyplugin.database.Database;
import dev.shiro8613.stamprallyplugin.database.DriverType;
import dev.shiro8613.stamprallyplugin.database.entry.Config;
import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import dev.shiro8613.stamprallyplugin.map.CustomMapRenderer;
import dev.shiro8613.stamprallyplugin.map.MapManager;
import dev.shiro8613.stamprallyplugin.memory.DataStore;
import dev.shiro8613.stamprallyplugin.utils.DetectItem;
import dev.shiro8613.stamprallyplugin.utils.HandItem;
import dev.shiro8613.stamprallyplugin.utils.json.StampData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

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

        DataStore.Init();
        DataStore.setConfig(new Config(configuration.getInt("config.radius")));

        LoadImages();
        CustomMapRenderer.Init();
        CustomMapRenderer.LoadRenderer();

        BackGround.Init(this);

        new CommandAPICommand("srp")
                .withSubcommand(new CommandAPICommand("gg")
                        .withPermission("srp.gift")
                        .executesPlayer((player, commandArguments) -> {
                            int mapId = HandItem.getMapId(player.getInventory());
                            if(DataStore.getMapStamp().containsKey(mapId)) {
                                String data = DataStore.getMapStamp().get(mapId);
                                Map<Integer, Boolean> map = StampData.DecodeStamps(data);
                                if (Objects.nonNull(map) && !map.containsValue(false)) {
                                    player.getInventory().addItem(new ItemStack(Material.DIAMOND)); //渡すものを記述
                                    player.sendMessage(Component.text("景品を付与しました。", NamedTextColor.AQUA));
                                } else {
                                    player.sendMessage(Component.text("まだスタンプが埋まっていません！", NamedTextColor.RED));
                                }
                            } else {
                                player.sendMessage(Component.text("カードを手に持って実行してください！　または対応していないマップ。", NamedTextColor.RED));
                            }
                        }))
                .withSubcommand(new CommandAPICommand("players")
                        .withPermission("srp.manage")
                        .executes((commandSender, commandArguments) -> {
                            commandSender.sendMessage("---List---");
                            BackGround.getHasPlayerMap().forEach(hasPlayer ->
                                    commandSender.sendMessage("["+ hasPlayer.mapId + "] " + hasPlayer.player.getName()));
                            commandSender.sendMessage("----------");
                        }))
                .withSubcommand(new CommandAPICommand("delete")
                        .withPermission("srp.manage")
                        .withArguments(new IntegerArgument("mapId"))
                        .executes((commandSender, commandArguments) -> {
                            Integer mapId = (Integer) commandArguments.get(0);
                            if (Objects.nonNull(mapId) && DataStore.getMapStamp().containsKey(mapId)) {
                                if (database.getConn().deleteMapStamp(mapId)) {
                                    commandSender.sendMessage("deleted map #" + mapId);
                                    BackGround.getHasPlayerMap().forEach(hp -> {
                                        if (hp.mapId == mapId) {
                                            PlayerInventory inventory = hp.player.getInventory();
                                            DetectItem.StackId stackId = DetectItem.search(inventory);
                                            if (stackId == null) return;
                                            inventory.remove(stackId.item);
                                        }
                                    });
                                    DataStore.LoadMapStampData();
                                    CustomMapRenderer.ReloadRenderer(mapId);
                                } else {
                                    commandSender.sendMessage("internal server error");
                                }
                            } else {
                                commandSender.sendMessage("map is not used");
                            }
                        }))
                .withSubcommand(new CommandAPICommand("create")
                        .withPermission("srp.manage")
                        .withSubcommand(new CommandAPICommand("new")
                                .withArguments(new EntitySelectorArgument.ManyPlayers("player"))
                                .executes((commandSender, commandArguments) -> {
                                    @SuppressWarnings("unchecked")
                                    Collection<Player> players = (Collection<Player>) commandArguments.get(0);
                                    if (players != null) {
                                        players.forEach(player -> {
                                            ItemStack itemStack = MapManager.CreateItem(player.getWorld());
                                            player.getInventory().addItem(itemStack);
                                        });
                                    } else {
                                        commandSender.sendMessage("Not Player");
                                    }
                                }))
                        .withSubcommand(new CommandAPICommand("get")
                                .withArguments(new IntegerArgument("id"))
                                .executesPlayer((commandSender, commandArguments) -> {
                                    Integer mapId = (Integer) commandArguments.get(0);
                                    if (Objects.nonNull(mapId) && DataStore.getMapStamp().containsKey(mapId)) {
                                        ItemStack itemStack = MapManager.GetItem(mapId);
                                        if(itemStack != null) {
                                            commandSender.getInventory().addItem(itemStack);
                                        } else {
                                            commandSender.sendMessage("MapId is not link item");
                                        }
                                    } else {
                                        commandSender.sendMessage("MapId is not used");
                                    }
                                })))
                .withSubcommand(new CommandAPICommand("config")
                        .withPermission("srp.manage")
                        .withSubcommand(new CommandAPICommand("pos")
                                .withSubcommand(new CommandAPICommand("gets")
                                        .executes((commandSender, commandArguments) -> {
                                            List<StampLocation> list = database.getConn().getStampLocation();
                                            if (list != null) {
                                                commandSender.sendMessage("---List---");
                                                list.forEach(l ->
                                                    commandSender.sendMessage("[" + l.StampId + "] "+ l.WorldName + "," + l.PosX + "," + l.PosY + "," + l.PosZ)
                                                );
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
                                            Integer stampId = (Integer) commandArguments.get(0);
                                            if (Objects.nonNull(stampId) && 0 < stampId && stampId < 10) {
                                                Location location = (Location) commandArguments.get(1);
                                                assert location != null;
                                                if (database.getConn().setStampLocation(stampId, location)) {
                                                    commandSender.sendMessage("Add " + stampId + " Pos -> " + location);
                                                    DataStore.LoadLocationsData();
                                                } else {
                                                    commandSender.sendMessage("Internal server error");
                                                }
                                            } else {
                                                commandSender.sendMessage("stampId is range 1-9");
                                            }
                                        }))))
                .register();

        getServer().getPluginManager().registerEvents(new Event(), this);
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

    private void LoadImages() {
        File dataFolder = getDataFolder();
        if(dataFolder.mkdirs()) getLogger().info("Created Folder");
        String path = dataFolder.getPath() + File.separator + "images";
        File imagesFolder = new File(path);
        if(imagesFolder.mkdirs()) getLogger().info("Create images Folder");
        String[] list = imagesFolder.list();

        assert list != null;
        DataStore.LoadImages(list, path);

    }

}
