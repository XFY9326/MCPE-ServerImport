package server.xfy9326.test;

import android.app.*;
import android.os.*;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import java.io.File;
import android.widget.Toast;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends Activity 
{
    private String filepath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/games/com.mojang/minecraftpe/external_servers.txt";
    public String[] server_name = {"Test"};
    public String[] server_ip = {"0.0.0.0"};
    public String[] server_port = {"19132"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button add_button = (Button)findViewById(R.id.button_add);
        add_button.setOnClickListener(new OnClickListener(){
                public void onClick(View view)
                {
                    for (int i = 0;i < server_ip.length;i++)
                    {
                        server_add(server_name[i], server_ip[i], server_port[i]);
                    }
                }
            });
    }

    private void server_add(String name, String ip, String port)
    {
        String file_data;
        File file = new File(filepath);
        int num = 1;
        pathset(filepath);
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                show(getString(R.string.set_err));
            }
        }
        try
        {
            String data = readfile(new FileInputStream(file));
            if (data.indexOf("\n") < 0)
            {
                String server_data = num + ":" + name + ":" + ip + ":" + port;
                writefile(filepath, server_data);
                show(getString(R.string.set_ok));
            }
            else
            {
                String[] str = data.split("\n");
                Boolean rewrite = false;
                String[] str_num = new String[str.length];
                for (int i = 0;i < str.length;i++)
                {
                    if (str[i].indexOf(":") >= 0)
                    {
                        String[] str_data = str[i].split(":");
                        str_num[i] = str_data[0];
                        for (int ii = 0;ii < server_ip.length;ii++)
                        {
                            if (str_data[2].equalsIgnoreCase(server_ip[ii]) == true && str_data[3].equalsIgnoreCase(server_port[ii]) == true)
                            {
                                rewrite = true;
                                break;
                            }
                        }
                    }
                }
                if (!rewrite)
                {
                    num = Integer.parseInt(str_num[str_num.length - 1]) + 1;
                    String server_data = num + ":" + name + ":" + ip + ":" + port;
                    file_data = data + server_data;
                    writefile(filepath, file_data);
                    show(getString(R.string.set_ok));
                }
                else
                {
                    show(getString(R.string.set_re));
                }
            }
        }
        catch (FileNotFoundException e)
        {
            show(getString(R.string.set_err));
        }
    }

    private String readfile(InputStream file_stream)
    {
        try
        {
            String output = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(file_stream)); 
            String line = ""; 
            while ((line = reader.readLine()) != null)
            { 
                output += line + "\n";
            }
            reader.close();
            return output;
        }
        catch (IOException e)
        {
            return "Failed";
        }
    }

    private boolean writefile(String path, String data)
    {
        try
        {
            File file = new File(path);
            pathset(path);
            byte[] Bytes = new String(data).getBytes();
            if (file.exists())
            {
                if (file.isFile())
                {
                    OutputStream writer = new FileOutputStream(file);
                    writer.write(Bytes);
                    writer.close();
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                file.createNewFile();
                OutputStream writer = new FileOutputStream(file);
                writer.write(Bytes);
                writer.close();
                return true;
            }
        }
        catch (IOException e)
        {
            return false;
        }
    }

    private void show(String str)
    {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void pathset(String path)
    {
        String[] dirs = path.split("/");
        String pth = "";
        for (int i = 0;i < dirs.length;i++)
        {
            if (i != dirs.length - 1)
            {
                pth += "/" + dirs[i];
            }
        }
        File dir = new File(pth);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
    }
}
