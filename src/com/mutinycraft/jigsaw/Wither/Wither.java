package com.mutinycraft.jigsaw.Wither;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.mutinycraft.jigsaw.Wither.WitherEventHandler;

public class Wither extends JavaPlugin implements Listener{
	Logger log;
	File configFile;
    FileConfiguration config;
	
    private boolean blockWitherDamage;
    private boolean blockWitherExplosion;
    private boolean blockWitherSpawn;
    private boolean blockWorlds;
    private List<String> worlds;
    private String message;
    private String worldMessage;
	private static final String VERSION = " v2.1";
	private WitherCommandExecutor cmdExecutor;
	
	/***************** Enable *****************/
	public void onEnable(){
		log = this.getLogger();
		configFile = new File(getDataFolder(), "config.yml");
		try {
	        firstRun();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		config = new YamlConfiguration();
		loadYamls();
		setConfigOptions();
		getServer().getPluginManager().registerEvents(this, this);
		new WitherEventHandler(this);
		loadCommands();
		log.info(this.getName() + VERSION + " enabled!");
	}
	
	private void loadCommands() {
		cmdExecutor = new WitherCommandExecutor(this);
		getCommand("wither").setExecutor(cmdExecutor);
	}
	
	/***************** Config Handling *****************/
	private void setConfigOptions() {
		blockWitherDamage = config.getBoolean("No-Wither-Block-Damage", true);
		blockWitherExplosion = config.getBoolean("No-Wither-Explosion-Damage", false);
		blockWitherSpawn = config.getBoolean("No-Wither-Spawn", false);
		blockWorlds = config.getBoolean("Enable-World-Restriction", false);
		worlds = config.getStringList("Worlds-To-Block-Wither-Spawn");
		message = ChatColor.translateAlternateColorCodes('&',
				  config.getString("Permsission-Blocked-Message", "&cYou are not allowed to spawn a Wither!"));
		worldMessage = ChatColor.translateAlternateColorCodes('&', 
				  config.getString("World-Spawn-Blocked-Message", "&cYou are not allowed to spawn a Wither in this world!"));
	}

	private void firstRun() throws Exception{
		if(!configFile.exists()){
	        configFile.getParentFile().mkdirs();
	        copy(getResource("config.yml"), configFile);
	    }		
	}
	
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream fout = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            fout.write(buf,0,len);
	        }
	        fout.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void loadYamls() {
	    try {
	        config.load(configFile);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void reloadConfig() {
		loadYamls();
		setConfigOptions();
	}
	
	/***************** Accessors *****************/
	public boolean isBlockWitherDamage(){
		return blockWitherDamage;
	}
	
	public boolean isBlockWitherExplosion(){
		return blockWitherExplosion;
	}
	
	public boolean isBlockWitherSpawn(){
		return blockWitherSpawn;
	}
	
	public boolean isWorldBlock(){
		return blockWorlds;
	}
	
	public List<String> getWorlds(){
		return worlds;
	}
	
	public String getMessage(){
		return message;
	}
	
	public String getWorldMessage(){
		return worldMessage;
	}
	
	/***************** Disable *****************/
	public void onDispable(){
		log.info(this.getName() + VERSION + " disabled!");
	}
}
