package com.demo.ios;

import com.jfinal.core.Controller;

import com.jfinal.kit.PropKit;
import org.json.JSONException;
import org.json.JSONObject;
import com.demo.ios.HttpClientUtil;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import com.jfinal.upload.UploadFile;
import java.util.UUID;
import java.io.BufferedReader;  
import java.io.InputStreamReader;  
import java.util.ArrayList;  
import java.util.List;  

public class IosController extends Controller {

    HttpClientUtil client = new HttpClientUtil();
    private ExecuteUtil bash = new ExecuteUtil();

    public static String sessionId="";
    public static String wdaPort = PropKit.get("wdaPort");

    public void index() throws IOException, InterruptedException, JSONException {

        render("ios.html");

    }

    public void initWda() throws JSONException, IOException, InterruptedException {

//        String wdaPort = PropKit.get("wdaPort");
        JSONObject tapLoc = new JSONObject("{\"desiredCapabilities\":{\"bundleId\":\"com.apple.Preferences\",\"arguments\":[],\"environment\":{},\"shouldWaitForQuiescence\":true,\"shouldUseTestManagerForVisibilityDetection\":false,\"maxTypingFrequency\":60,\"shouldUseSingletonTestManager\":true}}");
        System.out.println(tapLoc.toString());
        String tapResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session",tapLoc.toString());
        System.out.println("God Result: " + tapResult);
        if(null == tapResult){
            render("WDA is no running!");
            return;
        }
        home();
        renderJson(tapResult);
    }

    public void getDeviceInfo() throws IOException, InterruptedException, JSONException {
        String udid = bash.getUDID();
        System.out.println("Got UDID: " + udid);
        String name = bash.getDeviceName();
        String apps = bash.getInstalledApp();
        System.out.println("Got Apps: " + apps);

        JSONObject para = new JSONObject();
        para.put("udid",udid);
        para.put("name",name);
        para.put("apps",apps);
        renderJson(para.toString());
    }


    public void home() throws JSONException {

        String result = client.sendHttpPost("http://localhost:8200/wda/homescreen");
        renderJson(result);
    }

    public void windowsize() throws JSONException {

        if(this.sessionId == "") {
            getSessionId();
        }
        String size = client.sendHttpGet("http://localhost:8200/session/" + this.sessionId+ "/window/size");
        renderJson(size);
    }

    public void tap() throws JSONException {
        if(this.sessionId == "") {
            getSessionId();
        }
//        String wdaPort = PropKit.get("wdaPort");
        JSONObject tapLoc = new JSONObject();
        tapLoc.put("x",getPara("x"));
        tapLoc.put("y",getPara("y"));
        System.out.println(tapLoc.toString());
        String tapResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session/" + this.sessionId+ "/wda/tap/0",tapLoc.toString());

        if(null == tapResult){
            render("WDA is no running!");
            return;
        }
        if(tapResult.contains("Session does not exist")){
            getSessionId();
            tapResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session/" + this.sessionId+ "/wda/tap/0",tapLoc.toString());
        }
        renderJson(tapResult);
    }

    public void tapHold() throws JSONException {
        if(this.sessionId == "") {
            getSessionId();
        }
//        String wdaPort = PropKit.get("wdaPort");
        JSONObject tapPara = new JSONObject();
        tapPara.put("x",getPara("x"));
        tapPara.put("y",getPara("y"));
        tapPara.put("duration",getPara("duration"));
        System.out.println(tapPara.toString());
        String tapResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session/" + this.sessionId+ "/wda/touchAndHold",tapPara.toString());
        if(null == tapResult){
            render("WDA is no running!");
            return;
        }
        if(tapResult.contains("Session does not exist")){
            getSessionId();
            tapResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session/"  + this.sessionId+ "/wda/touchAndHold",tapPara.toString());
        }

        renderJson(tapResult);
    }



    public void swipe() throws JSONException {
        if(this.sessionId == "") {
            getSessionId();
        }
//        String wdaPort = PropKit.get("wdaPort");
        JSONObject swipePara = new JSONObject();
        swipePara.put("fromX",getPara("fromX"));
        swipePara.put("fromY",getPara("fromY"));
        swipePara.put("toX",getPara("toX"));
        swipePara.put("toY",getPara("toY"));
        swipePara.put("duration",getPara("duration"));
        System.out.println(swipePara.toString());
        String swipeResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session/"  + this.sessionId+ "/wda/dragfromtoforduration",swipePara.toString());
        if(null == swipeResult){
            render("WDA is no running!");
            return;
        }
        if(swipeResult.contains("Session does not exist")){
            getSessionId();
            swipeResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session/"  + this.sessionId+ "/wda/dragfromtoforduration",swipePara.toString());
        }

        renderJson(swipeResult);
    }

