package net.shadowban.network;

import net.shadowban.ShadowBan;
import net.shadowban.api.ConfigAPI;
import net.shadowban.api.MemberAPI;
import net.shadowban.api.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketReceiver {

    public static void ReceiveSocket() {

        try{

            System.out.println("[SocketAPI] checking...");
            ServerSocket server = new ServerSocket(9999);

            boolean done = false;
            while(!done) {
                Socket client = server.accept();
                client.setKeepAlive(true);

                System.out.println("Client: " + client.getInetAddress().getHostAddress() + " @ " + client.getPort() + " trying to connect...");

                InputStream in = client.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String s = null;
                //Creating String for reading
                if((s = reader.readLine()) != null) {
                    System.out.println("Client: " + client.getInetAddress().getHostAddress() + " @ " + client.getPort() + " input " + s);
                    //Reading the line out of the Socket and printing it out
                    if (s.startsWith("AUTH_KEY=")) {
                        String key = s.replaceAll("AUTH_KEY=", "");
                        if (ConfigAPI.getAuthKeys().contains(key)) {
                            if (!Utils.API_IP_CACHE.contains(client.getInetAddress())) {
                                    new SocketSender("CONNECTED", client.getInetAddress().getHostAddress(), client.getPort());
                                    Utils.API_IP_CACHE.add(client.getInetAddress());
                                System.out.println("AUTH_KEY for Client@" + client.getPort() + " is correct!");
                            } else {
                                new SocketSender("ERROR_ALREADY_CONNECTED", client.getInetAddress().getHostAddress(), client.getPort());
                            }
                        } else {
                            new SocketSender("ERROR_AUTH_KEY_NOT_VALID", client.getInetAddress().getHostAddress(), client.getPort());
                        }
                    }
                    if (s.startsWith("API_REQUEST_MEMBER_BANNED=")) {
                        if (Utils.API_IP_CACHE.contains(client.getInetAddress())) {
                            String[] data = s.split("=");
                            String memberId = data[1];
                            if (MemberAPI.getBoolean(memberId, "FLAGGED")) {
                                new SocketSender("REQUEST_BANNED=true", client.getInetAddress().getHostAddress(), client.getPort());
                            } else {
                                new SocketSender("REQUEST_BANNED=false", client.getInetAddress().getHostAddress(), client.getPort());
                            }
                        } else {
                            new SocketSender("ERROR_NOT_CONNECTED", client.getInetAddress().getHostAddress(), client.getPort());
                        }
                    }

                    if (s.startsWith("API_REQUEST_MEMBER_TYPE=")) {
                        if (Utils.API_IP_CACHE.contains(client.getInetAddress())) {
                            String[] data = s.split("=");
                            String memberId = data[1];
                            String type = MemberAPI.getString(memberId, "TYPE");
                            new SocketSender("REQUEST_TYPE=" + type, client.getInetAddress().getHostAddress(), client.getPort());
                        } else {
                            new SocketSender("ERROR_NOT_CONNECTED", client.getInetAddress().getHostAddress(), client.getPort());
                        }
                    }
                    if (s.startsWith("DISCONNECT")) {
                        if (Utils.API_IP_CACHE.contains(client.getInetAddress())) {
                            Utils.API_IP_CACHE.remove(client.getInetAddress());
                        } else {
                            new SocketSender("ERROR_NOT_CONNECTED", client.getInetAddress().getHostAddress(), client.getPort());
                        }
                    }

                }
                in.close();
                reader.close();
                client.close();
                server.close();

                ReceiveSocket();

            }

        }catch(IOException e){
            System.out.println("[Error] Socket closed or connection got reseted!");
            e.printStackTrace();
        }
    }

}
