package gui;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogStream {
    private PrintWriter writer;

    /**
     * Create an new logger, to log some usefull infos, like exceptions.
     * @param path the path of the file
     */
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

    /**
     * To close the stream
     */
    public void close(){
        writer.close();
    }

    /**
     * If you need to log an error
     * @param value the thing that need to be logged
     */
    public void Error(Object value) {
        Log(value, "ERROR");
    }

    /**
    *  If you need to log an information
    *  @param value the thing that need to be logged
    */
    public void Log(Object value){
        writer.println("[INFO] " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - " + value.toString());
        writer.flush();
    }

    /**
     * If you need to log something else
     * @param value the thing that need to be logged
     * @param status the level of priority of that thing
     */
    public void Log(Object value, String status){
        writer.println("[" + status + "] " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - " + value.toString());
        writer.flush();
    }
}
