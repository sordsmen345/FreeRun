package FreeRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.minecraft.client.Minecraft;

public class FR_Properties
{
	public FR_Properties(FreeRun mod)
	{
		baseMod = mod;
	}
	
	public void loadAllProperties()
	{
		Properties props = new Properties();
		if (loadFromFile(props))
		{
			readProperties(props);
			processProperties(props);
		} else
		{
			setStandardSettings();
			createFile(props);
			processProperties(props);
		}
	}
	
	private boolean loadFromFile(Properties props)
	{
		try
		{
			String propsPath = (new StringBuilder()).append(Minecraft.getMinecraftDir().getCanonicalPath()).append("/mods/freerun/freerun.properties").toString();
			FileInputStream f = new FileInputStream(propsPath);
			props.load(f);
			f.close();
			
		} catch (IOException e)
		{
			ModLoader.getLogger().warning("[Freerunner's Mod] Unable to read from properties.");
			return false;
		}
		return true;
	}
	
	private void readProperties(Properties props)
	{
		try
		{
			String path = (new StringBuilder()).append(Minecraft.getMinecraftDir().getCanonicalPath()).append("/mods/freerun/freerun.properties").toString();
			FileInputStream f = new FileInputStream(path);
			props.load(f);
			
			enableEdgeWood = Boolean.parseBoolean(props.getProperty("edgeWood"));
			enableEdgeStone = Boolean.parseBoolean(props.getProperty("edgeStone"));
			enableHayStack = Boolean.parseBoolean(props.getProperty("haystack"));
			enableBarWood = Boolean.parseBoolean(props.getProperty("barWood"));
			enableClimbingGlove = Boolean.parseBoolean(props.getProperty("climbGlove"));
			
			edgeWoodId = Integer.parseInt(props.getProperty("edgeWood-ID"));
			edgeStoneId = Integer.parseInt(props.getProperty("edgeStone-ID"));
			hayStackId = Integer.parseInt(props.getProperty("haystack-ID"));
			barWoodId = Integer.parseInt(props.getProperty("barWood-ID"));
			climbGloveId = Integer.parseInt(props.getProperty("climbGlove-ID"));
			
			barWoodModel = Integer.parseInt(props.getProperty("barWood-Model-ID"));
			keyRun = Keyboard.getKeyIndex(props.getProperty("freerun-key").toUpperCase());
			speedMultiplier = Float.parseFloat(props.getProperty("speed-multiplier"));
			enableAnimations = Boolean.parseBoolean(props.getProperty("enable-animations"));
			fixedGloveInSMP = Boolean.parseBoolean(props.getProperty("enable-glove-in-smp"));
			enableFreerunToggle = Boolean.parseBoolean(props.getProperty("toggle-freerun-key"));
			enableWallKick = Boolean.parseBoolean(props.getProperty("enable-wallkick"));
			f.close();
			ModLoader.getLogger().info("[Freerunner's Mod] Properties file read succesfully!");
		} catch (IOException e)
		{
			ModLoader.getLogger().warning("[Freerunner's Mod] Unable to read from properties.");
		}
	}
	
	private void processProperties(Properties props)
	{
		if (barWoodModel == 0)
		{
			barWoodModel = ModLoader.getUniqueBlockModelID(baseMod, true);
		}
		
		if (climbGloveId == 0)
		{
			climbGloveId = ModLoader.getUniqueEntityId();
		}
		
		if (speedMultiplier > 1.5F)
		{
			speedMultiplier = 1.5F;
		}
		if (speedMultiplier < 1.0F)
		{
			speedMultiplier = 1.0F;
		}
	}
	
	private void setStandardSettings()
	{
		ModLoader.getLogger().warning("[Freerunner's Mod] Using standard settings.");
		enableEdgeWood = true;
		enableEdgeStone = true;
		enableHayStack = true;
		enableBarWood = true;
		enableClimbingGlove = true;
		enableWallKick = true;
		edgeWoodId = 139;
		edgeStoneId = 140;
		hayStackId = 141;
		barWoodId = 142;
		climbGloveId = 0;
		barWoodModel = 0;
		keyRun = Keyboard.getKeyIndex("LCONTROL");
		speedMultiplier = 1.1F;
		enableAnimations = true;
		fixedGloveInSMP = true;
		enableFreerunToggle = false;
		
	}
	
	private void createFile(Properties props)
	{
		ModLoader.getLogger().info("[Freerunner's Mod] Creating new properties file with standard values.\n");
		try
		{
			String folderPath = (new StringBuilder()).append(Minecraft.getMinecraftDir().getCanonicalPath()).append("/mods/freerun/").toString();
			String propsPath = (new StringBuilder()).append(Minecraft.getMinecraftDir().getCanonicalPath()).append("/mods/freerun/freerun.properties").toString();
			new File(folderPath).mkdirs();
			FileOutputStream f = new FileOutputStream(propsPath);
			
			props.setProperty("edgeWood", String.valueOf(enableEdgeWood));
			props.setProperty("edgeStone", String.valueOf(enableEdgeStone));
			props.setProperty("haystack", String.valueOf(enableHayStack));
			props.setProperty("barWood", String.valueOf(enableBarWood));
			props.setProperty("climbGlove", String.valueOf(enableClimbingGlove));
			
			props.setProperty("edgeWood-ID", String.valueOf(edgeWoodId));
			props.setProperty("edgeStone-ID", String.valueOf(edgeStoneId));
			props.setProperty("haystack-ID", String.valueOf(hayStackId));
			props.setProperty("barWood-ID", String.valueOf(barWoodId));
			props.setProperty("climbGlove-ID", String.valueOf(climbGloveId));
			
			props.setProperty("barWood-Model-ID", String.valueOf(barWoodModel));
			
			props.setProperty("freerun-key", Keyboard.getKeyName(keyRun).toUpperCase());
			props.setProperty("speed-multiplier", String.valueOf(speedMultiplier));
			props.setProperty("enable-animations", String.valueOf(enableAnimations));
			props.setProperty("enable-glove-in-smp", String.valueOf(fixedGloveInSMP));
			props.setProperty("toggle-freerun-key", String.valueOf(enableFreerunToggle));
			props.setProperty("enable-wallkick", String.valueOf(enableWallKick));
			
			props.store(f, baseMod.getName() + " " + baseMod.getVersion() + " - Auto-generated properties file");
			f.close();
			
			ModLoader.getLogger().info("[Freerunner's Mod] New properties file created at " + propsPath + "\n");
		} catch (IOException e)
		{
			ModLoader.getLogger().warning("[Freerunner's Mod] Unable to create new properties file. Move the properties file included in the download to the .minecraft/mods/freerun/ folder.\n");
			e.printStackTrace();
		}
	}
	
	public int			keyRun;
	public int			barWoodModel;
	public float		speedMultiplier;
	public boolean		fixedGloveInSMP;
	public boolean		enableFreerunToggle;
	public boolean		enableAnimations;
	public boolean		enableEdgeWood;
	public boolean		enableEdgeStone;
	public boolean		enableHayStack;
	public boolean		enableBarWood;
	public boolean		enableClimbingGlove;
	public boolean		enableWallKick;
	public int			edgeWoodId;
	public int			edgeStoneId;
	public int			hayStackId;
	public int			barWoodId;
	public int			climbGloveId;
	private FreeRun		baseMod;
}
