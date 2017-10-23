package com.demo.ios;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.redis.Redis;
import org.json.JSONException;
import org.json.JSONObject;
import com.demo.ios.*;

import java.nio.ByteBuffer;


@ServerEndpoint("/websocket2")
public class WebSocket2Controller {

    private ExecuteUtil bash = new ExecuteUtil();
    public boolean getLog = false;
    public StringBuffer logsBuffer = new StringBuffer();
    Session mySession = null;

    @OnOpen
    public void onOpen(Session session) throws IOException, InterruptedException, JSONException {
        System.out.println("ws2 opened!:");
        this.mySession = session;
        this.getLog = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    getDeviceLogs();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//
//        while (getLog){
//            Thread.sleep(1000);
//            String buf = this.logsBuffer.toString();
//            if(buf.isEmpty()){
//                continue;
//            }
//            session.getBasicRemote().sendText(buf);
//            int  sb_length = this.logsBuffer.length();
//            this.logsBuffer.delete(0,sb_length);
//        }


    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("ws2 closed!");
        this.getLog = false;

    }

    @OnMessage
    public void onMessage(String requestString, Session session) throws IOException, InterruptedException {

    }

    public void getDeviceLogs() throws IOException {

        ProcessBuilder pb = new ProcessBuilder("idevicesyslog");
        Process p = null;
        System.out.println("服务器2启动");

        p = pb.start();
        InputStream ips = p.getInputStream();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips));
            String line;
            while (this.getLog && (line = reader.readLine()) != null) {
                line += "<br />";
                System.out.println(line);
//                this.logsBuffer.append(line);
                if(this.mySession.isOpen()){
                    this.mySession.getBasicRemote().sendText(line);
                }else{
                    p.destroy();
                    System.out.println("服务器2停止");
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                ips.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("服务器2停止");
        p.destroy();





    }


}


