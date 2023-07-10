package to.qc.qiyu.BotFunction;

import to.qc.qiyu.Utils.BotNetWorkUtils;

import java.io.IOException;


public class BotProcessCommandFromChat {
    private String url;
    private String session;


    public static void processCommand(String url, String session, String msg, Long replyId) throws IOException {
        String[] msgArray = msg.split("\\s+");

        System.out.println(msgArray[0]);
        switch (msgArray[0]){
            case "命令1":
                BotNetWorkUtils.sendPlainMessage(url,session,"对于命令1的回复",replyId);
                break;
            case "命令2":
                BotNetWorkUtils.sendPlainMessage(url,session,"对于命令2的回复",replyId);
                break;
            default:
                System.out.println(msgArray[0]);
        }

    }
    public void processCommand(String msg,Long replayId) throws IOException {
        processCommand(url,session,msg,replayId);
    }
    public void init(String url, String session){
        this.url = url;
        this.session = session;
    }


}
