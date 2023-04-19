package mods.orca.mffs.blocks.base;

import mods.orca.mffs.blocks.ModBlocks;
import mods.orca.mffs.client.MFFSTab;
import mods.orca.mffs.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block {

    protected String name;
    private BlockRenderLayer blockRenderLayer = BlockRenderLayer.SOLID;

    public BlockBase(String name, Material material, Boolean setCreativeTab) {
        super(material);

        this.name = name;

        setUnlocalizedName("mffs." + name);
        setRegistryName(name);

        if (setCreativeTab) setCreativeTab(MFFSTab.mffsTab);

        ModBlocks.BLOCKS.add(this);
    }

    public BlockBase(String name, Material material) {
        this(name, material, true);
    }

    public BlockBase setRegisterItem() {
        ModItems.ITEMS.add(createItemBlock());
        return this;
    }

    public BlockBase setBlockLayer(BlockRenderLayer blockRenderLayer) {
        this.blockRenderLayer = blockRenderLayer;
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return blockRenderLayer;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return blockRenderLayer == BlockRenderLayer.SOLID;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return isOpaqueCube(state);
    }

    private Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }
}
