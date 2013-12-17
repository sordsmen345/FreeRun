package FreeRun;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class FR_BlockEdge extends Block
{
	protected FR_BlockEdge(int i, int j)
	{
		super(i, j, Material.circuits);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{
		int l = world.getBlockMetadata(i, j, k);
		float f = 0.125F;
		if (l == 2)
		{
			setBlockBounds(0.0F, 0.7F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		}
		if (l == 3)
		{
			setBlockBounds(0.0F, 0.7F, 0.0F, 1.0F, 1.0F, f);
		}
		if (l == 4)
		{
			setBlockBounds(1.0F - f, 0.7F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
		if (l == 5)
		{
			setBlockBounds(0.0F, 0.7F, 0.0F, f, 1.0F, 1.0F);
		}
		
		return super.getCollisionBoundingBoxFromPool(world, i, j, k);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	{
		int l = world.getBlockMetadata(i, j, k);
		float f = 0.125F;
		
		if (l == 2)
		{
			setBlockBounds(0.0F, 0.7F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		}
		if (l == 3)
		{
			setBlockBounds(0.0F, 0.7F, 0.0F, 1.0F, 1.0F, f);
		}
		if (l == 4)
		{
			setBlockBounds(1.0F - f, 0.7F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
		if (l == 5)
		{
			setBlockBounds(0.0F, 0.7F, 0.0F, f, 1.0F, 1.0F);
		}
		
		return super.getSelectedBoundingBoxFromPool(world, i, j, k);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return 8;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
	{
		if (world.isBlockNormalCube(i - 1, j, k))
		{
			return true;
		}
		if (world.isBlockNormalCube(i + 1, j, k))
		{
			return true;
		}
		if (world.isBlockNormalCube(i, j, k - 1))
		{
			return true;
		}
		return world.isBlockNormalCube(i, j, k + 1);
	}
	
	@Override
	public void onBlockPlaced(World world, int i, int j, int k, int l)
	{
		int i1 = world.getBlockMetadata(i, j, k);
		if ((i1 == 0 || l == 2) && world.isBlockNormalCube(i, j, k + 1))
		{
			i1 = 2;
		}
		if ((i1 == 0 || l == 3) && world.isBlockNormalCube(i, j, k - 1))
		{
			i1 = 3;
		}
		if ((i1 == 0 || l == 4) && world.isBlockNormalCube(i + 1, j, k))
		{
			i1 = 4;
		}
		if ((i1 == 0 || l == 5) && world.isBlockNormalCube(i - 1, j, k))
		{
			i1 = 5;
		}
		world.setBlockMetadataWithNotify(i, j, k, i1);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		int i1 = world.getBlockMetadata(i, j, k);
		boolean flag = false;
		if (i1 == 2 && world.isBlockNormalCube(i, j, k + 1))
		{
			flag = true;
		}
		if (i1 == 3 && world.isBlockNormalCube(i, j, k - 1))
		{
			flag = true;
		}
		if (i1 == 4 && world.isBlockNormalCube(i + 1, j, k))
		{
			flag = true;
		}
		if (i1 == 5 && world.isBlockNormalCube(i - 1, j, k))
		{
			flag = true;
		}
		if (!flag)
		{
			dropBlockAsItem(world, i, j, k, i1, 0);
			world.setBlockWithNotify(i, j, k, 0);
		}
		super.onNeighborBlockChange(world, i, j, k, l);
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}
}
