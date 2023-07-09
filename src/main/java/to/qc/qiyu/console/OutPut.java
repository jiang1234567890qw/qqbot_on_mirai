package to.qc.qiyu.console;

import java.lang.invoke.SwitchPoint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OutPut{
    // 定义彩色字体的颜色代码
    public static final String RESET = "\u001B[0m";  // 重置字体样式
    public static final String BLACK = "\u001B[30m"; // 黑色字体
    public static final String RED = "\u001B[31m";   // 红色字体
    public static final String GREEN = "\u001B[32m"; // 绿色字体
    public static final String YELLOW = "\u001B[33m";// 黄色字体
    public static final String BLUE = "\u001B[34m";  // 蓝色字体
    public static final String PURPLE = "\u001B[35m";// 紫色字体
    public static final String CYAN = "\u001B[36m";  // 青色字体
    public static final String WHITE = "\u001B[37m"; // 白色字体
    public static final String[] colorList = {BLACK,RED,GREEN,YELLOW,BLUE,PURPLE,CYAN,WHITE};
        static Date dNow = new Date( );
        static SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
    public static void print(String msg){
        System.out.print(ft.format(dNow));
        System.out.println(WHITE +msg);
    }
    public static void print(String type, String msg){
        System.out.print(ft.format(dNow));
        switch (type) {
            case "info":
            case "I":
                System.out.println(WHITE + "[INFO]" + msg + RESET);
                break;
            case "error":
            case "E":
                System.out.println(RED + "[ERROR]" + msg + RESET);
                break;
            case "waring":
            case "W":
                System.out.println(YELLOW + "[Waring]" + msg +RESET);
                break;
            default:
                System.out.println(msg);
        }
    }
    public static void print(String msg,int color){
        System.out.print(colorList[color] + ft.format(dNow));
        System.out.println(msg + RESET);
    }

}
