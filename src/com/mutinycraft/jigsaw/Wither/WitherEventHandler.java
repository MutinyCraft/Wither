package com.mutinycraft.jigsaw.Wither;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import com.mutinycraft.jigsaw.Wither.Wither;

public class WitherEventHandler implements Listener{
	private Wither plugin;
	
	public WitherEventHandler(Wither pl){
		this.plugin = pl;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	//Wither Eat/Change Block Event
	@EventHandler
	public void WitherBlockBreak(EntityChangeBlockEvent  event){
		EntityType eType;
		try{
			eType = event.getEntityType();
		}catch(Exception e){
			eType = null;
		}
		if(eType !=null){
			if((eType == EntityType.WITHER) && plugin.isBlockWitherDamage()){
				event.setCancelled(true);			
			}
		}
	}
	
	//Wither Eat/Change Hanging Event
	@EventHandler
	public void NoPaintingBreak(HangingBreakByEntityEvent  event){
		EntityType eType;
		try{
			eType = event.getRemover().getType();
		}catch(Exception e){
			eType = null;
		}
		if(eType != null){
			if((eType == EntityType.WITHER) && plugin.isBlockWitherDamage()){
				event.setCancelled(true);
			}
		}
	}
	
	//Wither Explode Event
		@EventHandler
		public void WitherExplosionDamage(EntityExplodeEvent event){
			EntityType eType;
			try{
				eType = event.getEntityType();
			}catch(Exception e){
				eType = null;
			}
			if((eType != null)){
				if (eType == EntityType.WITHER) {
	                if (plugin.isBlockWitherDamage()) {
	                    event.blockList().clear();
	                    return;
	                }

	                if (plugin.isBlockWitherExplosion()) {
	                    event.setCancelled(true);
	                    return;
	                }
	            }

	            if (eType == EntityType.WITHER_SKULL) {
	                if (plugin.isBlockWitherDamage()) {
	                    event.blockList().clear();
	                    return;
	                }

	                if (plugin.isBlockWitherExplosion()) {
	                    event.setCancelled(true);
	                    return;
	                }
	            }
			}
		}
	
	//Wither Explosion Prime Event
	@EventHandler
	public void WitherExplode(ExplosionPrimeEvent  event){
		EntityType eType;
		try{
			eType = event.getEntityType();
		}catch(Exception e){
			eType = null;
		}
		if(eType != null){
			if (eType == EntityType.WITHER) {
	            if (plugin.isBlockWitherExplosion()) {
	                event.setCancelled(true);
	                return;
	            }
	        }
	        else if (eType == EntityType.WITHER_SKULL) {
	            if (plugin.isBlockWitherExplosion()) {
	                event.setCancelled(true);
	                return;
	            }
	        }
		}
		
	}
	
	//Wither Spawn Event
	@EventHandler
	public void WitherSpawn(CreatureSpawnEvent event){
		EntityType eType;
		try{
			eType = event.getEntityType();
		}catch(Exception e){
			eType = null;
		}
		if(eType != null){
			if(event.getEntityType() == EntityType.WITHER && plugin.isBlockWitherSpawn()){
				event.setCancelled(true);
			}
		}
	}
	
	//Wither Spawn Attempt Event
	@EventHandler
	public void WitherCreation(BlockPlaceEvent event){
		Player player = event.getPlayer();
		
		
		Block block = event.getBlock();

		if((event.getBlock().getType().equals(Material.SKULL)) && isAboveSoulSand(block)){
			if(!player.hasPermission("wither.create")){
				event.setCancelled(true);
				player.sendMessage(plugin.getMessage());
			}
			else if(plugin.isWorldBlock() && isBlockedWorld(event.getPlayer().getWorld().getName())){
				event.setCancelled(true);
				player.sendMessage(plugin.getWorldMessage());
			}
		}
		else if((event.getBlock().getType().equals(Material.SOUL_SAND)) && isBelowSkull(block)){
			if(!player.hasPermission("wither.create")){
				event.setCancelled(true);
				player.sendMessage(plugin.getMessage());
			}
			else if(plugin.isWorldBlock() && isBlockedWorld(event.getPlayer().getWorld().getName())){
				event.setCancelled(true);
				player.sendMessage(plugin.getWorldMessage());
			}
		}
	}
	
	//Exploit With Piston Attempt Event
	@EventHandler
	public void WitherCreationViaExploit(BlockPistonExtendEvent event){
		List<Block> blocks;
		try{
			blocks = event.getBlocks();
		}
		catch(Exception e){
			blocks = null;
		}
		if(blocks != null && blocks.size() > 0){
			for(int i = 0; i < blocks.size(); i++){
				Material type = blocks.get(i).getType();
				if(type == Material.SOUL_SAND){
					if(isNearWitherSkull(blocks.get(i))){
						event.setCancelled(true);
					}
				}
			}
		}		
	}
	
	/***************************HELPERS*****************************/
	
	//World Check
	private boolean isBlockedWorld(String world){
		for(int i = 0; i < plugin.getWorlds().size(); i++){
			String worldName = (String) plugin.getWorlds().get(i);
			if(worldName.equals(world)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isAboveSoulSand(Block block){
		Location blockbelowlocation = block.getLocation();
		blockbelowlocation.setY(block.getY() - 1);
		return(blockbelowlocation.getBlock().getType().equals(Material.SOUL_SAND));
	}
	
	private boolean isBelowSkull(Block block){
		Location blockabovelocation = block.getLocation();
		blockabovelocation.setY(block.getY() + 1);
		return(blockabovelocation.getBlock().getType().equals(Material.SKULL));
	}
	
	private boolean isNearWitherSkull(Block block){
		Location blockNearLocation = block.getLocation();
		blockNearLocation.setY(block.getY() + 1);
		//X Check
		blockNearLocation.setX(block.getX() + 1);
		if(blockNearLocation.getBlock().getType().equals(Material.SKULL)){
			return true;
		}
		blockNearLocation.setX(block.getX() - 1);
		if(blockNearLocation.getBlock().getType().equals(Material.SKULL)){
			return true;
		}
		//Y Check
		blockNearLocation.setX(block.getX());
		blockNearLocation.setZ(block.getZ() + 1);
		if(blockNearLocation.getBlock().getType().equals(Material.SKULL)){
			return true;
		}
		blockNearLocation.setZ(block.getZ() - 1);
		if(blockNearLocation.getBlock().getType().equals(Material.SKULL)){
			return true;
		}
		return false;
	}
	
	/***************************CUSTOM DAMAGE*****************************/
	
	
	
	
}