    public void inputText() throws JSONException, UnsupportedEncodingException {
        if(this.sessionId == "") {
            getSessionId();
        }
//        String wdaPort = PropKit.get("wdaPort");
        JSONObject inputPara = new JSONObject();
        String gotValue = URLDecoder.decode(getPara("value"), "utf-8");
        inputPara.put("value",gotValue.toCharArray());
        System.out.println("#####got value： " + gotValue);
        String inputResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session/"  + this.sessionId+ "/wda/keys",inputPara.toString());
        if(null == inputResult){
            render("WDA is no running!");
            return;
        }

        if(inputResult.contains("Session does not exist")){
            getSessionId();
            inputResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session/"  + this.sessionId+ "/wda/keys",inputPara.toString());
        }

        renderJson(inputResult);
    }


    public void launchapp() throws JSONException {

//        String wdaPort = PropKit.get("wdaPort");
        String bundleid = getPara("id");
        System.out.println("lanuching app id: " + bundleid);

        JSONObject launchCaps = new JSONObject("{\"desiredCapabilities\":{\"bundleId\":\""+bundleid+"\",\"arguments\":[],\"environment\":{},\"shouldWaitForQuiescence\":true,\"shouldUseTestManagerForVisibilityDetection\":false,\"maxTypingFrequency\":60,\"shouldUseSingletonTestManager\":true}}");
        System.out.println(launchCaps.toString());
        String launchResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session",launchCaps.toString());
        System.out.println("Got Result: " + launchResult);
        if(null == launchResult){
            render("WDA is no running!");
            return;
        }
        renderJson(launchResult);
        getSessionId();

    }

    public void clear() throws JSONException {
        if(this.sessionId == "") {
            getSessionId();
        }
        JSONObject inputPara = new JSONObject();
//        String wdaPort = PropKit.get("wdaPort");
        char[] delText = new char[30];
        for(int i=0;i<30;i++)
        {
            delText[i] = '\b';
        }

        inputPara.put("value",delText);
        System.out.println(inputPara.toString());
        String inputResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session/"  + this.sessionId+ "/wda/keys",inputPara.toString());
        if(null == inputResult){
            render("WDA is no running!");
            return;
        }
        if(inputResult.contains("Session does not exist")){
            getSessionId();
            inputResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session/"  + this.sessionId+ "/wda/keys",inputPara.toString());
        }

        renderJson(inputResult);
    }



    public void getSessionId(){
//        String wdaPort = PropKit.get("wdaPort");
        try {
            String status = client.sendHttpGet("http://localhost:"+wdaPort+"/status");
            JSONObject statusJson = null;
            statusJson = new JSONObject(status);
            this.sessionId = statusJson.get("sessionId").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void upload() throws JSONException {


        UploadFile uploadFile=this.getFile();


        String fileName=uploadFile.getOriginalFileName();

        JSONObject map = new JSONObject();
        File file=uploadFile.getFile();
        FileService fs=new FileService();
        File t=new File("/upload/file/"+file.getName());
        System.out.println("File generated: " + t.getAbsolutePath());
        try {
            t.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            map.put("status", false);
            map.put("msg", "服务器异常！");
        }

        

        map.put("status", true);
        map.put("msg", "上传成功！");

        
        //String command1 = "ideviceinstaller -i "+"/upload/file"+t;
        String command1 = "ideviceinstaller --udid 0c6c0a77471f5ae76dfdc55b57e0d1502ab92aaa -i /Users/xiatian/Desktop/Flow800.ipa";
        Process process = null;  

        List<String> processList = new ArrayList<String>(); 
        
        try {  
            process = Runtime.getRuntime().exec(command1);  
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));  
            String line = "";  
            while ((line = input.readLine()) != null) {  
                processList.add(line); 
            }  
            input.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        for (String line : processList) {  
            System.out.println(line);  
        }  
        fs.fileChannelCopy(file, t);
        file.delete();

        this.renderJson(map);
    }




}
