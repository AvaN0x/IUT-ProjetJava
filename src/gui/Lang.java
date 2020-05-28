package gui;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Lang {
    public String title;
    public String close;
    public String retry;
    public String error;
    public String cancel;
    public String validate;

    public String settings;
    public String settings_lang;
    public String settings_save;
    public String settings_local;
    public String settings_db;
    public String settings_host;
    public String settings_login;
    public String settings_pass;
    public String settings_base;
    public String settings_reload;

    public String connect_title;
    public String connect_try;
    public String connect_error;
    public String connect_error_question;
    public String connect_sucess;

    public String loading_title;
    public String loading_error;
    public String local_version;

    public String file;

    public String save_title;
    public String save;
    public String save_db_warn;

    public String edit;

    public String user_list;
    public String users;
    public String new_user;
    public String del_user;
    public String del_user_confirm;
    public String del_user_fail;
    public String edit_user;
    public String user_info;
    public String loyal_tooltip;

    public String orders;
    public String new_order;
    public String del_order;
    public String del_order_confirm;
    public String edit_order;
    public String edit_expired_order;
    public String order_info;
    public String export_order;
    public String order_date;
    public String order_date_warn;
    public String order_date_error;
    public String order_list;

    public String loans;
    public String new_loan;
    public String add_loan;
    public String rem_loan;
    public String edit_loan;
    public String loan_date_warn;
    public String loan_days;

    public String product_list;
    public String products;
    public String new_product;
    public String del_product;
    public String del_product_confirm;
    public String del_product_fail;
    public String edit_product;
    public String product_info;
    public String available_products;
    public String product_stock_error;

    public String export;

    public String field_name;
    public String field_first_name;
    public String field_client;
    public String field_loyal;
    public String field_date_start;
    public String field_date_end;
    public String field_order;
    public String field_title;
    public String field_type;
    public String field_price;
    public String field_categ;
    public String field_reduc;
    public String field_cost;
    public String field_sum;
    public String field_sum_reduc;
    public String field_available;
    public String field_loans;
    public String field_loaned;
    public String field_releaseDate;
    public String field_auteur;
    public String field_realisateur;
    public String field_quantity;
    public String field_langue;

    public String date_invalid;
    public String number_invalid;
    public String days_invalid;
    public String field_empty;
    public String field_wrong;
    public String name_error;

    public String class_BD;
    public String class_CD;
    public String class_Dictionnaire;
    public String class_DVD;
    public String class_ManuelScolaire;
    public String class_Roman;
	public String restart_lang;

    public Lang() {
        try {
            var jo = (JSONObject) new JSONParser()
                    .parse(new FileReader("lang/" + Utils.settings.language.toString() + ".json"));

            for (var field : Lang.class.getDeclaredFields()) {
                try {
                    field.set(this, (String) jo.get(field.getName()));
                } catch (IllegalAccessException e) {
                    try {
                        field.set(this, (String) field.getName());
                    } catch (IllegalAccessException | IllegalArgumentException ex) {
                        Utils.logStream.Error(e);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            Utils.logStream.Error(e);
        }
    }
}
