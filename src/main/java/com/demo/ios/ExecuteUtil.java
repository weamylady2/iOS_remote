package com.demo.ios;

import java.io.*;
import com.jfinal.kit.PropKit;

public class ExecuteUtil {


    public static final int SUCCESS = 0;            // 表示程序执行成功

    public static final String SUCCESS_MESSAGE = "程序执行成功！";

    public static final String ERROR_MESSAGE = "程序执行出错：";

    public static boolean wdaOpened = false;
    public static boolean minicapOpened = false;
    public static boolean minicapConnected = false;

    public boolean isWdaOpened(){
        return wdaOpened;
    }
    public boolean isMinicapOpened(){
        return minicapOpened;
    }
    public boolean isMinicapConnected(){
        return minicapConnected;
    }

    public void executeMinicap() throws IOException, InterruptedException {

        String minicapPath = PropKit.get("minicapPath");
        ProcessBuilder pb = new ProcessBuilder(minicapPath+"/run.sh");
        pb.directory(new File(minicapPath));
        Process p = pb.start();
//        int exitCode = p.waitFor();
        readProcessOutput(p);

        // 等待程序执行结束并输出状态
        int exitCode = p.waitFor();

        if (exitCode == SUCCESS) {
            System.out.println(SUCCESS_MESSAGE);
        } else {
            System.err.println(ERROR_MESSAGE + exitCode);
        }
    }

    public void executeWDA() throws IOException, InterruptedException {

        String minicapPath = PropKit.get("bashPath");
        ProcessBuilder pb = new ProcessBuilder(minicapPath+"/execWDA.sh");
        Process p = pb.start();
//        int exitCode = p.waitFor();

        Thread.sleep(15000);
        wdaOpened = true;
        readProcessOutput(p);

        // 等待程序执行结束并输出状态
        int exitCode = p.waitFor();

        if (exitCode == SUCCESS) {
            System.out.println(SUCCESS_MESSAGE);
        } else {
            System.err.println(ERROR_MESSAGE + exitCode);
        }
    }


    public void exitMincap() throws IOException, InterruptedException {

        String minicapPath = PropKit.get("bashPath");
        ProcessBuilder pb = new ProcessBuilder(minicapPath+"/exitMinicap.sh");
        Process p = pb.start();
//        int exitCode = p.waitFor();
        readProcessOutput(p);

        // 等待程序执行结束并输出状态
        int exitCode = p.waitFor();

        minicapOpened = false;
        minicapConnected = false;

        if (exitCode == SUCCESS) {
            System.out.println(SUCCESS_MESSAGE);
        } else {
            System.err.println(ERROR_MESSAGE + exitCode);
        }
    }

    public String getUDID() throws IOException, InterruptedException {

        String bashPath = PropKit.get("bashPath");

        ProcessBuilder pb = new ProcessBuilder(bashPath + "/getUDID.sh");
        Process p = pb.start();
        p.waitFor();
        return getOutputString(p.getInputStream()).trim();

    }


    public void exitWDA() throws IOException, InterruptedException {

        String minicapPath = PropKit.get("bashPath");
        ProcessBuilder pb = new ProcessBuilder(minicapPath+"/exitWDA.sh");
        Process p = pb.start();

//        int exitCode = p.waitFor();
        readProcessOutput(p);

        // 等待程序执行结束并输出状态
        int exitCode = p.waitFor();

        wdaOpened = false;

        if (exitCode == SUCCESS) {
            System.out.println(SUCCESS_MESSAGE);
        } else {
            System.err.println(ERROR_MESSAGE + exitCode);
        }
    }


    private static void readProcessOutput(final Process process) {
        // 将进程的正常输出在 System.out 中打印，进程的错误输出在 System.err 中打印
        read(process.getInputStream(), System.out);
        read(process.getErrorStream(), System.err);
    }

    // 读取输入流
    private static void read(InputStream inputStream, PrintStream out) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
                if (line.contains("quirks")){
                    Thread.sleep(3000);
                    minicapOpened=true;
                } else if (line.contains("New client connection")){
                    minicapConnected=true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 读取输入流
    private static String getOutputString(InputStream inputStream) {
        String s = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
//                out.println(line);
                s +=line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;
    }





}
