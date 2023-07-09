package to.qc.qiyu.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import to.qc.qiyu.console.OutPut;

import java.io.IOException;

import static java.lang.Thread.sleep;


public class BotNetWorkUtils {
    public static final String verify ="/verify";
    public static final String bind = "/bind";
    public static final String release= "/release";


    @org.jetbrains.annotations.Nullable
    public static String botInit(String url, String key, Long qq) throws IOException {
        String target = url + verify;

        String res = HttpUtils.sendJsonPostRequest(target, "{\"verifyKey\""+":"+key+"}");
        if (res == null) {
            OutPut.print("E","认证返回值为空");
            return null;
        }

        JSONObject tempjson = JSONObject.parseObject(res);
        String session = (String) tempjson.get("session");
        if (session == null) {
            OutPut.print("E","获取session为空");
            return null;
        }else {
            OutPut.print("I","获取session成功:"+session);
        }

        JSONObject sendJson = new JSONObject();
        sendJson.put("qq",qq);
        sendJson.put("sessionKey",session);
        String target1 = url + bind;
        String msg = sendJson.toJSONString();
        //sleep(1000);
        String res1 = HttpUtils.sendJsonPostRequest(target1,msg);

        JSONObject revjson = JSON.parseObject(res1);
        String success = "success";
        if (!revjson.get("msg").equals(success)){
            OutPut.print("E","绑定失败:");
            OutPut.print(revjson.toString());
            return null;
        }else {
            OutPut.print("绑定成功",2);
        }
        return session;
    }
    public static int releaseSession(String url, String session,Long qq) throws IOException {
        String target = url + release;
        JSONObject sendJson = new JSONObject();
        sendJson.put("qq",qq);
        sendJson.put("sessionKey",session);
        JSONObject revJson = JSONObject.parseObject(HttpUtils.sendJsonPostRequest(target,sendJson.toString()));
        String a = "success";
        if(revJson.get("msg").equals(a)){
            OutPut.print("I",session+"释放成功");
            return 0;
        }else {
            OutPut.print("E",session+"释放失败"+ revJson.toString());
            return -1;
        }
    }
    public static int getMessageCount(String url, String session) throws IOException {
        String target = url + "/countMessage?sessionKey="+session;
        JSONObject revJson = JSONObject.parseObject(HttpUtils.sendGetRequest(target));
        if (revJson.getIntValue("code") == 0 ){
            return revJson.getIntValue("data");
        }else {
            OutPut.print("E","获取未读信息数量异常，请检查相关组件"+revJson.toString());
            return -1;
        }
    }
    public static JSONObject getOldestMessageAndRemove(String url, String session,int count) throws IOException {
        String target = url +"fetchMessage?sessionKey="+session+"&count="+count;
        JSONObject revJson = JSONObject.parseObject(HttpUtils.sendGetRequest(target));
        if (!(revJson.getIntValue("code") == 0)){
            return null;
        }else {
            return revJson;
        }
    }
    public static JSONObject getLatestMessageAndRemove(String url, String session,int count) throws IOException {
        String target = url +"fetchLatestMessage?sessionKey="+session+"&count="+count;
        JSONObject revJson = JSONObject.parseObject(HttpUtils.sendGetRequest(target));
        if (!(revJson.getIntValue("code") == 0)){
            return null;
        }else {
            return revJson;
        }
    }
    public static JSONObject getOldestMessageButNotRemove(String url, String session,int count) throws IOException {
        String target = url +"peekMessage?sessionKey="+session+"&count="+count;
        JSONObject revJson = JSONObject.parseObject(HttpUtils.sendGetRequest(target));
        if (!(revJson.getIntValue("code") == 0)){
            return null;
        }else {
            return revJson;
        }
    }
    public static JSONObject getLatestMessageButNotRemove(String url, String session,int count) throws IOException {
        String target = url +"peekLatestMessage?sessionKey="+session+"&count="+count;
        JSONObject revJson = JSONObject.parseObject(HttpUtils.sendGetRequest(target));
        if (!(revJson.getIntValue("code") == 0)){
            return null;
        }else {
            return revJson;
        }
    }
}
