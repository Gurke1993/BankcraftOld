package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankcraftCommandListener implements CommandExecutor {
	
@SuppressWarnings("unused")
private Bankcraft plugin;
public Double betrag;

	public void sendHelp(Player p) {
		p.sendMessage("---Bankcraft-Help---");
		p.sendMessage("/bank help                     Shows this help");
		p.sendMessage("/bank balance PLAYER           Shows your banked money");
		p.sendMessage("/bank balancexp PLAYER         Shows your banked XP");
		p.sendMessage("/bank deposit AMOUNT           Deposits money on your Account");
		p.sendMessage("/bank debit AMOUNT             Debits money from your Account");
		p.sendMessage("/bank depositxp AMOUNT         Deposits XP on your Account");
		p.sendMessage("/bank debitxp AMOUNT           Debits XP from your Account");
		p.sendMessage("/bank transfer PLAYER AMOUNT   Transfers money");
		p.sendMessage("/bank transferxp PLAYER AMOUNT Transfers XP");
	}
	
	public void sendAdminHelp(Player p) {
		p.sendMessage("---Bankcraft-AdminHelp---");
		p.sendMessage("/bankadmin help                  Shows this help");
		p.sendMessage("/bankadmin set PLAYER AMOUNT     Sets a players money");
		p.sendMessage("/bankadmin setxp PLAYER AMOUNT   Sets a players XP");
		p.sendMessage("/bankadmin grant PLAYER AMOUNT   Grants a Player money");
		p.sendMessage("/bankadmin grantxp PLAYER AMOUNT Grants a player XP");
		p.sendMessage("/bankadmin clear PLAYER          Clears a players money-account");
		p.sendMessage("/bankadmin clearxp PLAYER        Clears a players XP-account");
	}
	public BankcraftCommandListener(Bankcraft b1) {
		plugin=b1;
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

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmdlabel,
			String[] vars) {
		File file,file2;
		FileWriter writer;
		Player p;
		p= (Player) sender;
		if (p!=null) {
			if (cmdlabel.equalsIgnoreCase("bank")) {
				if (vars.length == 0) {
					sendHelp(p);
					return true;
				}
				if (vars.length == 1) {
					if (vars[0].equalsIgnoreCase("help")) {
						sendHelp(p);
						return true;
					}
					if (vars[0].equalsIgnoreCase("balance") && (Bankcraft.perms.has(p, "bankcraft.command.balance") ||  Bankcraft.perms.has(p, "bankcraft.command"))) {
	    				bankInteract.kontoneu(0D,p,false);
	    				File f;
	    				String nachricht;
	    				nachricht = configHandler.balance;
 	 	    			f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts"+System.getProperty("file.separator")+p.getName()+".db");
 	    	            FileReader fr;
						try {
							fr = new FileReader(f);
 	    	            BufferedReader reader = new BufferedReader(fr);
 	    	            String st = "";
 	    	            st = reader.readLine(); 
                        p.sendMessage(nachricht.replace("%balance", st));
                        fr.close();
                        reader.close();
    					return true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (vars[0].equalsIgnoreCase("balancexp") && (Bankcraft.perms.has(p, "bankcraft.command.balancexp") ||  Bankcraft.perms.has(p, "bankcraft.command"))) {
	    				bankInteract.kontoneuxp(0,p,false);
	    				File f;
	    				String nachricht;
	    				nachricht = configHandler.balancexp;
 	 	    			f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts"+System.getProperty("file.separator")+p.getName()+".db");
 	    	            try {
 	    	                 FileReader fr = new FileReader(f);
 	    	            BufferedReader reader = new BufferedReader(fr);
 	    	            String st = "";
 	    	            st = reader.readLine(); 
                        p.sendMessage(nachricht.replace("%balance", st));
                        fr.close();
                        reader.close();
    					return true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				}
				if (vars.length == 2) {
					if (isDouble(vars[1])) {
						
					if (vars[0].equalsIgnoreCase("deposit") && (Bankcraft.perms.has(p, "bankcraft.command.deposit") ||  Bankcraft.perms.has(p, "bankcraft.command"))) {
					  	betrag = new Double(vars[1]); 
				             EconomyResponse r1 = Bankcraft.econ.withdrawPlayer(p.getName(), betrag);
				             if (r1.transactionSuccess()) {
				            	 bankInteract.kontoneu(betrag,p,false);
				     	  		   p.sendMessage(configHandler.success1);
				             } else {
				     	  		   p.sendMessage(configHandler.lowmoney1);
				             }
								return true;
				  		   }
						
					
					if (vars[0].equalsIgnoreCase("debit") && (Bankcraft.perms.has(p, "bankcraft.command.debit") ||  Bankcraft.perms.has(p, "bankcraft.command"))) {
					  	betrag = new Double(vars[1]); 
				  	  		  double differenz = bankInteract.kontoneu(-betrag,p,false);
				  	  			  if (differenz != -2) {
				  	                   EconomyResponse r2 = Bankcraft.econ.depositPlayer(p.getName(), differenz);
				  	                   if (r2.transactionSuccess()) {
				  	      	  		   p.sendMessage(configHandler.success2);
				  	                   }
				  	  			  } else {
				  	  		  		   p.sendMessage(configHandler.lowmoney2);
				  	  			  }
									return true;
				  	  		  }
					
					if (vars[0].equalsIgnoreCase("depositxp") && (Bankcraft.perms.has(p, "bankcraft.command.depositxp") ||  Bankcraft.perms.has(p, "bankcraft.command"))) {
						betrag = new Double(vars[1]);
			               if (Integer.valueOf((int)bankInteract.getTotalXp(p)) >= betrag) {
			            	   bankInteract.removeExp(p, betrag.intValue());
			            	   bankInteract.kontoneuxp(betrag.intValue(),p,false);
			       	  		   p.sendMessage(configHandler.success1xp);
			   			  } else {
			   		  		   p.sendMessage(configHandler.lowmoney1xp);
			   			  }
							return true;
					}
					if (vars[0].equalsIgnoreCase("debitxp") && (Bankcraft.perms.has(p, "bankcraft.command.debitxp") ||  Bankcraft.perms.has(p, "bankcraft.command"))) {
						//HELP
						betrag = new Double(vars[1]);
			  	  		  Integer differenz = bankInteract.kontoneuxp(-betrag.intValue(),p,false);
			  			  if (differenz != -2) {
			                  p.giveExp(differenz);
			       	  		   p.sendMessage(configHandler.success2xp);
			               } else {
			       	  		   p.sendMessage(configHandler.lowmoney2xp);
			               }
							return true;
			  		  }
		} else {
			Player p2 =Bukkit.getPlayer(vars[1]);
			if (p2!=null) {
			if (vars[0].equalsIgnoreCase("balance") && (Bankcraft.perms.has(p, "bankcraft.command.balance.other") ||  Bankcraft.perms.has(p, "bankcraft.command"))) {
				bankInteract.kontoneu(0D,p2,false);
				File f;
				String nachricht;
				nachricht = configHandler.balance;
	    			f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts"+System.getProperty("file.separator")+p2.getName()+".db");
 	            try {
 	            	FileReader fr = new FileReader(f);
 	            BufferedReader reader = new BufferedReader(fr);
 	            String st = "";
 	            st = reader.readLine(); 
                p.sendMessage(nachricht.replace("%balance", st));
                fr.close();
                reader.close();
				return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
	  		  }	
			
			if (vars[0].equalsIgnoreCase("balancexp") && (Bankcraft.perms.has(p, "bankcraft.command.balancexp.other") ||  Bankcraft.perms.has(p, "bankcraft.command"))) {
				bankInteract.kontoneuxp(0,p2,false);
				File f;
				String nachricht;
				nachricht = configHandler.balancexp;
	    			f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts"+System.getProperty("file.separator")+p2.getName()+".db");
 	            try {
 	            	FileReader fr = new FileReader(f);
 	            BufferedReader reader = new BufferedReader(fr);
 	            String st = "";
 	            st = reader.readLine(); 
                p.sendMessage(nachricht.replace("%balance", st));
                fr.close();
                reader.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
	  		  }		
		}
		}	
				}
				if (vars.length == 3) {
					if (isDouble(vars[2])) {
						Player p2 =Bukkit.getPlayer(vars[1]);
						if (p2 != null) {
					if (vars[0].equalsIgnoreCase("transfer") && (Bankcraft.perms.has(p, "bankcraft.command.transfer") ||  Bankcraft.perms.has(p, "bankcraft.command"))) {
					  	betrag = new Double(vars[2]); 
					  	Double differenz = bankInteract.kontoneu(-betrag,p,false);
			  	  			  if (differenz != -2) {
			  					
					            	 bankInteract.kontoneu(betrag,p2,false);
			  	      	  		   p.sendMessage(configHandler.success3);
			  	                   
			  	  			  } else {
			  	  		  		   p.sendMessage(configHandler.lowmoney2);
			  	  			  }
								return true;
					}
					
					
					if (vars[0].equalsIgnoreCase("transferxp") && (Bankcraft.perms.has(p, "bankcraft.command.transferxp") ||  Bankcraft.perms.has(p, "bankcraft.command"))) {
						Integer differenz = bankInteract.kontoneuxp(-betrag.intValue(),p,false);
			  			  if (differenz != -2) {
				            	 bankInteract.kontoneuxp(betrag.intValue(),p2,false);
			       	  		   p.sendMessage(configHandler.success3xp);
			               } else {
			       	  		   p.sendMessage(configHandler.lowmoney2xp);
			               }
							return true;
					}
						}
					}
				}
			p.sendMessage(ChatColor.RED+configHandler.prefix+"Wrong Syntax or missing permissions! Please see /bank help for more information!");
				return true;
			} else {
			
			if (cmdlabel.equalsIgnoreCase("bankadmin")) {
				if (vars.length==0) {
					sendAdminHelp(p);
					return true;
				}
				if (vars.length==1) {
				
				if (vars[0].equalsIgnoreCase("help")) {
					sendAdminHelp(p);
					return true;
				}
				}
				Player p2 = Bukkit.getPlayer(vars[1]);
				if (p2!=null) {
					if (vars.length==2) {
						if (vars[0].equalsIgnoreCase("clear") && (Bankcraft.perms.has(p, "bankcraft.command.clear") ||  Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
						bankInteract.kontoneu(0D, p2, true);
						p.sendMessage(configHandler.color+configHandler.prefix+"Account cleared!");
						return true;
						}
						if (vars[0].equalsIgnoreCase("clearxp") && (Bankcraft.perms.has(p, "bankcraft.command.clearxp") ||  Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
						bankInteract.kontoneuxp(0, p2, true);
						p.sendMessage(configHandler.color+configHandler.prefix+"XP-Account cleared!");
						return true;
						}
					}
					if (vars.length==3) {
						if (isDouble(vars[2])) {
							if (new Integer(vars[2])>=0) {
							if (vars[0].equalsIgnoreCase("set") && (Bankcraft.perms.has(p, "bankcraft.command.set") ||  Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
							     file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts");
						                if (!file.exists()) {
						                    file.mkdirs(); }
						                file2 = new File(file+System.getProperty("file.separator")+p2.getDisplayName()+".db");
						                try {
							       writer = new FileWriter(file2 ,false);
							       writer.write(vars[2]);
							       writer.flush();
							       writer.close();
							       p.sendMessage(configHandler.color+configHandler.prefix+"Account set!");
									return true;
										} catch (Exception e) {
											e.printStackTrace();
										}
							}
							
							if (vars[0].equalsIgnoreCase("setxp") && (Bankcraft.perms.has(p, "bankcraft.command.setxp") ||  Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
							     file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts");
					                if (!file.exists()) {
					                    file.mkdirs(); }
					                file2 = new File(file+System.getProperty("file.separator")+p2.getDisplayName()+".db");
					                try{
						       writer = new FileWriter(file2 ,false);
						       writer.write(vars[2]);
						       writer.flush();
						       writer.close();
						       p.sendMessage(configHandler.color+configHandler.prefix+"XP-Account set!");
								return true;
									} catch (Exception e) {
										e.printStackTrace();
									}
							}
							}
							
							if (vars[0].equalsIgnoreCase("grant") && (Bankcraft.perms.has(p, "bankcraft.command.grant") ||  Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
							betrag = new Double(vars[2]);
							Double differenz = bankInteract.kontoneu(betrag,p2,false);
				  			  if (differenz != -2) {
				  				  p.sendMessage(configHandler.success1);
				  			  } else {
				  				  p.sendMessage(configHandler.lowmoney3);
				  			  }
								return true;
								
							}
							
							if (vars[0].equalsIgnoreCase("grantxp") && (Bankcraft.perms.has(p, "bankcraft.command.grantxp") ||  Bankcraft.perms.has(p, "bankcraft.command.admin"))) {
								betrag = new Double(vars[2]);
								Integer differenz = bankInteract.kontoneuxp(betrag.intValue(),p2,false);
					  			  if (differenz != -2) {
					  				  p.sendMessage(configHandler.success1xp);
					  			  } else {
					  				  p.sendMessage(configHandler.lowmoney3xp);
					  			  }
									return true;
									
								
							}
						}
					}
				}
				p.sendMessage(ChatColor.RED+configHandler.prefix+"Wrong Syntax or missing permissions! Please see /bank help for more information!");
				return true;
			}
			}
		}
		
		return false;
	}

}
