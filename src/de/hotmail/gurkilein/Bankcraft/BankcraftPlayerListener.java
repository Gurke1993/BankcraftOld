package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BankcraftPlayerListener implements Listener {
	public Map<Block, Integer> signPosition = new HashMap<Block, Integer>();
	

	
    @EventHandler
    public void onklick(PlayerInteractEvent event) throws Exception{
    	Boolean all= false;
    	Player p = event.getPlayer();
    	if (event.getClickedBlock() != null) {
    	String block = event.getClickedBlock().getType().toString();
    	if (block == "WALL_SIGN" ) {
    		if (((Sign)event.getClickedBlock().getState()).getLine(0).contains("[Bank]")) {
    			if (!p.isSneaking()) {
  				Integer typ = bankInteract.getTypeBank(event.getClickedBlock().getX(),event.getClickedBlock().getY(),event.getClickedBlock().getZ(),event.getClickedBlock().getWorld());
  				if (typ==-1) {
  					p.sendMessage(configHandler.color+configHandler.prefix+"Sign not in Database! Removing Sign!");
  					event.getClickedBlock().setType(Material.AIR);
  					return;
  				}
  		       if (Bankcraft.perms.has(p, "bankcraft.use")| (Bankcraft.perms.has(p, "bankcraft.use.money") && (typ ==0 | typ == 1 | typ == 2 | typ == 3 | typ == 4)) | (Bankcraft.perms.has(p, "bankcraft.use.exp") && (typ ==5 | typ == 6 | typ == 7 | typ == 8 | typ == 9))) {
    		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	    			if (((Sign)event.getClickedBlock().getState()).getLine(1).contains(configHandler.balancesign) | ((Sign)event.getClickedBlock().getState()).getLine(1).contains(configHandler.balancesignxp)) {
	    				bankInteract.kontoneu(0D,p,false);
	    				File f;
	    				String nachricht;
 	    				if (typ ==0) {
	    					nachricht = configHandler.balance;
	    					bankInteract.kontoneu(0D,p,false);
 	 	    			    f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts"+System.getProperty("file.separator")+p.getName()+".db");
 	 	    				} else {
 		    					nachricht = configHandler.balancexp;
 		    					bankInteract.kontoneuxp(0,p,false);
 	 	 	    			f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts"+System.getProperty("file.separator")+p.getName()+".db");
 	 	    				}
 	    	            FileReader fr = new FileReader(f);
 	    	            BufferedReader reader = new BufferedReader(fr);
 	    	            String st = "";
 	    	            st = reader.readLine(); 
                         p.sendMessage(nachricht.replace("%balance", st));
			   fr.close();
			   reader.close();
	    			} else {
	    				if (typ ==3 | typ == 4 | typ == 8 | typ == 9) {
	    	  			updateSign(event.getClickedBlock(),1);
	    				}
	    			}
    		}
    		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
    			
    			//BALANCE sign
 	    			if (((Sign)event.getClickedBlock().getState()).getLine(1).contains(configHandler.balancesign) | ((Sign)event.getClickedBlock().getState()).getLine(1).contains(configHandler.balancesignxp)) {
 	    				File f;
 	    				String nachricht;
	    				if (typ ==0) {
	    					nachricht = configHandler.balance;
	    					bankInteract.kontoneu(0D,p,false);
 	    			    f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts"+System.getProperty("file.separator")+p.getName()+".db");
 	    				} else {
	    					nachricht = configHandler.balancexp;
	    					bankInteract.kontoneuxp(0,p,false);
 	 	    			f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts"+System.getProperty("file.separator")+p.getName()+".db");
 	    				}
 	    	            FileReader fr = new FileReader(f);
 	    	            BufferedReader reader = new BufferedReader(fr);
 	    	            String st = "";
 	    	            st = reader.readLine(); 
                         p.sendMessage(nachricht.replace("%balance", st));
			   fr.close();
			   reader.close();
 	    			} else {
 	    				//transfer sign
 	    				Double betrag = 0.00;
 	       if (typ == 3 | typ == 4 | typ == 8 | typ == 9) {
 	       updateSign(event.getClickedBlock(),0);
  		   betrag = updateSign(event.getClickedBlock(),0);
 	       } else {
 	   	   if (((Sign)event.getClickedBlock().getState()).getLine(2).equalsIgnoreCase("all")) {
 	   		   all=true;
 	   		   betrag = 0.00;
 	   	   } else {
 	       betrag = new Double(((Sign)event.getClickedBlock().getState()).getLine(2));
 	   	   }
 	       }
  		   if (typ == 1 | typ == 3) {
  			   if (all == true) {
  				   betrag = (Double)Bankcraft.econ.getBalance(p.getName());
  			   }
             EconomyResponse r1 = Bankcraft.econ.withdrawPlayer(p.getName(), betrag);
             if (r1.transactionSuccess()) {
            	 bankInteract.kontoneu(betrag,p,false);
     	  		   p.sendMessage(configHandler.success1);
             } else {
     	  		   p.sendMessage(configHandler.lowmoney1);
             }
  		   }
  		   if (typ == 6 | typ == 8) {
  			   if (all == true) {
  				   betrag = bankInteract.getTotalXp(p);
  			   }
               if (bankInteract.getTotalXp(p) >= betrag) {
            	   bankInteract.removeExp(p, betrag.intValue());
            	   bankInteract.kontoneuxp(betrag.intValue(),p,false);
       	  		   p.sendMessage(configHandler.success1xp);
   			  } else {
   		  		   p.sendMessage(configHandler.lowmoney1xp);
   			  }
    		   }
  		   
  		  if (typ == 2 | typ == 4) {
  			Double differenz = bankInteract.kontoneu(-betrag,p,all);
  			  if (differenz != -2) {
                   EconomyResponse r2 = Bankcraft.econ.depositPlayer(p.getName(), differenz);
                   if (r2.transactionSuccess()) {
      	  		   p.sendMessage(configHandler.success2);
                   }
  			  } else {
  		  		   p.sendMessage(configHandler.lowmoney2);
  			  }
  		  }
  		  if (typ == 7 | typ == 9) {
  			Integer differenz = bankInteract.kontoneuxp(-betrag.intValue(),p,all);
  			  if (differenz != -2) {
                  p.giveExp(differenz);
       	  		   p.sendMessage(configHandler.success2xp);
               } else {
       	  		   p.sendMessage(configHandler.lowmoney2xp);
               }
  		  }
 	    			}
    		}
 		} else {
 	  		   p.sendMessage(configHandler.disallow);
 		}
         }
        	((Sign)event.getClickedBlock().getState()).update();
    	}
    	}
    }
    }
    
    
    public Object[] getScrollingArray(Block block) {
    	String liststring=null;
	     File file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"banks.db");
	     try {
	    	 Integer x,y,z;
	    	 x=block.getX();
	    	 y=block.getY();
	    	 z=block.getZ();
	    	 World w= block.getWorld();
	         FileReader fr = new FileReader(file);
	            BufferedReader reader = new BufferedReader(fr);
	            String st = "";
	            while ((st = reader.readLine()) != null) {
	           	 Integer cordX = new Integer(st.split(":")[0]);
	           	 Integer cordY = new Integer(st.split(":")[1]);
	           	 Integer cordZ = new Integer(st.split(":")[2]);
	           	 World cordW = Bankcraft.server.getWorld(st.split(":")[3]);
	            	if (cordX.equals(x) && cordY.equals(y) && cordZ.equals(z) && cordW.equals(w)) {
	            		liststring= st.split(":")[5];
	            	}
	            }
			       fr.close();
			       reader.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
    	
		List<Double> loadArray=new ArrayList<Double>();
		for (int i=0;i<liststring.split(",").length;i++) {
			loadArray.add(new Double(liststring.split(",")[i]));
			
		}
      
		return loadArray.toArray();
    	
    	
    }
    
    
    
    public double updateSign(Block block,int steps){

    	Object[] scrollingSignArray = getScrollingArray(block);

    	Sign sign = (Sign)block.getState();
    	 
        if(signPosition.containsKey(block)){
            if(signPosition.get(block)<(scrollingSignArray.length-steps)){
            	signPosition.put(block, signPosition.get(block)+steps);
            	sign.setLine(2, "> "+scrollingSignArray[signPosition.get(block)]+"");
            	if (signPosition.get(block).equals(scrollingSignArray.length-1)) {
            		sign.setLine(3, scrollingSignArray[0]+"");
            	} else {
            		sign.setLine(3, (scrollingSignArray[signPosition.get(block)+1])+"");
            	}
            } else {
            	signPosition.put(block, 0);
            	sign.setLine(2, "> "+scrollingSignArray[0]+"");
            	sign.setLine(3, scrollingSignArray[1]+"");
            }
        } else {
        	signPosition.put(block, 0);
        	sign.setLine(2, "> "+scrollingSignArray[0]+"");
        	sign.setLine(3, scrollingSignArray[1]+"");
        }
    sign.update(true);
    return (Double)scrollingSignArray[signPosition.get(block)];
    }
}
