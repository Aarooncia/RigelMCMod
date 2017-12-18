package me.rigelmc.rigelmcmod.command;

import me.rigelmc.rigelmcmod.config.ConfigEntry;
import me.rigelmc.rigelmcmod.rank.Rank;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Goto the master builder world.", usage = "/<command>", aliases = "mbworld,mbw")
public class Command_masterbuilderworld extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (FUtil.isMasterBuilder(sender.getName()) || FUtil.isExecutive(sender.getName()) || plugin.al.isAdmin(sender))
        {
            if (ConfigEntry.FLATLANDS_GENERATE.getBoolean())
            {
                plugin.wm.masterbuilderworld.sendToWorld(playerSender);
                return true;
            }
            else
            {
                msg("MasterBuilderWorld is currently disabled.");
                return true;
            }
        }
        return true;
    }
}
