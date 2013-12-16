package FreeRun;

public class FR_MoveUpBehind extends FR_Move
{
	
	public FR_MoveUpBehind(FR_FreerunPlayer freerunhandler)
	{
		super(freerunhandler);
		yDone = xzDone = false;
	}
	
	@Override
	public void updateMove()
	{
		super.updateMove();
		float yspeed = 0.05F;
		float xzspeed = 0.05F;
		if (!yDone)
		{
			if (player.posY - startPosY <= 1.4F)
			{
				nextMotionY += yspeed;
			} else
			{
				yDone = true;
			}
		}
		
		if (!xzDone)
		{
			if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
			{
				if (player.posZ - startPosZ >= -1.0F)
				{
					nextMotionZ -= xzspeed;
				} else
				{
					xzDone = true;
				}
			} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
			{
				if (player.posX - startPosX <= 1.0F)
				{
					nextMotionX += xzspeed;
				} else
				{
					xzDone = true;
				}
			} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
			{
				if (player.posZ - startPosZ <= 1.0F)
				{
					nextMotionZ += xzspeed;
				} else
				{
					xzDone = true;
				}
			} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
			{
				if (player.posX - startPosX >= -1.0F)
				{
					nextMotionX -= xzspeed;
				} else
				{
					xzDone = true;
				}
			}
		}
		
		if (nextMotionX > limitSpeed)
		{
			nextMotionX = limitSpeed;
		} else if (nextMotionX < -limitSpeed)
		{
			nextMotionX = -limitSpeed;
		}
		
		if (nextMotionY > limitSpeed)
		{
			nextMotionY = limitSpeed;
		}
		
		if (nextMotionZ > limitSpeed)
		{
			nextMotionZ = limitSpeed;
		} else if (nextMotionZ < -limitSpeed)
		{
			nextMotionZ = -limitSpeed;
		}
		
		if (yDone)
		{
			nextMotionY = 0D;
		}
		if (xzDone)
		{
			nextMotionX = 0D;
			nextMotionZ = 0D;
			if (yDone)
			{
				moveDone();
			}
		}
		
		doMoves(player);
	}
	
	@Override
	public void moveDone()
	{
		xzDone = false;
		yDone = false;
		freerunEngine.tryGrabLedge();
		super.moveDone();
	}
	
	@Override
	public float getAnimationProgress()
	{
		return 0F;
	}
	
	private final float	limitSpeed	= 0.5F;
	private boolean		yDone;
	private boolean		xzDone;
}
