package mods.orca.mffs.client.gui.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public abstract class GuiHasPlayerInv extends GuiBase {
    private InventoryPlayer inventoryPlayer;

    public GuiHasPlayerInv(Container container, InventoryPlayer inventoryPlayer, ResourceLocation bgTexture) {
        super(container, bgTexture);

        this.inventoryPlayer = inventoryPlayer;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(getTranslationKey(), xSize / 2 - fontRenderer.getStringWidth(getTranslationKey()) / 2, 6, 0x404040);
        fontRenderer.drawString(inventoryPlayer.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
