package mods.orca.mffs.blocks.base.tile;

import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.tile.IMachine;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import mods.orca.mffs.MFFSMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.Set;

public abstract class TileMachine extends TileEntity implements IMachine, IEnergySink {
    private double energyStored = 0;
    public final double maxEnergy;

    private boolean hasChanged = false;
    protected boolean active = false;

    private static final String ENERGY_STORED_KEY = "energyStored";

    public TileMachine(double maxEnergy) {
        super();
        this.maxEnergy = maxEnergy;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setDouble(ENERGY_STORED_KEY, energyStored);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energyStored = compound.getDouble(ENERGY_STORED_KEY);
        super.readFromNBT(compound);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        if (!hasChanged) {
            return null;
        }

        hasChanged = false;
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        compound.setDouble(ENERGY_STORED_KEY, energyStored);

        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        if (net.getDirection() == EnumPacketDirection.CLIENTBOUND) {
            // the server should never be receiving power updates from a client
            // only the client will get updates from the server.
            NBTTagCompound compound = pkt.getNbtCompound();
            if (compound.hasKey(ENERGY_STORED_KEY)) {
                energyStored = compound.getDouble(ENERGY_STORED_KEY);
            }
        }
    }

    @Override
    public double getEnergy() {
        return energyStored;
    }

    @Override
    public boolean useEnergy(double amount, boolean simulate) {
        boolean canDoThat = energyStored - amount >= 0;

        if (!simulate && canDoThat) {
            changeEnergy(energyStored - amount);
        }

        return canDoThat;
    }

    @Override
    public void setRedstoneSensitive(boolean active) {}

    @Override
    public boolean isRedstoneSensitive() {
        return true;
    }

    @Override
    public boolean isProcessing() {
        return false;
    }

    @Override
    public boolean isValidInput(ItemStack par1) {
        return false;
    }

    @Override
    public Set<IMachineUpgradeItem.UpgradeType> getSupportedTypes() {
        return null;
    }

    @Override
    public World getMachineWorld() {
        return world;
    }

    @Override
    public BlockPos getMachinePos() {
        return pos;
    }

    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        changeEnergy(energyStored + amount);
        return Math.max(energyStored - maxEnergy, 0);
    }

    @Override
    public double getDemandedEnergy() {
        return Math.max(maxEnergy - energyStored, 0);
    }

    protected void changeEnergy(double newValue) {
        energyStored = newValue;
        hasChanged = true;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return true;
    }

    @Override
    public void invalidate() {
        logLoadedTest(false);
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        logLoadedTest(false);
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.onChunkUnload();
    }

    @Override
    public void onLoad() {
        logLoadedTest(true);
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    private void logLoadedTest(boolean l) {
        MFFSMod.logger.info((l ? "loaded" : "unloaded") + (world.isRemote ? " on client" : " on server"));
    }
}
