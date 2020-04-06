package me.Sa1ZeR_.DiverseCart.commands;

import me.Sa1ZeR_.DiverseCart.DiverseCart;
import me.Sa1ZeR_.DiverseCart.commands.base.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(String[] aliases, String perm, int args, String usage) {
        super(aliases, perm, args, usage);
    }

    @Override
    public void perform(CommandSender sender, String[] params) {
        DiverseCart.instance.getMessageManager().sendPlayer("commands.help.header", (Player) sender, new String[0]);
        Set<String> helps = DiverseCart.instance.getLang().getConfigurationSection("commands.help.list").getKeys(false);
        for(String s : helps) {
            DiverseCart.instance.getMessageManager().sendPlayer("commands.help.list." + s, (Player) sender, new String[0]);
        }
    }
}
