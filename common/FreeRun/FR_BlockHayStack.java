package FreeRun;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class FR_BlockHayStack extends Block
{
	public FR_BlockHayStack(int i, int j)
	{
		super(i, j, FreeRun.materialHay);
	}
	
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
	{
		double d = 0.001D;
		entity.fallDistance = 0F;
		if (entity.motionY <= -0.4D)
		{
			entity.addVelocity(0D, 0.4D, 0D);
		}
		entity.motionX *= d;
		entity.motionZ *= d;
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return null;
	}
	
	public boolean isOpaqueCube()
	{
		return false;
	}
}
