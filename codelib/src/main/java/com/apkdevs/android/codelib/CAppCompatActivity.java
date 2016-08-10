package com.apkdevs.android.codelib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.apkdevs.android.codelib.ui.TypefaceSpan;

public class CAppCompatActivity extends AppCompatActivity {
	// Variables
		// Private
			private Resources Res = null;

	// Alerting
		// Toasts
			public void Alert(String text) {Alert(text, Toast.LENGTH_SHORT);}
			public void Alert(String text, Integer time) {Toast.makeText(getApplicationContext(), text, time).show();}

	// Support Action Bar
		// Getting
			public ActionBar getSAB() {
				ActionBar actionbar = getSupportActionBar();
				ActionBar result = null;
				if (actionbar == null) {
					Log.e("apkdevs.lib.Activity", "getSupportActionBar (invoked from getSupportActionBar replacement getSAB) returns null");
					finish();
				} else {
					result = actionbar;
				}
				return result;
			}

		// Setting
			public void setSAB(Toolbar toolbar) {
				setSupportActionBar(toolbar);
			}

	// onCreate tools
		protected void onCreate(Bundle b) {
			super.onCreate(b);
			Res = getResources();
			/*if (Res != null) {
				int tlbr = Res.getClass("id").getInt("toolbar");
			}*/
		}

	// FindVIew improvements
		public View findView(int id) {
				View view = findViewById(id);
				View result = null;
				if (view == null) {
						Log.e("CAppCompatActivity", "findViewById (invoked from findViewById replacement findView) for " + id + " returns null");
						finish();
				} else {
						result = view;
				}
				return result;
		}
		public View findView(int id, String label) {
				//For debugging
				View view = findViewById(id);
				View result = null;
				if (view == null) {
						Log.e("CAppCompatActivity", "findViewById (invoked from findViewById replacement findView) for '" + label + "' returns null");
						finish();
				} else {
						result = view;
				}
				return result;
		}

	// Stuff
		public SpannableString newCFontedText(String text, String font) {
				SpannableString s = new SpannableString(text);
				s.setSpan(new TypefaceSpan(getApplicationContext(), font), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				return s;
		}

	// Short forms for stuff
		public Context getAppContext() {return getApplicationContext();}
		public SharedPreferences getPrefs() {return getPreferences(0);}
		public void launchActivity(Class<?> launchclass) {startActivity(new Intent(getAppContext(), launchclass));}
}

