package me.rigelmc.rigelmcmod.discord;

import me.rigelmc.rigelmcmod.RigelMCMod;
import me.rigelmc.rigelmcmod.admin.Admin;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageListener extends ListenerAdapter
{
    
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event)
    {
        if (!event.getAuthor().getId().equals(Discord.bot.getSelfUser().getId()))
        {

            // Handle link code
            if (event.getMessage().getRawContent().matches("[0-9][0-9][0-9][0-9][0-9]"))
            {
                String code = event.getMessage().getRawContent();
                if (Discord.LINK_CODES.get(code) != null)
                {
                    Admin admin = Discord.LINK_CODES.get(code);
                    admin.setDiscordID(event.getMessage().getAuthor().getId());
                    Discord.LINK_CODES.remove(code);
                    Discord.sendMessage(event.getChannel(), "Link successful. Now this Discord account is linked with the Minecraft account `" + admin.getName() + "`.");
                    Discord.sendMessage(event.getChannel(), "Now when you are an impostor on the server you may now use `/verify` to verify.");
                    Player player = RigelMCMod.plugin().getServer().getPlayer(admin.getName());
                    player.sendMessage(ChatColor.GREEN + "Succesfully linked your Discord account to this account!");
                }
            }
        }
    }
}
