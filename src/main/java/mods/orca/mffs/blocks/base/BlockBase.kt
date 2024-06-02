package mods.orca.mffs.blocks.base

import mods.orca.mffs.blocks.ModBlocks
import mods.orca.mffs.client.MFFSTab
import mods.orca.mffs.items.ModItems
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock

open class BlockBase(

    protected var name: String,
    material: Material,
    setCreativeTab: Boolean

) : Block(material) {

    constructor(name: String, material: Material) : this(name, material, true)

    init {

        translationKey = "mffs.$name"
        setRegistryName(name)

        if (setCreativeTab) creativeTab = MFFSTab

        ModBlocks.BLOCKS.add(this)

    }

    fun setRegisterItem(): BlockBase {
        ModItems.ITEMS.add(createItemBlock())
        return this
    }

    private fun createItemBlock(): Item = ItemBlock(this)
        .setRegistryName(registryName)

}
