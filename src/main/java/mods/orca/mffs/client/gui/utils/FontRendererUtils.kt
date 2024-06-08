package mods.orca.mffs.client.gui.utils

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.inventory.GuiContainer

/**
 * Draw a string using a [GuiColour].
 */
fun FontRenderer.drawString(text: String, x: Int, y: Int, color: GuiColour) {
    drawString(text, x, y, color.hex)
}

/**
 * Get the centred X-position of text within a container of width [containerWidth].
 *
 * @param text The text to measure.
 * @param containerWidth The width of the container to position within.
 */
fun FontRenderer.getStringCenteredOffsetX(text: String, containerWidth: Int) =
    (containerWidth / 2) - (getStringWidth(text) / 2)

/**
 * Get the centred X-position of text within the current container.
 *
 * @param text The text to measure.
 */
context(GuiContainer)
fun FontRenderer.getStringCenteredOffsetX(text: String) =
    getStringCenteredOffsetX(text, xSize)
