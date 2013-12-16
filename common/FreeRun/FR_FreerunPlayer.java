package FreeRun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

public class FR_FreerunPlayer extends PlayerBase
{
	public FR_Move				move;
	public FR_Situation			situation;
	public double				startPosY;
	public double				startPosX;
	public double				startPosZ;
	private float				startRollingYaw;
	private float				startRollingPitch;
	public MovingObjectPosition	objectMouseOver;
	public double				horizontalSpeed;
	public boolean				isClimbing;
	public boolean				wallRunning;
	public boolean				freeRunning;
	public float				rollAnimation;
	private float				prevRollAnimation;
	private boolean				freerunKeyEvent;
	
	public FR_FreerunPlayer(PlayerAPI playerapi)
	{
		super(playerapi);
		FreeRun.instance.setFreerunPlayer(this);
		climbableBlocks = new ArrayList<Integer>();
		climbableInside = new ArrayList<Integer>();
		setMove(null);
		FR_Move.addAllMoves(this);
		freerunKeyEvent = false;
		freeRunning = false;
		horizontalSpeed = 0D;
		rollAnimation = 1F;
		situation = null;
		addAllClimableBlocks();
	}
	
	private void addAllClimableBlocks()
	{
		climbableBlocks.clear();
		climbableInside.clear();
		
		//BESIDE
		climbableBlocks.add(Block.leaves.blockID);
		climbableBlocks.add(Block.dispenser.blockID);
		climbableBlocks.add(Block.music.blockID);
		climbableBlocks.add(Block.bed.blockID);
		climbableBlocks.add(Block.stairDouble.blockID);
		climbableBlocks.add(Block.stairSingle.blockID);
		climbableBlocks.add(Block.bookShelf.blockID);
		climbableBlocks.add(Block.tilledField.blockID);
		climbableBlocks.add(Block.mobSpawner.blockID);
		climbableBlocks.add(Block.stairCompactPlanks.blockID);
		climbableBlocks.add(Block.chest.blockID);
		climbableBlocks.add(Block.workbench.blockID);
		climbableBlocks.add(Block.stoneOvenIdle.blockID);
		climbableBlocks.add(Block.stoneOvenActive.blockID);
		climbableBlocks.add(Block.signWall.blockID);
		climbableBlocks.add(Block.signPost.blockID);
		climbableBlocks.add(Block.doorWood.blockID);
		climbableBlocks.add(Block.doorSteel.blockID);
		climbableBlocks.add(Block.pistonBase.blockID);
		climbableBlocks.add(Block.pistonStickyBase.blockID);
		climbableBlocks.add(Block.pistonExtension.blockID);
		climbableBlocks.add(Block.stairCompactCobblestone.blockID);
		climbableBlocks.add(Block.jukebox.blockID);
		climbableBlocks.add(Block.pumpkin.blockID);
		climbableBlocks.add(Block.pumpkinLantern.blockID);
		climbableBlocks.add(Block.fence.blockID);
		climbableBlocks.add(Block.trapdoor.blockID);
		climbableBlocks.add(Block.netherFence.blockID);
		climbableBlocks.add(Block.stairsNetherBrick.blockID);
		climbableBlocks.add(Block.stairsStoneBrickSmooth.blockID);
		climbableBlocks.add(Block.stairsBrick.blockID);
		climbableBlocks.add(Block.fenceGate.blockID);
		climbableBlocks.add(Block.lockedChest.blockID);
		climbableBlocks.add(Block.enchantmentTable.blockID);
		
		if (FreeRun.barWood != null)
		{
			climbableBlocks.add(FreeRun.barWood.blockID);
		}
		
		//INSIDE
		climbableInside.add(Block.button.blockID);
		climbableInside.add(Block.fenceIron.blockID);
		
		if (FreeRun.edgeWood != null)
		{
			climbableInside.add(FreeRun.edgeWood.blockID);
		}
		if (FreeRun.edgeStone != null)
		{
			climbableInside.add(FreeRun.edgeStone.blockID);
		}
	}
	
	public void beforeOnUpdate()
	{
		objectMouseOver = ModLoader.getMinecraftInstance().objectMouseOver;
		
		situation = FR_Situation.getSituation(player, getLookDirection(), player.worldObj, objectMouseOver);
		horizontalSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
		
		prevRollAnimation = rollAnimation;
		if (isRolling())
		{
			rollAnimation += 0.05F;
		}
		
		/*
		if (player.prevPosX != 0D && player.prevPosY != 0D && player.prevPosZ != 0D)
		{
			handleStats(player.posX - player.prevPosX, player.posY - player.prevPosY, player.posZ - player.prevPosZ);
		}
		*/
		handleInput();
		handleFreerunning();
		handleMoves();
		handleTimers();
	}
	
