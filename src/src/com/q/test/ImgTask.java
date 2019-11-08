
package com.q.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class ImgTask extends Thread
{
    String z;
    int xmin = 0;// x最小值
    int xmax = 0;// x最大值
    int ymin = 0;// y最小值
    int ymax = 0;// y最大值

    String savePath;

    @Override
    public void run()
    {
        File logfile = new File(xmin + "to" + xmax);
        BufferedWriter bWriter = null;
        if (!logfile.exists())
        {
            try
            {
                logfile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            bWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(logfile, true), "utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }

        String link = "http://online0.map.bdimg.com/tile/?qt=vtile&x={x}&y={y}&z={z}&styles=pl&scaler=1&udt=20181205&xxx={rand}";
        for (int i = xmin; i <= xmax; i++)
        { // 循环X
            for (int j = ymin; j <= ymax; j++)
            { // 循环Y
                try
                {
                    File dir = new File(savePath + z + "/" + i);
                    if (!dir.exists())
                    {
                        dir.mkdirs();
                    }
                    File file = new File(
                            savePath + z + "/" + i + "/" + j + ".png");
                    // System.out.println(file);
                    if (!file.exists())
                    {
                        file.createNewFile();
                    }
                    else
                    {
                        bWriter.write("文件存在跳过" + file);
                        bWriter.newLine();
                        bWriter.flush();
                        System.out.println("文件存在跳过" + file);
                        continue;
                    }
                    
                    URL url = new URL(link.replace("{x}", i + "")
                            .replace("{y}", j + "").replace("{z}", z + "")
                            .replace("{rand}", new Date().getTime() + ""));
                    //System.out.println(url);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setConnectTimeout(100);
                    conn.connect();
                    InputStream in = conn.getInputStream();
                    OutputStream out = new FileOutputStream(file);
                    byte[] bytes = new byte[1024 * 20];
                    int len = 0;
                    while ((len = in.read(bytes)) != -1)
                    {
                        out.write(bytes, 0, len);
                    }
                    out.close();
                    in.close();
                    bWriter.write("成功下载" + file);
                    bWriter.newLine();
                    bWriter.flush();
                    // System.out.println("已成功下载:" + z + "_" + i + "_" + j +
                    // ".jpg");
                }
                catch (Exception e)
                {
                    try
                    {
                        bWriter.write("文件下载失败：" + savePath + z + "/" + i + "/"
                                + j + ".png");
                        bWriter.newLine();
                        bWriter.flush();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                    System.out.println(e.getMessage());
                }

            } // 循环Y结束
        } // 循环X结束
        System.out.println("跑完了:" + xmin + "到" + xmax);
        try
        {
            bWriter.write("跑完了:" + xmin + "到" + xmax);
            bWriter.newLine();
            bWriter.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            try
            {
                bWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String getSavePath()
    {
        return savePath;
    }

    public void setSavePath(String savePath)
    {
        this.savePath = savePath;
    }

    public ImgTask()
    {
    }

    public ImgTask(String z, int xmin, int xmax, int ymin, int ymax)
    {
        super();
        this.z = z;
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        System.out.print("我跑的是" + xmin + "到" + xmax);
        System.out.println("   " + ymin + "到" + ymax);
    }
}
