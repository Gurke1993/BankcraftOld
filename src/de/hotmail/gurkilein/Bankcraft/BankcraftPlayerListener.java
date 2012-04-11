package de.hotmail.gurkilein.Bankcraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BankcraftPlayerListener implements Listener {
	
	  public void speichern(int x, int y, int z, int typ, World w, String betrag ){
		    // File anlegen
		  FileWriter writer;
		    File file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft");
		     try {
	                if (!file.exists()) {
	                    file.mkdirs(); }
	              File  file2 = new File(file+System.getProperty("file.separator")+"banks.db");
		       writer = new FileWriter(file2 ,true);
		       
		       // Text wird in den Stream geschrieben
		       writer.write(x+":"+y+":"+z+":"+w.getName()+":"+typ+":"+betrag);
		       writer.write(System.getProperty("line.separator"));     
		       writer.flush();
		       
		       // Schließt den Stream
		       writer.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	  }
	
	public boolean isDouble( String input )  {  
		   try  
		   {  
		      Double.parseDouble( input );  
		      return true;  
		   }  
		   catch (Exception e)
		   { 
		      return false;  
		   }  
		}  
	
	public boolean isPositive( String input )  {  
if (isDouble(input)) {
	if (new Double(input)>=0) {
	return true;
	}
}
	return false;
		} 
	

	
    @EventHandler
    public void onklick(PlayerInteractEvent event) throws Exception{
    	Player p = event.getPlayer();
    	if (event.getClickedBlock() != null) {
    	String block = event.getClickedBlock().getType().toString();
    	if (block == "WALL_SIGN" ) {
    		if (((Sign)event.getClickedBlock().getState()).getLine(0).contains("[Bank]")) {
    			if (!p.isSneaking()) {
  				Integer typ = bankInteract.getTypeBank(event.getClickedBlock().getX(),event.getClickedBlock().getY(),event.getClickedBlock().getZ(),event.getClickedBlock().getWorld());
  				if (typ==-1) {
  					if (Bankcraft.perms.has(p, "bankcraft.admin")) {
  	  					p.sendMessage(configHandler.color+configHandler.prefix+"Reinitializing Bankcraftsign...");
  	  					Sign sign = (Sign)event.getClickedBlock().getState();
  	  		   		if (((sign.getLine(1).contains(configHandler.depositsign) | sign.getLine(1).contains(configHandler.exchangesign) | sign.getLine(1).contains(configHandler.exchangesignxp) | sign.getLine(1).contains(configHandler.debitsign)| sign.getLine(1).contains(configHandler.debitsignxp)| sign.getLine(1).contains(configHandler.depositsignxp)) && (isPositive(sign.getLine(2))) || sign.getLine(2).equalsIgnoreCase("all")) == true) {
  	      	        //ERSTELLEN DER BANK
  	      	    	sign.setLine(0,configHandler.bankcolor+"[Bank]");
  	       		   double betrag = 0;
  	       		       String typreihe = sign.getLine(1);
  	          		   int signX = sign.getBlock().getX();
  	          		   int signY = sign.getBlock().getY();
  	          		   int signZ = sign.getBlock().getZ();
  	          		   Integer typ2 = -1;
  	          		   if (typreihe.equals(configHandler.depositsign)) {
  	              		   if (sign.getLine(2).equalsIgnoreCase("all")) {
  	              			   sign.setLine(2, "All");
  	              			   betrag = -1;
  	              		   } else {
  	                  		   betrag = new Double(sign.getLine(2));
  	              		   }
  	          			   typ = 1;
  	          		   }
  	          		   if (typreihe.equals(configHandler.debitsign)) {
  	              		   if (sign.getLine(2).equalsIgnoreCase("all")) {
  	              			   sign.setLine(2, "All");
  	              			   betrag = -1;
  	              		   } else {
  	                  		   betrag = new Double(sign.getLine(2));
  	              		   }
  	          			   typ = 2;
  	          		   }

  	          		   if (typreihe.equals(configHandler.depositsignxp)) {
  	              		   if (sign.getLine(2).equalsIgnoreCase("all")) {
  	              			   sign.setLine(2, "All");
  	              			   betrag = -1;
  	              		   } else {
  	                  		   betrag = new Double(sign.getLine(2));
  	              		   }
  	          			   typ = 6;
  	          		   }
  	          		   if (typreihe.equals(configHandler.debitsignxp)) {
  	              		   if (sign.getLine(2).equalsIgnoreCase("all")) {
  	              			   sign.setLine(2, "All");
  	              			   betrag = -1;
  	              		   } else {
  	                  		   betrag = new Double(sign.getLine(2));
  	              		   }
  	          			   typ = 7;
  	          		   }
  	          		   if (typreihe.equals(configHandler.exchangesign)) {
  	              		   if (sign.getLine(2).equalsIgnoreCase("all")) {
  	              			   sign.setLine(2, "All");
  	              			   betrag = -1;
  	              		   } else {
  	                  		   betrag = new Double(sign.getLine(2));
  	              		   }
  	          			   typ = 12;
  	          		   }
  	          		   if (typreihe.equals(configHandler.exchangesignxp)) {
  	              		   if (sign.getLine(2).equalsIgnoreCase("all")) {
  	              			   sign.setLine(2, "All");
  	              			   betrag = -1;
  	              		   } else {
  	                  		   betrag = new Double(sign.getLine(2));
  	              		   }
  	          			   typ = 13;
  	          		   }
  	          		   speichern(signX,signY,signZ,typ2,sign.getBlock().getWorld(),betrag+"");
  	            		   p.sendMessage(configHandler.make);
  	      	
  	      	} else {
  	      		if (sign.getLine(1).equals(configHandler.balancesign) | (sign.getLine(1).equals(configHandler.balancesignxp))) {
  	      	    	sign.setLine(0,configHandler.bankcolor+"[Bank]");
  	       		   int signX = sign.getBlock().getX();
  	       		   int signY = sign.getBlock().getY();
  	       		   int signZ = sign.getBlock().getZ();
  	       		   if (sign.getLine(1).equals(configHandler.balancesignxp)) {
  	       			   if (sign.getLine(2).isEmpty()) {
  	           		   speichern(signX,signY,signZ,5,sign.getBlock().getWorld(),"0");   
  	       			   } else {
  	               		   speichern(signX,signY,signZ,11,sign.getBlock().getWorld(),"0");   
  	       			   }
  	       		   } else {
  	       			   if (sign.getLine(2).isEmpty()) {
  	       		   speichern(signX,signY,signZ,0,sign.getBlock().getWorld(),"0");
  	       			   } else {
  	               		   speichern(signX,signY,signZ,10,sign.getBlock().getWorld(),"0");   
  	       			   }
  	       		   }
  	       		   p.sendMessage(configHandler.make);
  	      		} else {
  	      	  		   p.sendMessage(configHandler.color+configHandler.prefix+"Could not reinitialize the sign! Removing it now!");
  	  					event.getClickedBlock().setType(Material.AIR);
  	      		}
  	      	}
  					} else {
  					p.sendMessage(configHandler.color+configHandler.prefix+"Sign not in Database! Please contact an admin!");
  					}
  					return;
  				}
  		       if (Bankcraft.perms.has(p, "bankcraft.use")| (Bankcraft.perms.has(p, "bankcraft.use.money") && (typ ==0 | typ == 1 | typ == 2 | typ == 3 | typ == 4)) | (Bankcraft.perms.has(p, "bankcraft.use.exp") && (typ ==5 | typ == 6 | typ == 7 | typ == 8 | typ == 9))) {
    		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	    			if (((Sign)event.getClickedBlock().getState()).getLine(1).contains(configHandler.balancesign) | ((Sign)event.getClickedBlock().getState()).getLine(1).contains(configHandler.balancesignxp)) {
	    				bankInteract.kontoneu(0D,p.getName(),false);
	    				String nachricht;
 	    				if (typ ==0) {
	    					nachricht = configHandler.balance;
	    					bankInteract.kontoneu(0D,p.getName(),false);
 	 	    			} else {
 		    					nachricht = configHandler.balancexp;
 		    					bankInteract.kontoneuxp(0,p.getName(),false);
 	 	 	    			}
		     	  		   p.sendMessage(configHandler.getMessage(nachricht, p.getName(), 0D));
	    			} else {
	    				if (typ ==3 | typ == 4 | typ == 8 | typ == 9 | typ == 14 | typ == 15) {
	    	  			bankInteract.updateSign(event.getClickedBlock(),1);
	    				}
	    			}
    		}
    		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
    		bankInteract.use(p, ((Sign)event.getClickedBlock().getState()).getLine(2), typ, event.getClickedBlock(),((Sign)event.getClickedBlock().getState()).getLine(2));
    		}
  		       }
 		} else {
	  		   p.sendMessage(configHandler.getMessage(configHandler.disallow, p.getName(), 0D));
 		}
         }
        	((Sign)event.getClickedBlock().getState()).update();
    	}
    	}
    }
    }
    
    
