package fr.gonzyui.commands;

import java.util.List;
import java.util.UUID;
import fr.gonzyui.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class DonotdisturbCommand extends Command {
    Main main;

    public DonotdisturbCommand(Main main) {
        super("donotdisturb", "", new String[] { "dnd" });
        this.main = main;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            for (String msg : this.main.getConfig().getStringList("messages.console"))
                sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if (p.hasPermission("msg.donotdisturb")) {
            List<UUID> donotdisturb = this.main.convertString(this.main.getConfig().getStringList("donotdisturb"));
            if (donotdisturb.contains(p.getUniqueId())) {
                donotdisturb.remove(p.getUniqueId());
                List<String> donotdisturbString = this.main.convertUUID(donotdisturb);
                this.main.getConfig().set("donotdisturb", donotdisturbString);
                this.main.saveConfig();
                for (String msg : this.main.getConfig().getStringList("messages.disabledonotdisturb"))
                    p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
                return;
            }
            donotdisturb.add(p.getUniqueId());
            List<String> donotdisturbString2 = this.main.convertUUID(donotdisturb);
            this.main.getConfig().set("donotdisturb", donotdisturbString2);
            this.main.saveConfig();
            for (String msg : this.main.getConfig().getStringList("messages.enabledonotdisturb"))
                p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
            return;
        }
        for (String msg : this.main.getConfig().getStringList("messages.nopermission"))
            p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
    }
}