	public void startRolling()
	{
		rollAnimation = 0F;
		startRollingYaw = player.rotationYaw;
		startRollingPitch = player.rotationPitch;
	}
	
	protected void updateAnimations(float rendertime)
	{
		if (isRolling())
		{
			animateRoll(prevRollAnimation + (rollAnimation - prevRollAnimation) * rendertime);
		}
	}
	
	private void handleInput()
	{
		if (FreeRun.instance.properties.enableFreerunToggle)
		{
			if (Keyboard.isKeyDown(FreeRun.instance.properties.keyRun))
			{
				if (!freerunKeyEvent)
				{
					freeRunning = !freeRunning;
					freerunKeyEvent = true;
				}
			} else
			{
				freerunKeyEvent = false;
			}
		} else
		{
			freeRunning = Keyboard.isKeyDown(FreeRun.instance.properties.keyRun);
		}
	}
	
	private void handleMoves()
	{
		FR_Move.onUpdate(this);
	}
	
	private void handleStats(double d, double d1, double d2)
	{
		/*
		if (player.ridingEntity != null)
		{
			return;
		}
		if (isClimbing)
		{
			player.addStat(StatList.distanceClimbedStat, (int) Math.round(d1 * 100D));
		} else if (!player.isInsideOfMaterial(Material.water) && !player.isInWater() && !player.isOnLadder() && !player.onGround)
		{
			int l = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100F);
			if (l > 25 && freeRunning)
			{
				player.addStat(StatList.distanceFlownStat, -l);
			}
		}
		*/
	}
	
	private void handleTimers()
	{
	}
	
	private void handleFreerunning()
	{
		if (isTooHungry())
		{
			stopMove();
			return;
		}
		
		//Not in water
		if (!player.isInWater() && !player.handleLavaMovement())
		{
			if (player.onGround || player.isSneaking() || player.isOnLadder())
			{
				isClimbing = false;
				stopMove();
			}
			
			//Climbing
			if (isClimbing)
			{
				player.addExhaustion(0.001F);
				player.fallDistance = 0.0F;
				player.motionX = player.motionY = player.motionZ = 0D;
				
				if (!situation.canHangStill() && !(move instanceof FR_MoveAroundEdge))
				{
					//mc.ingameGUI.addChatMessage("falling! noooo!");
					isClimbing = false;
				} else if (isHangingStill())
				{
					Vec3D vec3d = situation.getHangPositions();
					player.motionX = vec3d.xCoord - player.posX;
					player.motionY = vec3d.yCoord - player.posY;
					player.motionZ = vec3d.zCoord - player.posZ;
					
					int lookdirection = situation.lookDirection;
					if (isMovingForwards())
					{
						float y = situation.canPushUp();
						if (situation.canJumpUpBehind())
						{
							FR_Move.upBehind.performMove(player, lookdirection);
							player.addExhaustion(0.3F);
						} else if (situation.canClimbUp())
						{
							FR_Move.climbUp.performMove(player, lookdirection);
						} else if (y != 0)
						{
							FR_Move.pushUp.performMove(player, lookdirection, y);
							player.addExhaustion(0.3F);
						}
					} else if (isMovingBackwards())
					{
						if (situation.canClimbDown())
						{
							FR_Move.climbDown.performMove(player, lookdirection);
						}
					} else if (isMovingLeft())
					{
						if (situation.canClimbLeft())
						{
							FR_Move.climbLeft.performMove(player, lookdirection);
						}/* else if (situation.canClimbAroundEdgeLeft())
							{
							FR_Move.climbAroundLeft.performMove(player, lookdirection);
							}*/
					} else if (isMovingRight())
					{
						if (situation.canClimbRight())
						{
							FR_Move.climbRight.performMove(player, lookdirection);
						}/* else if (situation.canClimbAroundEdgeRight())
							{
							FR_Move.climbAroundRight.performMove(player, lookdirection);
							}*/
					}
				}
				
				if (freeRunning && player.isJumping && isHangingStill())
				{
					if (isMovingForwards() && !isWallrunning())
					{
						FR_Move.ejectUp.performMove(player, situation.lookDirection);
						player.addExhaustion(0.3F);
					} else if (isMovingLeft())
					{
						FR_Move.ejectLeft.performMove(player, situation.lookDirection);
						player.addExhaustion(0.3F);
					} else if (isMovingRight())
					{
						FR_Move.ejectRight.performMove(player, situation.lookDirection);
						player.addExhaustion(0.3F);
					} else
					{
						FR_Move.ejectBack.performMove(player, situation.lookDirection);
						player.addExhaustion(0.3F);
					}
				}
				return;
			}
			
			//Not climbing
			if (freeRunning)
			{
				tryGrabLedge();
				
				//Wallkick
				if (!player.onGround)
				{
					if (FreeRun.instance.properties.enableWallKick && isWallrunning() && player.isJumping && move.getAnimationProgress() > 0.3F)
					{
						stopMove();
						if (isMovingLeft())
						{
							FR_Move.ejectLeft.performMove(player, situation.lookDirection, 0.8F);
							player.addExhaustion(0.3F);
						} else if (isMovingRight())
						{
							FR_Move.ejectRight.performMove(player, situation.lookDirection, 0.8F);
							player.addExhaustion(0.3F);
						} else
						{
							FR_Move.ejectBack.performMove(player, situation.lookDirection, 0.8F);
							player.addExhaustion(0.3F);
						}
					}
					
					return;
				}
				
				if ((isMovingForwards() || player.isCollidedHorizontally) && !isRolling())
				{
					int i = canJumpOverGap();
					int j = canHopOver();
					if (!player.isJumping)
					{
						if (i == 1)
						{
							player.addVelocity(0D, 0.35D, 0D);
							player.isJumping = true;
							player.addExhaustion(0.1F);
						} else if (i == 2)
						{
							player.jump();
						}
					}
					
					if (j == 1 && !player.isJumping)
					{
						player.jump();
					} else if (j > 1 || canWallrun())
					{
						FR_Move.wallrun.performMove(player, getLookDirection(), 1.8F);
						player.addExhaustion(0.8F);
					}
				}
			}
			
			if (isWallrunning())
			{
				if (player.isCollidedVertically && !player.onGround)
				{
					move.moveDone();
				}
				
				tryGrabLedge();
			}
			return;
		}
	}
	
