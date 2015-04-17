package exampleplugin;

import pokejava.api.events.EnableEvent;
import pokejava.api.events.EventHandler;
import pokejava.api.plugin.Plugin;

@Plugin(id = "sampleplugin", name = "Sample Plugin", version = "1.0", author = "PoketronHacker")
public class SamplePlugin {
	
	@EventHandler
	public void onEnable(EnableEvent enableEvent) {
		System.out.println("This Hook has been activated.");
	}
	
	public void onDisable() {
		
	}
	
}
