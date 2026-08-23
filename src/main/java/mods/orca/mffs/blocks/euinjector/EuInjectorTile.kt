package mods.orca.mffs.blocks.euinjector

import ic2.api.energy.event.EnergyTileLoadEvent
import ic2.api.energy.event.EnergyTileUnloadEvent
import ic2.api.energy.tile.IEnergyEmitter
import ic2.api.energy.tile.IEnergySink
import mods.orca.mffs.blocks.core.ForceFieldCoreTile
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge

class EuInjectorTile : TileEntity(), IEnergySink {
    private val coreEntity: ForceFieldCoreTile?
        get() {
            if (isInvalid || world.isRemote) {
                return null
            }

            for (direction in EnumFacing.entries) {
                val core = world.getTileEntity(pos.offset(direction)) as? ForceFieldCoreTile

                if (core != null) {
                    return core
                }
            }

            return null
        }

    override fun shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean =
        oldState.block != newState.block

    override fun getDemandedEnergy(): Double {
        if (world.getBlockState(pos).getValue(EuInjectorBlock.active)) {
            return coreEntity?.demandedEnergy ?: 0.0
        }

        return 0.0
    }

    override fun getSinkTier(): Int = ForceFieldCoreTile.IC2_TIER

    // Technically the wrong inject side but the core doesn't care.
    override fun injectEnergy(side: EnumFacing, amount: Double, voltage: Double): Double =
        coreEntity?.injectEnergy(side, amount, voltage) ?: amount

    override fun acceptsEnergyFrom(emitter: IEnergyEmitter, side: EnumFacing): Boolean = true

    override fun invalidate() {
        super.invalidate()
        MinecraftForge.EVENT_BUS.post(EnergyTileUnloadEvent(this))
    }

    override fun onChunkUnload() {
        super.onChunkUnload()
        MinecraftForge.EVENT_BUS.post(EnergyTileUnloadEvent(this))
    }

    override fun onLoad() {
        MinecraftForge.EVENT_BUS.post(EnergyTileLoadEvent(this))
    }
}