	public void stopMove()
	{
		if (isMoving())
		{
			move.moveDone();
		}
	}
	
	public boolean isMoving()
	{
		return move != null;
	}
	
	public float getSpeedModifier()
	{
		if (player.isSprinting() || isTooHungry())
		{
			return super.getSpeedModifier();
		}
		
		if (!player.isInWater() && !player.handleLavaMovement())
		{
			if (player.onGround && !isClimbing)
			{
				if (freeRunning)
				{
					return mod_FreeRun.instance.properties.speedMultiplier;
				} else if (isOnCertainBlock(mod_FreeRun.barWood.blockID))
				{
					return 0.5F;
				}
			}
		} else if (freeRunning)
		{
			return mod_FreeRun.instance.properties.speedMultiplier;
		}
		return super.getSpeedModifier();
	}
	
	public boolean isFreerunning()
	{
		return freeRunning;
	}
	
	public boolean isWallrunning()
	{
		return move instanceof FR_MoveWallrun;
	}
	
	public boolean isRolling()
	{
		return rollAnimation < 1F;
	}
	
	public boolean isHangingStill()
	{
		return isClimbing && move == null;
	}
	
	public boolean isTooHungry()
	{
		return player.foodStats.getFoodLevel() <= 6;
	}
	
	public void tryGrabLedge()
	{
		if (!player.onGround && situation.canHangStill() && !isWallrunning())
		{
			isClimbing = true;
			Vec3D vec3d = situation.getHangPositions();
			player.motionX = vec3d.xCoord - player.posX;
			player.motionY = vec3d.yCoord - player.posY;
			player.motionZ = vec3d.zCoord - player.posZ;
		}
	}
	
