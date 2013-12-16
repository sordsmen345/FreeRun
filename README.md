FreeRun
=======

My attempt at updating BalkondeurAlpha's Freerunner's Mod

The classes FreeRunClient.java and FreeRunProxy.java are actually not a part of the original mod source code, and is actually my attempt at re-working how the block and item textures are handled, but it seemed to fail when using "MinecraftForgeClient.preloadTexture()".  It seems that preloadTexture is not actually a function in MinecraftForgeClient.
