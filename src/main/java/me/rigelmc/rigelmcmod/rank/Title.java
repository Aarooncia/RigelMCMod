package me.rigelmc.rigelmcmod.rank;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Title implements Displayable
{
    BUDDHIST("a true", "Buddhist", ChatColor.YELLOW, "Buddhist"),
    GOD("a fucking", "God", ChatColor.LIGHT_PURPLE, "God"),
    MASTER_BUILDER("a", "Master Builder", ChatColor.DARK_AQUA, "MB"),
    DEVELOPER("a", "Developer", ChatColor.DARK_PURPLE, "TF-Dev"),
    RMC_DEV("a", "Developer", ChatColor.DARK_PURPLE, "RMC-Dev"),
    EXECUTIVE("an", "Executive", ChatColor.DARK_RED, "Executive"),
    OWNER("the", "Owner", ChatColor.BLUE, "Owner");

    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final String coloredTag;
    @Getter
    private final ChatColor color;

    private Title(String determiner, String name, ChatColor color, String tag)
    {
        this.determiner = determiner;
        this.name = name;
        this.tag = "[" + tag + "]";
        this.coloredTag = ChatColor.DARK_GRAY + "[" + color + tag + ChatColor.DARK_GRAY + "]" + color;
        this.color = color;
    }

    @Override
    public String getColoredName()
    {
        return color + name;
    }

    @Override
    public String getColoredLoginMessage()
    {
        return determiner + " " + color + ChatColor.ITALIC + name;
    }

}
