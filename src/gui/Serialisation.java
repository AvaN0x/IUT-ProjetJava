package gui;

import java.io.*;
import java.nio.file.Files;

public class Serialisation {
    private ByteArrayOutputStream data = new ByteArrayOutputStream();

    public void write(Object obj) {
        {
            try {
                var writer = new ObjectOutputStream(data);
                writer.writeObject(obj);
                writer.close();
            } catch (IOException ex) {
                // Catch exception
            }
        }
    }

    public Object read() {
        Object result = new Object();
        try {
            var reader = new ObjectInputStream(new ByteArrayInputStream(data.toByteArray()));
            result = reader.readObject();
            reader.close();
        } catch (IOException ex) {
            // Catch exception
        } catch (ClassNotFoundException ex) {
            // Catch exception
        }
        return result;
    }

    public void saveToFile(String filename) {
        try (var outputStream = new FileOutputStream("data/" + filename)) {
            data.writeTo(outputStream);
            outputStream.close();
        } catch (IOException ex) {
            // Catch exception
        }
        data.reset();
    }

    public void loadFromFile(String filename) {
        try {
            byte[] bFile = Files.readAllBytes(new File("data/" + filename).toPath());
            data = new ByteArrayOutputStream(bFile.length);
            data.write(bFile, 0, bFile.length);
        } catch (IOException ex) {
            // Catch exception
        }
    }

    public void close(){
        try {
            data.close();
        } catch (IOException ex) {
            // Catch exception
        }
    }
}
