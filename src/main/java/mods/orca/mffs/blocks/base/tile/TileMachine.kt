package mods.orca.mffs.blocks.base.tile

import ic2.api.classic.item.IMachineUpgradeItem.UpgradeType
import ic2.api.classic.tile.IMachine
import ic2.api.energy.event.EnergyTileLoadEvent
import ic2.api.energy.event.EnergyTileUnloadEvent
import ic2.api.energy.tile.IEnergyEmitter
import ic2.api.energy.tile.IEnergySink
import mods.orca.mffs.MFFSMod
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.EnumPacketDirection
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import kotlin.math.max

abstract class TileMachine(val maxEnergy: Double) : TileEntity(), IMachine, IEnergySink {
    private var energyStored = 0.0

    private var hasChanged = false
    protected var active: Boolean = false

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        compound.setDouble(ENERGY_STORED_KEY, energyStored)
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        energyStored = compound.getDouble(ENERGY_STORED_KEY)
        super.readFromNBT(compound)
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {

        if (!hasChanged) {
            return null
        }

        hasChanged = false
        return SPacketUpdateTileEntity(getPos(), blockMetadata, updateTag)

    }

    override fun getUpdateTag(): NBTTagCompound {
        val compound = super.getUpdateTag()
        compound.setDouble(ENERGY_STORED_KEY, energyStored)

        return compound
    }

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        if (net.direction == EnumPacketDirection.CLIENTBOUND) {
            // the server should never be receiving power updates from a client
            // only the client will get updates from the server.
            val compound = pkt.nbtCompound
            if (compound.hasKey(ENERGY_STORED_KEY)) {
                energyStored = compound.getDouble(ENERGY_STORED_KEY)
            }
        }
    }

    override fun getEnergy(): Double {
        return energyStored
    }

    override fun useEnergy(amount: Double, simulate: Boolean): Boolean {
        val canDoThat = energyStored - amount >= 0

        if (!simulate && canDoThat) {
            changeEnergy(energyStored - amount)
        }

        return canDoThat
    }

    override fun setRedstoneSensitive(active: Boolean) {}

    override fun isRedstoneSensitive(): Boolean {
        return true
    }

    override fun isProcessing(): Boolean {
        return false
    }

    override fun isValidInput(par1: ItemStack): Boolean {
        return false
    }

    override fun getSupportedTypes(): Set<UpgradeType> {
        return emptySet()
    }

    override fun getMachineWorld(): World {
        return world
    }

    override fun getMachinePos(): BlockPos {
        return pos
    }

    override fun injectEnergy(directionFrom: EnumFacing, amount: Double, voltage: Double): Double {
        changeEnergy(energyStored + amount)
        return max(energyStored - maxEnergy, 0.0)
    }

    override fun getDemandedEnergy(): Double {
        return max(maxEnergy - energyStored, 0.0)
    }

    protected fun changeEnergy(newValue: Double) {
        energyStored = newValue
        hasChanged = true
    }

    override fun acceptsEnergyFrom(emitter: IEnergyEmitter, side: EnumFacing): Boolean {
        return true
    }

    override fun invalidate() {
        logLoadedTest(false)
        MinecraftForge.EVENT_BUS.post(EnergyTileUnloadEvent(this))
        super.invalidate()
    }

    override fun onChunkUnload() {
        logLoadedTest(false)
        MinecraftForge.EVENT_BUS.post(EnergyTileUnloadEvent(this))
        super.onChunkUnload()
    }

    override fun onLoad() {
        logLoadedTest(true)
        MinecraftForge.EVENT_BUS.post(EnergyTileLoadEvent(this))
    }

    override fun shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newSate: IBlockState): Boolean {
        return oldState.block !== newSate.block
    }

    private fun logLoadedTest(l: Boolean) {
        MFFSMod.logger.info((if (l) "loaded" else "unloaded") + (if (world.isRemote) " on client" else " on server"))
    }

    companion object {
        private const val ENERGY_STORED_KEY = "energyStored"
    }

}
