package me.Sa1ZeR_.DiverseCart.commands;

import me.Sa1ZeR_.DiverseCart.DiverseCart;
import me.Sa1ZeR_.DiverseCart.commands.base.AbstractPlayerCommand;
import org.bukkit.entity.Player;

import java.util.IllegalFormatException;

public class GetCommand extends AbstractPlayerCommand {
    public GetCommand(String[] aliases, String perm, int args, String usage) {
        super(aliases, perm, args, usage);
    }

    @Override
    protected void perform(Player player, String[] args) {
        long id;
        try {
            id = Long.parseLong(args[1]);
            DiverseCart.instance.getStorageManager().getItem(id, player);
        } catch (IllegalFormatException e) {
            DiverseCart.instance.getMessageManager().sendPlayer("commands.get.bad-format", player, new String[0]);
        }
    }
}
