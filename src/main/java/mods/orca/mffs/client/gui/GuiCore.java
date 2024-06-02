package mods.orca.mffs.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.orca.mffs.MFFSMod;
import mods.orca.mffs.client.gui.base.GuiHasPlayerInv;
import mods.orca.mffs.container.ContainerCore;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiCore extends GuiHasPlayerInv {

    private static final int powerMeterX = 93;
    private static final int powerMeterY = 30;

    public GuiCore(Container container, InventoryPlayer inventoryPlayer) {
        super(container, inventoryPlayer, new ResourceLocation(MFFSMod.modId, "textures/gui/projector.png"));

        ySize = 224;
    }

    @Override
    protected String getDisplayName() {
        return "core";
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        // Draw the power meter
        Double power = (70 * ((ContainerCore)inventorySlots).getEnergyPercent());
        drawTexturedModalRect(drawPosX(powerMeterX), drawPosY(powerMeterY), 176, 0, power.intValue(), 13);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        // Draw power meter accompanying value
        ContainerCore container = (ContainerCore) inventorySlots;
        String power = Integer.toString(((Double) container.getEnergy()).intValue());

        fontRenderer.drawString(
                power,
                powerMeterX + 70 / 2 - fontRenderer.getStringWidth(power) / 2,
                powerMeterY + 13 + 4,
                0x404040
        );

        // Draw labels
        String[] storage = getTranslationKey("storage").split("\n");

        if (storage.length != 0) {
            fontRenderer.drawString(
                    storage[0],
                    10, 30,
                    0x404040
            );

            // draw subsequent lines centred to this one
            if (storage.length > 1) {
                for (int i = 1; i < storage.length; ++ i) {
                    fontRenderer.drawString(
                            storage[i],
                            10 + fontRenderer.getStringWidth(storage[0]) / 2 - fontRenderer.getStringWidth(storage[i]) / 2, 30 + 10 * i,
                            0x404040
                    );
                }
            }
        }

        fontRenderer.drawString(
                getTranslationKey("range"),
                10, 65,
                0x404040
        );
        fontRenderer.drawString(
                getTranslationKey("linked_projectors"),
                10, 80,
                0x404040
        );
        fontRenderer.drawString(
                getTranslationKey("freq_slot"),
                10, 123,
                0x404040
        );
    }
}
