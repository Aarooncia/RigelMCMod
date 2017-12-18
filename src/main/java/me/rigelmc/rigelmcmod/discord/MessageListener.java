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
                    Discord.sendMessage(event.getChannel(), "The linking process has been succesful. This discord account is now linked with `" + admin.getName() + "`.");
                    Discord.sendMessage(event.getChannel(), "If you show up as an impostor, you may use `/verify`.");
                    Player player = RigelMCMod.plugin().getServer().getPlayer(admin.getName());
                    player.sendMessage(ChatColor.GREEN + "Successfully linked " + event.getMessage().getAuthor().getName()
                            + "#" + event.getMessage().getAuthor().getDiscriminator()
                            + " to this account!");
                }
            }
        }
    }
}
