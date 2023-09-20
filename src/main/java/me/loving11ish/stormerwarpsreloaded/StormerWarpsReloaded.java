package me.loving11ish.stormerwarpsreloaded;

import com.rylinaux.plugman.api.PlugManAPI;
import com.tcoded.folialib.FoliaLib;
import io.papermc.lib.PaperLib;
import me.loving11ish.stormerwarpsreloaded.commands.WarpCommand;
import me.loving11ish.stormerwarpsreloaded.commands.WarpTabCompleter;
import me.loving11ish.stormerwarpsreloaded.files.MessagesFileManager;
import me.loving11ish.stormerwarpsreloaded.models.Warp;
import me.loving11ish.stormerwarpsreloaded.utils.ColorUtils;
import me.loving11ish.stormerwarpsreloaded.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public final class StormerWarpsReloaded extends JavaPlugin {

    public static StormerWarpsReloaded i;
    public MessagesFileManager messagesFileManager;
    private FoliaLib foliaLib = new FoliaLib(this);
    Logger logger = this.getLogger();

    @Override
    public void onEnable() {
        // Plugin startup logic
        i = this;

        //Suggest PaperMC if not using
        if (foliaLib.isUnsupported()||foliaLib.isSpigot()){
            PaperLib.suggestPaper(this);
        }

        //Check if PlugManX is enabled
        if (isPlugManXEnabled()) {
            if (!PlugManAPI.iDoNotWantToBeUnOrReloaded("StormerWarpsReloaded")) {
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&4WARNING WARNING WARNING WARNING!"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &4You appear to be using an unsupported version of &d&lPlugManX"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &4Please &4&lDO NOT USE PLUGMANX TO LOAD/UNLOAD/RELOAD THIS PLUGIN!"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &4Please &4&lFULLY RESTART YOUR SERVER!"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &4This plugin &4&lHAS NOT &4been validated to use this version of PlugManX!"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &4&lNo official support will be given to you if you use this!"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &4&lUnless Loving11ish has explicitly agreed to help!"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &4Please add StormerWarpsReloaded to the ignored-plugins list in PlugManX's config.yml"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &6Continuing plugin startup"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
            }else {
                logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
                logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &aSuccessfully hooked into PlugManX"));
                logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &aSuccessfully added StormerWarpsReloaded to ignoredPlugins list."));
                logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &6Continuing plugin startup"));
                logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            }
        }else {
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &cPlugManX not found!"));
            logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &cDisabling PlugManX hook loader"));
            logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &6Continuing plugin startup"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

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

        //Load warps
        this.reload();

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

    public boolean isPlugManXEnabled() {
        try {
            Class.forName("com.rylinaux.plugman.PlugMan");
            logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &aFound PlugManX main class at:"));
            logger.info(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &dcom.rylinaux.plugman.PlugMan"));
            return true;
        } catch (ClassNotFoundException e) {
            logger.warning(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &aCould not find PlugManX main class at:"));
            logger.warning(ColorUtils.translateColorCodes("&6StormerWarpsReloaded: &dcom.rylinaux.plugman.PlugMan"));
            return false;
        }
    }
}
