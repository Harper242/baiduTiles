
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
    int xmin = 0;// x��Сֵ
    int xmax = 0;// x���ֵ
    int ymin = 0;// y��Сֵ
    int ymax = 0;// y���ֵ

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
        { // ѭ��X
            for (int j = ymin; j <= ymax; j++)
            { // ѭ��Y
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
                        bWriter.write("�ļ���������" + file);
                        bWriter.newLine();
                        bWriter.flush();
                        System.out.println("�ļ���������" + file);
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
                    bWriter.write("�ɹ�����" + file);
                    bWriter.newLine();
                    bWriter.flush();
                    // System.out.println("�ѳɹ�����:" + z + "_" + i + "_" + j +
                    // ".jpg");
                }
                catch (Exception e)
                {
                    try
                    {
                        bWriter.write("�ļ�����ʧ�ܣ�" + savePath + z + "/" + i + "/"
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

            } // ѭ��Y����
        } // ѭ��X����
        System.out.println("������:" + xmin + "��" + xmax);
        try
        {
            bWriter.write("������:" + xmin + "��" + xmax);
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
        System.out.print("���ܵ���" + xmin + "��" + xmax);
        System.out.println("   " + ymin + "��" + ymax);
    }
}
