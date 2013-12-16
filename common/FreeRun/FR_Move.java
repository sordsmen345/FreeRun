package FreeRun;

import net.minecraft.entity.player.EntityPlayer;

public abstract class FR_Move
{
	protected FR_Move(FR_FreerunPlayer freerunhandler)
	{
		freerunEngine = freerunhandler;
		lookDirection = 0;
		blockings = 0;
		nextMotionX = nextMotionY = nextMotionZ = 0D;
		animProgress = prevAnimProgress = 0F;
		deltaPos = new double[3];
		prevDeltaPos = new double[3];
	}
	
	public void updateMove()
	{
		prevAnimProgress = animProgress;
		animProgress = Math.min(Math.max(getAnimationProgress(), 0F), 1F);
	}
	
	@Deprecated
	protected boolean canMoveFurther(EntityPlayer entityplayer)
	{
		for (int i = 0; i < 3; i++)
		{
			prevDeltaPos[i] = deltaPos[i];
			if (i == 0)
			{
				deltaPos[i] = entityplayer.posX - startPosX;
			} else if (i == 1)
			{
				deltaPos[i] = entityplayer.posY - startPosY;
			} else
			{
				deltaPos[i] = entityplayer.posZ - startPosZ;
			}
			
			if (deltaPos[i] != prevDeltaPos[i])
			{
				continue;
			} else
			{
				blockings++;
			}
		}
		return blockings < 15;
	}
	
	protected void doMoves(EntityPlayer entityplayer)
	{
		player.motionX = nextMotionX;
		player.motionY = nextMotionY;
		player.motionZ = nextMotionZ;
	}
	
	public final void performMove(EntityPlayer entityplayer, int lookdirection)
	{
		if (FR_Move.paused)
		{
			return;
		}
		lookDirection = lookdirection;
		player = entityplayer;
		startPosX = player.posX;
		startPosY = player.posY;
		startPosZ = player.posZ;
		freerunEngine.setMove(this);
	}
	
	public void moveDone()
	{
		abortMove();
	}
	
	private void abortMove()
	{
		blockings = 0;
		nextMotionX = 0D;
		nextMotionY = 0D;
		nextMotionZ = 0D;
		if (equals(freerunEngine.move))
		{
			freerunEngine.setMove(null);
		}
	}
	
	public double distanceFromStart(double x, double y, double z)
	{
		double dx = x - startPosX;
		double dy = y - startPosY;
		double dz = z - startPosZ;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	protected abstract float getAnimationProgress();
	
	public final FR_Animation getAnimation()
	{
		return animation;
	}
	
	public double				startPosX;
	public double				startPosY;
	public double				startPosZ;
	protected FR_Animation		animation;
	public float				animProgress;
	public float				prevAnimProgress;
	protected int				lookDirection;
	protected FR_FreerunPlayer	freerunEngine;
	protected EntityPlayer		player;
	protected double			nextMotionX;
	protected double			nextMotionY;
	protected double			nextMotionZ;
	protected int				blockings;
	protected double[]			deltaPos;
	protected double[]			prevDeltaPos;
	
	public static void addMovementPause(int ticks)
	{
		FR_Move.paused = true;
		FR_Move.pauseTimer = ticks;
	}
	
	public static void onUpdate(FR_FreerunPlayer freerunengine)
	{
		if (!FR_Move.paused)
		{
			if (freerunengine.move != null)
			{
				freerunengine.move.updateMove();
			}
		}
		
		if (FR_Move.pauseTimer > 0)
		{
			FR_Move.pauseTimer--;
		} else
		{
			FR_Move.paused = false;
		}
	}
	
	public static void addAllMoves(FR_FreerunPlayer freerunengine)
	{
		FR_Move.climbUp = new FR_MoveClimb(freerunengine, FR_MoveClimb.DIRECTION_UP, 0.8F);
		FR_Move.climbDown = new FR_MoveClimb(freerunengine, FR_MoveClimb.DIRECTION_DOWN, 1.0F);
		FR_Move.climbLeft = new FR_MoveClimb(freerunengine, FR_MoveClimb.DIRECTION_LEFT, 1.0F);
		FR_Move.climbRight = new FR_MoveClimb(freerunengine, FR_MoveClimb.DIRECTION_RIGHT, 1.0F);
		
		FR_Move.climbAroundLeft = new FR_MoveAroundEdge(freerunengine, FR_MoveClimb.DIRECTION_LEFT, 2.0F);
		FR_Move.climbAroundRight = new FR_MoveAroundEdge(freerunengine, FR_MoveClimb.DIRECTION_RIGHT, 2.0F);
		
		FR_Move.ejectUp = new FR_MoveEject(freerunengine, FR_MoveClimb.DIRECTION_UP);
		FR_Move.ejectBack = new FR_MoveEject(freerunengine, FR_MoveClimb.DIRECTION_DOWN);
		FR_Move.ejectLeft = new FR_MoveEject(freerunengine, FR_MoveClimb.DIRECTION_LEFT);
		FR_Move.ejectRight = new FR_MoveEject(freerunengine, FR_MoveClimb.DIRECTION_RIGHT);
		
		FR_Move.wallrun = new FR_MoveWallrun(freerunengine);
		FR_Move.pushUp = new FR_MovePushUp(freerunengine);
		FR_Move.upBehind = new FR_MoveUpBehind(freerunengine);
		
		FR_Move.roll = new FR_MoveRoll(freerunengine);
	}
	
	private static int				pauseTimer;
	protected static boolean		paused;
	public static FR_MoveWallrun	wallrun;
	public static FR_MoveClimb		climbUp;
	public static FR_MoveClimb		climbDown;
	public static FR_MoveClimb		climbLeft;
	public static FR_MoveClimb		climbRight;
	public static FR_MoveClimb		climbAroundLeft;
	public static FR_MoveClimb		climbAroundRight;
	public static FR_MoveEject		ejectUp;
	public static FR_MoveEject		ejectBack;
	public static FR_MoveEject		ejectLeft;
	public static FR_MoveEject		ejectRight;
	public static FR_MovePushUp		pushUp;
	public static FR_MoveUpBehind	upBehind;
	public static FR_MoveRoll		roll;
}
