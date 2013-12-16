package FreeRun;

import net.minecraft.util.MathHelper;

public class FR_MoveRoll extends FR_Move
{
	protected FR_MoveRoll(FR_FreerunPlayer freerunhandler)
	{
		super(freerunhandler);
	}
	
	@Override
	public void updateMove()
	{
		if (!moved)
		{
			float f = 1.0F;
			double d = -MathHelper.sin((player.rotationYaw / 180F) * 3.141593F) * f;
			double d1 = MathHelper.cos((player.rotationYaw / 180F) * 3.141593F) * f;
			player.setVelocity(d, 0, d1);
			moved = true;
		}
		
		if (progress > finished)
		{
			moveDone();
		}
		progress++;
	}
	
	@Override
	public void moveDone()
	{
		moved = false;
		progress = 0;
		super.moveDone();
	}
	
	@Override
	public float getAnimationProgress()
	{
		float f = (float) progress / (float) finished;
		if (f > 1.0F)
		{
			f = 1.0F;
		}
		if (f < 0.0F)
		{
			f = 0.0F;
		}
		return f;
	}
	
	private boolean		moved;
	private int			progress;
	private final int	finished	= 16;
}
