package me.rigelmc.rigelmcmod.discord;

import me.rigelmc.rigelmcmod.RigelMCMod;
import me.rigelmc.rigelmcmod.admin.Admin;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter
{
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event)
    {
        if (!event.getAuthor().getId().equals(RigelMCMod.plugin().dc.bot.getSelfUser().getId()))
        {
            
            // Handle link code
            if (event.getMessage().getRawContent().matches("[0-9][0-9][0-9][0-9][0-9]"))
            {
                String code = event.getMessage().getRawContent();
                if (RigelMCMod.plugin().dc.LINK_CODES.get(code) != null)
                {
                    Admin admin = RigelMCMod.plugin().dc.LINK_CODES.get(code);
                    admin.setDiscordID(event.getMessage().getAuthor().getId());
                    RigelMCMod.plugin().dc.LINK_CODES.remove(code);
                    RigelMCMod.plugin().dc.sendMessage(event.getChannel(), "Link successful. Now this Discord account is linked with the Minecraft account `" + admin.getName() + "`.");
                    RigelMCMod.plugin().dc.sendMessage(event.getChannel(), "Now when you are an impostor on the server you may now use `/verify` to verify.");
                }
            }
        }
    }
}