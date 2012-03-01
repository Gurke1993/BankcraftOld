package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BankcraftPlayerListener implements Listener {
	public Map<Block, Integer> signPosition = new HashMap<Block, Integer>();
	
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
	
    public Integer getTypeBank(Integer cordX1, Integer cordY1, Integer cordZ1) throws Exception{
    	Integer type=-1;
        File f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"banks.db");
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
	
	
	  public Integer kontoneu(Integer betrag,Player p, Boolean all){
		  FileWriter writer;
		  File file, file2;
		  Integer enough =-2;
		    // File anlegen
		     file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts");
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
	  
	  public Integer kontoneuxp(Integer betrag,Player p, Boolean all){
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
	
    @EventHandler
    public void onklick(PlayerInteractEvent event) throws Exception{
    	Boolean all= false;
    	Player p = event.getPlayer();
    	if (event.getClickedBlock() != null) {
    	String block = event.getClickedBlock().getType().toString();
    	if (block == "WALL_SIGN" ) {
    		if (((Sign)event.getClickedBlock().getState()).getLine(0).contains("[Bank]")) {
  				Integer typ = getTypeBank(event.getClickedBlock().getX(),event.getClickedBlock().getY(),event.getClickedBlock().getZ());
  		       if (Bankcraft.perms.has(p, "bankcraft.use") | (Bankcraft.perms.has(p, "bankcraft.use.money") && (typ ==0 | typ == 1 | typ == 2 | typ == 3 | typ == 4)) | (Bankcraft.perms.has(p, "bankcraft.use.exp") && (typ ==5 | typ == 6 | typ == 7 | typ == 8 | typ == 9))) {
    		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	    			if (((Sign)event.getClickedBlock().getState()).getLine(1).contains(Bankcraft.balancesign) | ((Sign)event.getClickedBlock().getState()).getLine(1).contains(Bankcraft.balancesignxp)) {
 	    				kontoneu(0,p,false);
	    				File f;
	    				String nachricht;
 	    				if (typ ==0) {
	    					nachricht = Bankcraft.balance;
	 	    				kontoneu(0,p,false);
 	 	    			    f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts"+System.getProperty("file.separator")+p.getName()+".db");
 	 	    				} else {
 		    					nachricht = Bankcraft.balancexp;
 		 	    				kontoneuxp(0,p,false);
 	 	 	    			f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts"+System.getProperty("file.separator")+p.getName()+".db");
 	 	    				}
 	    	            FileReader fr = new FileReader(f);
 	    	            BufferedReader reader = new BufferedReader(fr);
 	    	            String st = "";
 	    	            st = reader.readLine(); 
                         p.sendMessage(nachricht+st);
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
 	    			if (((Sign)event.getClickedBlock().getState()).getLine(1).contains(Bankcraft.balancesign) | ((Sign)event.getClickedBlock().getState()).getLine(1).contains(Bankcraft.balancesignxp)) {
 	    				File f;
 	    				String nachricht;
	    				if (typ ==0) {
	    					nachricht = Bankcraft.balance;
	 	    				kontoneu(0,p,false);
 	    			    f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts"+System.getProperty("file.separator")+p.getName()+".db");
 	    				} else {
	    					nachricht = Bankcraft.balancexp;
	 	    				kontoneuxp(0,p,false);
 	 	    			f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts"+System.getProperty("file.separator")+p.getName()+".db");
 	    				}
 	    	            FileReader fr = new FileReader(f);
 	    	            BufferedReader reader = new BufferedReader(fr);
 	    	            String st = "";
 	    	            st = reader.readLine(); 
                         p.sendMessage(nachricht+st);
			   fr.close();
			   reader.close();
 	    			} else {
 	    				//transfer sign
 	    				Integer betrag = 0;
 	       if (typ == 3 | typ == 4 | typ == 8 | typ == 9) {
 	       updateSign(event.getClickedBlock(),0);
  		   betrag = (Integer)Bankcraft.betragArray[signPosition.get(event.getClickedBlock())];
 	       } else {
 	   	   if (((Sign)event.getClickedBlock().getState()).getLine(2).equalsIgnoreCase("all")) {
 	   		   all=true;
 	   		   betrag = 0;
 	   	   } else {
 	       betrag = new Integer(((Sign)event.getClickedBlock().getState()).getLine(2));
 	   	   }
 	       }
  		   if (typ == 1 | typ == 3) {
  			   if (all == true) {
  				   betrag = (int)Bankcraft.econ.getBalance(p.getName());
  			   }
             EconomyResponse r1 = Bankcraft.econ.withdrawPlayer(p.getName(), betrag);
             if (r1.transactionSuccess()) {
             	kontoneu(betrag,p,false);
     	  		   p.sendMessage(Bankcraft.success1);
             } else {
     	  		   p.sendMessage(Bankcraft.lowmoney1);
             }
  		   }
  		   if (typ == 6 | typ == 8) {
  			   if (all == true) {
  				   betrag = Integer.valueOf((int)getTotalXp(p));
  			   }
               if (Integer.valueOf((int)getTotalXp(p)) >= betrag) {
                   removeExp(p, betrag);
               	kontoneuxp(betrag,p,false);
       	  		   p.sendMessage(Bankcraft.success1xp);
   			  } else {
   		  		   p.sendMessage(Bankcraft.lowmoney1xp);
   			  }
    		   }
  		   
  		  if (typ == 2 | typ == 4) {
  		  Integer differenz = kontoneu(-betrag,p,all);
  			  if (differenz != -2) {
                   EconomyResponse r2 = Bankcraft.econ.depositPlayer(p.getName(), differenz);
                   if (r2.transactionSuccess()) {
      	  		   p.sendMessage(Bankcraft.success2);
                   }
  			  } else {
  		  		   p.sendMessage(Bankcraft.lowmoney2);
  			  }
  		  }
  		  if (typ == 7 | typ == 9) {
  	  		  Integer differenz = kontoneuxp(-betrag,p,all);
  			  if (differenz != -2) {
                  p.giveExp(differenz);
       	  		   p.sendMessage(Bankcraft.success2xp);
               } else {
       	  		   p.sendMessage(Bankcraft.lowmoney2xp);
               }
  		  }
 	    			}
    		}
 		} else {
 	  		   p.sendMessage(Bankcraft.disallow);
 		}
         }
        	((Sign)event.getClickedBlock().getState()).update();
    	}
    }
    }
    public Integer updateSign(Block block,int steps){
    	 
        if(signPosition.containsKey(block)){
            if(signPosition.get(block)<9){
            	signPosition.put(block, signPosition.get(block)+steps);
            	((Sign)block.getState()).setLine(2, "> "+Bankcraft.betragArray[signPosition.get(block)]+"");
            	if (signPosition.get(block) == 9) {
            		((Sign)block.getState()).setLine(3, Bankcraft.betragArray[0]+"");
            	} else {
            		((Sign)block.getState()).setLine(3, (Bankcraft.betragArray[signPosition.get(block)+1])+"");
            	}
            } else {
            	signPosition.put(block, 0);
            	((Sign)block.getState()).setLine(2, "> "+Bankcraft.betragArray[0]+"");
            	((Sign)block.getState()).setLine(3, Bankcraft.betragArray[1]+"");
            }
        } else {
        	signPosition.put(block, 0);
        	((Sign)block.getState()).setLine(2, "> "+Bankcraft.betragArray[0]+"");
        	((Sign)block.getState()).setLine(3, Bankcraft.betragArray[1]+"");
        }
    ((Sign)block.getState()).update();
    return signPosition.get(block);
    }
}
