package mods.orca.mffs.util.handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.orca.mffs.blocks.core.TileCore;
import mods.orca.mffs.blocks.upgrades.camoflage.TileUpgradeCamo;
import mods.orca.mffs.client.gui.GuiUpgradeCamo;
import mods.orca.mffs.container.ContainerCore;
import mods.orca.mffs.container.ContainerUpgradeCamo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class ModGuiHandler implements IGuiHandler {

    public static final int CORE = 0;
    public static final int CAMOFLAGE_UPGRADE = 1;

    @SuppressWarnings("DataFlowIssue")
    @Nullable
    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case CORE:
                return new ContainerCore(player.inventory, (TileCore) world.getTileEntity(new BlockPos(x, y, z)));
            case CAMOFLAGE_UPGRADE:
                return new ContainerUpgradeCamo(player.inventory, (TileUpgradeCamo)world.getTileEntity(new BlockPos(x, y, z)));
            default:
                return null;
        }
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case CAMOFLAGE_UPGRADE:
                return new GuiUpgradeCamo(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
            default:
                return null;
        }
    }
}
