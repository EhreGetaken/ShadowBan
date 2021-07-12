package net.shadowban.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.shadowban.api.GuildAPI;
import net.shadowban.api.MemberAPI;
import net.shadowban.api.MessageAPI;
import net.shadowban.api.Utils;

import java.awt.*;

public class MemberJoinListener extends ListenerAdapter {

    MessageEmbed embed;

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        try {
            if (!MemberAPI.memberExists(member.getId())) {
                MemberAPI.createMember(member.getId());
            }
            if (!GuildAPI.getString(guild.getId(), "CHANNEL").equalsIgnoreCase("null")) {
                if (MemberAPI.getBoolean(member.getId(), "FLAGGED")) {
                    try {
                        TextChannel channel = guild.getTextChannelById(GuildAPI.getString(guild.getId(), "CHANNEL"));
                        assert channel != null;
                        embed = new MessageAPI().setAuthor("ShadowBan", Utils.URL, Utils.ICON)
                                .setDescription("Found a flagged member! \n" +
                                        "Member: " + member.getUser().getName() + " (" + member.getId() + ") \n" +
                                        "FlagType: " + MemberAPI.getString(member.getId(), "TYPE"))
                                .setColor(Color.ORANGE)
                                .setFooter(Utils.FOOTER_PREFIX)
                                .build();
                        channel.sendMessage(embed).queue();
                    } catch (NullPointerException exc) {
                        System.out.println("Channel equals null");
                    }
                }
            }

        } catch (Exception exc) {

        }

    }

}
