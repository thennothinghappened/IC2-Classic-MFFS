package mods.orca.mffs.blocks.utils

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack

/**
 * Get the block from a given item stack if it is an [ItemBlock], or [Blocks.AIR].
 */
fun ItemStack?.getBlock(): Block = this
    ?.takeIf { !it.isEmpty }
    ?.let { it.item as? ItemBlock }
    ?.block
    ?: Blocks.AIR

// TODO
//private fun isValidCamo(block: Block): Boolean {
//    return block.blockState.baseState.isFullBlock
//}
//
///**
// * Get whether the given block is valid for camouflage.
// */
//fun isValidCamo(itemStack: ItemStack?): Boolean {
//    return isValidCamo(itemStack.getBlock())
//}
