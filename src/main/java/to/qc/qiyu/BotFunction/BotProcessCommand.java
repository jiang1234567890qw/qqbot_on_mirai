package to.qc.qiyu.BotFunction;

public interface BotProcessCommand {
    void processCommand(String msg);

    String[] cutCommand(String msg);
}
