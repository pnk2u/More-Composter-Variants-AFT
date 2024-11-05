package de.pnku.mcmvaft.init;

import de.pnku.mcmvaft.MoreFeedingTroughVariants;
import de.pnku.mcmvaft.block.MoreFeedingTroughBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import slexom.animal_feeding_trough.platform.common.AnimalFeedingTroughMod;

public class McmvaftItemInit {
    public static final BlockItem OAK_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.OAK_FEEDING_TROUGH_BLOCK);
    public static final BlockItem SPRUCE_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.SPRUCE_FEEDING_TROUGH_BLOCK);
    public static final BlockItem BIRCH_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.BIRCH_FEEDING_TROUGH_BLOCK);
    public static final BlockItem JUNGLE_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.JUNGLE_FEEDING_TROUGH_BLOCK);
    public static final BlockItem ACACIA_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.ACACIA_FEEDING_TROUGH_BLOCK);
    public static final BlockItem DARK_OAK_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.DARK_OAK_FEEDING_TROUGH_BLOCK);
    public static final BlockItem MANGROVE_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.MANGROVE_FEEDING_TROUGH_BLOCK);
    public static final BlockItem CHERRY_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.CHERRY_FEEDING_TROUGH_BLOCK);
    public static final BlockItem BAMBOO_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.BAMBOO_FEEDING_TROUGH_BLOCK);
    public static final BlockItem CRIMSON_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.CRIMSON_FEEDING_TROUGH_BLOCK);
    public static final BlockItem WARPED_FEEDING_TROUGH_ITEM = itemFromBlock(McmvaftBlockInit.WARPED_FEEDING_TROUGH_BLOCK);

    public static BlockItem itemFromBlock(MoreFeedingTroughBlock moreFeedingTroughBlock) {
        return new BlockItem(moreFeedingTroughBlock, setProperties(moreFeedingTroughBlock));
    }

    public static Item.Properties setProperties(MoreFeedingTroughBlock moreFeedingTroughBlock) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM,BuiltInRegistries.BLOCK.getKey(moreFeedingTroughBlock))).useBlockDescriptionPrefix();
    }


    public static void registerItems() {
        registerFirstItem(OAK_FEEDING_TROUGH_ITEM);
        registerItem(SPRUCE_FEEDING_TROUGH_ITEM, OAK_FEEDING_TROUGH_ITEM);
        registerItem(BIRCH_FEEDING_TROUGH_ITEM, SPRUCE_FEEDING_TROUGH_ITEM);
        registerItem(JUNGLE_FEEDING_TROUGH_ITEM, BIRCH_FEEDING_TROUGH_ITEM);
        registerItem(ACACIA_FEEDING_TROUGH_ITEM, JUNGLE_FEEDING_TROUGH_ITEM);
        registerItem(DARK_OAK_FEEDING_TROUGH_ITEM, ACACIA_FEEDING_TROUGH_ITEM);
        registerItem(MANGROVE_FEEDING_TROUGH_ITEM, DARK_OAK_FEEDING_TROUGH_ITEM);
        registerItem(CHERRY_FEEDING_TROUGH_ITEM, MANGROVE_FEEDING_TROUGH_ITEM);
        registerItem(BAMBOO_FEEDING_TROUGH_ITEM, CHERRY_FEEDING_TROUGH_ITEM);
        registerItem(CRIMSON_FEEDING_TROUGH_ITEM, BAMBOO_FEEDING_TROUGH_ITEM);
        registerItem(WARPED_FEEDING_TROUGH_ITEM, CRIMSON_FEEDING_TROUGH_ITEM);
    }

    private static void registerItem(BlockItem feedingTrough, Item feedingTroughAfter) {
        Registry.register(BuiltInRegistries.ITEM, MoreFeedingTroughVariants.asId(((MoreFeedingTroughBlock) feedingTrough.getBlock()).feedingTroughWoodType + "_feeding_trough"), feedingTrough);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.addAfter(feedingTroughAfter, feedingTrough));
    }
    private static void registerFirstItem(BlockItem feedingTrough) {
        Registry.register(BuiltInRegistries.ITEM, MoreFeedingTroughVariants.asId(((MoreFeedingTroughBlock) feedingTrough.getBlock()).feedingTroughWoodType + "_feeding_trough"), feedingTrough);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.accept(feedingTrough));
    }
}