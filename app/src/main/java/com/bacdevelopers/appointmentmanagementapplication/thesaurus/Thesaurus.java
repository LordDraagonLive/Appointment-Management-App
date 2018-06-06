package com.bacdevelopers.appointmentmanagementapplication.thesaurus;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.bacdevelopers.appointmentmanagementapplication.models.Synonyms;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Buddhi Adhikari 16280809 on 28/04/2018.
 **/
public class Thesaurus {
    final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1";
    private String word;
    private final String language="en_US";
    private final String key="4ly9mQuYMpkkmVzEK4xE";
    private String output="json";
    private Context context;

    public Thesaurus(String word, Context context) {
        this.word = word;
        this.context=context;
    }

    public Thesaurus(String word, String output,Context context) {
        this.word = word;
        this.output = output;
        this.context=context;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Synonyms> sendRequest(){

        List<Synonyms>synonymsList= new ArrayList<>();

        try {
//            URL serverAddress = new URL(endpoint+"?word="+ word+"&language="+language+"&key="+key+"&output="+output);
            URL serverAddress = new URL(endpoint + "?word="+URLEncoder.encode(word, "UTF-8")+"&language="+language+"&key="+key+"&output="+output);
            HttpURLConnection connection = (HttpURLConnection)serverAddress.openConnection();
            connection.connect();
            int rc = connection.getResponseCode();
            if(rc==200) {
//                Toast.makeText(context,"HTTP Code: "+rc,Toast.LENGTH_LONG).show();
                String line = null;
                BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line + '\n');
                }
                JSONObject obj = (JSONObject) JSONValue.parse(sb.toString());
                JSONArray array = (JSONArray) obj.get("response");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject list = (JSONObject) ((JSONObject) array.get(i)).get("list");
//                    System.out.println(list.get("category")+":"+list.get("synonyms"));
                    Synonyms synonyms = new Synonyms();
                    synonyms.setCategory(list.get("category").toString());
                    //uncomment if we need to remove the brackets
//                    synonyms.setSynonym(list.get("synonyms").toString().replaceAll("\\(.*?\\)",""));
                    //not well formatted but this code piece is important as it shows if it's an Antonym
                    synonyms.setSynonym(list.get("synonyms").toString());
                    synonymsList.add(synonyms);
                }
            }else if(rc==404){
                Toast.makeText(context," No Matches Found!",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context,"HTTP Error: "+rc+". Use only alphanumeric values!",Toast.LENGTH_SHORT).show();
            }
            connection.disconnect();
        } catch (MalformedURLException e) {
            Toast.makeText(context,"MalformedURLException Occurred",Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(context,"UnsupportedEncodingException Occurred",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context,"IOException Occurred: \n1. Check your connection.\n 2.Check the values you entered",Toast.LENGTH_LONG).show();
        }
        return synonymsList;
    }


}
