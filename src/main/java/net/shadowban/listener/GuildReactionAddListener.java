package net.shadowban.listener;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.shadowban.api.ConfigAPI;
import net.shadowban.api.MemberAPI;
import net.shadowban.api.MessageAPI;
import net.shadowban.api.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GuildReactionAddListener extends ListenerAdapter {

    //⛔

    String unbanId = "";
    Message message;

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        try {
            assert Utils.BT_LOG != null;
            if (event.getChannel().getId().equalsIgnoreCase(Utils.BT_LOG.getId())) {
                if (!event.getUser().isBot()) {
                    channel.retrieveMessageById(event.getMessageId()).queue(message1 -> {
                        message = message1;
                    });
                    if (event.getReactionEmote().getName().equalsIgnoreCase("✅")) {
                        if (!Objects.equals(message.getEmbeds().get(0).getColor(), Color.GREEN) || !Objects.equals(message.getEmbeds().get(0).getColor(), Color.RED)) {
                            List<MessageEmbed.Field> fields = message.getEmbeds().get(0).getFields();
                            fields.forEach(field -> {
                                if (Objects.requireNonNull(field.getName()).equalsIgnoreCase("Unban")) {
                                    unbanId = field.getValue();
                                }
                            });
                            MessageEmbed embed = new MessageAPI().setAuthor("ShadowBan", Utils.URL, Utils.ICON)
                                    .addField("Unban", unbanId, true)
                                    .setDescription("RQID #" + ConfigAPI.getRQID() + "\n" +
                                            "REVIEWED | ACCEPTED")
                                    .setColor(Color.GREEN)
                                    .setFooter(Utils.FOOTER_PREFIX)
                                    .addTimestamp()
                                    .build();
                            message.editMessage(embed).queue();

                            MemberAPI.setBoolean(unbanId, "FLAGGED", false);
                            MemberAPI.setString(unbanId, "TYPE", "NONE");

                            ConfigAPI.removeCounter(1);

                            message.getReactions().forEach(messageReaction -> {
                                messageReaction.removeReaction().queue();
                            });

                        }
                    }
                    if (event.getReactionEmote().getName().equalsIgnoreCase("❎")) {
                        if (!Objects.equals(message.getEmbeds().get(0).getColor(), Color.GREEN) || !Objects.equals(message.getEmbeds().get(0).getColor(), Color.RED)) {
                            List<MessageEmbed.Field> fields = message.getEmbeds().get(0).getFields();
                            fields.forEach(field -> {
                                if (Objects.requireNonNull(field.getName()).equalsIgnoreCase("Unban")) {
                                    unbanId = field.getValue();
                                }
                            });
                            MessageEmbed embed = new MessageAPI().setAuthor("ShadowBan", Utils.URL, Utils.ICON)
                                    .addField("Unban", unbanId, true)
                                    .setDescription("RQID #" + ConfigAPI.getRQID() + "\n" +
                                            "REVIEWED | DECLINED")
                                    .setColor(Color.RED)
                                    .setFooter(Utils.FOOTER_PREFIX)
                                    .addTimestamp()
                                    .build();
                            message.editMessage(embed).queue();


                            message.getReactions().forEach(messageReaction -> {
                                messageReaction.removeReaction().queue();
                            });

                        }
                    }
                    event.getReaction().removeReaction(event.getUser()).queue();
                }
            }
        } catch (NullPointerException exc) {
            Message logMsg = channel.sendMessage("Please re-react to the message.").complete();
            logMsg.delete().queueAfter(5L, TimeUnit.SECONDS);
        } catch (Exception exc) {
            System.out.println("GuildReactionListener");
        }
    }

}