	public void beforeFall(float f)
	{
		if (freeRunning)
		{
			int i = MathHelper.floor_double(player.posX);
			int j = MathHelper.floor_double(player.boundingBox.minY);
			int k = MathHelper.floor_double(player.posZ);
			int b = player.worldObj.getBlockId(i, j, k);
			
			j = MathHelper.floor_double(player.boundingBox.minY + player.motionY);
			int b1 = player.worldObj.getBlockId(i, j, k);
			Material material = player.worldObj.getBlockMaterial(i, j, k);
			
			//Vec3D vec3d = Vec3D.createVector(player.posX, player.boundingBox.minY, player.posZ);
			//MovingObjectPosition mop = worldObj.rayTraceBlocks_do_do(vec3d, vec3d.addVector(player.motionX, player.motionY, player.motionZ), true, true);
			
			if (f > 3.0F && canLandOnMob())
			{
				EntityLiving entityliving = (EntityLiving) objectMouseOver.entityHit;
				player.worldObj.playSoundAtEntity(entityliving, entityliving.getDeathSound(), entityliving.getSoundVolume(), (entityliving.rand.nextFloat() - entityliving.rand.nextFloat()) * 0.2F + 1.0F);
				entityliving.setBeenAttacked();
				entityliving.performHurtAnimation();
				player.motionY *= 0.1F;
				f = 0F;
			} else
			
			if (b != b1 && material.isSolid() && material != Material.water && material != Material.lava)
			{
				f = roll(f);
			}
		}
		player.fallDistance = f;
	}
	
	public void afterDamageEntity(DamageSource damagesource, int i)
	{
		isClimbing = false;
	}
	
	private float roll(float f)
	{
		if (!freeRunning || f < 3F)
		{
			return f;
		}
		
		float maxFall = 6F;
		if (f < maxFall)
		{
			f /= 2F;
			return f;
		}
		
		int i = MathHelper.floor_double(player.posX);
		int j = MathHelper.floor_double(player.boundingBox.minY - 1.1F + player.motionY);
		int j1 = MathHelper.floor_double(player.boundingBox.minY + 0.1F + player.motionY);
		int k = MathHelper.floor_double(player.posZ);
		int b = player.worldObj.getBlockId(i, j, k);
		if (b == Block.fence.blockID || b == Block.netherFence.blockID || isOnCertainBlock(Block.leaves.blockID) || isOnCertainBlock(mod_FreeRun.barWood.blockID) || player.isInWater())
		{
			f /= 2F;
			return f;
		}
		
		if (!isMovingForwards())
		{
			f *= 0.8F;
			return f;
		}
		
		float f1 = 1.0F;
		double d = -MathHelper.sin((player.rotationYaw / 180F) * 3.141593F) * f1;
		double d1 = MathHelper.cos((player.rotationYaw / 180F) * 3.141593F) * f1;
		startRolling();
		player.setVelocity(d, 0, d1);
		player.addExhaustion(0.3F);
		f /= 2F;
		return f;
	}
	
	public boolean canLandOnMob()
	{
		if (objectMouseOver == null || objectMouseOver.typeOfHit != EnumMovingObjectType.ENTITY)
		{
			return false;
		}
		return objectMouseOver.entityHit instanceof EntityLiving && isSelectedEntityClose(3.0F, 0D, player.motionY, 0D);
	}
	
	public int getLookDirection()
	{
		return (MathHelper.floor_double(((player.rotationYaw * 4F) / 360F) + 0.5D) & 3);
	}
	
	public boolean isMovingForwards()
	{
		return Keyboard.isKeyDown(mod_FreeRun.instance.keyForward);
	}
	
	public boolean isMovingBackwards()
	{
		return Keyboard.isKeyDown(mod_FreeRun.instance.keyBackward);
	}
	
	public boolean isMovingLeft()
	{
		return Keyboard.isKeyDown(mod_FreeRun.instance.keyLeft);
	}
	
	public boolean isMovingRight()
	{
		return Keyboard.isKeyDown(mod_FreeRun.instance.keyRight);
	}
	
	public boolean canWallrun()
	{
		if (isSelectedBlockClose(2.0F))
		{
			Material m = getSelectedBlockMaterial();
			Material m1 = getSelectedBlockMaterial(0, 1, 0);
			
			if (isSelectedBlockOnLevel(1))
			{
				m = getSelectedBlockMaterial();
				m1 = getSelectedBlockMaterial(0, -1, 0);
			}
			
			if (!m1.isSolid())
			{
				if (situation.canPushUp() != 0)
				{
					return m.isSolid() && !player.isJumping;
				}
			}
			return m.isSolid() && m1.isSolid() && !player.isJumping;
		}
		return false;
	}
	
