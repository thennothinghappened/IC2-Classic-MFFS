package mods.orca.mffs.blocks.projector

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.BlockWithItem
import mods.orca.mffs.blocks.base.BlockMachine
import mods.orca.mffs.blocks.base.BlockTileEntity
import mods.orca.mffs.client.MFFSTab
import mods.orca.mffs.container.MFFSGuiHandler
import mods.orca.mffs.items.ItemFrequencyCard
import mods.orca.mffs.registry.Registry
import net.minecraft.block.Block
import net.minecraft.block.BlockLever
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.CapabilityItemHandler

class FieldProjectorBlock : BlockTileEntity<TileFieldProjector>(TileFieldProjector::class, Material.IRON),
    BlockWithItem {

    companion object {
        private const val NAME = "projector"
    }

    override val itemBlock = ItemBlock(this).apply {
        setRegistryName(NAME)
    }

    init {
        setRegistryName(NAME)
        setTranslationKey(MFFSMod.translationKey(NAME))
        setHardness(3f)
        setResistance(50f)
        setCreativeTab(Registry.creativeTab)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun neighborChanged(state: IBlockState, world: World, pos: BlockPos, block: Block, fromPos: BlockPos) {

        val projector = getTileEntity(world, pos)
            ?: TODO("FIXME: don't know how to deal with not having a tile.")

        val powered = world.isBlockPowered(pos)
        val active = projector.isActive()

        when {
            powered && !active -> projector.activateField()
            !powered && active -> projector.deactivateField()
            else -> Unit
        }

    }

    override fun onBlockClicked(worldIn: World, pos: BlockPos, playerIn: EntityPlayer) {
        val projector = getTileEntity(worldIn, pos)
            ?: TODO("FIXME: don't know how to deal with not having a tile.")

        projector.testExpandingTheField()
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val projector = getTileEntity(worldIn, pos)
            ?: TODO("FIXME: don't know how to deal with not having a tile.")

        projector.onDestroy()
        super.breakBlock(worldIn, pos, state)
    }

    override fun createTileEntity(world: World, state: IBlockState): TileFieldProjector {
        return TileFieldProjector(FieldPerimeterSdf.Sphere())
    }

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
            is ItemBlock -> when (item.block) {
                // Allow placing levers.
                is BlockLever -> { return false }

                // Allow placing other machines adjacent.
                is BlockMachine<*> -> { return false }
            }

            // Accept a frequency card if we don't already have one.
            is ItemFrequencyCard -> {
                val tile = getTileEntity(world, pos) ?: return false

                tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)
            }
        }

        MFFSGuiHandler.openGui(player, MFFSGuiHandler.Gui.Core, world, pos)
        return true
    }
}
