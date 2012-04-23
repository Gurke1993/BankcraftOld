package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.TimerTask;
import org.bukkit.entity.Player;

import de.hotmail.gurkilein.Bankcraft.database.BcMysql;

public class ZinsenTimerTask extends TimerTask{
	FileWriter writer;
	  public void run()  {
	  try {
		  double interest = 0D;
		  Player p;
		  File fB;
	      Player[] playerArray = Bankcraft.server.getOnlinePlayers();
		//  GELDzinsen
			if (configHandler.isMysql()) {
				BcMysql db = configHandler.getDb();
				// MySQL
				      Object[] accounts = configHandler.getDb().getAccounts();
				      Object[] xpaccounts = configHandler.getDb().getAccounts();
				      // MONEY
				      if (accounts != null) {
				    	  for (int i= 0; i < accounts.length; i++) {
								if (configHandler.onlinemoney == false || Bankcraft.server.getPlayer((String)accounts[i]) != null) {
									if (new Double (db.getBalance((String)accounts[i])) >= 0) {
										interest = configHandler.interest;
									} else {
										interest = configHandler.loaninterest;
									}
						            p = Bankcraft.server.getPlayer((String)accounts[i]);
						            if (p!=null) {
						            for (int k=0; k<configHandler.interestGroups[0].length;k++) {
						            if (Bankcraft.perms.playerHas(p, "bankcraft.interest."+configHandler.interestGroups[0][k])) {
						            	if (new Double(db.getBalance((String)accounts[i]))>=0) {
						            	interest = new Double(configHandler.interestGroups[1][k]);
						            	} else {
						            		p.damage(configHandler.loandamage);
						            		interest = new Double(configHandler.interestGroups[3][k]);
						            	}
						            }
						            }
						            }
					            	Double betrag = new Double(db.getBalance((String)accounts[i]))*(interest+1);
					            	DecimalFormat df= new DecimalFormat("#0.00");   
					            	String betragstring= df.format(betrag);  
					            	betrag = Double.parseDouble(betragstring); 
		        if (betrag <= configHandler.limit || configHandler.limit == -1) {
		        	db.setAccount((String)accounts[i], betrag);
		        } else {
		        	db.setAccount((String)accounts[i], new Double(db.getBalance((String)accounts[i])));
		        }
			       if (configHandler.broadcast == true) {
				       Double zinsen = new Double(db.getBalance((String)accounts[i]))*(interest);
			       	DecimalFormat dft= new DecimalFormat("#0.00");   
			       	String betragstringt= dft.format(zinsen);  
			       	zinsen = Double.parseDouble(betragstringt);  
				       for (int j =0; j< playerArray.length; j++) {
				    	   if ( db.getAccount(playerArray[j].getName()) == true && playerArray[j].getName().equals((String)accounts[i])) {
				    		   if (betrag <= configHandler.limit || configHandler.limit == -1) {
			     	  		   playerArray[j].sendMessage(configHandler.getMessage(configHandler.interestmsg, p.getName(), zinsen));
				    		   } else {
				    			   playerArray[j].sendMessage(configHandler.getMessage(configHandler.interestlimitmsg, p.getName(), zinsen));
				    		   }
				    	   }
								}
				    	  }
				      }
				}
				      }
				      
				      //XP ZINsen
				      
				      if (xpaccounts != null) {
				    	  for (int i= 0; i < xpaccounts.length; i++) {
								if (configHandler.onlinexp == false || Bankcraft.server.getPlayer((String)xpaccounts[i]) != null) {
									if (new Double (db.getXpBalance((String)xpaccounts[i])) >= 0) {
										interest = configHandler.interestxp;
									} else {
										interest = configHandler.loaninterestxp;
									}
						            p = Bankcraft.server.getPlayer((String)xpaccounts[i]);
						            if (p!=null) {
						            for (int k=0; k<configHandler.interestGroups[0].length;k++) {
						            if (Bankcraft.perms.playerHas(p, "bankcraft.interest."+configHandler.interestGroups[0][k])) {
						            	if (new Double(db.getBalance((String)xpaccounts[i]))>=0) {
						            	interest = new Double(configHandler.interestGroups[2][k]);
						            	} else {
						            		p.damage(configHandler.loandamage);
						            		interest = new Double(configHandler.interestGroups[4][k]);
						            	}
						            }
						            }
						            }
					            	Double betrag = new Double(db.getXpBalance((String)xpaccounts[i]))*(interest+1);
		        if (betrag <= configHandler.limitxp || configHandler.limitxp == -1) {
		        	Integer betragint = betrag.intValue();
		        	db.setXpAccount((String)xpaccounts[i], betragint.toString());
		        } else {
		        	db.setAccount((String)xpaccounts[i], new Double(db.getXpBalance((String)xpaccounts[i])));
		        }
			       if (configHandler.broadcastxp == true) {
				       Double zinsen = new Double(db.getXpBalance((String)xpaccounts[i]))*(interest);
			       	DecimalFormat dft= new DecimalFormat("#0");   
			       	String betragstringt= dft.format(zinsen);  
			       	zinsen = Double.parseDouble(betragstringt);  
				       for (int j =0; j< playerArray.length; j++) {
				    	   if ( db.getXpAccount(playerArray[j].getName()) == true && playerArray[j].getName().equals((String)xpaccounts[i])) {
				    		   if (betrag <= configHandler.limitxp || configHandler.limitxp == -1) {
			     	  		   playerArray[j].sendMessage(configHandler.getMessage(configHandler.interestxpmsg, p.getName(), zinsen));
				    		   } else {
				    			   playerArray[j].sendMessage(configHandler.getMessage(configHandler.interestlimitmsgxp, p.getName(), zinsen));
				    		   }
				    	   }
								}
				    	  }
				      }
				}
				      }
			} else {
				//Flatfile
		  File fA = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts");
		  File[] fileArray = fA.listFiles();
		  if (fileArray != null) {
			for (int i = 0; i < fileArray.length; i++) {
				if (configHandler.onlinemoney == false || Bankcraft.server.getPlayer(fileArray[i].getName().toString().split(".db")[0]) != null) {
				File file = fileArray[i];
		         FileReader fr = new FileReader(file);
		            BufferedReader reader = new BufferedReader(fr);
		            String st = reader.readLine(); 
		            if (new Double(st)>=0) {
		            	interest = configHandler.interest;
		            } else {
		            	interest = configHandler.loaninterest;
		            }
		            p = Bankcraft.server.getPlayer(fileArray[i].getName().toString().split(".db")[0]);
		            if (p!=null) {
		            for (int k=0; k<configHandler.interestGroups[0].length;k++) {
		            if (Bankcraft.perms.playerHas(p, "bankcraft.interest."+configHandler.interestGroups[0][k])) {
		            	if (new Double(st)>=0) {
		            	interest = new Double(configHandler.interestGroups[1][k]);
		            	} else {
		            		p.damage(configHandler.loandamage);
		            		interest = new Double(configHandler.interestGroups[3][k]);
		            	}
		            }
		            }
		            }
			            	Double betrag = new Double(st)*(interest+1);
			            	DecimalFormat df= new DecimalFormat("#0.00");   
			            	String betragstring= df.format(betrag);  
			            	betrag = Double.parseDouble(betragstring);  
		   fr.close();
		   reader.close();
        writer = new FileWriter(file);
        if (betrag <= configHandler.limit || configHandler.limit == -1) {
	       writer.write(betrag+System.getProperty("line.separator"));
        } else {
        	writer.write(st);
        }
	       writer.flush();
	       writer.close();
	       if (configHandler.broadcast == true) {
	       Double zinsen = new Double(st)*(interest);
       	DecimalFormat dft= new DecimalFormat("#0.00");   
       	String betragstringt= dft.format(zinsen);  
       	zinsen = Double.parseDouble(betragstringt);  
	       for (int j =0; j< playerArray.length; j++) {
	    	   fB = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"Accounts"+System.getProperty("file.separator")+playerArray[j].getName()+".db");
	    	   if ( fB.isFile() == true && fB.equals(file)) {
	    		   if (betrag <= configHandler.limit || configHandler.limit == -1) {
     	  		   playerArray[j].sendMessage(configHandler.getMessage(configHandler.interestmsg, p.getName(), zinsen));
	    		   } else {
	    			   playerArray[j].sendMessage(configHandler.getMessage(configHandler.interestlimitmsg, p.getName(), zinsen));
	    		   }
	    	   }
	       }
	       }
			}
			}
		  }
			}
	       //EXPZinsen
		  double interestxp= 0D;
			  File fAt = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts");
		       Player[] playerArrayt = Bankcraft.server.getOnlinePlayers();
			  File[] fileArrays = fAt.listFiles();
			  if (fileArrays != null) {
				for (int it = 0; it < fileArrays.length; it++) {
					if (configHandler.onlinexp == false || Bankcraft.server.getPlayer(fileArrays[it].getName().toString().split(".db")[0]) != null) {
					File filet = fileArrays[it];
			         FileReader frt = new FileReader(filet);
			            BufferedReader readert = new BufferedReader(frt);
			            String stt = readert.readLine(); 
			            if (new Integer(stt)>=0) {
			            	interestxp = configHandler.interestxp;
			            } else {
			            	interestxp = configHandler.loaninterestxp;
			            }
			            p = Bankcraft.server.getPlayer(fileArrays[it].getName().toString().split(".db")[0]);
			            if (p != null) {
			            for (int r=0; r<configHandler.interestGroups[0].length;r++) {
			            if (Bankcraft.perms.has(p, "bankcraft.interest."+configHandler.interestGroups[0][r])) {
			            	if (new Integer(stt)>=0) {
				            	interestxp = new Double(configHandler.interestGroups[2][r]);
				            	} else {
				            		p.damage(configHandler.loandamage);
				            		interestxp = new Double(configHandler.interestGroups[4][r]);
				            	}
			            }
			            }
			            }
			            
			            
				            	Double betragt = new Integer(stt)*(interestxp+1);
			   frt.close();
			   readert.close();
	        writer = new FileWriter(filet);
	        if (betragt <= configHandler.limitxp || configHandler.limitxp == -1) {
		       writer.write(betragt.intValue()+System.getProperty("line.separator"));
	        } else {
	        	writer.write(stt);
	        }
		       writer.flush();
		       writer.close();
		       if (configHandler.broadcastxp == true) {
			       Double zinsent = new Integer(stt)*(interestxp);
			       for (int j =0; j< playerArrayt.length; j++) {
			    	   fB = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"XPAccounts"+System.getProperty("file.separator")+playerArray[j].getName()+".db");
			    	   if ( fB.isFile() == true && fB.equals(filet)) {
			    		   if (betragt <= configHandler.limitxp || configHandler.limitxp == -1) {
		     	  		   p.sendMessage(configHandler.getMessage(configHandler.interestxpmsg, p.getName(), zinsent));
			    		   } else {
			    			   p.sendMessage(configHandler.getMessage(configHandler.interestlimitmsgxp, p.getName(), zinsent));
			    		   }
		     	  		   }
			       }
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