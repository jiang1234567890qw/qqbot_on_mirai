package to.qc.qiyu.BotFunction;

public class BotProcessCommandFromChat implements BotProcessCommand{
    public void processCommand(String msg){

    }

    @Override
    public String[] cutCommand(String msg) {
        return msg.split("\\s+");
    }

}
