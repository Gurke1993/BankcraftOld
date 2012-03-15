package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import org.bukkit.entity.Player;

public class bankInteract {
	@SuppressWarnings("unused")
	private Bankcraft plugin;
	public bankInteract (Bankcraft b1) {
		this.plugin= b1;
	}
	public static double getTotalXp(Player player) {
		
	    int level = player.getLevel();
	    float playerExpp = player.getExp();
	    int LevelinXP = (int)Math.round((1.75*level*level) + 5.00*level);
	    double Exp1 = (playerExpp*(3.5*level+6.7))+LevelinXP;
	    return Exp1;
	}
	
	  public static double removeExp(Player player, int value) {
        Integer Exp1 = Integer.valueOf((int)getTotalXp(player));
        int Expaktuell = (int) Exp1 - value;
        if (Expaktuell >= 0.0D) {
        	player.setLevel(0);
        	player.setExp(0);
        	player.setTotalExperience(0);
        	player.giveExp(Expaktuell);
          
       //     p.giveExp(remain);
       //   remain = value;
        }
        else
        {
        	Exp1 = -1;
        }

        return Exp1;
      }
	
    public static Integer getTypeBank(Integer cordX1, Integer cordY1, Integer cordZ1) throws Exception{
    	Integer type=-1;
        File f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"banks.db");
        f.createNewFile();
        FileReader fr = new FileReader(f);
        BufferedReader reader = new BufferedReader(fr);
        String st = "";
        while ((st = reader.readLine()) != null) {
       	 Integer cordX = new Integer(st.split(":")[0]);
       	 Integer cordY = new Integer(st.split(":")[1]);
       	 Integer cordZ = new Integer(st.split(":")[2]);
        	if ((cordX.equals(cordX1)) & (cordY.equals(cordY1)) & (cordZ.equals(cordZ1))) {
        	type = new Integer(st.split(":")[3]);
    }
        }
    return type;
    }
    
	  public static double kontoneu(Double betrag,Player p, Boolean all){
		  FileWriter writer;
		  File file, file2;
		  Double enough =-2.00;
		    // File anlegen
		     file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts");
             file2 = new File(file+System.getProperty("file.separator")+p.getName()+".db");
		     try {
	                if (!file2.exists()) {
	                    file.mkdirs();
	                    writer = new FileWriter(file2, true);
	                    writer.write("0.00");
	     		        writer.write(System.getProperty("line.separator"));  
	     		        writer.flush();
	    		        writer.close();
	                }
			         FileReader fr = new FileReader(file2);
			            BufferedReader reader = new BufferedReader(fr);
			            String st = reader.readLine(); 
			            	if ( new Double(st) >= -betrag) {
				            	enough = -betrag;
				            	betrag += new Double(st);
			            	} else {
			            		betrag = new Double(st);
			            		enough = -2.00;
			            	}
			            	if (all==true) {
			            		betrag = 0.00;
			            		enough = new Double(st);
			            	}
			   fr.close();
			   reader.close();
           	DecimalFormat df= new DecimalFormat("#0.00");   
           	String betragstring= df.format(betrag);  
           	betrag = Double.parseDouble(betragstring); 
               writer = new FileWriter(file2);
		       writer.write(betrag+System.getProperty("line.separator"));
		       writer.flush();
		       writer.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
			return enough;
	  }
	  
	  public static Integer kontoneuxp(Integer betrag,Player p, Boolean all){
		  FileWriter writer;
		  File file, file2;
		  Integer enough =-2;
		    // File anlegen
		     file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts");
             file2 = new File(file+System.getProperty("file.separator")+p.getName()+".db");
		     try {
	                if (!file2.exists()) {
	                    file.mkdirs();
	                    writer = new FileWriter(file2, true);
	                    writer.write("0");
	     		        writer.write(System.getProperty("line.separator"));  
	     		        writer.flush();
	    		        writer.close();
	                }
			         FileReader fr = new FileReader(file2);
			            BufferedReader reader = new BufferedReader(fr);
			            String st = reader.readLine(); 
			            	if ( new Integer(st) >= -betrag) {
				            	enough = -betrag;
				            	betrag += new Integer(st);
			            	} else {
			            		betrag = new Integer(st);
			            		enough = -2;
			            	}
			            	if (all==true) {
			            		betrag = 0;
			            		enough = new Integer(st);
			            	}
			   fr.close();
			   reader.close();
               writer = new FileWriter(file2);
		       writer.write(betrag+System.getProperty("line.separator"));
		       writer.flush();
		       writer.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
			return enough;
	  }

}
