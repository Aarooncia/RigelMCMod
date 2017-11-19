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
        final JSONArray executives = new JSONArray();
        final JSONArray gods = new JSONArray();
        final JSONArray masterbuilders = new JSONArray();
        final JSONArray buddhists = new JSONArray();

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
        developers.addAll(FUtil.TFDEVS);
        developers.addAll(FUtil.RMCDEVS);
        executives.addAll(ConfigEntry.SERVER_EXECUTIVES.getStringList());
        gods.addAll(ConfigEntry.SERVER_GODS.getStringList());
        masterbuilders.addAll(ConfigEntry.SERVER_MASTER_BUILDERS.getStringList());
        buddhists.addAll(ConfigEntry.SERVER_BUDDHISTS.getStringList());

        responseObject.put("buddhists", buddhists);
        responseObject.put("masterbuilders", masterbuilders);
        responseObject.put("gods", gods);
        responseObject.put("players", players);
        responseObject.put("superadmins", superadmins);
        responseObject.put("telnetadmins", telnetadmins);
        responseObject.put("senioradmins", senioradmins);
        responseObject.put("developers", developers);
        responseObject.put("executives", executives);

        final NanoHTTPD.Response response = new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_JSON, responseObject.toString());
        response.addHeader("Access-Control-Allow-Origin", "*");
        return response;
    }
}
