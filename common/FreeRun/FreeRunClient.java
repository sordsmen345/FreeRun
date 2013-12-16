package FreeRun;

import net.minecraftforge.client.MinecraftForgeClient;

public class FreeRunClient extends FreeRunProxy
{
	@Override
	public void registerRenderInformation()
	{
		MinecraftForgeClient.preloadTexture("/resources/terrain.png");
		MinecraftForgeClient.preloadTexture("/resources/gui/items.png");
	}
}
