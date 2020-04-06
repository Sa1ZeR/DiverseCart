package me.Sa1ZeR_.DiverseCart.commands.base;

import me.Sa1ZeR_.DiverseCart.DiverseCart;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandController {

    private HashMap<String, AbstractCommand> cmdsMap;
    private int pointer;

    public CommandController(int pointer) {
        cmdsMap = new HashMap<>();
        this.pointer = pointer;
    }

    public void registerCmd(AbstractCommand command) {
        for(String a : command.getAliases()) {
            AbstractCommand cmd = cmdsMap.get(a.toLowerCase());
            if(cmd != null) {
                DiverseCart.instance.getDebug().error("Duplicate command: " + a);
            } else {
                cmdsMap.put(a.toLowerCase(), command);
            }
        }
    }

    public AbstractCommand filterCommand(CommandSender sender, Command command, String label, String... args) {
        if(pointer > args.length - 1) {
            noCommand(sender);
            return null;
        }
        AbstractCommand executor = cmdsMap.get(args[pointer].toLowerCase());
        if(executor == null) {
            argsNotFound(sender);
        } else if((!(sender instanceof Player)) && (executor instanceof AbstractPlayerCommand)) {
            onlyPlayerCmd(sender);
        } else if(!sender.hasPermission(executor.getPerm())) {
            noPerm(sender);
        } else {
            if(args.length - (pointer + 1) >= executor.getArgs()) {
                return executor;
            }
            badArgs(sender, executor);
        }
        return null;
    }

    public void handleCmd(CommandSender sender, Command command, String label, String... args) {
        AbstractCommand ex = filterCommand(sender, command, label, args);
        if(ex != null) {
            ex.perform(sender, args);
        }
    }

    public void noCommand(CommandSender sender) {
        DiverseCart.instance.getMessageManager().sendMsg("messages.no-args", sender, new String[0]);
    }

    public void argsNotFound(CommandSender sender) {
        DiverseCart.instance.getMessageManager().sendMsg("messages.not-found", sender, new String[0]);
    }

    public void onlyPlayerCmd(CommandSender sender) {
        DiverseCart.instance.getMessageManager().sendMsg("messages.no-only-player", sender, new String[0]);
    }

    public void noPerm(CommandSender sender) {
        DiverseCart.instance.getMessageManager().sendMsg("messages.no-perm", sender, new String[0]);
    }

    public void badArgs(CommandSender sender, AbstractCommand executor) {
        DiverseCart.instance.getMessageManager().sendMsg(executor.getUsage(), sender, new String[0]);
    }
}
