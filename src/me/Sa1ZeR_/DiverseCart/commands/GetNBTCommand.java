package me.Sa1ZeR_.DiverseCart.commands;

import me.Sa1ZeR_.DiverseCart.DiverseCart;
import me.Sa1ZeR_.DiverseCart.commands.base.AbstractCommand;
import me.Sa1ZeR_.DiverseCart.commands.base.AbstractPlayerCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetNBTCommand extends AbstractPlayerCommand {
    public GetNBTCommand(String[] aliases, String perm, int args, String usage) {
        super(aliases, perm, args, usage);
    }


    @Override
    protected void perform(Player player, String[] args) {
        ItemStack itemStack = player.getItemInHand();
        if(itemStack != null && itemStack.getType() != Material.AIR) {
            String nbt = DiverseCart.instance.getNbtManager().getNbt(itemStack);
            if(nbt != null) {
                DiverseCart.instance.getMessageManager().sendPlayer("commands.getnbt.success", player, nbt);
            } else {
                DiverseCart.instance.getMessageManager().sendPlayer("commands.getnbt.no-nbt", player);
            }
        } else {
            DiverseCart.instance.getMessageManager().sendPlayer("commands.put.no-item-in-hand", player);
        }
    }
}
