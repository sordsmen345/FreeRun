package FreeRun;

public class FR_MoveClimb extends FR_Move
{
	protected FR_MoveClimb(FR_FreerunPlayer freerunhandler, int direction, float distance)
	{
		super(freerunhandler);
		this.direction = direction;
		this.distance = distance;
	}
	
	@Override
	public void updateMove()
	{
		super.updateMove();
		if (direction == FR_MoveClimb.DIRECTION_UP)
		{
			if (player.posY - startPosY <= distance)
			{
				nextMotionY += speed;
				doMoves(player);
			} else
			{
				moveDone();
			}
		} else if (direction == FR_MoveClimb.DIRECTION_DOWN)
		{
			if (player.posY - startPosY >= -distance)
			{
				nextMotionY -= speed;
				doMoves(player);
			} else
			{
				moveDone();
			}
		} else if (direction == FR_MoveClimb.DIRECTION_LEFT)
		{
			if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
			{
				if (player.posX - startPosX <= distance)
				{
					nextMotionX += speed;
					doMoves(player);
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
			{
				if (player.posZ - startPosZ <= distance)
				{
					nextMotionZ += speed;
					doMoves(player);
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
			{
				if (player.posX - startPosX >= -distance)
				{
					nextMotionX -= speed;
					doMoves(player);
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
			{
				if (player.posZ - startPosZ >= -distance)
				{
					nextMotionZ -= speed;
					doMoves(player);
				} else
				{
					moveDone();
				}
			}
		} else if (direction == FR_MoveClimb.DIRECTION_RIGHT)
		{
			if (lookDirection == FR_FreerunPlayer.LOOK_WEST)
			{
				if (player.posX - startPosX >= -distance)
				{
					nextMotionX -= speed;
					doMoves(player);
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FR_FreerunPlayer.LOOK_NORTH)
			{
				if (player.posZ - startPosZ >= -distance)
				{
					nextMotionZ -= speed;
					doMoves(player);
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FR_FreerunPlayer.LOOK_EAST)
			{
				if (player.posX - startPosX <= distance)
				{
					nextMotionX += speed;
					doMoves(player);
				} else
				{
					moveDone();
				}
			} else if (lookDirection == FR_FreerunPlayer.LOOK_SOUTH)
			{
				if (player.posZ - startPosZ <= distance)
				{
					nextMotionZ += speed;
					doMoves(player);
				} else
				{
					moveDone();
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
	}
	
	@Override
	public void moveDone()
	{
		int i = direction > 2 ? 5 : 10;
		FR_Move.addMovementPause(i);
		super.moveDone();
	}
	
	@Override
	public float getAnimationProgress()
	{
		return 0F;
	}
	
	protected int			direction;
	protected float			distance;
	protected final float	speed			= 0.03F;
	protected final float	limitSpeed		= 0.1F;
	public static final int	DIRECTION_LEFT	= 3;
	public static final int	DIRECTION_RIGHT	= 4;
	public static final int	DIRECTION_UP	= 1;
	public static final int	DIRECTION_DOWN	= 2;
}
