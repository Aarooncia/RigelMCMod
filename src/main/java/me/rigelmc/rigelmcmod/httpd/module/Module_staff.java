package me.rigelmc.rigelmcmod.httpd.module;

import me.rigelmc.rigelmcmod.RigelMCMod;
import me.rigelmc.rigelmcmod.httpd.NanoHTTPD;

/**
 *
 * @author savnith
 */
public class Module_staff extends HTTPDModule
{

    public Module_staff(RigelMCMod plugin, NanoHTTPD.HTTPSession session)
    {
        super(plugin, session);
    }

    @Override
    public String getBody()
    {
        final StringBuilder body = new StringBuilder();

        body.append(
                  "        <h2>Developers</h2>\n"
                + "        <ul id=\"developers\"></ul>\n"
                + "        <h2>Senior Admins</h2>\n"
                + "        <ul id=\"senioradmins\"></ul>\n"
                + "        <h2>Telnet Admins</h2>\n"
                + "        <ul id=\"telnetadmins\"></ul>\n"
                + "        <h2>Super Admins</h2>\n"
                + "        <ul id=\"superadmins\"></ul>\n"
                + "        \n"
                + "        <script src=\"https://frostedop.net/httpd/list/staff/js/moment.js\"></script>\n"
                + "        <script src=\"https://totalfreedom.me/js/functions.js\"></script>\n"
                + "        <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js\"></script>\n"
                + "        <script>\n"
                + "            // All Json content is loaded respectively into #(item)\n"
                + "            var jsonUrl = \"http://play.rigelmc.net:4040/players\";\n"
                + "            var jsonContent = [\"developers\",\"senioradmins\",\"telnetadmins\",\"superadmins\"];\n"
                + "\n"
                + "            $(document).ready(function () {\n"
                + "                loadJson(jsonUrl, jsonContent);\n"
                + "            }); \n"
                + "        </script>");

        return body.toString();
    }

    @Override
    public String getTitle()
    {
        return "RigelMC - Admin List";
    }
}
