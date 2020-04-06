package me.Sa1ZeR_.DiverseCart.manager;

import me.Sa1ZeR_.DiverseCart.CartType;
import me.Sa1ZeR_.DiverseCart.DiverseCart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
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

    public String getEnchantments(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Enchantment, Integer> pair : itemStack.getEnchantments().entrySet()) {
            sb.append(pair.getKey().getName()).append(":").append(pair.getValue()).append(SPLITTER);
        }
        return sb.length() > 0 ? sb.substring(0, sb.length()- SPLITTER.length()) : null;
    }

    public String getExtra(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        if(itemStack.hasItemMeta()) {
            if(itemStack.getItemMeta().hasDisplayName()) {
                sb.append("@name={" + itemStack.getItemMeta().hasDisplayName()+"}");
            }
            if(itemStack.getItemMeta().hasLore()) {
                sb.append("@lore={");
                for(String l : itemStack.getItemMeta().getLore()) {
                    sb.append(l).append(SPLITTER);
                }
                sb.substring(0, sb.length() - SPLITTER.length());
                sb.append("}");
            }
        }
        return sb.length() > 0 ? sb.substring(0, sb.length()- SPLITTER.length()) : null;
    }

    public void performCartItem(CartType type, String iid, int amount, String enchantments, String extra, Player player) {
        if(type == CartType.ITEM) {
            String[] arrID = iid.split(":");
            ItemStack itemStack;
            try {
                itemStack = new ItemStack(Material.getMaterial(arrID[0]), amount, (short) Integer.parseInt(arrID[1]));
            } catch (Exception ex) {
                itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(arrID[0])), amount, (short) Integer.parseInt(arrID[1]));
            }
            if(enchantments != null) {
                String[] arrEnch = enchantments.split(SPLITTER);
                Map<Enchantment, Integer> mapEnch = new HashMap<>();
                for(String e : arrEnch) {
                    String[] el = e.split(":");
                    mapEnch.put(Enchantment.getByName(el[0].toUpperCase()), Integer.parseInt(el[1]));
                }
                itemStack.addUnsafeEnchantments(mapEnch);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();;
            if(extra != null && extra.contains("@name={")) {
                String name = extra.split("@name=\\{")[1];
                int ind = name.indexOf("}");
                name = extra.substring(0, ind);
                itemMeta.setDisplayName(toColor(name));
            }
            if(extra != null && extra.contains("@lore{")) {
                String lore = extra.split("@lore=\\{")[1];
                int ind = lore.indexOf("}");
                String[] lores = lore.substring(0, ind).split(SPLITTER);
                List<String> loresItem = new ArrayList<>();
                for(String s : lores) {
                    loresItem.add(toColor(s));
                }
                itemMeta.setLore(loresItem);
            }
            itemStack.setItemMeta(itemMeta);
            if(!isFullInventory(player)) {
               player.getInventory().addItem(itemStack);
               DiverseCart.instance.getMessageManager().sendPlayer("commands.get.success.item", player, new String[] {itemStack.getType().name(), String.valueOf(itemStack.getAmount())});
            } else {
                World w = player.getWorld();
                w.dropItemNaturally(player.getLocation(), itemStack);
                DiverseCart.instance.getMessageManager().sendPlayer("messages.full-inv", player, new String[0]);
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
