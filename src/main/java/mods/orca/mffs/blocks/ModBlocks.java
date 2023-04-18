package mods.orca.mffs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block TEST_BLOCK = new BlockBase("forcefield", Material.GLASS).setBlockLayer(BlockRenderLayer.CUTOUT).setBlockUnbreakable();
}
