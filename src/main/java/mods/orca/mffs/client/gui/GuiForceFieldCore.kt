package mods.orca.mffs.client.gui

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import mods.orca.mffs.MFFSMod
import mods.orca.mffs.client.gui.base.GuiWithPlayerInventory
import mods.orca.mffs.client.gui.utils.GuiColour
import mods.orca.mffs.client.gui.utils.drawString
import mods.orca.mffs.client.gui.utils.getStringCenteredOffsetX
import mods.orca.mffs.container.ContainerForceFieldCore
import net.minecraft.entity.player.InventoryPlayer

@SideOnly(Side.CLIENT)
class GuiForceFieldCore(
    container: ContainerForceFieldCore,
    inventoryPlayer: InventoryPlayer
) : GuiWithPlayerInventory<ContainerForceFieldCore>(
    container,
    inventoryPlayer,
    "core",
    MFFSMod.resource("textures/gui/projector.png")
) {

    init {
        ySize = GUI_HEIGHT
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY)
        drawPowerMeter()
    }

    /**
     * Draw the power meter overlay with how much the core is filled by.
     */
    private fun drawPowerMeter() {

        val filledWidth = container.energyPercent * POWER_METER_WIDTH

        drawTexturedModalRect(
            drawPosX(POWER_METER_X),
            drawPosY(POWER_METER_Y),
            176,
            0,
            filledWidth.toInt(),
            13
        )

    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {

        super.drawGuiContainerForegroundLayer(mouseX, mouseY)

        // Draw power meter's accompanying value
        val powerAmount = container.energy.toInt().toString()

        fontRenderer.drawString(
            powerAmount,
            POWER_METER_X + 70 / 2 - fontRenderer.getStringWidth(powerAmount) / 2,
            POWER_METER_Y + 13 + 4,
            GuiColour.Text
        )

        // Draw labels

        getLabelName("storage")
            .lines()
            .dropLastWhile(String::isEmpty)
            .takeIf { it.isNotEmpty() }
            ?.run {

                val firstWidth = fontRenderer.getStringWidth(first())

                fontRenderer.drawString(
                    text = first(),
                    x = POWER_METER_TEXT_X,
                    y = POWER_METER_TEXT_Y,
                    color = GuiColour.Text
                )

                // Draw subsequent lines centred to the top one.
                forEachIndexed { index, line ->

                    val xOffset = fontRenderer.getStringCenteredOffsetX(line, firstWidth)
                    val yOffset = POWER_METER_TEXT_LINE_HEIGHT * index

                    fontRenderer.drawString(
                        text = line,
                        x = POWER_METER_TEXT_X + xOffset,
                        y = POWER_METER_TEXT_Y + yOffset,
                        color = GuiColour.Text
                    )

                }
            }

        fontRenderer.drawString(
            getLabelName("range"),
            10, 65,
            GuiColour.Text
        )

        fontRenderer.drawString(
            getLabelName("linked_projectors"),
            10, 80,
            GuiColour.Text
        )

        fontRenderer.drawString(
            getLabelName("freq_slot"),
            10, 123,
            GuiColour.Text
        )

    }

    private companion object {
        const val POWER_METER_X = 93
        const val POWER_METER_Y = 30
        const val POWER_METER_WIDTH = 70
        const val POWER_METER_TEXT_X = 10
        const val POWER_METER_TEXT_Y = 30
        const val POWER_METER_TEXT_LINE_HEIGHT = 10
        const val GUI_HEIGHT = 224
    }

}
