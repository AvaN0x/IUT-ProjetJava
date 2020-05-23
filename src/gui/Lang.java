package gui;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Lang {
    public String title;
    public String connect_title;
    public String connect_try;
    public String connect_error;
    public String connect_sucess;
    public String loading_title;
    public String loading_error;

    public Lang(){
        try {
            // TODO: en_US == en_EN + si .getDefault non trouvé
            var obj = new JSONParser().parse(new FileReader("lang/"+Utils.settings.language.toString() + ".json"));
            var jo = (JSONObject) obj;
            
            for(var field : Lang.class.getDeclaredFields()) {
                field.set(this, (String) jo.get(field.getName()));
            }
        } catch (Exception e) {
            Utils.logStream.Error(e);
        }
    }
}
