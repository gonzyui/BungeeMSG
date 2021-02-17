package fr.gonzyui.commands;

import java.util.List;
import java.util.UUID;
import fr.gonzyui.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SocialspyCommand extends Command {
    Main main;

    public SocialspyCommand(Main main) {
        super("socialspy", "", new String[] { "ss" });
        this.main = main;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            for (String msg : this.main.getConfig().getStringList("messages.console"))
                sender.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if (p.hasPermission("msg.socialspy")) {
            List<UUID> socialspy = this.main.convertString(this.main.getConfig().getStringList("socialspy"));
            if (socialspy.contains(p.getUniqueId())) {
                socialspy.remove(p.getUniqueId());
                List<String> list = this.main.convertUUID(socialspy);
                this.main.getConfig().set("socialspy", list);
                this.main.saveConfig();
                for (String msg : this.main.getConfig().getStringList("messages.disablesocialspy"))
                    p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
                return;
            }
            socialspy.add(p.getUniqueId());
            List<String> socialspyString = this.main.convertUUID(socialspy);
            this.main.getConfig().set("socialspy", socialspyString);
            this.main.saveConfig();
            for (String msg : this.main.getConfig().getStringList("messages.enablesocialspy"))
                p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
            return;
        }
        for (String msg : this.main.getConfig().getStringList("messages.nopermission"))
            p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
    }
}
