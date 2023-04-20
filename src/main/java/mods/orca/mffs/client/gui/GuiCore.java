package mods.orca.mffs.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.orca.mffs.MFFSMod;
import mods.orca.mffs.blocks.ModBlocks;
import mods.orca.mffs.client.gui.base.GuiHasPlayerInv;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiCore extends GuiHasPlayerInv {

    public GuiCore(Container container, InventoryPlayer inventoryPlayer) {
        super(container, inventoryPlayer, new ResourceLocation(MFFSMod.MODID, "textures/gui/projector.png"));
    }

    @Override
    protected String getDisplayName() {
        return ModBlocks.CORE.getLocalizedName();
    }
}
