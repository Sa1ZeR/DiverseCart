package me.Sa1ZeR_.DiverseCart.commands.base;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractPlayerCommand extends AbstractCommand {

    public AbstractPlayerCommand(String[] aliases, String perm, int args, String usage) {
        super(aliases, perm, args, usage);
    }

    @Override
    public void perform(CommandSender sender, String[] params) {
        if(!(sender instanceof Player)) {
            throw new UnsupportedOperationException("Only Player can use this");
        }
        perform((Player) sender, params);
    }

    protected abstract void perform(Player player, String[] args);
}
