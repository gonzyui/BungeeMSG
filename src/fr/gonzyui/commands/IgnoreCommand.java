package fr.gonzyui.commands;

import java.util.Arrays;
import java.util.List;
import fr.gonzyui.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class IgnoreCommand extends Command {
    Main main;

    public IgnoreCommand(Main main) {
        super("ignore", "", new String[0]);
        this.main = main;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            for (String msg : this.main.getConfig().getStringList("messages.console"))
                sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if (args.length > 0) {
            if (p.hasPermission("msg.ignore")) {
                ProxiedPlayer ignoree = BungeeCord.getInstance().getPlayer(args[0]);
                if (ignoree == null) {
                    for (String msg : this.main.getConfig().getStringList("messages.offline"))
                        p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
                    return;
                }
                if (this.main.getConfig().contains("ignore." + p.getUniqueId().toString())) {
                    if (this.main.getConfig().getStringList("ignore." + p.getUniqueId().toString()).contains(ignoree.getUniqueId().toString())) {
                        List<String> list = this.main.getConfig().getStringList("ignore." + p.getUniqueId().toString());
                        list.remove(ignoree.getUniqueId().toString());
                        this.main.getConfig().set("ignore." + p.getUniqueId(), list);
                        this.main.saveConfig();
                        for (String msg : this.main.getConfig().getStringList("messages.ignoreremoved"))
                            p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg.replace("%player%", ignoree.getName())))).create());
                        return;
                    }
                    List<String> ignorees = this.main.getConfig().getStringList("ignore." + p.getUniqueId().toString());
                    ignorees.add(ignoree.getUniqueId().toString());
                    this.main.getConfig().set("ignore." + p.getUniqueId(), ignorees);
                    this.main.saveConfig();
                    for (String msg : this.main.getConfig().getStringList("messages.ignoreadded"))
                        p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg.replace("%player%", ignoree.getName())))).create());
                    return;
                }
                this.main.getConfig().set("ignore." + p.getUniqueId().toString(), Arrays.asList(new String[] { ignoree.getUniqueId().toString() }));
                this.main.saveConfig();
                for (String msg : this.main.getConfig().getStringList("messages.ignoreadded"))
                    p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg.replace("%player%", ignoree.getName())))).create());
                return;
            }
            for (String msg : this.main.getConfig().getStringList("messages.nopermission"))
                p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
            return;
        }
        for (String msg : this.main.getConfig().getStringList("messages.ignoreusage"))
            p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
    }
}
