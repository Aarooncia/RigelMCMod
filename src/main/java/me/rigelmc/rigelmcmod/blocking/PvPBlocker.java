package me.rigelmc.rigelmcmod.blocking;

import me.rigelmc.rigelmcmod.FreedomService;
import me.rigelmc.rigelmcmod.RigelMCMod;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPBlocker extends FreedomService
{

    public PvPBlocker(RigelMCMod plugin)
    {
        super(plugin);
    }

    @Override
    public void onStart()
    {
    }

    @Override
    public void onStop()
    {
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        final Entity damager = event.getDamager();
        if (damager instanceof Player && event.getEntity() instanceof Player)
        {
            Player player = (Player) damager;

            if (player.getGameMode() == GameMode.CREATIVE || plugin.esb.getEssentialsUser(player.getName()).isGodModeEnabled())
            {
                player.sendMessage(ChatColor.RED + "You may not GMC/God PvP!");
                event.setCancelled(true);
            }
        }

        if (damager instanceof Projectile && event.getEntity() instanceof Player)
        {
            Player player = (Player) ((Projectile) damager).getShooter();

            if (player.getGameMode() == GameMode.CREATIVE || plugin.esb.getEssentialsUser(player.getName()).isGodModeEnabled())
            {
                player.sendMessage(ChatColor.RED + "You may not GMC/God PvP!");
                event.setCancelled(true);
            }
        }
    }
}
