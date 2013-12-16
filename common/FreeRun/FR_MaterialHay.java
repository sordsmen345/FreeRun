package FreeRun;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class FR_MaterialHay extends Material
{
	public FR_MaterialHay(MapColor mapcolor)
	{
		super(mapcolor);
	}
	
	public boolean isSolid()
	{
		return false;
	}
	
	public boolean blocksMovement()
    {
        return false;
    }
	
	public boolean getCanBurn()
	{
		return true;
	}
}
