package com.dumptruckman.tools.plugin.command;

import com.dumptruckman.tools.permission.Perm;
import com.dumptruckman.tools.plugin.AbstractPluginBase;
import com.dumptruckman.tools.locale.Message;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Enables debug-information.
 */
public class ReloadCommand<P extends AbstractPluginBase> extends PluginCommand<P> {

    public ReloadCommand(P plugin) {
        super(plugin);
        this.setName("Reloads config file");
        this.setCommandUsage("/" + plugin.getCommandPrefix() + " reload");
        this.setArgRange(0, 0);
        this.addKey(plugin.getCommandPrefix() + " reload");
        this.addKey(plugin.getCommandPrefix() + "reload");
        this.setPermission(Perm.COMMAND_RELOAD.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        plugin.reloadConfig();
        messager.normal(Message.RELOAD_COMPLETE, sender);
    }
}
