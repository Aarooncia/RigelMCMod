package me.rigelmc.rigelmcmod.command;

import java.io.File;
import me.rigelmc.rigelmcmod.config.ConfigEntry;
import me.rigelmc.rigelmcmod.rank.Rank;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SENIOR_ADMIN, source = SourceType.ONLY_CONSOLE, blockHostConsole = true)
@CommandParameters(description = "Removes essentials playerdata", usage = "/<command>")
public class Command_wipeuserdata extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!server.getPluginManager().isPluginEnabled(ConfigEntry.SERVER_ESSENTIALS.getString()))
        {
            msg(ConfigEntry.SERVER_ESSENTIALS.getString() + " is not enabled on this server, if you think this is an error contact a developer.");
            return true;
        }

        FUtil.adminAction(sender.getName(), "Wiping Essentials playerdata", true);

        FUtil.deleteFolder(new File(server.getPluginManager().getPlugin(ConfigEntry.SERVER_ESSENTIALS.getString()).getDataFolder(), "userdata"));

        msg("All playerdata deleted.");
        return true;
    }
}
