package de.hotmail.gurkilein.Bankcraft;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BankcraftPlayerListener implements Listener {
	

	
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
  					p.sendMessage(configHandler.color+configHandler.prefix+"Sign not in Database! Removing Sign!");
  					event.getClickedBlock().setType(Material.AIR);
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
	    				if (typ ==3 | typ == 4 | typ == 8 | typ == 9) {
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
    
    
