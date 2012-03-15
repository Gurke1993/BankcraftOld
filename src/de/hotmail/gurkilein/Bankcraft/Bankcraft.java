package de.hotmail.gurkilein.Bankcraft;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Bankcraft extends JavaPlugin{
    public static Economy econ = null;
    public static Permission perms = null;
    public static Vault vault = null;
    public static Server server;
    public final BankcraftPlayerListener playerListener = new BankcraftPlayerListener();
	public final BankcraftBlockListener blockListener = new BankcraftBlockListener();
	public bankInteract bankInteract;
	public configHandler configHandler;
	public static Logger log = Logger.getLogger("Minecraft");
	private static int taskId = -1;
	public static  Object[] betragArray;

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
		        this.configHandler = new configHandler(this);
		        this.configHandler.defaultConfig();
		        this.configHandler.setConfig();
		        this.bankInteract = new bankInteract(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
		pm.registerEvents(this.blockListener, this);
		BankcraftCommandListener bcl= new BankcraftCommandListener(this);
		getCommand("bank").setExecutor(bcl);
		getCommand("bankadmin").setExecutor(bcl);
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

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player player = null;
    	if (sender instanceof Player) {
    		player = (Player) sender;
    	}
    	if (cmd.getName().equalsIgnoreCase("bcreload")){ 
    		if (player == null) {
    	    	sender.sendMessage("Bankcraftconfig reloaded!");
		        this.configHandler = new configHandler(this);
    			this.configHandler.defaultConfig();
    			this.configHandler.setConfig();
    		return true;
    	}
    }
    	if (cmd.getName().equalsIgnoreCase("bctimer")){ 
    		if (player == null) {
    	    	sender.sendMessage("Bankcrafttimer restarted!");
                toggleTimerTask();
                toggleTimerTask();
    		return true;
    	}
    }
    	return false;
}

}

