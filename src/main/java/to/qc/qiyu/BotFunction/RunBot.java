package to.qc.qiyu.BotFunction;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import to.qc.qiyu.console.OutPut;

import java.io.IOException;

import static to.qc.qiyu.Utils.BotNetWorkUtils.*;

public class RunBot implements Runnable{
        static boolean flag;
        @Override
        public void run() {
                try {
                        botMain();
                } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                }
        }
        public static void botMain() throws IOException, InterruptedException {
                flag = true;
                String url = "http://192.168.0.123:8666";
                String key = "123456";
                Long qq = 2730157909L;

                String session = botInit(url,key,qq);
                if (session == null){
                        OutPut.print("E","无法正常绑定，请手动输入");
                        OutPut.print("URL(直接回车为默认值):",3);
                }
                while (flag){
                        while (true){
                                if (getMessageCount(url,session) == 0){
                                        Thread.sleep(2000);
                                        break;
                                }
                        }
                        JSONObject msgJson = getOldestMessageAndRemove(url,session,5);
                        if (msgJson.getIntValue("code") != 0){
                                OutPut.print("E","获取未读消息失败"+msgJson);
                                continue;
                        }
                        JSONArray data = msgJson.getJSONArray("data");
                        for (Object datum : data) {
                                JSONObject temp = JSONObject.parseObject(datum.toString());

                                if (!temp.get("type").toString().equals("GroupMessage")) continue;
                                JSONObject sender = temp.getJSONObject("sender");
                                JSONObject group = sender.getJSONObject("group");
                                Long id = group.getLong("id");
                                Long senderId = sender.getLongValue("id");
                                JSONArray messageChain = temp.getJSONArray("messageChain");
                                for (Object o : messageChain) {
                                        JSONObject t = JSONObject.parseObject(o.toString());
                                        if (!t.get("type").equals("Plain")) continue;
                                        String message = t.get("text").toString();
                                        BotProcessCommandFromChat.processCommandFromGroup(url, session, message, id,senderId);

                                }

                        }

                }
                releaseSession(url,session,qq);
        }
        public static void closeBot(){
                flag = false;
        }
}
