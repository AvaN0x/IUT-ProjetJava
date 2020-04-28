package gui;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogStream {
    private PrintWriter writer;

    public LogStream(String path){
        try{
            if((new File(path)).exists())
                (new File(path)).createNewFile();
            writer = new PrintWriter(path);
        }
        catch (FileNotFoundException ex){

        }
        catch (IOException ex){

        }
    }

    public void close(){
        writer.close();
    }

    public void Error(Object value) {
        Log(value, "ERROR");
    }

    public void Log(Object value){
        writer.println("[INFO] " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - " + value.toString());
        writer.flush();
    }

    public void Log(Object value, String status){
        writer.println("[" + status + "] " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - " + value.toString());
        writer.flush();
    }
}
