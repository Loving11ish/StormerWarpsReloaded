package me.loving11ish.stormerwarpsreloaded.listeners;

import me.loving11ish.stormerwarpsreloaded.StormerWarpsReloaded;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerDisconnectEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (StormerWarpsReloaded.getFloodgateApi() != null){
            if (StormerWarpsReloaded.i.getBedrockPlayers().containsKey(uuid)){
                StormerWarpsReloaded.i.removeFromBedrockPlayers(uuid);
            }
        }
    }
}
