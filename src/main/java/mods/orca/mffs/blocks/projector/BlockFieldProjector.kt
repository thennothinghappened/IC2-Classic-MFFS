package mods.orca.mffs.blocks.projector

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.BlockWithItem
import mods.orca.mffs.blocks.base.BlockTileEntity
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


object BlockFieldProjector : BlockTileEntity<TileFieldProjector>(TileFieldProjector::class, Material.IRON),
    BlockWithItem {

    private const val NAME = "projector"

    override val itemBlock = ItemBlock(this).apply {
        setRegistryName(NAME)
    }

    init {
        setRegistryName(NAME)
        setTranslationKey(MFFSMod.translationKey(NAME))
        setHardness(3f)
        setResistance(50f)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {

        val projector = getTileEntity(worldIn, pos)
            ?: TODO("FIXME: don't know how to deal with not having a tile.")

        val powered = worldIn.isBlockPowered(pos)

        when {
            powered && !projector.active -> projector.activateField()
            !powered && projector.active -> projector.deactivateField()
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

}
