
# We don't care about helper annotations.
-dontwarn org.jetbrains.annotations.**
-dontwarn javax.annotation.**

# Keep stuff that's found by Forge magic(TM).
-keep class mods.orca.mffs.MFFSMod { *; }
-keep class mods.orca.mffs.registry.RegistryHandler { *; }
-keep class mods.orca.mffs.proxy.ClientProxy { *; }
-keep class mods.orca.mffs.proxy.DedicatedProxy { *; }

# TODO: Supressing notes about duplicate classes, doesn't seem to cause issues but feels wrong.
-dontnote kotlin.**

# We don't care about obfuscation! We just want to minify.
-dontobfuscate
