package crazypants.enderio.machine.farm.farmers;

import com.enderio.core.common.util.BlockCoord;
import crazypants.enderio.machine.farm.TileFarmStation;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.ITreeRoot;
import forestry.api.genetics.AlleleManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ForestryFarmer implements IFarmerJoe {
    private ITreeRoot root;
    private Item forestrySapling;

    private ForestryFarmer(ITreeRoot root, Item forestrySapling) {
        this.root = root;
        this.forestrySapling = forestrySapling;
    }

    public static void init() {
        ITreeRoot root = (ITreeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");
        Item forestrySapling = Item.REGISTRY.getObject(new ResourceLocation("forestry", "sapling"));
        if (root != null && forestrySapling != null)
            FarmersCommune.joinCommune(new ForestryFarmer(root, forestrySapling));
    }

    @Override
    public boolean canPlant(ItemStack stack) {
        return stack != null && stack.getItem() == forestrySapling && root.getType(stack) == EnumGermlingType.SAPLING;
    }


    @Override
    public boolean prepareBlock(TileFarmStation farm, BlockCoord bc, Block block, IBlockState state) {
        ItemStack sapling = farm.getSeedTypeInSuppliesFor(bc);
        if (sapling != null && sapling.getItem() == forestrySapling) {
            ITree tree = root.getMember(sapling);
            if (tree != null && tree.canStay(farm.getWorld(), bc.getBlockPos())) {
                farm.takeSeedFromSupplies(sapling, bc, false);
                root.plantSapling(farm.getWorld(), tree, farm.getFakePlayer().getGameProfile(), bc.getBlockPos());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canHarvest(TileFarmStation farm, BlockCoord bc, Block block, IBlockState state) {
        return false;
    }


    @Override
    public IHarvestResult harvestBlock(TileFarmStation farm, BlockCoord bc, Block block, IBlockState state) {
        return null;
    }
}
