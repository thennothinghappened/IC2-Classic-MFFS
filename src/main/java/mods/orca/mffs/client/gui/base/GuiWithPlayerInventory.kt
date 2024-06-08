package mods.orca.mffs.client.gui.base

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import mods.orca.mffs.client.gui.utils.GuiColour
import mods.orca.mffs.client.gui.utils.drawString
import mods.orca.mffs.client.gui.utils.getStringCenteredOffsetX
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.ResourceLocation

/**
 * Base behaviour for a GUI which shows the player inventory.
 *
 * @param container The container associated with this front-end GUI.
 * @param inventoryPlayer Inventory instance of the player using the GUI.
 * @param translationKey The key corresponding to this instance in the language file.
 * @param bgTexture Texture to use for drawing the background.
 */
@SideOnly(Side.CLIENT)
abstract class GuiWithPlayerInventory<T : Container>(
    container: T,
    private val inventoryPlayer: InventoryPlayer,
    translationKey: String,
    bgTexture: ResourceLocation
) : GuiBase<T>(container, translationKey, bgTexture) {

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {

        fontRenderer.drawString(
            text = displayName,
            x = fontRenderer.getStringCenteredOffsetX(displayName),
            y = 6,
            color = GuiColour.Text
        )

        fontRenderer.drawString(
            text = inventoryPlayer.displayName.unformattedText,
            x = 8,
            y = ySize - 94,
            color = GuiColour.Text
        )

    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

}
