package com.demo.ios;
import com.demo.ios.*;
import com.jfinal.kit.PropKit;

import java.io.*;


public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {

        PropKit.use("config.properties");
        System.out.println(PropKit.get("bashPath"));

        ExecuteUtil eu = new ExecuteUtil();

    }
//        Thread.sleep(60000);



}
