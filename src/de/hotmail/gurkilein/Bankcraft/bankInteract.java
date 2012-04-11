package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class bankInteract {
	@SuppressWarnings("unused")
	private Bankcraft plugin;
	public static Map<Block, Integer> signPosition = new HashMap<Block, Integer>();
	public bankInteract (Bankcraft b1) {
		this.plugin= b1;
	}
	
	public static Boolean stopLoanPunishment(String pstring) {
		Player p = Bankcraft.server.getPlayer(pstring);
		if (p!=null) {
		if (!configHandler.loangroup.equals("-1")) {
		Bankcraft.perms.playerRemoveGroup(p, configHandler.loangroup); 
		}
		p.sendMessage(configHandler.getMessage(configHandler.nolongerloan, pstring, null));
		} else {
			//WENN SPIELER OFFLINE
		}
		return true;
	}
	
	public static Boolean startLoanPunishment(String pstring) {
		Player p = Bankcraft.server.getPlayer(pstring);
		if (p!=null) {
		if (!configHandler.loangroup.equals("-1")) {
		Bankcraft.perms.playerAddGroup(p, configHandler.loangroup);
		}
		p.sendMessage(configHandler.getMessage(configHandler.nowinloan, pstring, null));
		} else {
			//WENN SPIELER OFFLINE
		}
		return true;
	}
	
	public static Boolean stopLoanPunishmentXP(String pstring) {
		Player p = Bankcraft.server.getPlayer(pstring);
		if (p!=null) {
		if (!configHandler.loangroupxp.equals("-1")) {
		Bankcraft.perms.playerRemoveGroup(p, configHandler.loangroupxp);
		}
		p.sendMessage(configHandler.getMessage(configHandler.nolongerloanxp, pstring, null));
		} else {
			//WENN SPIELER OFFLINE
		}
		return true;
	}
	
	public static Boolean startLoanPunishmentXP(String pstring) {
		Player p = Bankcraft.server.getPlayer(pstring);
		if (p!=null) {
		if (!configHandler.loangroupxp.equals("-1")) {
		Bankcraft.perms.playerAddGroup(p, configHandler.loangroupxp);
		}
		p.sendMessage(configHandler.getMessage(configHandler.nowinloanxp, pstring, null));
		} else {
			//WENN SPIELER OFFLINE
		}
		return true;
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
	
    public static Integer getTypeBank(Integer cordX1, Integer cordY1, Integer cordZ1, World cordW1) throws Exception{
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
       	 World cordW = Bankcraft.server.getWorld(st.split(":")[3]);
        	if ((cordX.equals(cordX1)) & (cordY.equals(cordY1)) & (cordZ.equals(cordZ1))& (cordW.equals(cordW1))) {
        	type = new Integer(st.split(":")[4]);
    }
        }
    return type;
    }
    
    public static Double getBalance(String pstring) {
    	
    	Double balance = null;
    	
		   File f = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts"+System.getProperty("file.separator")+pstring+".db");
     try {
     FileReader fr = new FileReader(f);
     BufferedReader reader = new BufferedReader(fr);
     String st = "";
     st = reader.readLine();
     fr.close();
     reader.close();
     balance = new Double(st);
     } catch (Exception e) {
    	 e.printStackTrace();
     }
    	
    	return balance;
    }
    
    public static Integer getBalanceXP(String pstring) {
    	Integer balancexp = null;
    	
		   File ft = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts"+System.getProperty("file.separator")+pstring+".db");
  try {
  FileReader frt = new FileReader(ft);
  BufferedReader readert = new BufferedReader(frt);
  String st = "";
  st = readert.readLine();
  frt.close();
  readert.close();
  balancexp = new Integer(st);
  } catch (Exception e) {
 	 e.printStackTrace();
  }
    	
    	return balancexp;
    }
    
    public static boolean deposit(Player sender, Boolean money, String amountstr) {
    	//Boolean all = false;
    	
    	double amount = 0D;
   	   if (amountstr.equalsIgnoreCase("all")) {
   		   if (money)
   			   amount = (Double)Bankcraft.econ.getBalance(sender.getName());
   		   else
   			   amount = bankInteract.getTotalXp(sender);
  	   } else {
      amount = new Double(amountstr);
  	   }
   	   //MONEY DEPOSIT
   	   if (money) {
		   if (configHandler.limit == -1 || configHandler.limit >= amount+bankInteract.getBalance(sender.getName())) {
			   if (Bankcraft.econ.getBalance(sender.getName()) >= amount) {
       EconomyResponse r1 = Bankcraft.econ.withdrawPlayer(sender.getName(), amount);
       if (r1.transactionSuccess()) {
      	 bankInteract.kontoneu(amount,sender.getName(),false);
      	 if (bankInteract.getBalance(sender.getName())>=0 && (bankInteract.getBalance(sender.getName())-amount)<0) {
      		 bankInteract.stopLoanPunishment(sender.getName());
      	 }
       }
	  		   sender.sendMessage(configHandler.getMessage(configHandler.success1, sender.getName(), amount));
	  		   return true;
       } else {
	  		   sender.sendMessage(configHandler.getMessage(configHandler.lowmoney1, sender.getName(), amount));
       }
		   } else {
			   sender.sendMessage(configHandler.getMessage(configHandler.limitmsg, sender.getName(), amount));
		   }
   	   } else {
   		   //XP DEPOSIT
		   if (configHandler.limitxp == -1 || configHandler.limitxp >= amount+bankInteract.getBalanceXP(sender.getName())) {
		         if (bankInteract.getTotalXp(sender) >= amount) {
		      	   bankInteract.removeExp(sender, (int)amount);
		      	   bankInteract.kontoneuxp((int)amount,sender.getName(),false);
		        	 if (bankInteract.getBalanceXP(sender.getName())>=0 && bankInteract.getBalanceXP(sender.getName())-amount<0) {
		      		 bankInteract.stopLoanPunishmentXP(sender.getName());
		      	 }
			  		   sender.sendMessage(configHandler.getMessage(configHandler.success1xp, sender.getName(), amount));
					  } else {
			  		   sender.sendMessage(configHandler.getMessage(configHandler.lowmoney1xp, sender.getName(), amount));
					  }
				   } else {
					   sender.sendMessage(configHandler.getMessage(configHandler.limitmsgxp, sender.getName(), amount));
				   }
   	   }
    	return false;
    	
    }
    
    public static boolean debit(Player sender, Boolean money, String amount) {
    	return true;
    	
    }
    
    public static boolean balance(Player sender, Boolean money, String pstring) {
   				String nachricht = "";
  				if (sender.getName().equals(pstring)) {
  					if (money) {
  					nachricht = configHandler.balance;
  					bankInteract.kontoneu(0D,pstring,false);
  				} else {
  					nachricht = configHandler.balancexp;
  					bankInteract.kontoneuxp(0,pstring,false);
   				}
    } else {
  				if (money) {
  					nachricht = configHandler.balanceother;
  							bankInteract.kontoneu(0D, pstring, false);
  				} else {
  					nachricht = configHandler.balancexpother;
  					bankInteract.kontoneuxp(0, pstring, false);
  				}
    }

  	  		   sender.sendMessage(configHandler.getMessage(nachricht, pstring, 0D));
    	return true;
    	
    }
    
	  public static String kontoneu(Double betrag,String pstring, Boolean all){
		  FileWriter writer;
		  File file,fileparent;
		  String enough = "error";
		    // File anlegen
		  fileparent = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts");
		     file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts"+System.getProperty("file.separator")+pstring+".db");
		     try {
	                if (!file.exists()) {
	                	fileparent.mkdir();
	                    file.createNewFile();
	                    writer = new FileWriter(file, true);
	                    writer.write("0.00");
	     		        writer.write(System.getProperty("line.separator"));  
	     		        writer.flush();
	    		        writer.close();
	                }
			         FileReader fr = new FileReader(file);
			            BufferedReader reader = new BufferedReader(fr);
			            String st = reader.readLine(); 
			            	if ( new Double(st)+configHandler.maxloan >= -betrag) {
				            	enough = (-betrag)+"";
				            	betrag += new Double(st);
			            	} else {
			            		betrag = new Double(st);
			            		enough = "error";
			            	}
			            	if (all==true && new Double(st) >= -betrag) {
			            		betrag = 0.00;
			            		enough = st;
			            	}
			   fr.close();
			   reader.close();
           	DecimalFormat df= new DecimalFormat("#0.00");   
           	String betragstring= df.format(betrag);  
           	betrag = Double.parseDouble(betragstring); 
               writer = new FileWriter(file);
		       writer.write(betrag+System.getProperty("line.separator"));
		       writer.flush();
		       writer.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
			return enough;
	  }
	  
	  public static boolean use(Player p, String betragstring, Integer typ, Block b, String p2string) {
		  /* 0 = Balance
		   * 1 = Deposit
		   * 2= debit
		   * 3 = DepositS
		   * 4= DebitS
		   * 5=Balancexp
		   * 6 = Deposit XP
		   * 7= Debit XP
		   * 8= Depositxp scroll
		   * 9= debitxp Scroll
		   * 10= balance other
		   * 11 = balancexp other
		   * 12 = exchange
		   * 13= exchangexp
		   * 14= exchangeS
		   * 15= exchangexpS
		   */
		  
			Boolean all = false;
			//BALANCE sign
   			if (typ==0 || typ==5 || typ == 10 || typ == 11) {
   				String nachricht = "";
  				if (typ ==0) {
  					nachricht = configHandler.balance;
  					bankInteract.kontoneu(0D,p.getName(),false);
	     	  		   p.sendMessage(configHandler.getMessage(nachricht, p.getName(), 0D));
  				}
  				if (typ == 5) {
  					nachricht = configHandler.balancexp;
  					bankInteract.kontoneuxp(0,p.getName(),false);
	     	  		   p.sendMessage(configHandler.getMessage(nachricht, p.getName(), 0D));
   				}
  				if (typ == 10) {
  					nachricht = configHandler.balanceother;
  							bankInteract.kontoneu(0D, p2string, false);
  		     	  		   p.sendMessage(configHandler.getMessage(nachricht, p2string, 0D));
  				}
  				if (typ== 11 ) {
  					nachricht = configHandler.balancexpother;
  					bankInteract.kontoneuxp(0, p2string, false);
	     	  		   p.sendMessage(configHandler.getMessage(nachricht, p2string, 0D));
  				}
   			} else {
   				//transfer sign
   				Double betrag = 0.00;
      if (typ == 3 | typ == 4 | typ == 8 | typ == 9 | typ == 14 | typ == 15) {
    	  // S-sign
	   betragstring = updateSign(b,0);
      }
  	   if (betragstring.equalsIgnoreCase("all")) {
  		   all=true;
  		   betrag = 0.00;
  	   } else {
      betrag = new Double(betragstring);
  	   }
	   if (typ == 1 | typ == 3) {
		   //Deposit
		   if (all == true) {
			   betrag = (Double)Bankcraft.econ.getBalance(p.getName());
		   }
		   if (configHandler.limit == -1 || configHandler.limit >= betrag+bankInteract.getBalance(p.getName())) {
			   if (Bankcraft.econ.getBalance(p.getName()) >= betrag) {
       EconomyResponse r1 = Bankcraft.econ.withdrawPlayer(p.getName(), betrag);
       if (r1.transactionSuccess()) {
      	 bankInteract.kontoneu(betrag,p.getName(),false);
      	 if (bankInteract.getBalance(p.getName())>=0 && (bankInteract.getBalance(p.getName())-betrag)<0) {
      		 bankInteract.stopLoanPunishment(p.getName());
      	 }
       }
	  		   p.sendMessage(configHandler.getMessage(configHandler.success1, p.getName(), betrag));
       } else {
	  		   p.sendMessage(configHandler.getMessage(configHandler.lowmoney1, p.getName(), betrag));
       }
		   } else {
			   p.sendMessage(configHandler.getMessage(configHandler.limitmsg, p.getName(), betrag));
		   }
	   }
	   if (typ == 6 | typ == 8) {
		   //Deposit XP
		   if (all == true) {
			   betrag = bankInteract.getTotalXp(p);
		   }
		   if (configHandler.limitxp == -1 || configHandler.limitxp >= betrag+bankInteract.getBalanceXP(p.getName())) {
         if (bankInteract.getTotalXp(p) >= betrag) {
      	   bankInteract.removeExp(p, betrag.intValue());
      	   bankInteract.kontoneuxp(betrag.intValue(),p.getName(),false);
        	 if (bankInteract.getBalanceXP(p.getName())>=0 && bankInteract.getBalanceXP(p.getName())-betrag<0) {
      		 bankInteract.stopLoanPunishmentXP(p.getName());
      	 }
	  		   p.sendMessage(configHandler.getMessage(configHandler.success1xp, p.getName(), betrag));
			  } else {
	  		   p.sendMessage(configHandler.getMessage(configHandler.lowmoney1xp, p.getName(), betrag));
			  }
		   } else {
			   p.sendMessage(configHandler.getMessage(configHandler.limitmsgxp, p.getName(), betrag));
		   }
	   }
	   
	  if (typ == 2 | typ == 4) {
		  //Debit
		String differenzstring = bankInteract.kontoneu(-betrag,p.getName(),all);
		  if (!differenzstring.equals("error")) {
			  Double differenz = new Double(differenzstring);
       	 if (bankInteract.getBalance(p.getName())<0 && bankInteract.getBalance(p.getName())+betrag>=0) {
      		 bankInteract.startLoanPunishment(p.getName());
      	 }
             EconomyResponse r2 = Bankcraft.econ.depositPlayer(p.getName(), differenz);
             if (r2.transactionSuccess()) {
   	  		   p.sendMessage(configHandler.getMessage(configHandler.success2, p.getName(), betrag));
             }
		  } else {
	  		   p.sendMessage(configHandler.getMessage(configHandler.lowmoney2, p.getName(), betrag));
		  }
	  }
	  if (typ == 7 | typ == 9) {
		  //Debit XP
		String differenzstring = bankInteract.kontoneuxp(-betrag.intValue(),p.getName(),all);
		  if (!differenzstring.equals("error")) {
			  Integer differenz = new Integer(differenzstring);
        	 if (bankInteract.getBalanceXP(p.getName())<0 && bankInteract.getBalanceXP(p.getName())+betrag>=0) {
       		 bankInteract.startLoanPunishmentXP(p.getName());
       	 }
            p.giveExp(differenz);
	  		   p.sendMessage(configHandler.getMessage(configHandler.success2xp, p.getName(), betrag));
         } else {
	  		   p.sendMessage(configHandler.getMessage(configHandler.lowmoney2xp, p.getName(), betrag));
         }
	  }
	  if (typ == 12 | typ == 14) {
		  //exchange
		  	String differenzstring = bankInteract.kontoneu(-betrag,p.getName(),all);
	  			  if (!differenzstring.equals("error")) {
	  				  Integer betragneu = (int)(new Double(differenzstring)*configHandler.exchangerate);
	  				  if (configHandler.limitxp == -1 || configHandler.limitxp>=betragneu+getBalanceXP(p.getName())) {
	             	 if (bankInteract.getBalance(p.getName())<0 && bankInteract.getBalance(p.getName())+new Double(differenzstring)>=0) {
	            		 bankInteract.startLoanPunishment(p.getName());
	            	 }
					
		            	 bankInteract.kontoneuxp(betragneu,p.getName(),false);
		            	 if (bankInteract.getBalanceXP(p.getName())>=0 && bankInteract.getBalanceXP(p.getName())-betragneu<0) {
		            		 bankInteract.stopLoanPunishmentXP(p.getName());
		            	 }
		     	  		   p.sendMessage(configHandler.getMessage(configHandler.success4, p.getName(), betrag));
	  				  } else {
	  					bankInteract.kontoneu(new Double(differenzstring),p.getName(),false);
		     	  		   p.sendMessage(configHandler.getMessage(configHandler.limitmsgxp, p.getName(), betrag));
	  				  }
	                   
	  			  } else {
	     	  		   p.sendMessage(configHandler.getMessage(configHandler.lowmoney2, p.getName(), betrag));
	  			  }
	  }
	  if (typ == 13 | typ == 15) {
		  //exchangexp
		  	String differenzstring = bankInteract.kontoneuxp(-betrag.intValue(),p.getName(),all);
	  			  if (!differenzstring.equals("error")) {
	  				  Double betragneu = new Double(differenzstring)/configHandler.exchangerate;
	  				  if (configHandler.limit == -1 || configHandler.limit>=betragneu+getBalance(p.getName())) {
	             	 if (bankInteract.getBalanceXP(p.getName())<0 && bankInteract.getBalanceXP(p.getName())+new Double(differenzstring)>=0) {
	            		 bankInteract.startLoanPunishmentXP(p.getName());
	            	 }
					
		            	 bankInteract.kontoneu(betragneu,p.getName(),false);
		            	 if (bankInteract.getBalance(p.getName())>=0 && bankInteract.getBalance(p.getName())-betragneu<0) {
		            		 bankInteract.stopLoanPunishment(p.getName());
		            	 }
		     	  		   p.sendMessage(configHandler.getMessage(configHandler.success4xp, p.getName(), betrag));
	  				  } else {
		     	  		   p.sendMessage(configHandler.getMessage(configHandler.limitmsg, p.getName(), betrag));
		  					bankInteract.kontoneuxp(new Integer(differenzstring),p.getName(),false);
	  				  }
	                   
	  			  } else {
	     	  		   p.sendMessage(configHandler.getMessage(configHandler.lowmoney2xp, p.getName(), betrag));
	  			  }
	  }
	  }
		  return false;
	  
}
	  
	  public static String kontoneuxp(Integer betrag,String pstring, Boolean all){
		  FileWriter writer;
		  File file, fileparent;
		  String enough ="error";
		    // File anlegen
		  fileparent = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts");
		     file = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts"+System.getProperty("file.separator")+pstring+".db");
		     try {
	                if (!file.exists()) {
	                	fileparent.mkdir();
	                    file.createNewFile();
	                    writer = new FileWriter(file, true);
	                    writer.write("0");
	     		        writer.write(System.getProperty("line.separator"));  
	     		        writer.flush();
	    		        writer.close();
	                }
			         FileReader fr = new FileReader(file);
			            BufferedReader reader = new BufferedReader(fr);
			            String st = reader.readLine(); 
			            	if ( new Integer(st)+configHandler.maxloanxp >= -betrag) {
				            	enough = (-betrag)+"";
				            	betrag += new Integer(st);
			            	} else {
			            		betrag = new Integer(st);
			            		enough = "error";
			            	}
			            	if (all==true && new Integer(st) >= -betrag) {
			            		betrag = 0;
			            		enough = st;
			            	}
			   fr.close();
			   reader.close();
               writer = new FileWriter(file);
		       writer.write(betrag+System.getProperty("line.separator"));
		       writer.flush();
		       writer.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
			return enough;
	  }
	  
	    public static Object[] getScrollingArray(Block block) {
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
	    
	    	
			List<String> loadArray=new ArrayList<String>();
			for (int i=0;i<liststring.split(",").length;i++) {
				loadArray.add(liststring.split(",")[i]);
				
			}
	      
			return loadArray.toArray();
	    	
	    	
	    }
	    
	    
	    
	    public static String updateSign(Block block,int steps){

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
	    return (String)scrollingSignArray[signPosition.get(block)];
	    }
	}


