package com.hasawi.sears.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHandler {

    public static final int TOKEN_LOGIN = 0;
    public static final int TOKEN_PRE_LOGIN = 1;
    //    TOKEN_PRE_LOGIN
    public static final String PRE_LOGIN_TOKEN = "token";
    public static final String PRE_LOGIN_IV = "iv";
    public static final String PRE_LOGIN_TOKEN_OBJECT_ID = "_id";
    public static final String PRE_LOGIN_BLOCKED = "version_blocked_pre";

    public static final String PRE_FIREBASE = "fire_base";
    public static final String PRE_DEVICE_ID = "device_id";
    public static final String PRE_DEVICE_ID_READABLE = "device_id_rd";


    //    TOKEN_LOGIN
    public static final String FIREBASE_TOKEN = "firebase_token";
    public static final String LOGIN_TOKEN = "lg_tkn";
    public static final String LOGIN_IV = "lg_iv";
    public static final String LOGIN_SESSION_TOKEN = "lg_stkn";
    public static final String LOGIN_SESSION_IV = "lg_siv";


    public static final String LOGIN_STATUS = "login_status";
    public static final String LOGIN_USER_ID = "login_user_id";
    public static final String LOGIN_USERNAME = "username";
    public static final String LOGIN_GENDER = "gender";
    public static final String LOGIN_PHONENUMBER = "phone_number";
    public static final String LOGIN_NATIONALITY = "nationality";
    public static final String LOGIN_EMAIL = "email";
    public static final String LOGIN_CATEGORY_ID = "category_id";
    public static final String LOGIN_CATEGORY_NAME = "category_name";
    public static final String LOGIN_PASSWORD = "login_password";
    public static final String LOGIN_CONFIRM_PASSWORD = "login_confirm_password";

    public static final String LOGIN_ITEM_TO_BE_WISHLISTED = "item_to_be_wishlisted";
    public static final String LOGIN_ITEM_TO_BE_CARTED = "item_to_be_carted";
    public static final String LOGIN_USER_CART_ID = "cart_id";
    public static final String LOGIN_USER_SELECTED_ADDRESS_ID = "selected_address_id";
    public static final String LOGIN_USER_SELECTED_SHIPPING_MODE_ID = "selected_shipping_mode_id";
    public static final String HAS_CATEGORY_PAGE_SHOWN = "has_category_page_shown";

    private final Context context;
    String selected = "";

    public PreferenceHandler(Context context, int TOKEN_TYPE) {
        this.context = context;
        selected = getStringPreferenceName(TOKEN_TYPE);
    }

    public void saveData(String name, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(selected, Context.MODE_PRIVATE);
        boolean b = sharedPreferences.edit().putBoolean(name, value).commit();
    }

    public void saveData(String name, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(selected, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(name, value).commit();
    }

    public void saveData(String name, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(selected, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(name, value).commit();
    }

    public String getData(String name, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(selected, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, defaultValue);
    }

    public int getData(String name, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(selected, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(name, defaultValue);
    }

    public boolean getData(String name, boolean defaultValue) {
        return context.getSharedPreferences(selected, Context.MODE_PRIVATE).getBoolean(name, defaultValue);
    }

    public boolean contains(String name) {
        return context.getSharedPreferences(selected, Context.MODE_PRIVATE).contains(name);
    }

    public void deleteData(String name) {
        if (contains(name))
            try {
                context.getSharedPreferences(selected, Context.MODE_PRIVATE).edit().remove(name).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void deleteSession() {
        try {
            context.getSharedPreferences(selected, Context.MODE_PRIVATE).edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getStringPreferenceName(int TOKEN) {
        String out = "";
        switch (TOKEN) {
            case TOKEN_LOGIN:
                out = "lg";
                break;
            case TOKEN_PRE_LOGIN:
                out = "pre";
        }
        return out;
    }
}
