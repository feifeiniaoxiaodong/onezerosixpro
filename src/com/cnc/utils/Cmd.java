package com.cnc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.util.Log;

public class Cmd {
	private static final String Tag = "hnctest";

	public static String ExeCmd(String cmd) {
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
        	Log.i(Tag, "Create runtime false!");
        }
        try {
            pro = runTime.exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line=null;
            while ((line = input.readLine()) != null) {		//读取数据
                returnString = returnString + line + "\n"; //行与行之间添加换行符
            }
            
            input.close();
            output.close();
            pro.destroy();
            
        } catch (IOException e) {
        	Log.i(Tag, e.getMessage());
        }
 
        return returnString;
    }

}
