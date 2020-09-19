package me.Sa1ZeR_.DiverseCart.manager.NBTManager;

import net.minecraft.server.v1_7_R4.MojangsonParser;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTManager17 extends NBTManager {

    @Override
    public ItemStack createItem(ItemStack itemStack, String nbt) {
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTagCompound = (NBTTagCompound) MojangsonParser.parse(nbt);
        if(nbtTagCompound != null) {
            nmsItem.setTag(nbtTagCompound);
            return CraftItemStack.asBukkitCopy(nmsItem);
        }
        return null;
    }

    @Override
    public String getNbt(ItemStack itemStack) {
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        if(nmsItem.hasTag()) {
            NBTTagCompound nbtTagCompound = nmsItem.getTag();
            return nbtTagCompound.toString();
        }
        return null;
    }
}
