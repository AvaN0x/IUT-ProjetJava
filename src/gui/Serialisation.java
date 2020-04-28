package gui;

import java.io.*;

public class Serialisation {
    private WriteData writer;
    private ReadData reader;

    public Serialisation() {
        var saveFile = new File(Utils.savingDir + "data.ser");
        if (!new File(Utils.savingDir).exists())
            new File(Utils.savingDir).mkdir();
        if (!saveFile.exists())
            try {
                saveFile.createNewFile();
            } catch (IOException ex) {

            }
        writer = new WriteData(saveFile);
        reader = new ReadData(saveFile);
    }

    public void writeData(Object obj) {
        try {
            writer.write(obj);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void writeClose() {
        try {
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public Object readData() {
        var result = new Object();
        try {
            result = reader.read();
        } catch (IOException ex) {

        } catch (ClassNotFoundException ex) {

        }
        return result;
    }

    public void readClose() {
        try {
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}

class WriteData {
    private OutputStream stream;

    public WriteData(File savefile) {
        try {
            stream = new FileOutputStream(savefile);
        } catch (FileNotFoundException ex) {

        }
    }

    public void write(Object obj) throws IOException {
        var writer = new ObjectOutputStream(stream);
        writer.writeObject(obj);
        writer.flush();
    }

    public void close() throws IOException {
        stream.close();
    }

}

class ReadData {
    private InputStream stream;

    public ReadData(File savefile) {
        try {
            stream = new FileInputStream(savefile);
        } catch (FileNotFoundException ex) {

        }
    }

    public Object read() throws IOException, ClassNotFoundException {
        var reader = new ObjectInputStream(stream);
        var result = reader.readObject();
        reader.close();
        return result;
    }

    public void close() throws IOException {
        stream.close();
    }
}
