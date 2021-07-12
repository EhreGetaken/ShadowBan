package net.shadowban.api;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.shadowban.ShadowBan;

import java.awt.*;
import java.net.InetAddress;
import java.util.ArrayList;

public class Utils {

    public static final String FOOTER_PREFIX = "ShadowBan v1.0";
    public static final String URL = "https://betterproject.net/shadowban";
    public static final String ICON = "https://betterproject.net/repo/galery/shadowban.png";

    private static final String LOG_GUILD_ID = "734763893871738938";
    private static final String LOG_CHANNEL_ID = "744956456796684289";

    public static ArrayList<InetAddress> API_IP_CACHE = new ArrayList<>();

    private static Guild BT_GUILD = ShadowBan.getInstance().getManagerBuilder().getGuildById(LOG_GUILD_ID);
    public static final TextChannel BT_LOG = BT_GUILD.getTextChannelById(LOG_CHANNEL_ID);

    public static final MessageEmbed HELP_EMBED = new MessageAPI().setAuthor("ShadowBan", Utils.URL, Utils.ICON)
            .setDescription("ShadowBan - Help \n" +
                    "NOTE: For the setup you need Administrator permission also the bot need the " +
                    "following permissions: \n" +
                    "- Send messages \n" +
                    "- Delete messages \n" +
                    "- Create channels \n" +
                    " \n" +
                    "Help page for ShadowBan \n" +
                    "sb!help > Show this page \n" +
                    "sb!ban @Member (Type) > Ban a member and add him to the database \n" +
                    "sb!ban (memberId) (Type) > Basn a member with his id \n" +
                    "sb!changeJoinType (type) > Change the type for banned members (not available) \n" +
                    "sb!unban (memberId) > Unban a Member \n" +
                    "\n" +
                    "Types: \n" +
                    "SPAMMING, RAIDING, BULLYING \n \n" +
                    "Global-Commands: \n" +
                    "sb!setup > Setup the bot (Administrator needed)")
            .setFooter(Utils.FOOTER_PREFIX)
            .setColor(Color.GREEN)
            .build();

    public static final MessageEmbed ERROR_EMBED = new MessageAPI().setAuthor("ShadowBan", Utils.URL, Utils.ICON)
            .setDescription("ShadowBan - Error \n" +
                    "I don't have enough permission to execute this command. \n" +
                    "I need the following permission: \n" +
                    "- Send messages \n" +
                    "- Delete messages \n" +
                    "- Create channels \n" +
                    "- For setup you need admin")
            .setFooter(Utils.FOOTER_PREFIX)
            .setColor(Color.RED)
            .build();

    public static final MessageEmbed ERROR_NO_PERM_EMBED = new MessageAPI().setAuthor("ShadowBan", URL, ICON)
            .setDescription("You donÄt have enough permission to execute this command!")
            .setFooter(FOOTER_PREFIX)
            .setColor(Color.RED)
            .build();

    public static final MessageEmbed ERROR_NO_SETUP_EMBED = new MessageAPI().setAuthor("ShadowBan", URL, ICON)
            .setDescription("You did not set up ShadowBan. Pleas set the bot up")
            .setFooter(FOOTER_PREFIX)
            .setColor(Color.RED)
            .build();


}
