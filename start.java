package io.github.JoltMuz.bracket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class start implements CommandExecutor
{
    public static String usage = main.sign + ChatColor.RED + "/bracket [coords|start|end] [x1 y1 z1] [x2 y2 z2]";
    public static boolean ongoing = false;

    public static Location loc1;
    public static Location loc2;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (sender.isOp())
        {
        	if (args.length == 0)
        	{
        		sender.sendMessage(main.sign + ChatColor.RED + "Usage: /bracket [coords/start/end]");
        	}
        	else
        	{
        		if (args[0].equalsIgnoreCase("coords") && sender instanceof Player)
                {
                    if (args.length == 7)
                    {

                        try
                        {
                            int x1 = Integer.parseInt(args[1]);
                            int y1 = Integer.parseInt(args[2]);
                            int z1 = Integer.parseInt(args[3]);
                            int x2 = Integer.parseInt(args[4]);
                            int y2 = Integer.parseInt(args[5]);
                            int z2 = Integer.parseInt(args[6]);

                            loc1 = new Location(((Player) sender).getWorld(),x1, y1,z1);
                            loc2 = new Location(((Player) sender).getWorld(),x2, y2,z2);
                            
                            loc1.setDirection(loc2.clone().subtract(loc1).toVector());
                            loc2.setDirection(loc1.clone().subtract(loc2).toVector());
                            

                            sender.sendMessage(main.sign + ChatColor.GREEN + "Location 1: " + x1 + " " + y1 + " " + z1);
                            sender.sendMessage(main.sign + ChatColor.GREEN + "Location 2: " + x2 + " " + y2 + " " + z2);
                            sender.sendMessage(main.sign + ChatColor.GREEN + "You may start the bracket using /bracket start.");

                        }
                        catch (NumberFormatException error)
                        {
                            sender.sendMessage(main.sign + ChatColor.RED + "Coordinates must be numbers.");
                        }
                    }
                    else if (args.length == 1)
                    {
                        if (loc1 != null && loc2!= null)
                        {
                            sender.sendMessage(main.sign + ChatColor.GREEN + "Coordinates set are: " + ChatColor.DARK_GREEN + loc1.getX() + " " + loc1.getY() + " " + loc1.getZ() + " <=> " + loc2.getX() + " " + loc2.getY() + " " + loc2.getZ() );
                        }
                        else
                        {
                            sender.sendMessage(main.sign + usage);
                        }
                    }
                    else
                    {
                        sender.sendMessage(main.sign + usage);
                    }
                }
                else if (args[0].equalsIgnoreCase("end"))
                {
                    if (ongoing)
                    {
                        Bukkit.broadcastMessage(main.sign + ChatColor.GOLD + "The bracket has been Force-Ended!");
                        tp.round = 1;
                        ongoing = false;
                    }
                    else
                    {
                        sender.sendMessage(main.sign + ChatColor.RED + "There is no on-going bracket.");
                    }
                }
                else if (args[0].equalsIgnoreCase("start"))
                {
                    if (!ongoing)
                    {
                        if (loc1 != null && loc2 != null)
                        {
                            Bukkit.broadcastMessage(main.sign + ChatColor.GOLD + "The bracket has started!");
                            ongoing = true;

                            tp.remaining.addAll(0,Bukkit.getOnlinePlayers());

                            tp.teleport();
                        }
                        else
                        {
                            sender.sendMessage(main.sign + ChatColor.RED + "You must first set the coordinates using /bracket coords");
                        }
                    }
                    else
                    {
                        sender.sendMessage(main.sign + ChatColor.RED + "A bracket is already on-going.");
                    }
                }
                else
                {
                    sender.sendMessage(usage);
                }
        	}
            
        }
        else
        {
            sender.sendMessage(main.sign + ChatColor.RED + "This command requires OP.");
        }
        return true;
    }
}
