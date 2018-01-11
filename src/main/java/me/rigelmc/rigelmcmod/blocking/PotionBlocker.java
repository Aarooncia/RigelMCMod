package me.rigelmc.rigelmcmod.blocking;

import java.util.Collection;
import me.rigelmc.rigelmcmod.FreedomService;
import me.rigelmc.rigelmcmod.RigelMCMod;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionBlocker extends FreedomService
{

    public static final int POTION_BLOCK_RADIUS_SQUARED = 20 * 20;

    public PotionBlocker(RigelMCMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onThrowPotion(PotionSplashEvent event)
    {
        Player player = null;

        if (event.getEntity().getShooter() instanceof Player)
        {
            player = (Player) event.getEntity().getShooter();
        }

        if (player == null)
        {
            return;
        }

        if (isDeathPotion(event.getEntity().getEffects()))
        {
            player.sendMessage(ChatColor.RED + "You may not use death potions!");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onThrowLingeringPotion(LingeringPotionSplashEvent event)
    {
        Player player = null;

        if (event.getEntity().getShooter() instanceof Player)
        {
            player = (Player) event.getEntity().getShooter();
        }

        if (player == null)
        {
            return;
        }

        if (isDeathPotion(event.getEntity().getEffects()))
        {
            player.sendMessage(ChatColor.RED + "You may not use death potions!");
            event.setCancelled(true);
        }
    }

    public boolean isDeathPotion(Collection<PotionEffect> effects)
    {
        for (PotionEffect effect : effects)
        {
            if (effect.getType().equals(PotionEffectType.HEAL) && effect.getAmplifier() == 125)
            {
                return true;
            }
        }
        return false;
    }
}
