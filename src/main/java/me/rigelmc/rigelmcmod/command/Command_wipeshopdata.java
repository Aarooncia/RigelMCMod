package me.rigelmc.rigelmcmod.command;

import java.io.File;
import me.rigelmc.rigelmcmod.RigelMCMod;
import me.rigelmc.rigelmcmod.rank.Rank;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SENIOR_ADMIN, source = SourceType.ONLY_CONSOLE, blockHostConsole = true)
@CommandParameters(description = "Removes the shopdata", usage = "/<command>")
public class Command_wipeshopdata extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {

        FUtil.adminAction(sender.getName(), "Wiping the Shop Data", true);

        FUtil.deleteFolder(new File(RigelMCMod.plugin().getDataFolder(), "shopdata"));

        msg("All shopdata has been deleted.");
        return true;
    }
}