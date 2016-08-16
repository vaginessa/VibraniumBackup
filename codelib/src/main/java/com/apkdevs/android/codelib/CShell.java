package com.apkdevs.android.codelib;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CShell {
  //Variables:
    private Process process;
    private DataOutputStream dos;
		private DataInputStream dis;

  //Variable-based functions:
    //Constructors:
      public CShell(String[] command) {
        if (command[0].equals("root")) {
          try {
            process = Runtime.getRuntime().exec(new String[]{"su", "-s", "/system/bin/sh"});
          } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOexception");}
        } else {
          try {
            process = Runtime.getRuntime().exec(command);
          } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOexception");}
        }
      }

      public CShell(String command) {
        if (command.equals("root")) {
          try {
            process = Runtime.getRuntime().exec(new String[]{"su", "-s", "/system/bin/sh"});
          } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOexception");}
        } else {
          try {
            process = Runtime.getRuntime().exec(command);
          } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOexception");}
        }
      }

    public ArrayList<String> getOutput() {
      ArrayList<String> output = new ArrayList<>();
      /*try {dis = new DataInputStream(process.getInputStream());byte[] buffer = new byte[4096];int read;int i = 0;while (true) {i++;read = dis.read(buffer);
      output.add(new String(buffer, 0, read));if (read < 4096) {break;}}} catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOException");}*/
			/*InputStream in = process.getInputStream();
			try {CLog.V("" + in.available());
				for (int i = 0; i < in.available(); i++) output.add("" + in.read()); in.close();
			} catch(IOException e) { e.printStackTrace(); CLog.E("Error: IOException"); }*/
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = reader.readLine())!= null) output.add(line);
			} catch (Exception e) {e.printStackTrace();}
			return output;
    }

		public String getOutput(int line) {return getOutput().get(line);}

    public CShell write(String output) {
      try {
        dos = new DataOutputStream(process.getOutputStream());
        dos.writeBytes(output + "\n");
      } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOException");}
			return this;
    }
    public void reset(String command) {
      if (command.equals("root")) {
        try {
          String currentpath = write("pwd").getOutput().get(0);
          process = Runtime.getRuntime().exec(new String[]{"su", "-s", "/system/bin/sh"});
          write("cd " + currentpath);
        } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOexception");}
      } else {
    	  try {
          String currentpath = execute("pwd").get(0);
          process = Runtime.getRuntime().exec(command);
          write("cd " + currentpath);
        } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOexception"); CLog.V(e.getClass().getPackage().getName()); }
      }
    }
		public Process getProc() { return process; }
		public void waitForEnd() { try { process.waitFor(); } catch(InterruptedException e) { e.printStackTrace(); } }

	/*------------------------------*/
  //Static functions:
    public static ArrayList<String> execute(String command) {
      CShell shell = new CShell(command);
			return shell.getOutput();
    }
		public static boolean checkSU() { return execute("/system/bin/sh -c \"su -c exit\"; echo \"BLAH\"").indexOf("BLAH") == 0; }
}
