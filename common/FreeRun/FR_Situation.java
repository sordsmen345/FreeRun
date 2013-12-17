package FreeRun;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class FR_Situation
{
	private FR_Situation(double x, double y, double z, int lookdirection, World world, MovingObjectPosition objectmouseover)
	{
		nextPosX = posX = MathHelper.floor_double(x);
		nextPosY = posY = (int) Math.ceil(y);
		nextPosZ = posZ = MathHelper.floor_double(z);
		lookDirection = lookdirection;
		worldObj = world;
		objectMouseOver = objectmouseover;
	}
	
	public boolean canClimbLeft()
	{
		return hasEdgeLeft();
	}
	
	public boolean canClimbRight()
	{
		return hasEdgeRight();
	}
	
	public boolean canClimbUp()
	{
		return hasEdgeUp();
	}
	
	public boolean canClimbDown()
	{
		return hasEdgeDown();
	}
	
	public boolean canHangStill()
	{
		return hasEdgeOnLocation(posX, posY, posZ);
	}
	
	public float canPushUp()
	{
		if (canClimbUp() || !canHangStill())
		{
			return 0F;
		}
		int x = posX;
		int y = posY - 1;
		int z = posZ;
		
		boolean flag = hasAirAbove(x, y, z, 2);
		
		if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
		{
			z++;
		} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
		{
			x--;
		} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
		{
			z--;
		} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
		{
			x++;
		}
		
		Material material = worldObj.getBlockMaterial(x, y, z);
		//if (material.isSolid())
		{
			if (hasAirAbove(x, y, z, 2) && flag)
			{
				float f = y;
				Block block = Block.blocksList[worldObj.getBlockId(x, y, z)];
				if (block != null)
				{
					AxisAlignedBB bb = block.getCollisionBoundingBoxFromPool(worldObj, x, y, z);
					if (bb != null)
					{
						f += bb.maxY - y - 1F;
					}
				}
				return f;
			}
		}
		return 0F;
	}
	
	public boolean canJumpUpBehind()
	{
		return hasEdgeUpBehind();
	}
	
	public boolean canClimbAroundEdgeLeft()
	{
		boolean flag = false;
		int i = lookDirection;
		lookDirection = (lookDirection + 1) % 4;
		if (i == FR_FreerunPlayer.LOOK_WEST)
		{
			nextPosX = posX + 1;
			nextPosZ = posZ + 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
			
		} else if (i == FR_FreerunPlayer.LOOK_NORTH)
		{
			nextPosZ = posZ + 1;
			nextPosX = posX - 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
			
		} else if (i == FR_FreerunPlayer.LOOK_EAST)
		{
			nextPosX = posX - 1;
			nextPosZ = posZ - 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
			
		} else if (i == FR_FreerunPlayer.LOOK_SOUTH)
		{
			nextPosZ = posZ - 1;
			nextPosX = posX + 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
		}
		lookDirection = i;
		
		return flag;
	}
	
	public boolean canClimbAroundEdgeRight()
	{
		boolean flag = false;
		int i = lookDirection;
		lookDirection = (lookDirection - 1) % 4;
		if (i == FR_FreerunPlayer.LOOK_WEST)
		{
			nextPosX = posX + 1;
			nextPosZ = posZ + 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
			
		} else if (i == FR_FreerunPlayer.LOOK_NORTH)
		{
			nextPosZ = posZ + 1;
			nextPosX = posX - 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
			
		} else if (i == FR_FreerunPlayer.LOOK_EAST)
		{
			nextPosX = posX - 1;
			nextPosZ = posZ - 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
			
		} else if (i == FR_FreerunPlayer.LOOK_SOUTH)
		{
			nextPosZ = posZ - 1;
			nextPosX = posX + 1;
			flag = hasEdgeOnLocation(nextPosX, posY, nextPosZ);
		}
		lookDirection = i;
		
		return flag;
	}
	
	private boolean hasAirAbove(int x, int y, int z, int i)
	{
		if (i >= 1)
		{
			Material material = worldObj.getBlockMaterial(x, y + 1, z);
			if (i >= 2)
			{
				Material material1 = worldObj.getBlockMaterial(x, y + 2, z);
				return !material.isSolid() && !material1.isSolid();
			}
			return !material.isSolid();
		}
		return false;
	}
	
	private boolean hasEdgeLeft()
	{
		boolean b = false;
		if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
		{
			nextPosX = posX + 1;
			b = hasEdgeOnLocation(nextPosX, posY, posZ);
		} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
		{
			nextPosZ = posZ + 1;
			b = hasEdgeOnLocation(posX, posY, nextPosZ);
		} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
		{
			nextPosX = posX - 1;
			b = hasEdgeOnLocation(nextPosX, posY, posZ);
		} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
		{
			nextPosZ = posZ - 1;
			b = hasEdgeOnLocation(posX, posY, nextPosZ);
		}
		return b;
	}
	
	private boolean hasEdgeRight()
	{
		boolean b = false;
		if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
		{
			nextPosX = posX - 1;
			b = hasEdgeOnLocation(nextPosX, posY, posZ);
		} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
		{
			nextPosZ = posZ - 1;
			b = hasEdgeOnLocation(posX, posY, nextPosZ);
		} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
		{
			nextPosX = posX + 1;
			b = hasEdgeOnLocation(nextPosX, posY, posZ);
		} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
		{
			nextPosZ = posZ + 1;
			b = hasEdgeOnLocation(posX, posY, nextPosZ);
		}
		return b;
	}
	
	private boolean hasEdgeUp()
	{
		nextPosY = posY + 1;
		return hasEdgeOnLocation(posX, nextPosY, posZ);
	}
	
	private boolean hasEdgeDown()
	{
		nextPosY = posY - 1;
		return hasEdgeOnLocation(posX, nextPosY, posZ);
	}
	
	private boolean hasEdgeUpBehind()
	{
		boolean b = false;
		nextPosY = posY + 2;
		if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
		{
			nextPosZ = posZ - 1;
			b = hasEdgeOnLocation(posX, nextPosY, nextPosZ);
		} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
		{
			nextPosX = posX + 1;
			b = hasEdgeOnLocation(nextPosX, nextPosY, posZ);
		} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
		{
			nextPosZ = posZ + 1;
			b = hasEdgeOnLocation(posX, nextPosY, nextPosZ);
		} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
		{
			nextPosX = posX - 1;
			b = hasEdgeOnLocation(nextPosX, nextPosY, posZ);
		}
		return b;
	}
	
	public static int getMetaData(int lookdirection)
	{
		if (lookdirection == FR_FreerunPlayer.LOOK_WEST)
		{
			return 2;
		} else if (lookdirection == FR_FreerunPlayer.LOOK_NORTH)
		{
			return 5;
		} else if (lookdirection == FR_FreerunPlayer.LOOK_EAST)
		{
			return 3;
		} else if (lookdirection == FR_FreerunPlayer.LOOK_SOUTH)
		{
			return 4;
		}
		return 0;
	}
	
	private boolean hasEdgeOnLocation(int x, int y, int z)
	{
		int b = worldObj.getBlockId(x, y - 1, z);
		int b1 = worldObj.getBlockId(x, y, z);
		int md = worldObj.getBlockMetadata(x, y - 1, z);
		int md1 = FR_Situation.getMetaData(lookDirection);
		
		if (b1 == Block.vine.blockID || md == 0 || md == md1)
		{
			if (FR_FreerunPlayer.climbableInside.contains(b))
			{
				return true;
			} else if (worldObj.getBlockMaterial(x, y, z).isSolid() || worldObj.getBlockMaterial(x, y - 1, z).isSolid())
			{
				return false;
			}
		}
		
		if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
		{
			z++;
		} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
		{
			x--;
		} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
		{
			z--;
		} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
		{
			x++;
		}
		
		b = worldObj.getBlockId(x, y - 1, z);
		b1 = worldObj.getBlockId(x, y, z);
		
		if (FR_FreerunPlayer.climbableInside.contains(b))
		{
			return false;
		}
		
		if (FR_FreerunPlayer.climbableBlocks.contains(b))
		{
			blockHeight = (float) (Block.blocksList[b].maxY);
			return true;
		}
		
		if (worldObj.getBlockMaterial(x, y - 1, z).isSolid())
		{
			if (b != b1)
			{
				blockHeight = (float) (Block.blocksList[b].maxY);
				return !(((b == Block.stone.blockID || b == 14 || b == 15 || b == 16 || b == 21 || b == 56 || b == 73 || b == 74) && (b1 == Block.stone.blockID || b1 == 14 || b1 == 15 || b1 == 16 || b1 == 21 || b1 == 56 || b1 == 73 || b1 == 74)) || ((b == Block.dirt.blockID || b == Block.grass.blockID) && (b1 == Block.dirt.blockID || b1 == Block.grass.blockID)) || ((b == Block.cobblestone.blockID || b == Block.cobblestoneMossy.blockID || b == Block.stairCompactCobblestone.blockID) && (b1 == Block.cobblestone.blockID || b1 == Block.cobblestoneMossy.blockID || b1 == Block.stairCompactCobblestone.blockID)) || ((b == Block.planks.blockID || b == Block.stairCompactPlanks.blockID) && (b1 == Block.planks.blockID || b1 == Block.stairCompactPlanks.blockID)));
			}
		}
		blockHeight = 1.0F;
		return false;
	}
	
	public Vec3D getHangPositions()
	{
		double x = posX + 0.5D;
		double y = posY - 0.1D - (1.0F - blockHeight);
		double z = posZ + 0.5D;
		Vec3D vec3d = Vec3D.createVector(x, y, z);
		return vec3d;
	}
	
	private World					worldObj;
	public float					blockHeight	= 1.0F;
	public int						lookDirection;
	private int						posX;
	private int						posY;
	private int						posZ;
	private int						nextPosX;
	private int						nextPosY;
	private int						nextPosZ;
	private MovingObjectPosition	objectMouseOver;
	
	public static FR_Situation getSituation(EntityPlayer player, int lookdirection, World world, MovingObjectPosition objectmouseover)
	{
		return new FR_Situation(player.posX, player.posY, player.posZ, lookdirection, world, objectmouseover);
	}
	
	public static FR_Situation getSituation(double x, double y, double z, int lookdirection, World world, MovingObjectPosition objectmouseover)
	{
		return new FR_Situation(x, y, z, lookdirection, world, objectmouseover);
	}
}
