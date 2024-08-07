package de.pnku.mcmvaft;

import de.pnku.mcmvaft.block.MoreFeedingTroughBlockEntity;
import de.pnku.mcmvaft.init.McmvaftBlockInit;
import de.pnku.mcmvaft.init.McmvaftItemInit;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static slexom.animal_feeding_trough.platform.common.AnimalFeedingTroughMod.BLOCK_ENTITY_TYPE_REGISTRAR;
import static slexom.animal_feeding_trough.platform.common.AnimalFeedingTroughMod.FEEDING_TROUGH_BLOCK;

public class MoreFeedingTroughVariants implements ModInitializer {
    public static final String MODID = "lolmcmv-aft";
    public static boolean isAftLoaded = false;
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isModLoaded("animal_feeding_trough")) {
            isAftLoaded = true;
            LOGGER.info("MCMV-AFT:\"AFT loaded.\"");
        }
        if (isAftLoaded) {
            McmvaftBlockInit.registerBlocks();
            McmvaftItemInit.registerItems();
        }
    }

    public static ResourceLocation asId(String path) {
        return new ResourceLocation(MODID, path);
    }

}