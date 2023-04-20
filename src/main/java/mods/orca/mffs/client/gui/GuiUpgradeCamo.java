package mods.orca.mffs.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.orca.mffs.MFFSMod;
import mods.orca.mffs.blocks.ModBlocks;
import mods.orca.mffs.client.gui.base.GuiHasPlayerInv;
import mods.orca.mffs.container.ContainerPlayerInvBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiUpgradeCamo extends GuiHasPlayerInv {

    public GuiUpgradeCamo(Container container, InventoryPlayer inventoryPlayer) {
        super(container, inventoryPlayer, new ResourceLocation(MFFSMod.MODID, "textures/gui/camoflage_upgrade.png"));
    }

    @Override
    protected String getDisplayName() {
        return ModBlocks.UPGRADE_CAMOFLAGE.getLocalizedName();
    }
}
