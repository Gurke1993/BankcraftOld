package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class BankcraftBlockListener implements Listener {
	private static final BlockFace[] faces =  new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
	  FileWriter writer;
	  File file, file2;
	  
	  public void speichern(int x, int y, int z, int typ, Integer betrag ){
		    // File anlegen
		     file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft");
		     try {
	                if (!file.exists()) {
	                    file.mkdirs(); }
	                file2 = new File(file+System.getProperty("file.separator")+"banks.db");
		       writer = new FileWriter(file2 ,true);
		       
		       // Text wird in den Stream geschrieben
		       writer.write(x+":"+y+":"+z+":"+typ+":"+betrag);
		       writer.write(System.getProperty("line.separator"));     
		       writer.flush();
		       
		       // Schlieﬂt den Stream
		       writer.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	  }
	  public void delete(int x, int y, int z){
		    // File anlegen
		     file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"banks.db");
		     try {
         		String stringneu = "";
		         FileReader fr = new FileReader(file);
		            BufferedReader reader = new BufferedReader(fr);
		            String st = "";
		            while ((st = reader.readLine()) != null) {
		           	 Integer cordX = new Integer(st.split(":")[0]);
		           	 Integer cordY = new Integer(st.split(":")[1]);
		           	 Integer cordZ = new Integer(st.split(":")[2]);
		            	if (!(cordX == x & cordY == y & cordZ == z)) {
		            		stringneu += st+System.getProperty("line.separator");
		            	}
		            }
				       fr.close();
				       reader.close();
		       writer = new FileWriter(file);
	           writer.write(stringneu);		       
		       writer.flush();
		       writer.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	  }
	public boolean isInteger( String input )  {  
		   try  
		   {  
		      Integer.parseInt( input );  
		      return true;  
		   }  
		   catch (Exception e)
		   { 
		      return false;  
		   }  
		}  

	
	
	@EventHandler
	public void onBlockDestroy(BlockBreakEvent event) throws Exception {
    Player p = event.getPlayer();
    Block testblock = event.getBlock();
    Material type = testblock.getType();
  //Check blocks around destroyed block for signs
    for(BlockFace face: faces){
    type = testblock.getRelative(face).getType();
    Block nachbarblock = testblock.getRelative(face);
     
    //if sign found
    if(type == Material.WALL_SIGN){
    	if ((nachbarblock.getData() == 3 & face.equals(BlockFace.WEST)) | (nachbarblock.getData() == 2 & face.equals(BlockFace.EAST)) | (nachbarblock.getData() == 4 & face.equals(BlockFace.NORTH)) | (nachbarblock.getData() == 5 & face.equals(BlockFace.SOUTH))) {
    	
    //check the found sign for the players name.
    Sign sign = (Sign) testblock.getRelative(face).getState();
    //You may want to add to lowercase function
    if(sign.getLine(0).contains("[Bank]")) {
    	if (!Bankcraft.perms.has(p, "bankcraft.admin")) {
    		p.sendMessage(Bankcraft.disallow);
    	    event.setCancelled(true);
    return;	
    	} else {
    	    File f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"banks.db");
    	    if (!f.exists()) {
    	            speichern(0,0,0,0,0);
    	            delete(0,0,0);
    	    }
    	            FileReader fr = new FileReader(f);
    	            BufferedReader reader = new BufferedReader(fr);
    	            String st = "";
    	            while ((st = reader.readLine()) != null) {
    	           	 Integer cordX = new Integer(st.split(":")[0]);
    	           	 Integer cordY = new Integer(st.split(":")[1]);
    	           	 Integer cordZ = new Integer(st.split(":")[2]);
    	            	if (cordX == nachbarblock.getX() & cordY == nachbarblock.getY() & cordZ == nachbarblock.getZ()) {
    	            	delete(cordX,cordY,cordZ);
    	            	}
    	            }
 	      		   p.sendMessage(Bankcraft.destroy);
    	            }
    }
    }
    }
    }
	String block = event.getBlock().getType().toString();
    if (block == "WALL_SIGN") {
    	Sign sign = ((Sign)event.getBlock().getState());
    	if (sign.getLine(0).contains("[Bank]")) {
	          if (p.getGameMode().equals(GameMode.CREATIVE) && !p.isSneaking()) {
	              event.setCancelled(true);
	              return;
	            }
    	if (Bankcraft.perms.has(p, "bankcraft.admin")) {
    File f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"banks.db");
    if (!f.exists()) {
        speichern(0,0,0,0,0);
        delete(0,0,0);
    }
            FileReader fr = new FileReader(f);
            BufferedReader reader = new BufferedReader(fr);
            String st = "";
            while ((st = reader.readLine()) != null) {
           	 Integer cordX = new Integer(st.split(":")[0]);
           	 Integer cordY = new Integer(st.split(":")[1]);
           	 Integer cordZ = new Integer(st.split(":")[2]);
            	if (cordX == event.getBlock().getX() & cordY == event.getBlock().getY() & cordZ == event.getBlock().getZ()) {
            	delete(cordX,cordY,cordZ);
            	}
            }
   		   p.sendMessage(Bankcraft.destroy);
    } else {
    	event.setCancelled(true);
  		   p.sendMessage(Bankcraft.disallow);
    }
    	}
    }
    }
	
	
    @EventHandler
    public void onSignPlace(SignChangeEvent event) {
    	Player p = event.getPlayer();
        String ersteReihe = event.getLine(0);
    	if (ersteReihe.equalsIgnoreCase("[Bank]")) {
        	if (Bankcraft.perms.has(p, "bankcraft.admin")) {
    		if ((((event.getLine(1).equals(Bankcraft.depositsign) | event.getLine(1).equals(Bankcraft.debitsign)| event.getLine(1).equals(Bankcraft.debitsignxp)| event.getLine(1).equals(Bankcraft.depositsignxp)) & (isInteger(event.getLine(2))) | event.getLine(2).equalsIgnoreCase("all")) | ((event.getLine(1).equals(Bankcraft.depositsignscroll)) | (event.getLine(1).equals(Bankcraft.debitsignscroll)) | (event.getLine(1).equals(Bankcraft.depositsignscrollxp)) | (event.getLine(1).equals(Bankcraft.debitsignscrollxp)))) == true) {
    	        //ERSTELLEN DER BANK
    	    	event.setLine(0,Bankcraft.bankcolor+"[Bank]");
     		   Integer betrag = 0;
     		       String typreihe = event.getLine(1);
        		   int signX = event.getBlock().getX();
        		   int signY = event.getBlock().getY();
        		   int signZ = event.getBlock().getZ();
        		   Integer typ = -1;
        		   if (typreihe.equals(Bankcraft.depositsign)) {
            		   if (event.getLine(2).equalsIgnoreCase("all")) {
            			   event.setLine(2, "All");
            			   betrag = -1;
            		   } else {
                		   betrag = new Integer(event.getLine(2));
            		   }
        			   typ = 1;
        		   }
        		   if (typreihe.equals(Bankcraft.debitsign)) {
            		   if (event.getLine(2).equalsIgnoreCase("all")) {
            			   event.setLine(2, "All");
            			   betrag = -1;
            		   } else {
                		   betrag = new Integer(event.getLine(2));
            		   }
        			   typ = 2;
        		   }
        		   if (typreihe.equals(Bankcraft.depositsignscroll)) {
        			   event.setLine(1, Bankcraft.depositsign);
        			   typ = 3;
        		   }
        		   if (typreihe.equals(Bankcraft.debitsignscroll)) {
        			   event.setLine(1, Bankcraft.debitsign);
        			   typ = 4;
        		   }
        		   if (typreihe.equals(Bankcraft.depositsignxp)) {
            		   if (event.getLine(2).equalsIgnoreCase("all")) {
            			   event.setLine(2, "All");
            			   betrag = -1;
            		   } else {
                		   betrag = new Integer(event.getLine(2));
            		   }
        			   typ = 6;
        		   }
        		   if (typreihe.equals(Bankcraft.debitsignxp)) {
            		   if (event.getLine(2).equalsIgnoreCase("all")) {
            			   event.setLine(2, "All");
            			   betrag = -1;
            		   } else {
                		   betrag = new Integer(event.getLine(2));
            		   }
        			   typ = 7;
        		   }
        		   if (typreihe.equals(Bankcraft.depositsignscrollxp)) {
        			   event.setLine(1, Bankcraft.depositsignxp);
        			   typ = 8;
        		   }
        		   if (typreihe.equals(Bankcraft.debitsignscrollxp)) {
        			   event.setLine(1, Bankcraft.debitsignxp);
        			   typ = 9;
        		   }
        		   
        		   speichern(signX,signY,signZ,typ,betrag);
          		   p.sendMessage(Bankcraft.make);
    	
    	} else {
    		if (event.getLine(1).equals(Bankcraft.balancesign) | (event.getLine(1).equals(Bankcraft.balancesignxp))) {
    	    	event.setLine(0,Bankcraft.bankcolor+"[Bank]");
     		   int signX = event.getBlock().getX();
     		   int signY = event.getBlock().getY();
     		   int signZ = event.getBlock().getZ();
     		   if (event.getLine(1).equals(Bankcraft.balancesignxp)) {
         		   speichern(signX,signY,signZ,5,0);   
     		   } else {
     		   speichern(signX,signY,signZ,0,0);
     		   }
     		   p.sendMessage(Bankcraft.make);
    		} else {
    	  		   p.sendMessage(Bankcraft.errorcreate);
    		event.setLine(0, "");
    		event.setLine(1, "");
    		event.setLine(2, "");
    		event.setLine(3, ""); 
    		}
    	}
    	} else {
	  		   p.sendMessage(Bankcraft.disallow);
    		event.setLine(0, "");
    		event.setLine(1, "");
    		event.setLine(2, "");
    		event.setLine(3, "");
    	}
    	}
    	
    }
}

