package net.shadowban.network;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.shadowban.ShadowBan;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class SocketSender {

    private String informationToken;

    public SocketSender(String informationToken, String host, int port) {

        this.informationToken = informationToken;

        System.out.println("[SocketAPI] Trying to connect...");

        Socket client;

        try {

            //CREATING CLIENT SIDE SOCKET
            client = new Socket();
            client.connect(new InetSocketAddress(host, 9998));
            System.out.println("[SocketAPI] Trying to send a Socket...");
            OutputStream out = client.getOutputStream();
            PrintWriter writer = new PrintWriter(out);

            //Writing a String to the Server via a PrintWriter
            writer.write(informationToken);
            writer.flush();


            writer.close();
            client.close();

        } catch (ConnectException exc) {
            System.out.println("[Error] Failed to connect to the remote Server.");
            exc.printStackTrace();
        } catch (IOException exc) {
            System.out.println("[Error] Failed to create Writers.");
            exc.printStackTrace();
        }

    }

    public String getInformationToken() {
        return informationToken;
    }

}
