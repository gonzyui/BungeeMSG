package fr.gonzyui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.gonzyui.commands.*;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {
    Configuration config;

    public void onEnable() {
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadResource(this, "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getProxy().getPluginManager().registerCommand(this, (Command)new MessageCommand(this));
        getProxy().getPluginManager().registerCommand(this, (Command)new ReplyCommand(this));
        getProxy().getPluginManager().registerCommand(this, (Command)new DonotdisturbCommand(this));
        getProxy().getPluginManager().registerCommand(this, (Command)new SocialspyCommand(this));
        getProxy().getPluginManager().registerCommand(this, (Command)new IgnoreCommand(this));
    }

    public void onDisable() {}

    public Configuration getConfig() {
        return this.config;
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                Exception exception2;
                resourceFile.createNewFile();
                Exception exception1 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

    public List<String> convertUUID(List<UUID> uuidList) {
        List<String> stringList = new ArrayList();
        for (UUID uuid : uuidList) {
            if (uuid != null)
                stringList.add(uuid.toString());
        }
        return stringList;
    }

    public List<UUID> convertString(List<String> stringList) {
        List<UUID> uuidList = new ArrayList();
        for (String string : stringList) {
            if (string != null)
                uuidList.add(UUID.fromString(string));
        }
        return uuidList;
    }

    public void message(ProxiedPlayer p, ProxiedPlayer reciever, String message) {
        List<UUID> donotdisturb = convertString(getConfig().getStringList("donotdisturb"));
        if (donotdisturb.contains(p.getUniqueId())) {
            for (String msg : getConfig().getStringList("messages.donotdisturb"))
                p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
            return;
        }
        if (getConfig().contains("ignore." + reciever.getUniqueId().toString())) {
            List<UUID> ignore = convertString(getConfig().getStringList("ignore." + p.getUniqueId().toString()));
            if (ignore.contains(p.getUniqueId())) {
                for (String msg : getConfig().getStringList("messages.donotdisturb"))
                    p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg))).create());
                return;
            }
        }
        for (String msg : getConfig().getStringList("messages.sentformat"))
            p.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg).replace("%reciever%", reciever.getName()).replace("%message%", message)
                    .replace("%server%", p.getServer().getInfo().getName()))).create());
        for (String msg : getConfig().getStringList("messages.recievedformat"))
            reciever.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg).replace("%sender%", p.getName()).replace("%message%", message)
                    .replace("%server%", p.getServer().getInfo().getName()))).create());
        for (String uuid : getConfig().getStringList("socialspy")) {
            ProxiedPlayer ss = BungeeCord.getInstance().getPlayer(UUID.fromString(uuid));
            if (ss == null || ss.equals(p) || ss.equals(reciever))
                return;
            for (String msg : getConfig().getStringList("messages.socialspyformat"))
                ss.sendMessage((new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', msg).replace("%sender%", p.getName())
                        .replace("%reciever%", reciever.getName()).replace("%message%", message)
                        .replace("%server%", p.getServer().getInfo().getName()))).create());
        }
    }
}
