package io.github.JoltMuz.bracket;

import org.bukkit.ChatColor;

import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin
{
	
	
    @Override
    public void onEnable()
    {
    	
        this.getCommand("bracket").setExecutor(new start());
        getServer().getPluginManager().registerEvents(new tp(), this);
    }

    @Override
    public void onDisable()
    {

    }

    public static String sign = ChatColor.YELLOW + ChatColor.BOLD.toString()+ "Bracket" + ChatColor.DARK_GRAY + " 》 ";
}
