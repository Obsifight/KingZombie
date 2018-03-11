package fr.thisismac.zombie;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Listeners implements Listener {
	
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event){
		if(Core.INSTANCE.isSpawned() && event.getChunk() == Core.INSTANCE.getSpawnLoc().getChunk()){
			event.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if (e.getEntity().hasMetadata("event")) {
			Core.INSTANCE.dropStuff(e.getEntity().getLocation(), true);
			Core.INSTANCE.spawning = false;
		}
	}
	
}