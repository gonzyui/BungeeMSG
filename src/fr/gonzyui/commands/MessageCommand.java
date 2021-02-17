package fr.gonzyui.commands;

import fr.gonzyui.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MessageCommand extends Command {
    Main main;

    public MessageCommand(Main main) {
        super("message", "", new String[] { "msg", "pm", "m" });
        this.main = main;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            for (String msg : this.main.getConfig().getStringList("messages.console"))
                sender.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if (args.length > 1) {
            if (p.hasPermission("msg.message")) {
                ProxiedPlayer reciever = BungeeCord.getInstance().getPlayer(args[0]);
                if (reciever == null) {
                    for (String msg : this.main.getConfig().getStringList("messages.offline"))
                        p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
                    return;
                }
                String message = "";
                int i = 0;
                byte b;
                int j;
                String[] arrayOfString;
                for (j = (arrayOfString = args).length, b = 0; b < j; ) {
                    String word = arrayOfString[b];
                    if (i != 0)
                        message = String.valueOf(message) + word + " ";
                    i++;
                    b++;
                }
                ReplyCommand.replies.put(reciever.getUniqueId(), p.getUniqueId());
                this.main.message(p, reciever, message);
                return;
            }
            for (String msg : this.main.getConfig().getStringList("messages.nopermission"))
                p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
            return;
        }
        for (String msg : this.main.getConfig().getStringList("messages.messageusage"))
            p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
    }
}
