package com.apkdevs.android.codelib;

import android.graphics.Color;
import android.util.Log;

import java.util.HashMap;

public class Prefs {           //This is a settings class, used for storing SharedPreferences (SharedPreferences)
    //Variables
        private HashMap<String, Color> colors = null;

    public Prefs() {}

    public Color getColor(String name) {
        Color color = colors.get(name);
        if (color != null) {return color;}
        Log.e("Preferences", "The requested color '" + name + "' is null");
        return null;
    }
    public void setColor(String name, Color color) {
        colors.put(name, color);
    }
    /*public void setColor(String name, int red, int green, int blue) {
        colors.put(name, );
    }*/
    public void setColors(HashMap<String, Color> allColors) {
        colors.putAll(allColors);
    }
}
