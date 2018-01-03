package me.rigelmc.rigelmcmod;

import me.rigelmc.rigelmcmod.util.FUtil;
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
                p.sendMessage(FUtil.colorize(plugin.rm.getRank(player).getColoredTag() + " &7" + player.getName() + ": " + event.getMessage()));
            }
        }
    }

}
