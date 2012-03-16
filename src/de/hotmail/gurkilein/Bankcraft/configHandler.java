package de.hotmail.gurkilein.Bankcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;


public class configHandler{
    public static Double interest,interestxp;
    public static ChatColor bankcolor,color;
    public static Integer timer;
    public static Boolean broadcast, broadcastxp, onlinemoney,onlinexp;
    public static String amountadded, prefix, make, destroy, depositsign, debitsign, balancesign, depositsignxp, debitsignxp, balancesignxp, disallow, errorcreate, lowmoney1, lowmoney2, lowmoney3, success1, success2, balance, lowmoney1xp, lowmoney2xp, lowmoney3xp, success3, success3xp, success1xp, success2xp, balancexp, interestmsg, interestxpmsg;
	private static final ChatColor[] colors =  new ChatColor[]{ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.WHITE, ChatColor.YELLOW};
	public static String [][] interestGroups;
	private Bankcraft plugin;
	public configHandler(Bankcraft b1) {
		this.plugin= b1;
	}
	

	public boolean setConfig() {
		
	     try {
		        File interestdb = new File("plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"interests.db");
		        interestdb.createNewFile();
             
		         FileReader fr = new FileReader(interestdb);
		            BufferedReader reader = new BufferedReader(fr);
		            String st;
		    		List<String> interestGroupsLoad1 =new ArrayList<String>();
		    		List<String> interestGroupsLoad2 =new ArrayList<String>();
		    		List<String> interestGroupsLoad3 =new ArrayList<String>();
		            while ((st = reader.readLine()) != null) {
		            	interestGroupsLoad1.add(st.split("=")[0]);
		            	interestGroupsLoad2.add(st.split("=")[1]);
		            	interestGroupsLoad3.add(st.split("=")[2]);
		            }
		            interestGroups = new String [3][interestGroupsLoad2.size()];
		            for (int i=0; i<interestGroups[1].length;i++) {
		            	interestGroups[0][i] = interestGroupsLoad1.get(i);
		            	interestGroups[1][i] = interestGroupsLoad2.get(i);
		            	interestGroups[2][i] = interestGroupsLoad3.get(i);
		            }
		   fr.close();
		   reader.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
		
    	color= colors[this.plugin.getConfig().getInt("language.color")];
    	bankcolor = colors[this.plugin.getConfig().getInt("language.signcolor")];
    	prefix = this.plugin.getConfig().getString("language.prefix")+" ";
		interest = this.plugin.getConfig().getDouble("general.interest");
		interestxp = this.plugin.getConfig().getDouble("general.interestxp");
		onlinemoney = this.plugin.getConfig().getBoolean("general.justonlinemoney");
		onlinexp = this.plugin.getConfig().getBoolean("general.justonlinexp");
		broadcast = this.plugin.getConfig().getBoolean("general.broadcast");
		broadcastxp = this.plugin.getConfig().getBoolean("general.broadcastxp");
	    timer = this.plugin.getConfig().getInt("general.timer");
		depositsign = this.plugin.getConfig().getString("language.depositsign");
		debitsign = this.plugin.getConfig().getString("language.debitsign");
		balancesign = this.plugin.getConfig().getString("language.balancesign");
		depositsignxp = this.plugin.getConfig().getString("language.depositsignxp");
		debitsignxp = this.plugin.getConfig().getString("language.debitsignxp");
		balancesignxp = this.plugin.getConfig().getString("language.balancesignxp");
		make = (color+prefix+this.plugin.getConfig().getString("language.make"));
		destroy = (color+prefix+this.plugin.getConfig().getString("language.destroy"));
		disallow = (color+prefix+this.plugin.getConfig().getString("language.disallow"));
		errorcreate = (color+prefix+this.plugin.getConfig().getString("language.errorcreate"));
		lowmoney1 = (color+prefix+this.plugin.getConfig().getString("language.lowmoney1"));
		lowmoney2 = (color+prefix+this.plugin.getConfig().getString("language.lowmoney2"));
		lowmoney3 = (color+prefix+this.plugin.getConfig().getString("language.lowmoney3"));
		success1 = (color+prefix+this.plugin.getConfig().getString("language.success1"));
		success2 = (color+prefix+this.plugin.getConfig().getString("language.success2"));
		success3 = (color+prefix+this.plugin.getConfig().getString("language.success3"));
		balance = (color+prefix+this.plugin.getConfig().getString("language.balance"));
		lowmoney1xp = (color+prefix+this.plugin.getConfig().getString("language.lowmoney1xp"));
		lowmoney2xp = (color+prefix+this.plugin.getConfig().getString("language.lowmoney2xp"));
		lowmoney3xp = (color+prefix+this.plugin.getConfig().getString("language.lowmoney3xp"));
		success1xp = (color+prefix+this.plugin.getConfig().getString("language.success1xp"));
		success2xp = (color+prefix+this.plugin.getConfig().getString("language.success2xp"));
		success3xp = (color+prefix+this.plugin.getConfig().getString("language.success3xp"));
		balancexp = (color+prefix+this.plugin.getConfig().getString("language.balancexp"));
		interestmsg = (color+prefix+this.plugin.getConfig().getString("language.interestmsg"));
		interestxpmsg = (color+prefix+this.plugin.getConfig().getString("language.interestxpmsg"));
		amountadded = (color+prefix+this.plugin.getConfig().getString("language.amountadded"));
		return true;
	}
	public boolean defaultConfig() {
		FileConfiguration config;
		try{
			config = plugin.getConfig();
			File configfile = new File ( "plugins"+System.getProperty("file.separator")+"Bankcraft"+System.getProperty("file.separator")+"config.yml");
			configfile.mkdir();
			//CONFIGDEFAULTS
			if(!config.contains("general.interest")) {
				config.set("general.interest", 0.005);
				}
			if(!config.contains("general.interestxp")) {
				config.set("general.interestxp", 0.005);
			}
			if(!config.contains("general.timer")) {
				config.set("general.timer", 120);
				}
			if(!config.contains("general.broadcast")) {
				config.set("general.broadcast", true);
				}
			if(!config.contains("general.broadcastxp")) {
				config.set("general.broadcastxp", true);
				}
			if(!config.contains("general.justonlinemoney")) {
				config.set("general.justonlinemoney", false);
				}
			if(!config.contains("general.justonlinexp")) {
				config.set("general.justonlinexp", false);
				}
			if(!config.contains("language.color")) {
				config.set("language.color", 11);
			}
			if(!config.contains("language.prefix")) {
				config.set("language.prefix", "[BANK]");
			}
				if(!config.contains("language.signcolor")) {
					config.set("language.signcolor", 11);
			}
			if(!config.contains("language.balancesign")) {
				config.set("language.balancesign", "Balance");
			}
			if(!config.contains("language.depositsign")) {
				config.set("language.depositsign", "Deposit");
			}
			if(!config.contains("language.debitsign")) {
				config.set("language.debitsign", "Debit");
			}
			if(!config.contains("language.balancesignxp")) {
				config.set("language.balancesignxp", "XPBalance");
			}
			if(!config.contains("language.depositsignxp")) {
				config.set("language.depositsignxp", "XPDeposit");
			}
			if(!config.contains("language.debitsignxp")) {
				config.set("language.debitsignxp", "XPDebit");
			}
			if(!config.contains("language.make")) {
				config.set("language.make", "Bank created!");
				}
			if(!config.contains("language.destroy")) {
				config.set("language.destroy", "Bank removed!");
				}
			if(!config.contains("language.disallow")) {
				config.set("language.disallow", "You are not allowed to do this!");
				}
			if(!config.contains("language.errorcreate")) {
				config.set("language.errorcreate", "Error creating bank!");
				}
			if(!config.contains("language.lowmoney1")) {
				config.set("language.lowmoney1", "Not enough money to do this!");
				}
			if(!config.contains("language.lowmoney2")) {
				config.set("language.lowmoney2", "Not enough money on your account!");
				}
			if(!config.contains("language.lowmoney3")) {
				config.set("language.lowmoney3", "Not enough money on the players account!");
				}
			if(!config.contains("language.success1")) {
				config.set("language.success1", "Money deposited!");
				}
			if(!config.contains("language.success2")) {
				config.set("language.success2", "Money debited!");
				}
			if(!config.contains("language.success3")) {
				config.set("language.success3", "Money transfered!");
				}
			if(!config.contains("language.balance")) {
				config.set("language.balance", "Balance: %balance Dollar");
				}
			if(!config.contains("language.lowmoney1xp")) {
				config.set("language.lowmoney1xp", "Not enough experience to do this!");
				}
			if(!config.contains("language.lowmoney2xp")) {
				config.set("language.lowmoney2xp", "Not enough experience on your account!");
				}
			if(!config.contains("language.lowmoney3xp")) {
				config.set("language.lowmoney3xp", "Not enough experience on the players account!");
			}
			if(!config.contains("language.success1xp")) {
				config.set("language.success1xp", "Experience deposited!");
				}
			if(!config.contains("language.success2xp")) {
				config.set("language.success2xp", "Experience debited!");
				}
			if(!config.contains("language.success3xp")) {
				config.set("language.success3xp", "Experience transfered!");
				}
			if(!config.contains("language.balancexp")) {
				config.set("language.balancexp", "Banked experience: %balance XP");
			}
			if(!config.contains("language.interestmsg")) {
				config.set("language.interestmsg", "%interest money granted! You know have %balance!");
			}
			if(!config.contains("language.interestxpmsg")) {
				config.set("language.interestxpmsg", "%interest XP granted! You know have %balance!");
			}
			if(!config.contains("language.amountadded")) {
				config.set("language.amountadded", "Your value was successfully added to the sign!");
			}
			this.plugin.saveConfig();
			this.plugin.reloadConfig();
		} catch(Exception e1){

		e1.printStackTrace();

		}
		return false;
		
	}
}
