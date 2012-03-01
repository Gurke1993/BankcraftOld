package de.hotmail.gurkilein.Bankcraft;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Bankcraft extends JavaPlugin{
    public static Economy econ = null;
    public static Permission perms = null;
    public static Vault vault = null;
    public static Double interest,interestxp;
    public static ChatColor bankcolor,color;
    public static Integer timer;
    public static Server server;
    public static Boolean broadcast, broadcastxp;
    public static String prefix, make, destroy, depositsign, debitsign, depositsignscroll, debitsignscroll, balancesign, depositsignxp, debitsignxp, depositsignscrollxp, debitsignscrollxp, balancesignxp, disallow, errorcreate, lowmoney1, lowmoney2, success1, success2, balance, lowmoney1xp, lowmoney2xp, success1xp, success2xp, balancexp;
	public final BankcraftPlayerListener playerListener = new BankcraftPlayerListener();
	public final BankcraftBlockListener blockListener = new BankcraftBlockListener();
	public static Logger log = Logger.getLogger("Minecraft");
	private static int taskId = -1;
	public static  Object[] betragArray;
	private static final ChatColor[] colors =  new ChatColor[]{ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.WHITE, ChatColor.YELLOW};
	 

	public void onEnable(){
		Server serverl = this.getServer();
		server = serverl;
		
        Plugin x = this.getServer().getPluginManager().getPlugin("Vault");
        if(x != null & x instanceof Vault) {
            vault = (Vault) x; }
		        if (!setupEconomy() ) {
		            log.info("Vault needed");
		            getServer().getPluginManager().disablePlugin(this);
		            return;
		        }
		        setupPermissions();
		//Settings
		FileConfiguration config;
		try{
			File configfile = new File("plugins" + System.getProperty("file.separator") + "Bankcraft");
			configfile.mkdir();
			//CONFIGDEFAULTS
			config = getConfig();
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
			if(!config.contains("language.depositsignscroll")) {
				config.set("language.depositsignscroll", "DepositS");
			}
			if(!config.contains("language.debitsignscroll")) {
				config.set("language.debitsignscroll", "DebitS");	
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
			if(!config.contains("language.depositsignscrollxp")) {
				config.set("language.depositsignscrollxp", "XPDepositS");
			}
			if(!config.contains("language.debitsignscrollxp")) {
				config.set("language.debitsignscrollxp", "XPDebitS");
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
			if(!config.contains("language.success1")) {
				config.set("language.success1", "Money deposited!");
				}
			if(!config.contains("language.success2")) {
				config.set("language.success2", "Money debited!");
				}
			if(!config.contains("language.balance")) {
				config.set("language.balance", "Balance: ");
				}
			if(!config.contains("language.lowmoney1xp")) {
				config.set("language.lowmoney1xp", "Not enough experience to do this!");
				}
			if(!config.contains("language.lowmoney2xp")) {
				config.set("language.lowmoney2xp", "Not enough experience on your account!");
				}
			if(!config.contains("language.success1xp")) {
				config.set("language.success1xp", "Experience deposited!");
				}
			if(!config.contains("language.success2xp")) {
				config.set("language.success2xp", "Experience debited!");
				}
			if(!config.contains("language.balancexp")) {
				config.set("language.balancexp", "Banked experience: ");
			}
			saveConfig();
			configReload();
		} catch(Exception e1){

		e1.printStackTrace();

		}
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.blockListener, this);
		//BETRAGARRAY
		List<Integer> betragArrayload=new ArrayList<Integer>();
		for (int i= 0; i <= 9; i++) {
			betragArrayload.add((int)Math.pow(10, i));
		}
		betragArray = betragArrayload.toArray();
		
	    toggleTimerTask();
		log.info("Bankcraft has been enabled!");
	}
 
	public void onDisable(){
		log.info("Bankcraft has been disabled.");
	}
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
        return econ != null;
    }
    public void toggleTimerTask() {
		Integer timer = getConfig().getInt("general.timer");
        if (taskId != -1)
          getServer().getScheduler().cancelTask(taskId);
        else
          taskId = getServer().getScheduler().scheduleAsyncRepeatingTask(
            this, new ZinsenTimerTask(), 1200*timer, 
            1200 * timer);
      }
    public void configReload() {
    	reloadConfig();
    	color= colors[getConfig().getInt("language.color")];
    	bankcolor = colors[getConfig().getInt("language.signcolor")];
    	prefix = getConfig().getString("language.prefix")+" ";
		interest = getConfig().getDouble("general.interest");
		interestxp = getConfig().getDouble("general.interestxp");
		broadcast = getConfig().getBoolean("general.broadcast");
		broadcastxp = getConfig().getBoolean("general.broadcastxp");
	    timer = getConfig().getInt("general.timer");
		depositsign = getConfig().getString("language.depositsign");
		debitsign = getConfig().getString("language.debitsign");
		depositsignscroll = getConfig().getString("language.depositsignscroll");
		debitsignscroll = getConfig().getString("language.debitsignscroll");
		balancesign = getConfig().getString("language.balancesign");
		depositsignxp = getConfig().getString("language.depositsignxp");
		debitsignxp = getConfig().getString("language.debitsignxp");
		depositsignscrollxp = getConfig().getString("language.depositsignscrollxp");
		debitsignscrollxp = getConfig().getString("language.debitsignscrollxp");
		balancesignxp = getConfig().getString("language.balancesignxp");
		make = color+prefix+getConfig().getString("language.make");
		destroy = color+prefix+getConfig().getString("language.destroy");
		disallow = color+prefix+getConfig().getString("language.disallow");
		errorcreate = color+prefix+getConfig().getString("language.errorcreate");
		lowmoney1 = color+prefix+getConfig().getString("language.lowmoney1");
		lowmoney2 = color+prefix+getConfig().getString("language.lowmoney2");
		success1 = color+prefix+getConfig().getString("language.success1");
		success2 = color+prefix+getConfig().getString("language.success2");
		balance = color+prefix+getConfig().getString("language.balance");
		lowmoney1xp = color+prefix+getConfig().getString("language.lowmoney1xp");
		lowmoney2xp = color+prefix+getConfig().getString("language.lowmoney2xp");
		success1xp = color+prefix+getConfig().getString("language.success1xp");
		success2xp = color+prefix+getConfig().getString("language.success2xp");
		balancexp = color+prefix+getConfig().getString("language.balancexp");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player player = null;
    	if (sender instanceof Player) {
    		player = (Player) sender;
    	}
    	if (cmd.getName().equalsIgnoreCase("bcreload")){ 
    		if (player == null) {
    	    	sender.sendMessage("Bankcraftconfig reloaded!");
    			configReload();
    		return true;
    	}
    }
    	return false;
}

}

