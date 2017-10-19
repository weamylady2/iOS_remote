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


@ServerEndpoint("/websocket")
public class WebSocketController {

    private Stack<Byte[]> stack = new Stack<Byte[]>();

    private static final int PORT = 1717;
    private Socket socket;
    private int readBannerBytes = 0;
    private int bannerLength = 2;
    private int readFrameBytes = 0;
    private int frameBodyLength = 0;
    private byte[] frameBody = new byte[0];
    private int total;
    private boolean debug = false;
    private int port = 8888;

    private boolean ready = false;
    private boolean socketConnected = false;
    private byte[] finalBytes = null;

    private BufferedImage bufferedImage;
    private final ExecuteUtil bash = new ExecuteUtil();
    private static final int minicapPort = Integer.parseInt(PropKit.get("minicapPort"));


    @OnOpen
    public void onOpen(Session session) throws IOException, InterruptedException, JSONException {
//        session.getBasicRemote().sendText("Helloword!");
//        System.out.println("Connected!!!!!!!!!!!!!");

        this.socketConnected = false;
        Thread.sleep(1000);
        closeWDA();
        closeMinicap();
        startMinicap();
        this.ready = false;


        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    getImage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        int i=30;
        while(i-- > 0){
            System.out.println("waiting to get image");
            if(ready){
                System.out.println("Image Got, Opening WDA..");
                startWDA();
                break;
            }else{
                Thread.sleep(1000);
            }
        }

        if(!socketConnected){
            System.out.println("Session Closing...");
            session.close();
            return;
        }


        while (true) {
            if(!socketConnected){
                System.out.println("Session Closing...");
                session.close();
                return;
            }

            if(ready) {
                ByteBuffer bBuffer = ByteBuffer.wrap(finalBytes);
//                System.out.println("发送图片的大小为：" + finalBytes.length);
                session.getBasicRemote().sendBinary(bBuffer);
//                finalBytes = null;
                ready = false;
            } else{
                Thread.sleep(20);
            }
        }

    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnMessage
    public void onMessage(String requestJson, Session session) throws IOException {


        System.out.println("服务器启动");




    }

    public void initWDA() throws JSONException, IOException, InterruptedException {
        IosController c = new IosController();
        c.initWda();
    }



    public void closeWDA() throws IOException, InterruptedException {
        System.out.println("closing wda...");
        bash.exitWDA();
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                try {
//                    bash.exitWDA();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    public void closeMinicap() throws IOException, InterruptedException {
        System.out.println("closing minicap...");
        bash.exitMincap();
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                try {
//                    bash.exitMincap();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    public void startWDA() throws InterruptedException, IOException, JSONException {
        System.out.println("starting wda...");
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    bash.executeWDA();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        int i=30;
        while(i-- > 0){
            if(bash.isWdaOpened()){
                System.out.println("wda stared, initing WDA...");
                initWDA();
                return;
            }else{
                Thread.sleep(1000);
            }
        }
    }

    public void startMinicap() throws InterruptedException {
        System.out.println("starting minicap...");
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    bash.executeMinicap();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        int i=30;
        while(i-- > 0){
            if(bash.isMinicapOpened()){
                System.out.println("MiniCap is Opened!!!");
                return;
            }else{
                Thread.sleep(1000);
            }
        }

    }


    public void getImage() throws InterruptedException {


        System.out.println("服务器启动，开始获取Image... ");

        InputStream stream = null;
        DataInputStream input = null;

        int pid = 0;
        int virtualHeight = 0;
        int virtualWidth = 0;
        int realWidth = 0;
        int realHeight = 0;


        try{
            socket = new Socket("127.0.0.1", minicapPort);
            System.out.println("Socket "+minicapPort+" connectd!");
            this.socketConnected = true;
            while (true) {
                if(!this.socketConnected){
                    System.out.println("Socket "+minicapPort+" Closed!!!!!!!");
                    this.ready = false;
                    if (socket != null && socket.isConnected()) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    return;
                }
                stream = socket.getInputStream();
                input = new DataInputStream(stream);
                byte[] buffer;
                int len = 0;
                while (len == 0) {
                    if(!this.socketConnected){
                        System.out.println("Socket "+minicapPort+" Closed!!!!!!!");
                        if (socket != null && socket.isConnected()) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        return;
                    }
                    Thread.sleep(5);
                    len = input.available();
                }
                buffer = new byte[len];
                input.read(buffer);
//                System.out.println("length=" + buffer.length);
                if(buffer.length == 4){
//                    System.out.println("content=" + buffer[0]);
                }
                if (debug) {
                    continue;
                }
                byte[] currentBuffer = subByteArray(buffer, 0, buffer.length);
                for (int cursor = 0; cursor < len;) {
                    int byte10 = buffer[cursor] & 0xff;
                    if (readBannerBytes < bannerLength) {
                        switch (readBannerBytes) {
                            case 0:
                                // version
                                System.out.println("version=" + byte10);
                                break;
                            case 1:
                                // length
                                bannerLength = byte10;
                                System.out.println("banner length=" + byte10);
                                break;
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                // pid
                                pid += (byte10 << ((readBannerBytes - 2) * 8)) >>> 0;
                                System.out.println("pid=" + pid);
                                break;
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                                // real width
                                realWidth += (byte10 << ((readBannerBytes - 6) * 8)) >>> 0;
                                System.out.println("realwidth=" + realWidth);
                                break;
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                                // real height
                                realHeight += (byte10 << ((readBannerBytes - 10) * 8)) >>> 0;
                                System.out.println("height=" + realHeight);
                                break;
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                                // virtual width
                                virtualWidth += (byte10 << ((readBannerBytes - 14) * 8)) >>> 0;
                                System.out.println("virtual width=" + virtualWidth);

                                break;
                            case 18:
                            case 19:
                            case 20:
                            case 21:
                                // virtual height
                                virtualHeight += (byte10 << ((readBannerBytes - 18) * 8)) >>> 0;
                                System.out.println("virtual height=" + virtualHeight);
                                break;
                            case 22:
                                // orientation
                                System.out.println("Orientation=" + byte10 * 90);
                                break;
                            case 23:
                                // quirks
                                System.out.println("Quirks=" + byte10);
                                break;
                        }

                        cursor += 1;
                        readBannerBytes += 1;

                        if (readBannerBytes == bannerLength) {
//                            LOG.info(banner.toString());
                        }
                    } else if (readFrameBytes < 4) {
                        // 第二次的缓冲区中前4位数字和为frame的缓冲区大小
//                        System.out.println("计算前的frameBodyLength : " + frameBodyLength);
                        frameBodyLength += (byte10 << (readFrameBytes * 8)) >>> 0;
                        cursor += 1;
                        readFrameBytes += 1;
                        total = frameBodyLength;

                    } else {
//                        System.out.println("图片大小 : " + total);
//                        System.out.println(String.format("len= %d, cursor = %d , frameBodyLength = %d  : ", len, cursor, frameBodyLength));
                        // LOG.info("frame body部分");
                        // LOG.info(String.format("设想图片的大小 : %d", total));
//                        if(total == 0){
//                            frameBodyLength = 0;
//                            break;
//                        }
                        if (len - cursor >= frameBodyLength) {
                            byte[] subByte = subByteArray(currentBuffer,
                                    cursor, cursor + frameBodyLength);
                            frameBody = byteMerger(frameBody, subByte);
                            if ((frameBody[0] != -1) || frameBody[1] != -40) {
                                System.out.println(String
                                        .format("Frame body does not start with JPG header"));
                                return;
                            }
//                            System.out.println(String.format("实际图片的大小 : %d",
//                                    frameBody.length));
//                            if (finalBytes == null) {
                            finalBytes = subByteArray(frameBody, 0,
                                    frameBody.length);
//                                System.out.println(String.format("实际图片的大小 : %d",
//                                        frameBody.length));
                            finalBytes = subByteArray(frameBody, 0,
                                    frameBody.length);
                            ready = true;
//                            }
                            cursor += frameBodyLength;
                            frameBodyLength = 0;
                            readFrameBytes = 0;
                            frameBody = new byte[0];
                        } else {
                            // LOG.debug(String.format("body(len=%d)", len
                            // - cursor));
                            byte[] subByte = subByteArray(currentBuffer,
                                    cursor, len);
                            frameBody = byteMerger(frameBody, subByte);
                            frameBodyLength -= (len - cursor);
                            readFrameBytes += (len - cursor);
                            cursor = len;
//                            System.out.println(String.format("Final cursor,len,frameBodyLen,readFramebytes : %d, %d, %d, %d", cursor,len,frameBodyLength,readBannerBytes));
                        }
                    }
                }
            }


        }
        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            if (socket != null && socket.isConnected()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }



    }


    // java合并两个byte数组
    private static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    private static byte[] subByteArray(byte[] byte1, int start, int end) {
        byte[] byte2 = new byte[end - start];
        System.arraycopy(byte1, start, byte2, 0, end - start);
        return byte2;
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv + " ");
        }
        return stringBuilder.toString();

    }


}


