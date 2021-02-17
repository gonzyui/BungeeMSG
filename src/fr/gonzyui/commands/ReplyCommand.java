package fr.gonzyui.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import fr.gonzyui.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCommand extends Command {
    Main main;

    static Map<UUID, UUID> replies = new HashMap();

    public ReplyCommand(Main main) {
        super("reply", "", new String[] { "r" });
        this.main = main;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            for (String msg : this.main.getConfig().getStringList("messages.console"))
                sender.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if (args.length > 0) {
            if (p.hasPermission("msg.reply")) {
                if (!replies.containsKey(p.getUniqueId())) {
                    for (String msg : this.main.getConfig().getStringList("messages.noreplies"))
                        p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
                    return;
                }
                ProxiedPlayer reciever = BungeeCord.getInstance().getPlayer(replies.get(p.getUniqueId()));
                String message = "";
                byte b;
                int i;
                String[] arrayOfString;
                for (i = (arrayOfString = args).length, b = 0; b < i; ) {
                    String word = arrayOfString[b];
                    message = String.valueOf(message) + word + " ";
                    b++;
                }
                replies.put(reciever.getUniqueId(), p.getUniqueId());
                this.main.message(p, reciever, message);
                return;
            }
            for (String msg : this.main.getConfig().getStringList("messages.nopermission"))
                p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
            return;
        }
        for (String msg : this.main.getConfig().getStringList("messages.replyusage"))
            p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
    }
}
