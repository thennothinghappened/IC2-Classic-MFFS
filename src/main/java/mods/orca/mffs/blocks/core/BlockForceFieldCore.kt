package mods.orca.mffs.blocks.core

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.IHasItemBlock
import mods.orca.mffs.blocks.base.BlockUpgradableMachine
import mods.orca.mffs.client.MFFSTab
import mods.orca.mffs.container.MFFSGuiHandler
import mods.orca.mffs.items.ItemFrequencyCardBlank
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Block component of the Force-Field Core - see [TileForceFieldCore]!
 */
object BlockForceFieldCore : BlockUpgradableMachine<TileForceFieldCore>(TileForceFieldCore::class), IHasItemBlock {

    private const val NAME = "core"

    override val itemBlock = ItemBlock(this).apply {
        setRegistryName(NAME)
    }

    init {
        setRegistryName(NAME)
        setTranslationKey(MFFSMod.translationKey(NAME))
        setCreativeTab(MFFSTab)
    }

    override fun createTileEntity(world: World, state: IBlockState): TileForceFieldCore {
        return TileForceFieldCore()
    }

    /**
     * When the player right-clicks the Core, we either take them to the GUI, link held cards, or allow placement
     * of an upgrade.
     */
    override fun onBlockActivated(
        world: World,
        pos: BlockPos,
        state: IBlockState,
        player: EntityPlayer,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {

        val heldItemStack = player.getHeldItem(hand)
            .takeUnless { it.isEmpty }

        when (val item = heldItemStack?.item) {

            is ItemFrequencyCardBlank -> {

                val core = getTileEntity(world, pos)
                    ?: return false

                val linkedCardStack = core.linkBlankCardStack(heldItemStack)
                    ?: return false

                if (!world.isRemote) {
                    player.setHeldItem(hand, linkedCardStack)
                }

                return true

            }

//            is ItemBlock -> {
//
//                // If the player is trying to place an upgrade, don't get in the way!
//                // TODO: Search for upgrade
//                //                if (heldBlock is IBlockUpgrade) {
//                //                    return false
//                //                }
//
//            }

            else -> {
                player.openGui(MFFSMod.instance, MFFSGuiHandler.GuiId.Core.ordinal, world, pos.x, pos.y, pos.z)
                return true
            }

        }

    }

}
