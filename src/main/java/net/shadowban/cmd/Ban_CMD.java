package net.shadowban.cmd;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.shadowban.ShadowBan;
import net.shadowban.api.*;

import java.awt.*;

public class Ban_CMD extends ListenerAdapter {

    private static MessageEmbed embed;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split("\\s+");
        assert member != null;

        if (!GuildAPI.getString(guild.getId(), "CHANNEL").equalsIgnoreCase("null")) {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("sb!ban")) {
                    if (args[2].equalsIgnoreCase("SPAMMING") || args[2].equalsIgnoreCase("RAIDING") || args[2].equalsIgnoreCase("BULLYING")) {
                        try {
                            if (!message.getMentionedMembers().isEmpty()) {
                                Member mention = message.getMentionedMembers().get(0);
                                if (!mention.getUser().isBot()) {
                                    String reason = args[2];
                                    if (!member.getId().equalsIgnoreCase(mention.getId())) {
                                        if (member.hasPermission(Permission.BAN_MEMBERS)) {
                                            banMember(mention, reason, guild, channel);
                                        } else {
                                            member.getUser().openPrivateChannel().queue(privateChannel -> {
                                                privateChannel.sendMessage(Utils.ERROR_NO_PERM_EMBED).queue();
                                            });
                                        }

                                    } else {
                                        member.getUser().openPrivateChannel().queue(privateChannel -> {
                                            privateChannel.sendMessage(Utils.ERROR_NO_PERM_EMBED).queue();
                                        });
                                    }
                                } else {
                                    channel.sendMessage("You are not allowed to ban a bot!").queue();
                                }
                            } else {
                                if (ConfigAPI.getModerator().contains(member.getId())) {
                                    String id = args[1];
                                    String reason = args[2];

                                    guild.getMembers().forEach(member1 -> {
                                        if (member1.getId().equalsIgnoreCase(id)) {
                                            member1.ban(7, reason).queue();
                                        }
                                    });

                                    ConfigAPI.addCounter(1);

                                    MemberAPI.setString(id, "TYPE", reason);
                                    MemberAPI.setBoolean(id, "FLAGGED", true);

                                    embed = new MessageAPI().setAuthor("ShadowBan", Utils.URL, Utils.ICON)
                                            .setDescription("New ban applied to the global database \n" +
                                                    "User: " + "(" + id + ")" + "\n" +
                                                    "Guild: " + guild.getName() + " (" + guild.getId() + ") \n" +
                                                    "Reason: " + reason)
                                            .setColor(Color.ORANGE)
                                            .setFooter(Utils.FOOTER_PREFIX)
                                            .build();

                                    TextChannel log = guild.getTextChannelById(GuildAPI.getString(guild.getId(), "CHANNEL"));
                                    assert log != null;
                                    log.sendMessage(embed).queue();

                                    assert Utils.BT_LOG != null;
                                    Utils.BT_LOG.sendMessage(embed).queue();

                                    channel.sendMessage(id + " got banned globally for " + reason + " #" + ConfigAPI.getCounter()).queue();
                                } else {
                                    member.getUser().openPrivateChannel().queue(privateChannel -> {
                                        privateChannel.sendMessage(Utils.ERROR_NO_PERM_EMBED).queue();
                                    });
                                }
                            }
                        } catch (InsufficientPermissionException exc) {
                            member.getUser().openPrivateChannel().queue(privateChannel -> {
                                privateChannel.sendMessage(Utils.ERROR_EMBED).queue();
                            });
                        }
                    }
                }
            }
        } else {
            try {
                member.getUser().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage(Utils.ERROR_NO_SETUP_EMBED).queue();
                });
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    private static void banMember(Member mention, String reason, Guild guild, TextChannel channel) {
        mention.ban(7, reason).queue();

        ConfigAPI.addCounter(1);

        if (!MemberAPI.memberExists(mention.getId())) {
            MemberAPI.createMember(mention.getId());
        }

        MemberAPI.setString(mention.getId(), "TYPE", reason);
        MemberAPI.setBoolean(mention.getId(), "FLAGGED", true);

        embed = new MessageAPI().setAuthor("ShadowBan", Utils.URL, Utils.ICON)
                .setDescription("New ban applied to the global database \n" +
                        "User: " + mention.getUser().getName() + " (" + mention.getId() + ") \n" +
                        "Guild: " + guild.getName() + " (" + guild.getId() + ") \n" +
                        "Reason: " + reason)
                .setColor(Color.ORANGE)
                .setFooter(Utils.FOOTER_PREFIX)
                .build();

        TextChannel log = guild.getTextChannelById(GuildAPI.getString(guild.getId(), "CHANNEL"));
        assert log != null;
        log.sendMessage(embed).queue();

        assert Utils.BT_LOG != null;
        Utils.BT_LOG.sendMessage(embed).queue();

        channel.sendMessage(mention.getAsMention() + " got banned globally for " + reason + " #" + ConfigAPI.getCounter()).queue();
    }

}
