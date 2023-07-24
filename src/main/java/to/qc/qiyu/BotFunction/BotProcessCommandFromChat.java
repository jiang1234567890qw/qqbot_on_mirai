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


    public static void processCommandFromGroup(String url, String session, String msg, Long replyId,long senderId) throws IOException {
        String[] msgArray = msg.split("\\s+");

        switch (msgArray[0]){
            case "今日老婆":
                dailyWife(url, session, replyId,senderId);
                break;
            case "命令2":
                BotNetWorkUtils.sendPlainMessageToGroup(url,session,"对于命令2的回复",replyId);
                break;
            default:
                System.out.println(msgArray[0]);
        }

    }

    public void processCommandFromGroup(String msg, Long replayId,Long senderId) throws IOException {
        processCommandFromGroup(url,session,msg,replayId,senderId);
    }
    public void init(String url, String session){
        this.url = url;
        this.session = session;
    }
    private static void dailyWife(String url, String session, Long replyId, Long senderId) throws IOException {
        if(HashDB.groupMemberCache == null || HashDB.groupMemberCache.get(replyId) == null || System.currentTimeMillis() - HashDB.groupMemberCache.get(replyId).getLongValue("LastChangeTime") >= 60*60*1000) {
            JSONObject memberList = BotNetWorkUtils.getMemberList(url, session, replyId);
            if (memberList == null) {
                OutPut.print("E", "获取" + replyId + "成员失败");
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
            HashDB.groupMemberCache.put(replyId,t);
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

        if (HashDB.dailyDB.get(senderId)==null || HashDB.dailyDB.get(senderId).getBoolean("isGotWife") != true) {

            Long targetId;
            while (true) {
                Random r = new Random(System.currentTimeMillis());
                int num = r.nextInt(HashDB.groupMemberCache.get(replyId).getJSONArray("idList").size());
                targetId = HashDB.groupMemberCache.get(replyId).getJSONArray("idList").getLongValue(num);
                if (!targetId.equals(senderId)) break;
            }


            JSONObject wifeProfile = BotNetWorkUtils.getMemberProfile(url, session, replyId, targetId);
            wifeId = targetId;
            wifeNickName = wifeProfile.getString("nickname");
            msg1.put("text", "\n今天你的老婆是" + wifeNickName + "(" + wifeId + ")");

            JSONObject j = new JSONObject();
            j.put("isGotWife", true);
            j.put("wifeNickName", wifeNickName);
            j.put("wifeId", wifeId);
            HashDB.writeDailyCleanDB(senderId, j);

        }else {
            wifeNickName = HashDB.dailyDB.get(senderId).getString("wifeNickName");
            wifeId = HashDB.dailyDB.get(senderId).getLongValue("wifeId");
            msg1.put("text","\n今天你已经取过了，ta是"+wifeNickName+"("+wifeId+")");
        }


        msg2.put("type","Image");
        msg2.put("url","https://q.qlogo.cn/g?b=qq&nk=" + wifeId + "&s=160");

        msgChain.add(0,msg0);
        msgChain.add(1,msg1);
        msgChain.add(2,msg2);
        BotNetWorkUtils.sendMessageToGroup(url,session,msgChain,replyId);
    }

}
