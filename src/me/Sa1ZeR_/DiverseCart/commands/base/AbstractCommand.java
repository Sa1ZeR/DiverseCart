package me.Sa1ZeR_.DiverseCart.commands.base;

import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {

    private int args;
    private String usage;
    private String perm;
    private String[] aliases;

    public AbstractCommand(String[] aliases, String perm, int args, String usage) {
        this.aliases = aliases;
        this.perm = perm;
        this.args = args;
        this.usage = usage;
    }

    public abstract void perform(CommandSender sender, String[] params);

    public int getArgs() {
        return args;
    }

    public String getUsage() {
        return usage;
    }

    public String getPerm() {
        return perm;
    }

    public String[] getAliases() {
        return aliases;
    }
}
