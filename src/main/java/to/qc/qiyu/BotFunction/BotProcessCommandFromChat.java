package to.qc.qiyu.BotFunction;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import to.qc.qiyu.Utils.BotNetWorkUtils;
import to.qc.qiyu.Utils.HashDB;
import to.qc.qiyu.console.OutPut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class BotProcessCommandFromChat {
    private String url;
    private String session;
    private static String[] luckyLevel = {"大凶","凶","平","吉","大吉"};
    private static Random random = new Random(System.currentTimeMillis());
    public static void processCommandFromGroup(String url, String session, String msg, long groupId,long senderId) throws IOException {
        String[] msgArray = msg.split("\\s+");

        switch (msgArray[0]){
            case "今日老婆":
                dailyWife(url, session, groupId,senderId);
                break;
            case "今日幸运":
            case ".jrxy":
                todayLucky(url, session, groupId,senderId);
                break;
            case "今日运势":
            case ".jrrp":
                todayLuckyLevel(url, session, groupId,senderId);
                break;
            default:
                System.out.println(msgArray[0]);
        }

    }

    public void processCommandFromGroup(String msg, Long replyId,Long senderId) throws IOException {
        processCommandFromGroup(url,session,msg,replyId,senderId);
    }
    public void init(String url, String session){
        this.url = url;
        this.session = session;
    }
    private static void dailyWife(String url, String session, Long groupId, Long senderId) throws IOException {
        if(HashDB.groupMemberCache == null || HashDB.groupMemberCache.get(groupId) == null || System.currentTimeMillis() - HashDB.groupMemberCache.get(groupId).getLongValue("LastChangeTime") >= 60*60*1000) {
            JSONObject memberList = BotNetWorkUtils.getMemberList(url, session, groupId);
            if (memberList == null) {
                OutPut.print("E", "获取" + groupId + "成员失败");
                return;
            }
            JSONArray data = memberList.getJSONArray("data");
            ArrayList<Long> idList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                idList.add(data.getJSONObject(i).getLongValue("id"));
            }
            JSONObject t =new JSONObject();
            t.put("idList", JSONArray.parseArray(JSONObject.toJSONString(idList)));
            t.put("LastChangeTime",System.currentTimeMillis());
            HashDB.groupMemberCache.put(groupId,t);
        }

        Long wifeId;
        String wifeNickName;
        JSONArray msgChain = new JSONArray();
        JSONObject msg0 = new JSONObject();
        JSONObject msg1 = new JSONObject();
        JSONObject msg2 = new JSONObject();

        msg0.put("type","At");
        msg0.put("target",senderId);
        msg0.put("display","来自mirai的老婆");

        msg1.put("type","Plain");

        if (HashDB.dailyDB.get(senderId)==null || HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()) == null || HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()).getBoolean("isGotWife") == null || //防止null
                !HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()).getBoolean("isGotWife")) {

            Long targetId;
            while (true) {
                int num = random.nextInt(HashDB.groupMemberCache.get(groupId).getJSONArray("idList").size());
                targetId = HashDB.groupMemberCache.get(groupId).getJSONArray("idList").getLongValue(num);
                if (!targetId.equals(senderId)) break;
            }

            JSONObject wifeProfile = BotNetWorkUtils.getMemberProfile(url, session, groupId, targetId);
            wifeId = targetId;
            wifeNickName = wifeProfile.getString("nickname");
            msg1.put("text", "\n今天你的老婆是" + wifeNickName + "(" + wifeId + ")");

            JSONObject j = new JSONObject();
            j.put("isGotWife", true);
            j.put("wifeNickName", wifeNickName);
            j.put("wifeId", wifeId);

            writeUserDataToDailyDB(groupId,senderId,j);

        }else {
            wifeNickName = HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()).getString("wifeNickName");
            wifeId = HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()).getLongValue("wifeId");
            msg1.put("text","\n今天你已经取过了，ta是"+wifeNickName+"("+wifeId+")");
        }


        msg2.put("type","Image");
        msg2.put("url","https://q.qlogo.cn/g?b=qq&nk=" + wifeId + "&s=160");

        msgChain.add(0,msg0);
        msgChain.add(1,msg1);
        msgChain.add(2,msg2);
        BotNetWorkUtils.sendMessageToGroup(url,session,msgChain,groupId);
    }
    private static void todayLucky(String url, String session, Long groupId, Long senderId) throws IOException {
        JSONArray msgChain = new JSONArray();
        JSONObject msg0 = new JSONObject();
        JSONObject msg1 = new JSONObject();

        msg0.put("type","At");
        msg0.put("target",senderId);
        msg0.put("display","今日幸运");

        msg1.put("type","Plain");

        if (HashDB.dailyDB.get(senderId) == null || HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString())==null || HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()).getBoolean("isGotLucky") == null
                || !HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()).getBoolean("isGotLucky")) {
            int lucky = random.nextInt(200) - 100;
            msg1.put("text", "\n今天你的幸运值是" + lucky);
            JSONObject j = new JSONObject();
            j.put("isGotLucky",true);
            j.put("lucky",lucky);
            writeUserDataToDailyDB(groupId,senderId,j);
        }else {
            msg1.put("text", "\n今天你已经使用过了,幸运值是" + HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()).getIntValue("lucky"));
        }
        msgChain.add(0,msg0);
        msgChain.add(1,msg1);
        BotNetWorkUtils.sendMessageToGroup(url,session,msgChain,groupId);
    }
    private  static void todayLuckyLevel(String url, String session, Long groupId, Long senderId) throws IOException {
        JSONArray msgChain = new JSONArray();
        JSONObject msg0 = new JSONObject();
        JSONObject msg1 = new JSONObject();


        msg0.put("type","At");
        msg0.put("target",senderId);
        msg0.put("display","今日运势");

        msg1.put("type","Plain");

        if (HashDB.dailyDB.get(senderId) == null ||HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString())==null || HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()).getBoolean("isGotLuckyLevel") == null
                ||!HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()).getBoolean("isGotLuckyLevel")) {
            int luckyNum = random.nextInt(4);
            msg1.put("text", "\n今天你的运势是" + luckyLevel[luckyNum]);
            JSONObject j = new JSONObject();
            j.put("isGotLuckyLevel",true);
            j.put("luckyLevel",luckyNum);
            writeUserDataToDailyDB(groupId,senderId,j);

        }else {
            msg1.put("text", "\n今天你已经使用过了,运势是" + luckyLevel[HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()).getIntValue("luckyLevel")]);
        }
        msgChain.add(0,msg0);
        msgChain.add(1,msg1);
        BotNetWorkUtils.sendMessageToGroup(url,session,msgChain,groupId);
    }
    private static void writeUserDataToDailyDB(Long groupId,Long senderId,JSONObject data){
        JSONObject j;
        JSONObject j1;


        if (HashDB.dailyDB.get(senderId) == null){
            j = new JSONObject();
            j1 = new JSONObject();
        }else {
            j = HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString());
            if ( HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString()) == null){
                j1 = new JSONObject();
            }else {
                j1 = HashDB.dailyDB.get(senderId).getJSONObject(groupId.toString());
            }
        }
        j1.putAll(data);
        j.put(groupId.toString(),j1);
        HashDB.writeDailyCleanDB(senderId,j);


    }
}

