package net.shadowban.cmd;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ProxyServer;
import net.shadowban.ShadowBan;
import net.shadowban.api.*;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Unban_CMD extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split("\\s+");
        assert member != null;

        try {

            if (!GuildAPI.getString(guild.getId(), "CHANNEL").equalsIgnoreCase("null")) {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("sb!unban")) {
                        if (ConfigAPI.getModerator().contains(member.getId())) {
                            if (MemberAPI.getBoolean(args[1], "FLAGGED")) {
                                ConfigAPI.removeCounter(1);
                                channel.sendMessage("You successfully unbanned " + args[1] + " #" + ConfigAPI.getCounter()).queue();
                                MemberAPI.setBoolean(args[1], "FLAGGED", false);
                                MemberAPI.setString(args[1], "TYPE", "NONE");
                            } else {
                                channel.sendMessage("This member is not banned!").queue();
                            }
                        }
                    }
                }
                if (args.length >= 3) {
                    if (args[0].equalsIgnoreCase("sb!unban")) {
                        String reason = "";
                        for (int i = 2; i < args.length; i++) {
                            reason = reason + " " + args[i];
                        }
                        if (MemberAPI.getBoolean(args[1], "FLAGGED")) {
                            ConfigAPI.addRQID(1);
                            MessageEmbed embed = new MessageAPI().setAuthor("ShadowBan", Utils.URL, Utils.ICON)
                                    .addField("Requester", member.getUser().getName() + ";" + member.getId(), true)
                                    .addField("Unban", args[1], true)
                                    .setDescription("RQID #" + ConfigAPI.getRQID() + "\n" +
                                            "Unban reason: \n" + reason + "\n" +
                                            "NOT REVIEWED")
                                    .setColor(Color.ORANGE)
                                    .setFooter(Utils.FOOTER_PREFIX)
                                    .addTimestamp()
                                    .build();

                            assert Utils.BT_LOG != null;
                            Message logMsg = Utils.BT_LOG.sendMessage(embed).complete();

                            ProxyServer.getInstance().getScheduler().schedule(ShadowBan.getInstance(), new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        logMsg.addReaction("✅").queue();
                                        logMsg.addReaction("❎").queue();
                                    } catch (NullPointerException exc) {
                                        System.out.println("Unban error: Cannot assign reaction " + exc.getCause());
                                    }
                                }
                            }, 2L, TimeUnit.SECONDS);

                            channel.sendMessage("Thank you for unban request. A Moderator will review your request.").queue();
                        } else {
                            channel.sendMessage("This user is not banned.").queue();
                        }
                    }
                }
            }
        } catch (Exception exc) {

        }
    }

}
