package net.shadowban.api;

import net.shadowban.driver.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberAPI {

    //update("CREATE TABLE IF NOT EXISTS MemberAPI (MEMBER VARCHAR(100), FLAGGED VARCHAR(100), TYPE VARCHAR(100))");

    public static boolean memberExists(String memberId) {
        ResultSet rs = MySQL.getResult("SELECT MEMBER FROM MemberAPI WHERE MEMBER='" + memberId + "'");

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

    public static void createMember(String memberId) {
        if (!memberExists(memberId)) {
            MySQL.update("INSERT INTO MemberAPI (MEMBER, FLAGGED, TYPE) VALUES ('" + memberId + "', 'false', 'NONE')");
        }
    }

    public static String getString(String memberId, String row) {
        ResultSet rs = MySQL.getResult("SELECT " + row + " FROM MemberAPI WHERE MEMBER='" + memberId + "'");
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

    public static void setString(String memberId, String row, String s) {
        if (memberExists(memberId)) {
            MySQL.update("UPDATE MemberAPI SET " + row + "='" + s + "' WHERE MEMBER='" + memberId + "'");
        } else {
            createMember(memberId);
            setString(memberId, row, s);
        }
    }

    public static Boolean getBoolean(String memberId, String row) {
        ResultSet rs = MySQL.getResult("SELECT " + row + " FROM MemberAPI WHERE MEMBER='" + memberId + "'");
        boolean result = false;

        try {
            if (rs != null) {
                while (rs.next()) {
                    result = rs.getBoolean(row);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return result;
    }

    public static void setBoolean(String memberId, String row, boolean b) {
        if (memberExists(memberId)) {
            MySQL.update("UPDATE MemberAPI SET " + row + "='" + b + "' WHERE MEMBER='" + memberId + "'");
        } else {
            createMember(memberId);
            setBoolean(memberId, row, b);
        }
    }

}
