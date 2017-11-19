package me.rigelmc.rigelmcmod.rank;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Title implements Displayable
{
    BUDDHIST("a true", "Buddhist", ChatColor.YELLOW, "Buddhist"),
    GOD("a fucking", "God", ChatColor.LIGHT_PURPLE, "God"),
    MASTER_BUILDER("a", "Master Builder", ChatColor.DARK_AQUA, "MB"),
    DEVELOPER("a", "TotalFreedom Developer", ChatColor.DARK_PURPLE, "TF-Dev"),
    RMCDEV("a", "RigelMC Developer", ChatColor.DARK_PURPLE, "RMC-Dev"),
    LEAD_DEV("the", "Lead Developer", ChatColor.DARK_PURPLE, "Lead-Dev"),
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
    
    @Override
    public String getDeterminer()
    {
        return determiner;
    }


    @Override
    public String getItalicColoredName()
    {
        return color.toString() + ChatColor.ITALIC + name;
    }
    
}
