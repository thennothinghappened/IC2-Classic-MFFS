package mods.orca.mffs.utils

import net.minecraft.world.World

inline fun <R> World.whenOn(
    client: () -> R,
    server: () -> R
): R = when (isRemote) {
    true -> client()
    false -> server()
}

inline fun <R> World.onClient(block: () -> R): R? =
    whenOn(client = block, server = { null })

inline fun <R> World.onServer(block: () -> R): R? =
    whenOn(client = { null }, server = block)
