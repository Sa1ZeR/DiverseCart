package me.Sa1ZeR_.DiverseCart.commands;

import me.Sa1ZeR_.DiverseCart.DiverseCart;
import me.Sa1ZeR_.DiverseCart.commands.base.AbstractPlayerCommand;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ListCommand extends AbstractPlayerCommand {
    public ListCommand(String[] aliases, String perm, int args, String usage) {
        super(aliases, perm, args, usage);
    }

    @Override
    protected void perform(Player player, String[] args) {
        HashMap<Integer,String> map = DiverseCart.instance.getStorageManager().cartList(player.getName());
        if(map.size() > 0) {
            DiverseCart.instance.getMessageManager().sendPlayer("commands.list.header", player, new String[0]);
            DiverseCart.instance.getMessageManager().sendPlayer("commands.list.info", player, new String[] {String.valueOf(map.size())});
            for(Map.Entry<Integer, String> pair : map.entrySet()) {
                String[] arr = pair.getValue().split(";");
                DiverseCart.instance.getMessageManager().sendPlayer("commands.list.item", player, new String[] {String.valueOf(pair.getKey()),arr[0], arr[1], arr[2]});
            }
        } else {
            DiverseCart.instance.getMessageManager().sendPrefixMessage("commands.list.cart-empty", player, new String[0]);
        }
    }
}
