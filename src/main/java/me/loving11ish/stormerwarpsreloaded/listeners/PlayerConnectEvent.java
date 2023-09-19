package me.loving11ish.stormerwarpsreloaded.listeners;

import me.loving11ish.stormerwarpsreloaded.StormerWarpsReloaded;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerConnectEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (StormerWarpsReloaded.getFloodgateApi() != null){
            if (StormerWarpsReloaded.getFloodgateApi().isFloodgatePlayer(uuid)){
                StormerWarpsReloaded.i.addToBedrockPlayers(uuid, player.getName());
            }
        }
    }
}