	public int canHopOver()
	{
		if (objectMouseOver != null && objectMouseOver.typeOfHit == EnumMovingObjectType.TILE)
		{
			if (isSelectedBlockClose(2.0F) && isSelectedBlockOnLevel(0))
			{
				int b = getSelectedBlockId();
				Material m = getSelectedBlockMaterial();
				AxisAlignedBB boundingbox = Block.blocksList[b].getCollisionBoundingBoxFromPool(player.worldObj, objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ);
				
				if (m.isSolid() && isBlockAboveAir(2, false, isClimbing))
				{
					if (boundingbox != null && boundingbox.maxY - objectMouseOver.blockY > 1.0F)
					{
						return 2;
					} else if (Block.blocksList[b].maxY > player.stepHeight && Block.blocksList[b].maxY <= 1.0F && b != Block.stairCompactPlanks.blockID && b != Block.stairCompactCobblestone.blockID)
					{
						return 1;
					}
				}
			}
		}
		
		if (objectMouseOver == null || objectMouseOver.typeOfHit != EnumMovingObjectType.ENTITY || objectMouseOver.entityHit == null)
		{
			return 0;
		}
		if (isSelectedEntityClose(2.0F) && isSelectedEntityOnLevel(0))
		{
			if (objectMouseOver.entityHit.boundingBox.maxY - objectMouseOver.entityHit.boundingBox.minY <= 1.5D)
			{
				return 3;
			}
		}
		
		return 0;
	}
	
	public boolean isSelectedBlockOnLevel(int i)
	{
		if (objectMouseOver == null || objectMouseOver.typeOfHit != EnumMovingObjectType.TILE)
		{
			return false;
		}
		int j = MathHelper.floor_double(player.boundingBox.minY) + i;
		return j == objectMouseOver.blockY;
	}
	
	public boolean isSelectedBlockClose(float f)
	{
		return isSelectedBlockClose(f, 0D, 0D, 0D);
	}
	
	public boolean isSelectedBlockClose(float f, double addX, double addY, double addZ)
	{
		if (objectMouseOver == null || objectMouseOver.typeOfHit != EnumMovingObjectType.TILE)
		{
			return false;
		}
		double d1 = Math.sqrt(Math.pow((objectMouseOver.blockX + 0.5D) + addX - player.posX, 2));
		double d2 = Math.sqrt(Math.pow((objectMouseOver.blockY + 0.5D) + addY - player.posY, 2));
		double d3 = Math.sqrt(Math.pow((objectMouseOver.blockZ + 0.5D) + addZ - player.posZ, 2));
		double dXYZ = Math.sqrt((d1 * d1) + (d2 * d2) + (d3 * d3));
		return dXYZ <= f;
	}
	
	public boolean isSelectedEntityOnLevel(int i)
	{
		if (objectMouseOver == null || objectMouseOver.typeOfHit != EnumMovingObjectType.ENTITY)
		{
			return false;
		}
		if (objectMouseOver.entityHit == null)
		{
			return false;
		}
		
		int j = MathHelper.floor_double(player.boundingBox.minY) + i;
		return j == MathHelper.floor_double(objectMouseOver.entityHit.boundingBox.minY);
	}
	
	public boolean isSelectedEntityClose(float f)
	{
		return isSelectedEntityClose(f, 0D, 0D, 0D);
	}
	
	public boolean isSelectedEntityClose(float f, double addX, double addY, double addZ)
	{
		if (objectMouseOver == null || objectMouseOver.typeOfHit != EnumMovingObjectType.ENTITY)
		{
			return false;
		}
		if (objectMouseOver.entityHit == null)
		{
			return false;
		}
		double d1 = Math.sqrt(Math.pow((objectMouseOver.entityHit.posX + 0.5D) + addX - player.posX, 2));
		double d2 = Math.sqrt(Math.pow((objectMouseOver.entityHit.posY + 0.5D) + addY - player.posY, 2));
		double d3 = Math.sqrt(Math.pow((objectMouseOver.entityHit.posZ + 0.5D) + addZ - player.posZ, 2));
		double dXYZ = Math.sqrt((d1 * d1) + (d2 * d2) + (d3 * d3));
		return dXYZ <= f;
	}
	
	public int getSelectedBlockId()
	{
		return getSelectedBlockId(0, 0, 0);
	}
	
	public int getSelectedBlockId(int addX, int addY, int addZ)
	{
		if (objectMouseOver == null || objectMouseOver.typeOfHit != EnumMovingObjectType.TILE)
		{
			return 0;
		}
		return player.worldObj.getBlockId(objectMouseOver.blockX + addX, objectMouseOver.blockY + addY, objectMouseOver.blockZ + addZ);
	}
	
	public Material getSelectedBlockMaterial()
	{
		return getSelectedBlockMaterial(0, 0, 0);
	}
	
