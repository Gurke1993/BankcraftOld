package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimerTask;
import org.bukkit.entity.Player;

public class ZinsenTimerTask extends TimerTask{
	  FileWriter writer;
	  public void run()  {
	  try {
		//  GELDzinsen
		  File fB;
		  File fA = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts");
	       Player[] playerArray = Bankcraft.server.getOnlinePlayers();
		  File[] fileArray = fA.listFiles();
			for (int i = 0; i < fileArray.length; i++) {
				File file = fileArray[i];
		         FileReader fr = new FileReader(file);
		            BufferedReader reader = new BufferedReader(fr);
		            String st = reader.readLine(); 
			            	Double betrag = new Integer(st)*(Bankcraft.interest+1);
		   fr.close();
		   reader.close();
        writer = new FileWriter(file);
	       writer.write(betrag.intValue()+System.getProperty("line.separator"));
	       writer.flush();
	       writer.close();
	       if (Bankcraft.broadcast == true) {
	       Double zinsen = new Integer(st)*(Bankcraft.interest);
	       for (int j =0; j< playerArray.length; j++) {
	    	   fB = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts"+System.getProperty("file.separator")+playerArray[j].getName()+".db");
	    	   if ( fB.isFile() == true ) {
	    		   playerArray[j].sendMessage(Bankcraft.color+Bankcraft.prefix+zinsen.intValue()+" money granted! You now have "+betrag.intValue()+" on your Account!");
	    	   }
	       }
	       }
			}
	       
	       //EXPZinsen
			  File fAt = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts");
		       Player[] playerArrayt = Bankcraft.server.getOnlinePlayers();
			  File[] fileArrays = fAt.listFiles();
				for (int it = 0; it < fileArrays.length; it++) {
					File filet = fileArrays[it];
			         FileReader frt = new FileReader(filet);
			            BufferedReader readert = new BufferedReader(frt);
			            String stt = readert.readLine(); 
				            	Double betragt = new Integer(stt)*(Bankcraft.interestxp+1);
			   frt.close();
			   readert.close();
	        writer = new FileWriter(filet);
		       writer.write(betragt.intValue()+System.getProperty("line.separator"));
		       writer.flush();
		       writer.close();
		       if (Bankcraft.broadcastxp == true) {
			       Double zinsent = new Integer(stt)*(Bankcraft.interest);
			       for (int j =0; j< playerArrayt.length; j++) {
			    	   fB = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts"+System.getProperty("file.separator")+playerArray[j].getName()+".db");
			    	   if ( fB.isFile() == true ) {
			    		   playerArrayt[j].sendMessage(Bankcraft.color+Bankcraft.prefix+zinsent.intValue()+" XP granted! You now have "+betragt.intValue()+" XP on your Account");
			    	   }
			       }
			       }
			}
			}
 catch (IOException e) {
    e.printStackTrace();
	  }
}
}