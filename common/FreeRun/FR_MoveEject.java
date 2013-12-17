package FreeRun;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class FR_MoveEject extends FR_Move
{
	protected FR_MoveEject(FR_FreerunPlayer freerunhandler, int direction)
	{
		super(freerunhandler);
		this.direction = direction;
		power = 1F;
	}
	
	@Override
	public void updateMove()
	{
		super.updateMove();
		if (player.motionY != 0D && freerunEngine.isClimbing)
		{
			return;
		}
		if (direction != FR_MoveClimb.DIRECTION_UP)
		{
			if (direction == FR_MoveClimb.DIRECTION_LEFT)
			{
				player.rotationYaw -= 90F;
			} else if (direction == FR_MoveClimb.DIRECTION_RIGHT)
			{
				player.rotationYaw += 90F;
			} else
			{
				player.rotationYaw += 180F;
			}
			freerunEngine.isClimbing = false;
			float f = 0.35F * power;
			double d = -MathHelper.sin((player.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F) * f;
			double d1 = MathHelper.cos((player.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((0 / 180F) * 3.141593F) * f;
			player.addVelocity(d, f, d1);
		} else
		{
			if (!freerunEngine.player.worldObj.isRemote)
			{
				if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem().itemID == FreeRun.climbGlove.itemID)
				{
					freerunEngine.isClimbing = false;
					player.addVelocity(0D, 0.5D, 0D);
				}
			} else if (FreeRun.instance.properties.fixedGloveInSMP)
			{
				freerunEngine.isClimbing = false;
				player.addVelocity(0D, 0.5D, 0D);
			}
		}
		FR_Move.addMovementPause(15);
		moveDone();
	}
	
	public void performMove(EntityPlayer entityplayer, int lookdirection, float power)
	{
		this.power = power;
		super.performMove(entityplayer, lookdirection);
	}
	
	@Override
	public void moveDone()
	{
		power = 1.0F;
		super.moveDone();
	}
	
	@Override
	public float getAnimationProgress()
	{
		return 0F;
	}
	
	private float	power;
	private int		direction;
}
