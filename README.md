FreeRun
=======

My attempt at updating BalkondeurAlpha's Freerunner's Mod

The classes FreeRunClient.java and FreeRunProxy.java are actually not a part of the original mod source code, and is actually my attempt at re-working how the block and item textures are handled, but it seemed to fail when using "MinecraftForgeClient.preloadTexture()".  It seems that preloadTexture is not actually a function in MinecraftForgeClient.

**Update**
~Wednesday, December 18, 2013~
I have stopped trinyg to form these base classes into a fucntioning mod, and I am now beginning work on creating a brand spanking new File structure.  Because I am not a Java genius, I am for the time being just copying micdoodle8's open source mod Galacticcraft's file structure.
