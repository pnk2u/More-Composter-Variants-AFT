package de.pnku.mcmvaft.init;

import de.pnku.mcmvaft.MoreFeedingTroughVariants;
import de.pnku.mcmvaft.block.MoreFeedingTroughBlock;
import de.pnku.mcmvaft.block.MoreFeedingTroughBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import slexom.animal_feeding_trough.platform.common.block.FeedingTroughBlock;

import java.util.ArrayList;
import java.util.List;

import static de.pnku.mcmvaft.MoreFeedingTroughVariants.asId;
import static slexom.animal_feeding_trough.platform.common.AnimalFeedingTroughMod.BLOCK_ENTITY_TYPE_REGISTRAR;

public class McmvaftBlockInit {
    public static final MoreFeedingTroughBlock OAK_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.WOOD, "oak");
    public static final MoreFeedingTroughBlock SPRUCE_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.PODZOL, "spruce");
    public static final MoreFeedingTroughBlock BIRCH_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.SAND, "birch");
    public static final MoreFeedingTroughBlock JUNGLE_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.DIRT, "jungle");
    public static final MoreFeedingTroughBlock ACACIA_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.COLOR_ORANGE, "acacia");
    public static final MoreFeedingTroughBlock DARK_OAK_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.COLOR_BROWN, "dark_oak");
    public static final MoreFeedingTroughBlock MANGROVE_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.COLOR_RED, "mangrove");
    public static final MoreFeedingTroughBlock CHERRY_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.TERRACOTTA_WHITE, SoundType.CHERRY_WOOD, "cherry");
    public static final MoreFeedingTroughBlock BAMBOO_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.COLOR_YELLOW, SoundType.BAMBOO_WOOD, "bamboo");
    public static final MoreFeedingTroughBlock CRIMSON_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.CRIMSON_STEM, SoundType.NETHER_WOOD, "crimson");
    public static final MoreFeedingTroughBlock WARPED_FEEDING_TROUGH_BLOCK = new MoreFeedingTroughBlock(MapColor.WARPED_STEM, SoundType.NETHER_WOOD, "warped");

    public static final List<Block> more_feeding_troughs = new ArrayList<>();
    public static BlockEntityType<MoreFeedingTroughBlockEntity> MORE_FEEDING_TROUGH_BLOCK_ENTITY;


    public static void registerBlocks() {
        registerBlock(OAK_FEEDING_TROUGH_BLOCK);
        registerBlock(SPRUCE_FEEDING_TROUGH_BLOCK);
        registerBlock(BIRCH_FEEDING_TROUGH_BLOCK);
        registerBlock(JUNGLE_FEEDING_TROUGH_BLOCK);
        registerBlock(ACACIA_FEEDING_TROUGH_BLOCK);
        registerBlock(DARK_OAK_FEEDING_TROUGH_BLOCK);
        registerBlock(MANGROVE_FEEDING_TROUGH_BLOCK);
        registerBlock(CHERRY_FEEDING_TROUGH_BLOCK);
        registerBlock(BAMBOO_FEEDING_TROUGH_BLOCK);
        registerBlock(CRIMSON_FEEDING_TROUGH_BLOCK);
        registerBlock(WARPED_FEEDING_TROUGH_BLOCK);

        MORE_FEEDING_TROUGH_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, asId("variant_feeding_troughs"), BlockEntityType.Builder.of(MoreFeedingTroughBlockEntity::new, McmvaftBlockInit.more_feeding_troughs.toArray(Block[]::new)).build(null));

    }

    private static void registerBlock(MoreFeedingTroughBlock feedingTrough) {
        Registry.register(BuiltInRegistries.BLOCK, asId(feedingTrough.feedingTroughWoodType + "_feeding_trough"), feedingTrough);
        more_feeding_troughs.add(feedingTrough);
    }
}