	public Material getSelectedBlockMaterial(int addX, int addY, int addZ)
	{
		if (objectMouseOver == null || objectMouseOver.typeOfHit != EnumMovingObjectType.TILE)
		{
			return Material.air;
		}
		return player.worldObj.getBlockMaterial(objectMouseOver.blockX + addX, objectMouseOver.blockY + addY, objectMouseOver.blockZ + addZ);
	}
	
	public boolean isBlockAboveAir(int l, boolean blockAboveBlockIsSolid, boolean climbing)
	{
		if (objectMouseOver == null || objectMouseOver.typeOfHit != EnumMovingObjectType.TILE)
		{
			return false;
		}
		int i = objectMouseOver.blockX;
		int j = objectMouseOver.blockY + 1;
		int k = objectMouseOver.blockZ;
		
		Material m = player.worldObj.getBlockMaterial(i, j, k);
		Material m1 = player.worldObj.getBlockMaterial(i, j + 1, k);
		Material m2 = player.worldObj.getBlockMaterial(i, j + 2, k);
		
		if (l == 1)
		{
			if (blockAboveBlockIsSolid)
			{
				return !m.isSolid() && m1.isSolid();
			} else
			{
				return !m.isSolid();
			}
		} else if (l == 2)
		{
			if (blockAboveBlockIsSolid)
			{
				return !m.isSolid() && !m1.isSolid() && m2.isSolid();
			} else
			{
				return !m.isSolid() && !m1.isSolid();
			}
		} else if (l == 3)
		{
			return !m.isSolid() && !m1.isSolid() && !m2.isSolid();
		} else
		{
			return !m.isSolid();
		}
	}
	
	public int canJumpOverGap()
	{
		double d = -MathHelper.sin((player.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F);
		double d1 = MathHelper.cos((player.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F);
		
		int i = MathHelper.floor_double(player.posX);
		int i1 = MathHelper.floor_double(player.posX + d);
		int i2 = MathHelper.floor_double(player.posX + 2 * d);
		
		int j = MathHelper.floor_double(player.boundingBox.minY - 0.1F);
		
		int k = MathHelper.floor_double(player.posZ);
		int k1 = MathHelper.floor_double(player.posZ + d1);
		int k2 = MathHelper.floor_double(player.posZ + 2 * d1);
		
		/*if (player.onGround && !player.worldObj.getBlockMaterial(i, j, k).isSolid() && player.worldObj.getBlockMaterial(i1, j, k1).isSolid())
		{
			return 1;
		} else */if (player.onGround && !player.worldObj.getBlockMaterial(i, j, k).isSolid() && !player.worldObj.getBlockMaterial(i1, j, k1).isSolid() && player.worldObj.getBlockMaterial(i2, j, k2).isSolid())
		{
			return 2;
		}
		return 0;
	}
	
	public boolean isOnCertainBlock(int blockID)
	{
		int i = MathHelper.floor_double(player.posX);
		int j = MathHelper.floor_double(player.boundingBox.minY - 0.1F + player.motionY);
		int k = MathHelper.floor_double(player.posZ);
		return player.worldObj.getBlockId(i, j, k) == blockID;
	}
	
	public boolean hasBlockInFront()
	{
		double d = -MathHelper.sin((player.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F);
		double d1 = MathHelper.cos((player.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F);
		int i = MathHelper.floor_double(player.posX + d);
		int j = MathHelper.floor_double(player.boundingBox.minY);
		int k = MathHelper.floor_double(player.posZ + d1);
		return player.worldObj.getBlockMaterial(i, j, k).isSolid();
	}
	
	public void animateRoll(float f)
	{
		if (ModLoader.getMinecraftInstance().gameSettings.thirdPersonView == 0)
		{
			float yaw = (startRollingYaw + 360F * f);
			float pitch = (startRollingPitch + 360F * f);
			//pitch = startRollingPitch;
			for (; yaw >= 360F; yaw -= 360F)
			{}
			if (pitch >= 180F)
			{
				pitch -= 270F;
			}
			player.setRotation(yaw, pitch);
		}
	}
	
	protected void setMove(FR_Move move)
	{
		this.move = move;
	}
	
	public static List<Integer>	climbableBlocks;
	public static List<Integer>	climbableInside;
	public static final int		LOOK_WEST	= 0;
	public static final int		LOOK_NORTH	= 1;
	public static final int		LOOK_EAST	= 2;
	public static final int		LOOK_SOUTH	= 3;
}
