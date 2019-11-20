package com.priyank.sendsms.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.priyank.sendsms.Model.GroupModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreference {

    public static SharedPreferences sharedPreferences;

    public static void enterPrefrences(Context context, String key, String value){
        sharedPreferences = context.getSharedPreferences("SendSMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void enterPreferenceList(Context context, String key, ArrayList<GroupModel> list) {
        sharedPreferences = context.getSharedPreferences("SendSMS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.commit();
    }

    public static SharedPreferences getPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SendSMS",Context.MODE_PRIVATE);
        return sharedPreferences;
    }


    public static String getPreferencesValues(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SendSMS",Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key,null);
        return value;
    }

    public static ArrayList<GroupModel> getPreferenceList(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SendSMS",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<GroupModel>>(){}.getType();
        ArrayList<GroupModel> list = gson.fromJson(json, type);
        return list;
    }



    public static void cleanPreferences(Context context, String key){
        context.getSharedPreferences("SendSMS",Context.MODE_PRIVATE).edit().remove(key).commit();
    }
}
