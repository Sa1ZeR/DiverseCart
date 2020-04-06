package me.Sa1ZeR_.DiverseCart.commands;

import me.Sa1ZeR_.DiverseCart.DiverseCart;
import me.Sa1ZeR_.DiverseCart.commands.base.AbstractCommand;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends AbstractCommand {
    public ReloadCommand(String[] aliases, String perm, int args, String usage) {
        super(aliases, perm, args, usage);
    }

    @Override
    public void perform(CommandSender sender, String[] params) {
        DiverseCart.instance.onDisable();
        DiverseCart.instance.onEnable();
        DiverseCart.instance.getMessageManager().sendMsg("commands.reload.success", sender, new String[0]);
    }
}
