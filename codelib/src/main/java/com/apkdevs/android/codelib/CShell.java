package com.apkdevs.android.codelib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CShell {
    private Process process;
    private DataOutputStream dos;
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
    public String getOutput() {
        String output = "";
        try {
            DataInputStream stdout = new DataInputStream(process.getInputStream());
            byte[] buffer = new byte[4096];
            int read;
            while (true) {
                read = stdout.read(buffer);
                output += new String(buffer, 0, read);
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
    public void clearOutput() {
        write("clear");
    }
}
