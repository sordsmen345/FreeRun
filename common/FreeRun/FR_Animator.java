package FreeRun;

public class FR_Animator extends RenderPlayerBase
{
	public FR_Animator(RenderPlayerAPI renderplayerapi)
	{
		super(renderplayerapi);
		freerun = null;
		renderTime = 0F;
		mod_FreeRun.instance.setAnimator(this);
	}
	
	public void setFreerunPlayer(FR_FreerunPlayer freerun)
	{
		this.freerun = freerun;
	}
	
	public void setRenderTime(float f)
	{
		renderTime = f;
	}
	
	public void onRender(ModelBiped model, Entity entity)
	{
		if (!mod_FreeRun.instance.properties.enableAnimations || freerun == null || freerun.player != entity)
		{
			return;
		}
		FR_Move move = freerun.move;
		if (move != null)
		{
			FR_Animation anim = freerun.move.getAnimation();
			if (anim != null)
			{
				System.out.println(renderTime);
				anim.doAnimate(model, move.prevAnimProgress + (move.animProgress - move.prevAnimProgress) * renderTime, renderTime);
			}
		}
	}
	
	private FR_FreerunPlayer	freerun;
	private float				renderTime;
}
