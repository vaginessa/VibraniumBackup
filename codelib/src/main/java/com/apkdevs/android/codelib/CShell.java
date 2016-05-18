package com.apkdevs.android.codelib;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class CShell {
    private Process process;
    DataInputStream dis;
    DataOutputStream dos;

    public CShell(String command, String args) {
        try {
            process = Runtime.getRuntime().exec(command + " " + args);
            process.waitFor();
        } catch(Exception e) {e.printStackTrace();}
    }
    public String[] getOutput() {
        String[] output = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                output[i] = line + "\n";
                CLog.V(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    public void exec(String command, String args) {

    }
}
