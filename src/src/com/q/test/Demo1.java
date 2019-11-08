
package com.q.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Demo1
{
    static volatile Integer c = 0;// 成功数
    static volatile Integer fail = 0;// 失败数量
    static String zIndex;
    static Integer xmin;
    static Integer ymin;
    static Integer xmax;
    static Integer ymax;
    static Integer threadSize;
    static String savePath;
    static
    {
        Properties pro = new Properties();
        try
        {
            // 开发用这一行 读取当前目录配置文件
            // pro.load(Demo1.class.getResourceAsStream("config.properties"));
            // 打jar包用这一行 方便外部修改配置文件
            pro.load(new FileInputStream("config.properties"));
            zIndex = pro.getProperty("zIndex");
            savePath = pro.getProperty("savepath");
            String leftbottomx = pro.getProperty("left-bottom-x");
            String leftbottomy = pro.getProperty("left-bottom-y");
            String righttopx = pro.getProperty("right-top-x");
            String righttopy = pro.getProperty("right-top-y");
            String threadsize = pro.getProperty("thread-size");
            xmin = Integer.valueOf(leftbottomx);
            ymin = Integer.valueOf(leftbottomy);
            xmax = Integer.valueOf(righttopx);
            ymax = Integer.valueOf(righttopy);
            threadSize = Integer.valueOf(threadsize);
            System.out.println("读取数据完成xmin:" + xmin + " ymin:" + ymin + " xmax:"
                    + xmax + " ymax:" + ymax);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void getImg()
    {

        // 以x分开多线程
        Integer needRunTaskSize = xmax - xmin;
        Integer erevtSize = needRunTaskSize / threadSize;

        for (int i = 0; i < threadSize; i++)
        {
            // 算出这个线程的范围
            ImgTask imgTask = new ImgTask(zIndex, erevtSize * (i) + xmin,
                    erevtSize * (i + 1) + xmin, ymin, ymax);
            imgTask.setSavePath(savePath);
            imgTask.start();
        }

        int lave = needRunTaskSize % threadSize;
        if (lave != 0)
        {
            // 多跑一个处理剩余的
            ImgTask imgTask = new ImgTask(zIndex, xmax - lave, xmax, ymin,
                    ymax);
            imgTask.setSavePath(savePath);
            imgTask.start();
        }
    }

    public static void main(String[] args) throws Exception
    {
        new Demo1().getImg();;

    }
}
