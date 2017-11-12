package me.rigelmc.rigelmcmod.httpd.module;

import me.rigelmc.rigelmcmod.RigelMCMod;
import me.rigelmc.rigelmcmod.admin.Admin;
import me.rigelmc.rigelmcmod.config.ConfigEntry;
import me.rigelmc.rigelmcmod.httpd.NanoHTTPD;
import me.rigelmc.rigelmcmod.util.FUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Module_players extends HTTPDModule
{

    public Module_players(RigelMCMod plugin, NanoHTTPD.HTTPSession session)
    {
        super(plugin, session);
    }

    @Override
    @SuppressWarnings("unchecked")
    public NanoHTTPD.Response getResponse()
    {
        final JSONObject responseObject = new JSONObject();

        final JSONArray players = new JSONArray();
        final JSONArray superadmins = new JSONArray();
        final JSONArray telnetadmins = new JSONArray();
        final JSONArray senioradmins = new JSONArray();
        final JSONArray developers = new JSONArray();

        // All online players
        for (Player player : Bukkit.getOnlinePlayers())
        {
            players.add(player.getName());
        }

        // Admins
        for (Admin admin : plugin.al.getActiveAdmins())
        {
            final String username = admin.getName();

            switch (admin.getRank())
            {
                case SUPER_ADMIN:
                    superadmins.add(username);
                    break;
                case TELNET_ADMIN:
                    telnetadmins.add(username);
                    break;
                case SENIOR_ADMIN:
                    senioradmins.add(username);
                    break;
            }
        }

        // Developers
        developers.addAll(FUtil.DEVELOPERS);
        developers.addAll(ConfigEntry.SERVER_DEVELOPERS.getStringList());

        responseObject.put("players", players);
        responseObject.put("superadmins", superadmins);
        responseObject.put("telnetadmins", telnetadmins);
        responseObject.put("senioradmins", senioradmins);
        responseObject.put("developers", developers);

        final NanoHTTPD.Response response = new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_JSON, responseObject.toString());
        response.addHeader("Access-Control-Allow-Origin", "*");
        return response;
    }
}
