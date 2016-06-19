package com.apkdevs.android.codelib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CShell {
    //Variables:
        private Process process;
        private DataOutputStream dos;

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
            try {
                DataInputStream stdout = new DataInputStream(process.getInputStream());
                byte[] buffer = new byte[4096];
                int read;
                int i = 0;
                while (true) {
                    i++;
                    read = stdout.read(buffer);
                    output.add(new String(buffer, 0, read));
                    if (read < 4096) {
                        break;
                    }
                }
            } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOException");}
            return output;
        }

        public void write(String output) {
            try {
                dos = new DataOutputStream(process.getOutputStream());
                dos.writeBytes(output + "\n");
            } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOException");}
        }
        public void reset(String command) {
          if (command.equals("root")) {
            try {
              String currentpath = execute("pwd").get(0);
              process = Runtime.getRuntime().exec(new String[]{"su", "-s", "/system/bin/sh"});
              write("cd " + currentpath);
            } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOexception");}
          } else {
            try {
              String currentpath = execute("pwd").get(0);
              process = Runtime.getRuntime().exec(command);
              write("cd " + currentpath);
            } catch(IOException e) {e.printStackTrace(); CLog.E("Error: IOexception");}
          }
        }

    /*------------------------------*/
    //Static functions:
        public static ArrayList<String> execute(String command) {
            CShell shell = new CShell("root");
            shell.write(command);
            return shell.getOutput();
        }
}
