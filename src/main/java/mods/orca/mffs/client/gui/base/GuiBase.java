package mods.orca.mffs.client.gui.base;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GuiBase extends GuiContainer {
    protected ResourceLocation bgTexture;

    public GuiBase(Container container, ResourceLocation bgTexture) {
        super(container);

        this.bgTexture = bgTexture;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(bgTexture);

        drawTexturedModalRect(drawPosX(0), drawPosY(0), 0, 0, xSize, ySize);
    }

    protected int drawPosX(int x) {
        return (width - xSize) / 2 + x;
    }

    protected int drawPosY(int y) {
        return (height - ySize) / 2 + y;
    }

    protected abstract String getDisplayName();

    protected String getTranslationKey() {
        return I18n.format("container.mffs."+getDisplayName()+".name");
    }

    protected String getTranslationKey(String key) {
        return I18n.format("container.mffs."+getDisplayName()+".label."+key+".name");
    }
}
