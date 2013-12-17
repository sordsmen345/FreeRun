package FreeRun;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class FR_BlockWoodBar extends Block
{
	protected FR_BlockWoodBar(int i, int j)
	{
		super(i, j, Material.wood);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{
		int l = world.getBlockMetadata(i, j, k);
		float f = 1.0F;
		
		if (l == 2)
		{
			setBlockBounds(0.4F, 0.8F, 1.0F - f, 0.6F, 1.0F, 1.0F);
		} else if (l == 3)
		{
			setBlockBounds(0.4F, 0.8F, 0.0F, 0.6F, 1.0F, f);
		} else if (l == 4)
		{
			setBlockBounds(1.0F - f, 0.8F, 0.4F, 1.0F, 1.0F, 0.6F);
		} else if (l == 5)
		{
			setBlockBounds(0.0F, 0.8F, 0.4F, f, 1.0F, 0.6F);
		}
		
		return super.getCollisionBoundingBoxFromPool(world, i, j, k);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return AxisAlignedBB.getBoundingBoxFromPool((double) i, (double) j, (double) k, (double) i + 1D, (double) j + 1D, (double) k + 1D);
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
		return FreeRun.instance.properties.barWoodModel;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
	{
		if (world.isBlockNormalCube(i - 1, j, k) || world.getBlockId(i - 1, j, k) == this.blockID)
		{
			return true;
		}
		if (world.isBlockNormalCube(i + 1, j, k) || world.getBlockId(i + 1, j, k) == this.blockID)
		{
			return true;
		}
		if (world.isBlockNormalCube(i, j, k - 1) || world.getBlockId(i, j, k - 1) == this.blockID)
		{
			return true;
		}
		if (world.isBlockNormalCube(i, j, k + 1) || world.getBlockId(i, j, k + 1) == this.blockID)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void onBlockPlaced(World world, int i, int j, int k, int l)
	{
		int i1 = world.getBlockMetadata(i, j, k);
		if ((i1 == 0 || l == 2) && (world.isBlockNormalCube(i, j, k + 1) || world.getBlockId(i, j, k + 1) == this.blockID))
		{
			i1 = 2;
		}
		if ((i1 == 0 || l == 3) && (world.isBlockNormalCube(i, j, k - 1) || world.getBlockId(i, j, k - 1) == this.blockID))
		{
			i1 = 3;
		}
		if ((i1 == 0 || l == 4) && (world.isBlockNormalCube(i + 1, j, k) || world.getBlockId(i + 1, j, k) == this.blockID))
		{
			i1 = 4;
		}
		if ((i1 == 0 || l == 5) && (world.isBlockNormalCube(i - 1, j, k) || world.getBlockId(i - 1, j, k) == this.blockID))
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
		if (i1 == 2 && (world.isBlockNormalCube(i, j, k + 1) || world.getBlockId(i, j, k + 1) == this.blockID))
		{
			flag = true;
		}
		if (i1 == 3 && (world.isBlockNormalCube(i, j, k - 1) || world.getBlockId(i, j, k - 1) == this.blockID))
		{
			flag = true;
		}
		if (i1 == 4 && (world.isBlockNormalCube(i + 1, j, k) || world.getBlockId(i + 1, j, k) == this.blockID))
		{
			flag = true;
		}
		if (i1 == 5 && (world.isBlockNormalCube(i - 1, j, k) || world.getBlockId(i - 1, j, k) == this.blockID))
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
