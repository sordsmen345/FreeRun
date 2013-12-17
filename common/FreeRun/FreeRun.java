package FreeRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.lwjgl.opengl.GL11;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import api.player.forge.PlayerAPIContainer;
import api.player.server.ServerPlayerAPI;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.modloader.ModLoaderHelper;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;


@Mod(modid = "FreeRun", name = "Free Run", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class FreeRun
{

	public FreeRun()
	{
		properties = new FR_Properties(this);
		properties.loadAllProperties();
		registerItemsAndBlocks();
		
		Minecraft mc = ModLoader.getMinecraftInstance();
		keyForward = mc.gameSettings.keyBindForward.keyCode;
		keyBackward = mc.gameSettings.keyBindBack.keyCode;
		keyLeft = mc.gameSettings.keyBindLeft.keyCode;
		keyRight = mc.gameSettings.keyBindRight.keyCode;
		
		ModLoader.setInGameHook(this, true, true);
		
		instance = this;
		
		ClientPlayerAPI.register("Freerun", FR_FreerunPlayer.class);
		ServerPlayerAPI.register("Freerun", FR_Animator.class);
	}
	
	public String getVersion()
	{
		return "1.6.4 v1.0.0";
	}
	
	public String getName()
	{
		return "FreeRun Mod";
	}
	
	public void load(FMLInitializationEvent event)
	{
	}
	
	public void setFreerunPlayer(FR_FreerunPlayer freerun)
	{
		this.freerun = freerun;
	}
	
	public void setAnimator(FR_Animator animator)
	{
		this.animator = animator;
		animator.setFreerunPlayer(freerun);
	}
	
	private void registerItemsAndBlocks()
	{
		if (properties.enableEdgeWood)
		{
			
			/*overrideID = ModLoader.addOverride("/terrain.png", "/freerun/edge_wood.png");
			FreeRun.edgeWood = (new FR_BlockEdge(properties.edgeWoodId, overrideID)).setHardness(1.0F).setResistance(5F).setStepSound(Block.soundWoodFootstep).setBlockName("edgeWood");
			
			GameRegistry.registerBlock(FreeRun.edgeWood);
			LanguageRegistry.addName(FreeRun.edgeWood, "Wooden Edge");
			GameRegistry.addRecipe(new ItemStack(FreeRun.edgeWood, 2), new Object[] { "#X", Character.valueOf('#'), Block.wood, Character.valueOf('X'), Item.stick });
			*/
		}
		
		if (properties.enableEdgeStone)
		{
			overrideID = ModLoader.addOverride("/terrain.png", "/freerun/edge_stone.png");
			FreeRun.edgeStone = (new FR_BlockEdge(properties.edgeStoneId, overrideID)).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundStoneFootstep).setBlockName("edgeStone");
		
			GameRegistry.registerBlock(FreeRun.edgeStone);
			LanguageRegistry.addName(FreeRun.edgeStone, "Stone Edge");
			GameRegistry.addRecipe(new ItemStack(FreeRun.edgeStone, 2), new Object[] { "#X", Character.valueOf('#'), Block.stone, Character.valueOf('X'), Block.cobblestone });
		}
		
		if (properties.enableHayStack)
		{
			FreeRun.materialHay = (new FR_MaterialHay(MapColor.woodColor));
			
			hayStack = new FR_BlockHayStack(2001, 0).setUnlocalizedName("haystack").setHardness(0.5F).setStepSound(Block.soundGrassFootstep);
			GameRegistry.registerBlock(hayStack, "hayStack");
			LanguageRegistry.addName(hayStack, "Hay Stack");
			/*
			FreeRun.materialHay = (new FR_MaterialHay(MapColor.woodColor)).setBurning();
			
			overrideID = ModLoader.addOverride("/terrain.png", "/freerun/haystack.png");
			FreeRun.hayStack = (new FR_BlockHayStack(properties.hayStackId, overrideID)).setHardness(0.5F).setStepSound(Block.soundGrassFootstep).setBlockName("hayStack");
			
			GameRegistry.registerBlock(FreeRun.hayStack);
			LanguageRegistry.addName(FreeRun.hayStack, "Haystack");
			GameRegistry.addRecipe(new ItemStack(FreeRun.hayStack, 4), new Object[] { " # ", "###", Character.valueOf('#'), Item.wheat });
			GameRegistry.addRecipe(new ItemStack(Item.wheat, 1), new Object[] { "#", Character.valueOf('#'), FreeRun.hayStack });
			try
			{
				setHaystackBurnable();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			 */
		}
		
		if (properties.enableBarWood)
		{
			barWood = (new FR_BlockWoodBar(properties.barWoodId, 4)).setHardness(2.0F).setResistance(5F).setStepSound(Block.soundWoodFootstep).setBlockName("barWood");
			
			GameRegistry.registerBlock(FreeRun.barWood);
			LanguageRegistry.addName(FreeRun.barWood, "Wooden Bar");
			GameRegistry.addRecipe(new ItemStack(FreeRun.barWood, 4), new Object[] { "###", Character.valueOf('#'), Block.wood });
		}
		
		if (properties.enableClimbingGlove)
		{
			overrideID = ModLoader.addOverride("/gui/items.png", "/gui/freerun/climbglove.png");
			FreeRun.climbGlove = (new Item(ModLoader.getUniqueEntityId())).setMaxStackSize(1).setIconIndex(overrideID).setItemName("climbGlove");
			
			LanguageRegistry.addName(FreeRun.climbGlove, "Climbing Glove");
			GameRegistry.addRecipe(new ItemStack(FreeRun.climbGlove, 1), new Object[] { " # ", "#X#", Character.valueOf('#'), Item.leather, Character.valueOf('X'), Item.ingotIron });
		}
	}
	
	private void setHaystackBurnable() throws Exception
	{
		Object o = null;
		Object o1 = null;
		o = ModLoader.getPrivateValue(BlockFire.class, Block.fire, 0);
		o1 = ModLoader.getPrivateValue(BlockFire.class, Block.fire, 1);
		
		if (o instanceof int[])
		{
			int[] chanceToEncourageFire = (int[]) o;
			chanceToEncourageFire[hayStack.blockID] = 30;
		} else
		{
			throw new Exception("Invalid field: changeToEncourageFire");
		}
		if (o1 instanceof int[])
		{
			int[] abilityToCatchFire = (int[]) o1;
			abilityToCatchFire[hayStack.blockID] = 100;
		} else
		{
			throw new Exception("Invalid field: abilityToCatchFire");
		}
	}
	
	public boolean onTickInGame(float f, Minecraft minecraft)
	{
		if (properties.enableAnimations)
		{
			freerun.updateAnimations(f);
			animator.setRenderTime(f);
		}
		
		return true;
	}
	
	public boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess blockAccess, int i, int j, int k, Block block, int m)
	{
		if (m == properties.barWoodModel)
		{
			float f = 1.0F;
			int l = blockAccess.getBlockMetadata(i, j, k);
			if (l == 2)
			{
				block.setBlockBounds(0.4F, 0.8F, 1.0F - f, 0.6F, 1.0F, 1.0F);
			} else if (l == 3)
			{
				block.setBlockBounds(0.4F, 0.8F, 0.0F, 0.6F, 1.0F, f);
			} else if (l == 4)
			{
				block.setBlockBounds(1.0F - f, 0.8F, 0.4F, 1.0F, 1.0F, 0.6F);
			} else if (l == 5)
			{
				block.setBlockBounds(0.0F, 0.8F, 0.4F, f, 1.0F, 0.6F);
			}
			renderblocks.renderStandardBlock(block, i, j, k);
			return true;
		}
		return false;
	}
	
	public void renderInvBlock(RenderBlocks renderblocks, Block block, int i, int j) //FIXME
	{
		int k = block.getRenderType();
		if (k == properties.barWoodModel)
		{
			Tessellator tess = Tessellator.instance;
			float f4 = 1.2F;
			
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			tess.startDrawingQuads();
			{
				tess.setNormal(0.0F, -1.0F, 0.0F);
				renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
				
				tess.setNormal(0.0F, 1.0F, 0.0F);
				renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
				
				tess.setNormal(0.0F, 0.0F, -1.0F);
				renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
				
				tess.setNormal(0.0F, 0.0F, 1.0F);
				renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
				
				tess.setNormal(-1.0F, 0.0F, 0.0F);
				renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
				
				tess.setNormal(1.0F, 0.0F, 0.0F);
				renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
			}
			tess.draw();
			
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
	}
	
	public FR_Properties		properties;
	public FR_FreerunPlayer		freerun;
	public FR_Animator			animator;
	
	public int					keyForward;
	public int					keyBackward;
	public int					keyLeft;
	public int					keyRight;
	
	public static FreeRun		instance;
	public static Block			edgeWood;
	public static Block			edgeStone;
	public static Block			hayStack;
	public static Block			barWood;
	public static Item			climbGlove;
	public static Material		materialHay;
}
