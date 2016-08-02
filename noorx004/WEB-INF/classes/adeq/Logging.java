package adeq;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logging {
public Logging(){
	
}

public void logaction(String action){
	File logfile = new File("Logfile.txt");
	String dtformat = new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
	try {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logfile,true)));
		out.println(action+" "+dtformat);
		out.close();
	} catch (Exception e) {
		System.out.println("Logfile Error Occured");
 	}
	}
}
