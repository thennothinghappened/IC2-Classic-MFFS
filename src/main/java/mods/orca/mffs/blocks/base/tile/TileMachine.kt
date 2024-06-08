package mods.orca.mffs.blocks.base.tile

import ic2.api.classic.item.IMachineUpgradeItem
import ic2.api.classic.tile.IMachine
import ic2.api.energy.event.EnergyTileLoadEvent
import ic2.api.energy.event.EnergyTileUnloadEvent
import ic2.api.energy.tile.IEnergySink
import mods.orca.mffs.utils.getBooleanOrNull
import mods.orca.mffs.utils.getDoubleOrNull
import net.minecraft.block.state.IBlockState
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

/**
 * A machine which stores a quantity of energy, which can be used to perform operations.
 *
 * @param maxEnergy The maximum amount of energy this machine can store.
 */
abstract class TileMachine(val maxEnergy: Double) : TileEntity(), IMachine, IEnergySink {

    /**
     * The amount of energy currently stored in this machine.
     */
    private var energy = 0.0

    /**
     * Whether this machine is sensitive to redstone current.
     */
    private var isRedstoneSensitive = false

    /**
     * Whether this machine is currently processing, aka, is currently active/enabled.
     */
    protected var processing = false
        private set

    /**
     * Set whether the machine is processing something, i.e., is active. This method additionally marks the tile as
     * dirty so that it can be synchronised as needed.
     *
     * @param processing Whether the machine is now processing.
     */
    protected fun setProcessing(processing: Boolean) {
        this.processing = processing
        markDirty()
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {

        compound.setDouble(NBT_KEY_ENERGY, energy)
        compound.setBoolean(NBT_KEY_REDSTONE_SENSITIVE, isRedstoneSensitive)
        compound.setBoolean(NBT_KEY_IS_PROCESSING, processing)

        return super.writeToNBT(compound)

    }

    override fun readFromNBT(compound: NBTTagCompound) {

        super.readFromNBT(compound)

        compound.getDoubleOrNull(NBT_KEY_ENERGY)?.let { energy = it }
        compound.getBooleanOrNull(NBT_KEY_REDSTONE_SENSITIVE)?.let { isRedstoneSensitive = it }
        compound.getBooleanOrNull(NBT_KEY_IS_PROCESSING)?.let { processing = it }

    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        return SPacketUpdateTileEntity(pos, blockMetadata, updateTag)
    }

    override fun getUpdateTag(): NBTTagCompound {
        return super.getUpdateTag().apply(::writeToNBT)
    }

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {

        when (net.direction) {

            EnumPacketDirection.CLIENTBOUND -> {

                // the server should never be receiving power updates from a client
                // only the client will get updates from the server.
                readFromNBT(pkt.nbtCompound)

            }

            EnumPacketDirection.SERVERBOUND -> {

            }

            else -> Unit

        }

    }

    override fun useEnergy(amount: Double, simulate: Boolean): Boolean {

        val newEnergy = energy - amount

        if (newEnergy < 0) {
            return false
        }

        if (!simulate) {
            changeEnergy(newEnergy)
        }

        return true

    }

    override fun getEnergy() =
        energy

    override fun setRedstoneSensitive(active: Boolean) {
        isRedstoneSensitive = active
        markDirty()
    }

    override fun isRedstoneSensitive() =
        isRedstoneSensitive

    override fun isProcessing() =
        processing

    override fun getMachineWorld(): World =
        world

    override fun getMachinePos(): BlockPos =
        pos

    /**
     * We don't support any item-based upgrades as we use adjacent blocks for modifying behaviour!
     */
    override fun getSupportedTypes() = emptySet<IMachineUpgradeItem.UpgradeType>()

    override fun injectEnergy(directionFrom: EnumFacing, amount: Double, voltage: Double): Double {
        changeEnergy(energy + amount)
        return max(energy - maxEnergy, 0.0)
    }

    override fun getDemandedEnergy(): Double {
        return max(maxEnergy - energy, 0.0)
    }

    /**
     * Modify the amount of energy stored in this tile, marking it for re-saving.
     *
     * @param newValue The new amount of energy to be stored.
     */
    private fun changeEnergy(newValue: Double) {
        energy = newValue
        markDirty()
    }

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

    override fun shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean {
        return oldState.block !== newState.block
    }

    private companion object {
        const val NBT_KEY_ENERGY = "energy"
        const val NBT_KEY_REDSTONE_SENSITIVE = "isRedstoneSensitive"
        const val NBT_KEY_IS_PROCESSING = "isProcessing"
    }

}
