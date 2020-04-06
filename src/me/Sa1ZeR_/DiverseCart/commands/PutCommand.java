package me.Sa1ZeR_.DiverseCart.commands;

import me.Sa1ZeR_.DiverseCart.DiverseCart;
import me.Sa1ZeR_.DiverseCart.commands.base.AbstractPlayerCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PutCommand extends AbstractPlayerCommand {
    public PutCommand(String[] aliases, String perm, int args, String usage) {
        super(aliases, perm, args, usage);
    }

    @Override
    protected void perform(Player player, String[] args) {
        if(player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
            DiverseCart.instance.getStorageManager().putItem(player.getItemInHand(), player.getName());
            DiverseCart.instance.getMessageManager().sendPrefixMessage("commands.put.success", player, new String[0]);
        } else {
            DiverseCart.instance.getMessageManager().sendPrefixMessage("commands.put.no-item-in-hand", player, new String[0]);
        }
    }
}
