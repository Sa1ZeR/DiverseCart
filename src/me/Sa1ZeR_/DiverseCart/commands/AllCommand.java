package me.Sa1ZeR_.DiverseCart.commands;

import me.Sa1ZeR_.DiverseCart.DiverseCart;
import me.Sa1ZeR_.DiverseCart.commands.base.AbstractPlayerCommand;
import org.bukkit.entity.Player;

public class AllCommand extends AbstractPlayerCommand {
    public AllCommand(String[] aliases, String perm, int args, String usage) {
        super(aliases, perm, args, usage);
    }

    @Override
    protected void perform(Player player, String[] args) {
        DiverseCart.instance.getStorageManager().getAll(player);
    }
}
