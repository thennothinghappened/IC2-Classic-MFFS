package mods.orca.mffs.client.gui.base

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Base class for a GUI which uses a standard text-and-background-image setup.
 *
 * @param container The container associated with this front-end GUI
 * @param translationKey The key corresponding to this instance in the language file.
 * @param bgTexture Texture to use for drawing the background.
 */
@SideOnly(Side.CLIENT)
abstract class GuiBase<T : Container>(
    protected val container: T,
    private val translationKey: String,
    private val bgTexture: ResourceLocation
) : GuiContainer(container) {

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {

        GlStateManager.color(1f, 1f, 1f, 1f)
        mc.textureManager.bindTexture(bgTexture)

        drawTexturedModalRect(drawPosX(0), drawPosY(0), 0, 0, xSize, ySize)

    }

    protected fun drawPosX(x: Int): Int {
        return (width - xSize) / 2 + x
    }

    protected fun drawPosY(y: Int): Int {
        return (height - ySize) / 2 + y
    }

    /**
     * The localized display name of this container.
     */
    protected val displayName: String
        get() = I18n.format("container.mffs.$translationKey.name")

    /**
     * Get the display name of the given label.
     *
     * @param label The name of the label to get by.
     */
    protected fun getLabelName(label: String): String =
        I18n.format("container.mffs.$translationKey.label.$label.name")

}
