package net.shadowban;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.shadowban.api.ConfigAPI;
import net.shadowban.api.Utils;
import net.shadowban.cmd.*;
import net.shadowban.driver.MySQL;
import net.shadowban.listener.GuildLeaveListener;
import net.shadowban.listener.GuildMemberRemoveListener;
import net.shadowban.listener.GuildReactionAddListener;
import net.shadowban.listener.MemberJoinListener;
import net.shadowban.network.SocketReceiver;
import org.discordbots.api.client.DiscordBotListAPI;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

public class ShadowBan extends Plugin {

    private static ShadowBan instance;

    private static DiscordBotListAPI discordBotListAPI;
    private static ShardManager managerBuilder;

    @Override
    public void onEnable() {
        instance = this;

        ConfigAPI.loadConfig();
        ConfigAPI.createCounter();

        MySQL.readMySQL();

        MySQL.connect();
        MySQL.createTable();

        discordBotListAPI = new DiscordBotListAPI.Builder()
                .token("")
                .botId("")
                .build();

        try {
            managerBuilder = DefaultShardManagerBuilder.createDefault("")
                    .addEventListeners(new Ban_CMD(), new Help_CMD(), new Setup_CMD(), new GuildLeaveListener(), new GuildMemberRemoveListener(), new MemberJoinListener(), new Unban_CMD(), new GuildReactionAddListener(), new Stats_CMD())
                    .setShardsTotal(1)
                    .build();
        } catch (LoginException exc) {
            exc.printStackTrace();
        }

       updateOnlineGuilds();
        updateBotsUsing();

        ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                SocketReceiver.ReceiveSocket();
            }
        }, 1L, TimeUnit.SECONDS);

    }

    @Override
    public void onDisable() {

    }

    public static ShadowBan getInstance() {
        return instance;
    }

    public DiscordBotListAPI getDiscordBotListAPI() {
        return discordBotListAPI;
    }

    public ShardManager getManagerBuilder() {
        return managerBuilder;
    }

    public static void updateOnlineGuilds() {
        ProxyServer.getInstance().getScheduler().schedule(ShadowBan.getInstance(), new Runnable() {
            @Override
            public void run() {
                int guilds = getInstance().getManagerBuilder().getGuilds().size();
                ShadowBan.getInstance().getManagerBuilder().setActivity(Activity.watching(ConfigAPI.getCounter() + " banned users globally on " + guilds + " servers | sb!help"));

            }
        }, 15L,15L, TimeUnit.SECONDS);
    }

    public static void updateBotsUsing() {
        ProxyServer.getInstance().getScheduler().schedule(getInstance(), new Runnable() {
            @Override
            public void run() {
                getInstance().getDiscordBotListAPI().setStats(getInstance().getManagerBuilder().getGuilds().size());
            }
        }, 0L, 1L, TimeUnit.HOURS);
    }

}
