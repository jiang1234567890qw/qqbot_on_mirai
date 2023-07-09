package to.qc.qiyu;


import to.qc.qiyu.BotFunction.RunBot;

import java.io.IOException;


public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException {
        new Thread(new RunBot()).start();
    }
}