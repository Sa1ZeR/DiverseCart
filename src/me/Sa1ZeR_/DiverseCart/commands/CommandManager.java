package me.Sa1ZeR_.DiverseCart.commands;

import me.Sa1ZeR_.DiverseCart.commands.base.CommandHandler;

public class CommandManager extends CommandHandler {
    public CommandManager() {
        registerCmd(new HelpCommand(new String[] {"help"}, "diversecart.cmd.help", 0, "commands.help.usage"));
        registerCmd(new PutCommand(new String[] {"put"}, "diversecart.admin.put", 0, "commands.put.usage"));
        registerCmd(new ReloadCommand(new String[] {"reload"}, "diversecart.admin.reload", 0, "commands.reload.usage"));
        registerCmd(new ListCommand(new String[] {"list"}, "diversecart.cmd.list", 0, "commands.list.usage"));
        registerCmd(new GetCommand(new String[] {"get"}, "diversecart.cmd.get", 1, "commands.get.usage"));
        registerCmd(new AllCommand(new String[] {"all"}, "diversecart.cmd.all", 0, "commands.all.usage"));
        registerCmd(new GetNBTCommand(new String[] {"getnbt"}, "diversecart.cmd.getnbt", 0, "commands.getnbt.usage"));
    }
}
