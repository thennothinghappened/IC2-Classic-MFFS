package mods.orca.mffs.blocks.euinjector

import mods.orca.mffs.MFFSMod
import mods.orca.mffs.blocks.BlockWithItem
import mods.orca.mffs.blocks.base.BlockMachine
import mods.orca.mffs.registry.Registry
import net.minecraft.block.Block
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class EuInjectorBlock : BlockMachine<EuInjectorTile>(EuInjectorTile::class), BlockWithItem {
    companion object {
        private const val NAME = "eu_injector"
        val active: IProperty<Boolean> = PropertyBool.create("active")
    }

    override val itemBlock = ItemBlock(this).apply {
        setRegistryName(NAME)
    }

    init {
        setRegistryName(NAME)
        setTranslationKey(MFFSMod.translationKey(NAME))
        setCreativeTab(Registry.creativeTab)

        defaultState = blockState.baseState.withProperty(active, false)
    }

    override fun createTileEntity(world: World, state: IBlockState): EuInjectorTile {
        return EuInjectorTile()
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun neighborChanged(state: IBlockState, world: World, pos: BlockPos, block: Block, fromPos: BlockPos) {
        val powered = world.isBlockPowered(pos)

        if (powered != state.getValue(active)) {
            world.setBlockState(pos, state.withProperty(active, powered))
        }
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, active)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return if (state.getValue(active)) 1 else 0
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(active, (meta and 1) == 1)
    }
}
