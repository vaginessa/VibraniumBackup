package com.apkdevs.android.codelib;

import android.app.Activity;
import android.widget.Toast;

public class CActivity extends Activity {
    public void Alert(String text) {Alert(text, Toast.LENGTH_SHORT);}
    public void Alert(String text, Integer time) {Toast.makeText(getApplicationContext(), text, time).show();}
}
