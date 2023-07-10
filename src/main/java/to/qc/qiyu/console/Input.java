package to.qc.qiyu.console;

import to.qc.qiyu.BotFunction.RunBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input implements Runnable{
    @Override
    public void run() {
        this.getLine();
    }
    public String getLine(){
        InputStreamReader isr = new InputStreamReader(System.in); //这样是为了将数据传入流中，相当于数据流的入口
        BufferedReader br = new BufferedReader(isr);
        while (true){
            OutPut.print(">");
            String temp;
            try {
                temp = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (temp.equals("stop")){
                RunBot.closeBot();
                return null;
            }
        }


    }
}

