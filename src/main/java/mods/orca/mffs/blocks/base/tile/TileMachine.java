package mods.orca.mffs.blocks.base.tile;

import ic2.api.classic.item.IMachineUpgradeItem;
import ic2.api.classic.tile.IMachine;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import mods.orca.mffs.MFFSMod;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Set;

public abstract class TileMachine extends TileEntity implements IMachine, IEnergySink {
    private double energyStored = 0;

    @Override
    public double getEnergy() {
        return energyStored;
    }

    @Override
    public boolean useEnergy(double amount, boolean simulate) {
        return false;
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
        energyStored += amount;
        return Math.max(energyStored - getDemandedEnergy(), 0);
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return true;
    }

    @Override
    public void invalidate() {
        dt(false);
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        dt(false);
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.onChunkUnload();
    }

    @Override
    public void onLoad() {
        dt(true);
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
    }

    private void dt(boolean l) {
        MFFSMod.logger.info((l ? "loaded" : "unloaded") + (world.isRemote ? " on client" : " on server"));
    }
}
