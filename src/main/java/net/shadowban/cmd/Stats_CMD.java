package net.shadowban.cmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.shadowban.ShadowBan;

import java.awt.*;
import java.util.Objects;

public class Stats_CMD extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split("\\s+");
        assert member != null;

        try {
            if (!member.getUser().isBot()) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("sb!stats")) {
                        final int[] userOnline = {0};
                        final int[] allUsers = {0};
                        JDA.ShardInfo shardInfo = event.getJDA().getShardInfo();
                        ShadowBan.getInstance().getManagerBuilder().getGuilds().forEach(guilds -> {
                            if (!guilds.getId().equalsIgnoreCase("264445053596991498")) {
                                guilds.getMembers().forEach(members -> {
                                    if (members.getOnlineStatus().equals(OnlineStatus.ONLINE) || members.getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB) || members.getOnlineStatus().equals(OnlineStatus.IDLE)) {
                                        userOnline[0]++;
                                    }
                                    allUsers[0]++;
                                });
                            }
                        });

                        event.getChannel().sendMessage(new EmbedBuilder()
                                .setTitle("ShadowBan | Stats")
                                .addField("User", String.valueOf(userOnline[0]), true)
                                .addField("All users", String.valueOf(allUsers[0]), true)
                                .addField("Shards", String.valueOf(ShadowBan.getInstance().getManagerBuilder().getShards().size()), true)
                                .addField("Your shardId", String.valueOf(shardInfo.getShardId()), true)
                                .addField("Servers", String.valueOf(ShadowBan.getInstance().getManagerBuilder().getGuilds().size()), true)
                                .setColor(Color.GREEN)
                                .build()).complete();
                    }
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("sb!stats")) {
                        try {

                            int shardId = Integer.parseInt(args[1]);

                            final int[] userOnline = {0};
                            final int[] allUsers = {0};
                            final int[] allGuilds = {0};
                            JDA.ShardInfo shardInfo = event.getJDA().getShardInfo();
                            ShadowBan.getInstance().getManagerBuilder().getGuilds().forEach(guilds -> {
                                if (Objects.requireNonNull(ShadowBan.getInstance().getManagerBuilder().getGuildById(guilds.getId())).getJDA().getShardInfo().getShardId() == shardId) {
                                    if (!guilds.getId().equalsIgnoreCase("264445053596991498")) {
                                        allGuilds[0]++;
                                        guilds.getMembers().forEach(members -> {
                                            if (members.getOnlineStatus().equals(OnlineStatus.ONLINE) || members.getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB) || members.getOnlineStatus().equals(OnlineStatus.IDLE)) {
                                                userOnline[0]++;
                                            }
                                            allUsers[0]++;
                                        });
                                    }
                                }
                            });

                            event.getChannel().sendMessage(new EmbedBuilder()
                                    .setTitle("ShadowBan | Stats for ShardID " + shardId)
                                    .addField("User", String.valueOf(userOnline[0]), true)
                                    .addField("All users", String.valueOf(allUsers[0]), true)
                                    .addField("Servers", String.valueOf(allGuilds[0]), true)
                                    .setColor(Color.GREEN)
                                    .build()).complete();
                        } catch (NumberFormatException exc) {

                        }
                    }
                }
            }
        } catch (Exception exc) {

        }

    }

}
