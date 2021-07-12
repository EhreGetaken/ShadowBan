package net.shadowban.cmd;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.shadowban.api.MessageAPI;
import net.shadowban.api.Utils;

import java.awt.*;

public class Help_CMD extends ListenerAdapter {

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
            if (args[0].equalsIgnoreCase("sb!help")) {
                try {
                    embed = Utils.HELP_EMBED;
                    channel.sendMessage(embed).queue();
                } catch (InsufficientPermissionException exc) {
                    member.getUser().openPrivateChannel().queue(privateChannel -> {
                        embed = Utils.ERROR_EMBED;
                        privateChannel.sendMessage(embed).queue();
                    });
                }
            }
        }
    }

}
