package net.shadowban.cmd;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.shadowban.api.GuildAPI;
import net.shadowban.api.Utils;

public class Setup_CMD extends ListenerAdapter {

    MessageEmbed embed;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Message message = event.getMessage();
        String[] args = message.getContentRaw().split("\\s+");
        assert member != null;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("sb!setup")) {
                if (member.hasPermission(Permission.ADMINISTRATOR)) {
                    try {
                        channel.sendMessage("Trying to set up ShadowBan logging...").queue();

                        TextChannel logChannel = guild.createTextChannel("shadow-log")
                                .setTopic("ShadowBan - Logging").complete();
                        GuildAPI.createGuild(guild.getId());
                        GuildAPI.setString(guild.getId(), "CHANNEL", logChannel.getId());

                        channel.sendMessage("Setup done!").queue();
                        logChannel.sendMessage("ShadowBan Logging").queue();

                    } catch (InsufficientPermissionException exc) {
                        member.getUser().openPrivateChannel().queue(privateChannel -> {
                            privateChannel.sendMessage(Utils.ERROR_EMBED).queue();
                        });
                    }
                } else {
                    member.getUser().openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage(Utils.ERROR_NO_PERM_EMBED).queue();
                    });
                }
            }
        }
    }

}
