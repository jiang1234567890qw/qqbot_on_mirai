package to.qc.qiyu.Utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HashDB implements Runnable{
    public HashMap<Integer, JSONObject> DailyDB;
    public HashMap<Integer,JSONObject> HalfDayDB;
    public HashMap<Integer,JSONObject> TimingDB;
    private boolean flag = true;

    @Override
    public void run() {
        if (flag){
            timer();
        }
    }
    private void timer() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        long ss = (c.getTime().getTime() - System.currentTimeMillis())/1000;
        // 创建任务队列   10 为线程数量
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(10);
        // 执行任务
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            //每天凌晨1点清理
            writeDailyCleanDB(114514, (JSONObject) new JSONObject().put("cleanAll",true));
        }, ss, 60*60*24, TimeUnit.SECONDS);
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.HOUR, 13);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        long ss1 = (c1.getTime().getTime() - System.currentTimeMillis())/1000;
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            //每天1,13点清理
            writeHalfDayCleanDB(114514, (JSONObject) new JSONObject().put("cleanAll",true));
        }, ss>ss1?ss1:ss, 60*60*12, TimeUnit.SECONDS);

    }
    public synchronized void writeDailyCleanDB(int qq,JSONObject data){
        if ((qq==114514) && data.getBoolean("cleanAll")){
            DailyDB.clear();
            return ;
        }
        DailyDB.put(qq,data);
    }
    public synchronized void writeHalfDayCleanDB(int qq,JSONObject data){
        if ((qq==114514) && data.getBoolean("cleanAll")){
            HalfDayDB.clear();
            return ;
        }
        HalfDayDB.put(qq,data);
    }
    public synchronized void writeTimingDB(int qq,JSONObject data){
        TimingDB.put(qq,data);
        //未完成
    }
    public void stop(){
        flag = false;
    }
}
