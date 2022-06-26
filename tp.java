package io.github.JoltMuz.bracket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class tp implements Listener
{
	public static List<Player> currentlyPlaying = new ArrayList<>();
    public static List<Player> remaining = new ArrayList<>();
    public static List<Player> winners = new ArrayList<>();
    public static List<Player> thirdplacers = new ArrayList<>();
    static String third;

    public static int round = 1;

    public static void teleport()
    {
        if (remaining.size() > 1)
        {
            Bukkit.broadcastMessage(main.sign +ChatColor.YELLOW + remaining.get(0).getName() + ChatColor.GOLD + " VS " + ChatColor.YELLOW + remaining.get(1).getName());
            currentlyPlaying.add(remaining.get(0));
            remaining.get(0).teleport(start.loc1);
            remaining.get(0).setBedSpawnLocation(remaining.get(0).getWorld().getSpawnLocation());
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lasergun " + remaining.get(0).getName());
            remaining.remove(0);
            currentlyPlaying.add(remaining.get(0));
            remaining.get(0).teleport(start.loc2);
            remaining.get(0).setBedSpawnLocation(remaining.get(0).getWorld().getSpawnLocation());
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lasergun " + remaining.get(0).getName());
            remaining.remove(0);
        }
        else
        {
            Bukkit.getConsoleSender().sendMessage(main.sign + ChatColor.RED + "Error, remaining players are somehow less than 2." );
        }
    }

    @EventHandler
    public void Murder(PlayerDeathEvent e)
    {
        if (start.ongoing)
        {
            Bukkit.broadcastMessage(main.sign + ChatColor.DARK_GREEN + "Winner: " + ChatColor.GREEN + e.getEntity().getKiller().getName() + ChatColor.DARK_RED + "| Loser: " + ChatColor.RED + e.getEntity().getName());
            winners.add(e.getEntity().getKiller());
            e.getEntity().getKiller().teleport(e.getEntity().getKiller().getWorld().getSpawnLocation());
            e.getEntity().getKiller().getInventory().clear();
            e.getEntity().getInventory().clear();
            e.getEntity().getKiller().setHealth(20);
            e.getEntity().getKiller().sendMessage(main.sign + ChatColor.AQUA + "You won the round!");
            currentlyPlaying.clear();

            if (thirdplacers.contains(e.getEntity()))
            {
                third = e.getEntity().getKiller().getName();
                thirdplacers.clear();
                winners.remove(e.getEntity().getKiller());
            }

            if (remaining.size() == 0)
            {
                if (winners.size() == 1)
                {
                    start.ongoing = false;
                    Bukkit.broadcastMessage(main.sign + ChatColor.GOLD + "The Bracket has ended.");
                    Bukkit.broadcastMessage(main.sign + ChatColor.AQUA + "♕ #1 》" + e.getEntity().getKiller().getName());
                    Bukkit.broadcastMessage(main.sign + ChatColor.YELLOW + "♖ #2 》" + e.getEntity().getName());
                    Bukkit.broadcastMessage(main.sign + ChatColor.WHITE + "♖ #3 》" + third);
                    round = 1;
                    start.ongoing = false;
                    
                }
                if (winners.size() == 2)
                {
                    if (third != null)
                    {
                        Bukkit.broadcastMessage(main.sign + ChatColor.AQUA + "The " + ChatColor.DARK_AQUA + "Final Match" + ChatColor.AQUA + " has started!");
                        remaining.addAll(winners);
                        winners.clear();
                        teleport();
                    }
                    else
                    {
                        thirdplacers.add(e.getEntity());
                        Bukkit.broadcastMessage(main.sign + ChatColor.LIGHT_PURPLE + "The " + ChatColor.DARK_PURPLE + "Third-Place Match" + ChatColor.LIGHT_PURPLE + " has started!");
                        thirdplacers.get(0).teleport(start.loc1);
                        thirdplacers.get(1).teleport(start.loc2);
                        Bukkit.broadcastMessage(main.sign + ChatColor.YELLOW + "The " + ChatColor.GOLD + "Third-Place Match" + ChatColor.YELLOW + " has started!");
                        Bukkit.broadcastMessage(main.sign +ChatColor.GOLD + thirdplacers.get(0).getName() + ChatColor.YELLOW + " VS " + ChatColor.GOLD + thirdplacers.get(1).getName());
                        
                    }

                }
                if (winners.size() == 4)
                {
                    Bukkit.broadcastMessage(main.sign + ChatColor.GREEN + "The " + ChatColor.DARK_GREEN + "Semi-Finals" + ChatColor.GREEN + " have started!");
                    remaining.addAll(winners);
                    winners.clear();
                    teleport();
                }
                else
                {
                    remaining.addAll(winners);
                    winners.clear();
                    round += 1;
                    Bukkit.broadcastMessage(main.sign + ChatColor.YELLOW + "Round " + ChatColor.GOLD + round + ChatColor.YELLOW + " has started!");
                }

            }
            else if (remaining.size() == 1)
            {
                Bukkit.broadcastMessage(main.sign + ChatColor.GOLD + remaining.get(0).getName() + ChatColor.YELLOW + " is the only one remaining, so they win this round!");
                remaining.addAll(winners);
                winners.clear();
                round += 1;
                Bukkit.broadcastMessage(main.sign + ChatColor.YELLOW + "Round " + ChatColor.GOLD + round + ChatColor.YELLOW + " has started!");
                teleport();
            }
            else if (remaining.size() == 2)
            {
                if (winners.size() == 1)
                {
                    thirdplacers.add(e.getEntity());   
                }
                else if (winners.size() == 0)
                {
                	third = e.getEntity().getKiller().getName();
                }
                teleport();
            }
            else
            {
                teleport();
            }
        }
    }
    @EventHandler
    public void DitchingEvent(PlayerQuitEvent e)
    {
    	if (start.ongoing)
    	{
    		if (remaining.contains(e.getPlayer()))
            {
                remaining.remove(e.getPlayer());
                Bukkit.broadcastMessage(main.sign + ChatColor.GOLD + e.getPlayer().getName() + ChatColor.YELLOW + " has ditched the event.");
            }
            else if (currentlyPlaying.contains(e.getPlayer()))
            {
            	currentlyPlaying.remove(e.getPlayer());
            	e.getPlayer().damage(20,currentlyPlaying.get(0));
            }
    	}
  
     }
}
