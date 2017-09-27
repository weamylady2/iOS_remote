package com.demo.ios;

import com.jfinal.core.Controller;

import com.jfinal.kit.PropKit;
import org.json.JSONException;
import org.json.JSONObject;
import com.demo.ios.HttpClientUtil;
import java.io.File;
import java.io.IOException;

public class IosController extends Controller {

    HttpClientUtil client = new HttpClientUtil();
    private ExecuteUtil bash = new ExecuteUtil();

    public static String sessionId="";

    public void index()  {
        render("ios.html");

    }

    public void initWda() throws JSONException, IOException, InterruptedException {

        String wdaPort = PropKit.get("wdaPort");
        String udid = bash.getUDID().trim();
        JSONObject tapLoc = new JSONObject("{\"desiredCapabilities\":{\"bundleId\":\"com.apple.Preferences\",\"arguments\":[],\"environment\":{},\"shouldWaitForQuiescence\":true,\"shouldUseTestManagerForVisibilityDetection\":false,\"maxTypingFrequency\":60,\"shouldUseSingletonTestManager\":true}}");
        System.out.println(tapLoc.toString());
        String tapResult = client.sendHttpPostJson("http://localhost:"+wdaPort+"/session",tapLoc.toString());
        System.out.println("God Result: " + tapResult);
        if(null == tapResult){
            render("WDA is no running!");
            return;
        }
        renderJson(tapResult);
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
        String wdaPort = PropKit.get("wdaPort");
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
        String wdaPort = PropKit.get("wdaPort");
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
        String wdaPort = PropKit.get("wdaPort");
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

    public void inputText() throws JSONException {
        if(this.sessionId == "") {
            getSessionId();
        }
        String wdaPort = PropKit.get("wdaPort");
        JSONObject inputPara = new JSONObject();
        inputPara.put("value",getPara("value").toCharArray());
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

    public void clear() throws JSONException {
        if(this.sessionId == "") {
            getSessionId();
        }
        JSONObject inputPara = new JSONObject();
        String wdaPort = PropKit.get("wdaPort");
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
        String wdaPort = PropKit.get("wdaPort");
        try {
            String status = client.sendHttpGet("http://localhost:"+wdaPort+"/status");
            JSONObject statusJson = null;
            statusJson = new JSONObject(status);
            this.sessionId = statusJson.get("sessionId").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
