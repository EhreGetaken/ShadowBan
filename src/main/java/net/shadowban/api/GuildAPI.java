package net.shadowban.api;

import net.shadowban.driver.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GuildAPI {

    public static boolean guildExists(String guildId) {
        ResultSet rs = MySQL.getResult("SELECT GUILD FROM GuildAPI WHERE GUILD='" + guildId + "'");

        if (rs != null) {
            try {
                while (rs.next()) {
                    return true;
                }
            } catch (SQLException exc) {

            }
        }
        return false;
    }

    public static void createGuild(String guildId) {
        if (!guildExists(guildId)) {
            MySQL.update("INSERT INTO GuildAPI (GUILD, CHANNEL, RESULT) VALUES ('" + guildId + "', 'null', 'LOG')");
        }
    }

    public static void removeGuild(String guildId) {
        if (guildExists(guildId)) {
            MySQL.update("DELETE FROM GuildAPI WHERE GUILD='" + guildId + "'");
        }
    }

    public static String getString(String guildId, String row) {
        ResultSet rs = MySQL.getResult("SELECT " + row + " FROM GuildAPI WHERE GUILD='" + guildId + "'");
        String result = "null";

        try {
            if (rs != null) {
                while (rs.next()) {
                    result = rs.getString(row);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return result;
    }

    public static void setString(String guildId, String row, String s) {
        if (guildExists(guildId)) {
            MySQL.update("UPDATE GuildAPI SET " + row + "='" + s + "' WHERE GUILD='" + guildId + "'");
        } else {
            createGuild(guildId);
            setString(guildId, row, s);
        }
    }


}
