package exampleplugin2;

import pokejava.api.events.EnableEvent;
import pokejava.api.events.EventHandler;
import pokejava.api.plugin.Plugin;

@Plugin(id = "secondplugin", name = "Second Plugin", version = "1.0", author = "PoketronHacker")
public class SecondPlugin {
	
	@EventHandler
	public void enableEvent(EnableEvent enableEvent) {
		System.out.println("[Second Plugin] Hook Executed");
	}
	
}
