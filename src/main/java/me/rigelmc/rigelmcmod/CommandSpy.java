package me.rigelmc.rigelmcmod;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpy extends FreedomService
{

    public CommandSpy(RigelMCMod plugin)
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        for (Player p : server.getOnlinePlayers())
        {
            if (plugin.al.isAdmin(p) && plugin.pl.getPlayer(p).cmdspyEnabled()
                    && plugin.rm.getRank(p).isHigher(plugin.rm.getRank(player)))
            {
                p.sendMessage(plugin.rm.getRank(player).getColoredTag() + ChatColor.GRAY + " " + player.getName() + ": " + event.getMessage());
            }
        }
    }

}
