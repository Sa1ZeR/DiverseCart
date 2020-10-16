package me.Sa1ZeR_.DiverseCart.manager;

import me.Sa1ZeR_.DiverseCart.CartType;
import me.Sa1ZeR_.DiverseCart.DiverseCart;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CartManager {

    public static String SPLITTER;

    public CartManager() {
        SPLITTER = DiverseCart.instance.getCfg().getString("split-symbol");
    }

    public String getExtra(ItemStack itemStack) {
        return DiverseCart.instance.getNbtManager().getNbt(itemStack);
    }

    public void performCartItem(CartType type, String iid, int amount, String extra, Player player) {
        if(type == CartType.ITEM) {
            if(!iid.contains(":")) {
                iid = iid + ":0";
            }
            String[] arrID = iid.split(":");
            ItemStack itemStack;
            try {
                itemStack = new ItemStack(Material.getMaterial(arrID[0]), amount, (short) Integer.parseInt(arrID[1]));
            } catch (Exception ex) {
                itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(arrID[0])), amount, (short) Integer.parseInt(arrID[1]));
            }
            if(!StringUtils.isEmpty(extra)) {
                itemStack = DiverseCart.instance.getNbtManager().createItem(itemStack, extra);
            }
            if(!isFullInventory(player)) {
               player.getInventory().addItem(itemStack);
               player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 3F);
               DiverseCart.instance.getMessageManager().sendPlayer("commands.get.success.item", player, new String[] {itemStack.getType().name(), String.valueOf(itemStack.getAmount())});
            } else {
                World w = player.getWorld();
                w.dropItemNaturally(player.getLocation(), itemStack);
                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1F, 3F);
                DiverseCart.instance.getMessageManager().sendPlayer("messages.full-inv", player);
            }
        } else {
            DiverseCart.instance.getDebug().error("Unknown type: " + type);
        }
    }

    public boolean isFullInventory(Player player) {
        for(int i = 0; i < player.getInventory().getSize(); i++) {
            if(player.getInventory().getItem(i) == null) {
                return false;
            }
        }
        return true;
    }

    private String toColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
