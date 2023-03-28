package me.loving11ish.stormerwarpsreloaded;

import me.loving11ish.stormerwarpsreloaded.commands.WarpCommand;
import me.loving11ish.stormerwarpsreloaded.commands.WarpTabCompleter;
import me.loving11ish.stormerwarpsreloaded.files.MessagesFileManager;
import me.loving11ish.stormerwarpsreloaded.listeners.PlayerConnectEvent;
import me.loving11ish.stormerwarpsreloaded.listeners.PlayerDisconnectEvent;
import me.loving11ish.stormerwarpsreloaded.models.Warp;
import me.loving11ish.stormerwarpsreloaded.utils.ColorUtils;
import me.loving11ish.stormerwarpsreloaded.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.*;
import java.util.logging.Logger;

public final class StormerWarpsReloaded extends JavaPlugin {

    public static StormerWarpsReloaded i;
    private static FloodgateApi floodgateApi;
    public MessagesFileManager messagesFileManager;
    Logger logger = this.getLogger();

    private HashMap<UUID, String> bedrockPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        i = this;

        //Load main plugin config
        this.loadConfig();

        //Load messages.yml
        this.messagesFileManager = new MessagesFileManager();
        messagesFileManager.MessagesFileManager(this);

        //Register commands
        this.getCommand("warp").setExecutor(new WarpCommand());
        this.getCommand("warp").setTabCompleter(new WarpTabCompleter());
        this.getCommand("setwarp").setExecutor(new WarpCommand());
        this.getCommand("setwarp").setTabCompleter(new WarpTabCompleter());
        this.getCommand("delwarp").setExecutor(new WarpCommand());
        this.getCommand("delwarp").setTabCompleter(new WarpTabCompleter());
        this.getCommand("warps").setExecutor(new WarpCommand());
        this.getCommand("swreload").setExecutor(new WarpCommand());

        //Register events
        this.getServer().getPluginManager().registerEvents(new PlayerConnectEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDisconnectEvent(), this);

        //Load warps
        this.reload();

        //Register FloodgateApi hooks
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("floodgate")){
            floodgateApi = FloodgateApi.getInstance();
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &3FloodgateApi found!"));
            logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &3Full Bedrock support enabled!"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        }else {
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &3FloodgateApi not found!"));
            logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &3Bedrock support may not function!"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    public void reload() {
        Warp.all.clear();
        Set<String> keys = this.getConfig().getKeys(true);
        Set<String> warps = new HashSet();
        Iterator var4 = keys.iterator();

        String warp;
        while(var4.hasNext()) {
            warp = (String)var4.next();
            String[] args = warp.split("\\.");
            if (args.length > 1 && args[0].equalsIgnoreCase("warps") && !warps.contains(args[1])) {
                warps.add(args[1]);
            }
        }

        var4 = warps.iterator();

        while(var4.hasNext()) {
            warp = (String)var4.next();
            String path = "warps." + warp + ".";
            Message.systemNormal(messagesFileManager.getMessagesConfig().getString("warp-load-attempt").replace("%PATH%", path));
            String sx = this.getConfig().getString(path + "x");
            String sy = this.getConfig().getString(path + "y");
            String sz = this.getConfig().getString(path + "z");
            String syaw = this.getConfig().getString(path + "yaw");
            String spitch = this.getConfig().getString(path + "pitch");
            String sworld = this.getConfig().getString(path + "world");
            String sOpOnly = this.getConfig().getString(path + "opOnly");

            double x;
            try {
                x = Double.parseDouble(sx);
            } catch (Exception var30) {
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-error-string").replace("%PATH%", path));
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-warp-x"));
                continue;
            }

            double y;
            try {
                y = Double.parseDouble(sy);
            } catch (Exception var29) {
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-error-string").replace("%PATH%", path));
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-warp-y"));
                continue;
            }

            double z;
            try {
                z = Double.parseDouble(sz);
            } catch (Exception var28) {
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-error-string").replace("%PATH%", path));
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-warp-z"));
                continue;
            }

            float yaw;
            try {
                yaw = Float.parseFloat(syaw);
            } catch (Exception var27) {
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-error-string").replace("%PATH%", path));
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-warp-yaw"));
                continue;
            }

            float pitch;
            try {
                pitch = Float.parseFloat(spitch);
            } catch (Exception var26) {
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-error-string").replace("%PATH%", path));
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-warp-pitch"));
                continue;
            }

            boolean opOnly;
            try {
                opOnly = Boolean.parseBoolean(sOpOnly);
            } catch (Exception var25) {
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-error-string").replace("%PATH%", path));
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-warp-opOnly"));
                continue;
            }

            try {
                World world = Bukkit.getWorld(sworld);
                if (world == null) {
                    Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-error-string-world").replace("%PATH%", path));
                } else {
                    Warp h = new Warp(new Location(world, x, y, z, yaw, pitch), warp, opOnly);
                    Message.systemNormal(messagesFileManager.getMessagesConfig().getString("warp-created-success"));
                    Message.systemNormal(h.toString());
                }
            } catch (Exception var24) {
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-error-string").replace("%PATH%", path));
                Message.systemError(messagesFileManager.getMessagesConfig().getString("invalid-warp-name"));
            }
        }
    }

    public HashMap<UUID, String> getBedrockPlayers() {
        return bedrockPlayers;
    }

    public void setBedrockPlayers(HashMap<UUID, String> bedrockPlayers) {
        this.bedrockPlayers = bedrockPlayers;
    }

    public void addToBedrockPlayers(UUID uuid, String playerName){
        this.bedrockPlayers.put(uuid, playerName);
    }

    public void removeFromBedrockPlayers(UUID uuid){
        this.bedrockPlayers.remove(uuid);
    }

    public static FloodgateApi getFloodgateApi() {
        return floodgateApi;
    }
}
