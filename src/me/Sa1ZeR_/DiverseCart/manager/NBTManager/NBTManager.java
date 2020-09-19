package me.Sa1ZeR_.DiverseCart.manager.NBTManager;

import org.bukkit.inventory.ItemStack;

public abstract class NBTManager {

    public abstract ItemStack createItem(ItemStack itemStack, String nbt);

    public abstract String getNbt(ItemStack itemStack);
}
