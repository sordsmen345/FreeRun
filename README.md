FreeRun
=======

My attempt at updating BalkondeurAlpha's Freerunner's Mod

The classes FreeRunClient.java and FreeRunProxy.java are actually not a part of the original mod source code, and is actually my attempt at re-working how the block and item textures are handled, but it seemed to fail when using "MinecraftForgeClient.preloadTexture()".  It seems that preloadTexture is not actually a function in MinecraftForgeClient.

**Update**
~Wednesday, December 18, 2013~
I have stopped trying to form these base classes into a fucntioning mod, and I am now beginning work on creating a brand spanking new File structure.  Because I am not a Java genius, I am for the time being just copying micdoodle8's open source mod Galacticcraft's file structure.

**Update**
~Thursday, December 19, 2013~
The new mod is looking great, so far I have no errors, and I am now learning how to use the custom freerunning animations in my new mod structure.  I am learning loads of things about Java such as abstract classes and abstract methods, because BalkondeurAlpha used these in his FreeRun mod source.
