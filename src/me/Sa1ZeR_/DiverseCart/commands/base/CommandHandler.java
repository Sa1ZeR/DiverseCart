package me.Sa1ZeR_.DiverseCart.commands.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler extends CommandController implements CommandExecutor {
    public CommandHandler() {
        super(0);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        handleCmd(commandSender, command, s, strings);
        return false;
    }
}
