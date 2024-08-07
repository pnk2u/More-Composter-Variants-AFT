package de.pnku.mcmvaft.block;

import de.pnku.mcmvaft.MoreFeedingTroughVariants;
import de.pnku.mcmvaft.init.McmvaftBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slexom.animal_feeding_trough.platform.common.inventory.BlockEntityInventory;
import slexom.animal_feeding_trough.platform.common.screen.FeedingTroughScreenHandler;

import java.util.List;

public class MoreFeedingTroughBlockEntity extends BlockEntity implements MenuProvider, BlockEntityInventory {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private final String NBT_STORED_EXP = "StoredExp";
    private int storedExp = 0;

    public MoreFeedingTroughBlockEntity(BlockPos pos, BlockState state) {
        super(McmvaftBlockInit.MORE_FEEDING_TROUGH_BLOCK_ENTITY, pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, MoreFeedingTroughBlockEntity blockEntity) {
        if (world.isClientSide()) {
            return;
        }

        int count = blockEntity.getItem(0).getCount();
        int newLevel = 0;
        if (count > 0) {
            newLevel = Mth.floor(blockEntity.getItem(0).getCount() / 16.0F) + 1;
            newLevel = Math.min(newLevel, 4);
        }
        int currentLevel = state.getValue(MoreFeedingTroughBlock.LEVEL);
        if (currentLevel != newLevel) {
            BlockState blockState = state.setValue(MoreFeedingTroughBlock.LEVEL, newLevel);
            world.setBlockAndUpdate(pos, blockState);
        }
    }

    private boolean playersAround(Level world, BlockPos pos) {
        AABB lookupArea = new AABB(pos.getX() - 5, pos.getY() - 2, pos.getZ() - 5, pos.getX() + 5, pos.getY() + 2, pos.getZ() + 5);
        List<Player> playersInArea = world.getEntitiesOfClass(Player.class, lookupArea, (e) -> true);
        return !playersInArea.isEmpty();
    }

    public void gatherExperienceOrbs(Level world, BlockPos pos) {
        if (playersAround(world, pos)) {
            return;
        }

        AABB lookupArea = new AABB(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
        List<ExperienceOrb> experienceOrbEntities = world.getEntitiesOfClass(ExperienceOrb.class, lookupArea, (e) -> true);

        if (experienceOrbEntities.isEmpty()) {
            return;
        }

        experienceOrbEntities.forEach(orb -> {
            this.storedExp += orb.getValue();
            orb.remove(Entity.RemovalReason.DISCARDED);
        });
    }

    public void dropStoredXp(Level world, Player playerEntity) {
        if (this.storedExp == 0) {
            return;
        }

        ExperienceOrb entity = new ExperienceOrb(world, playerEntity.getX(), playerEntity.getY() + 0.5F, playerEntity.getZ(), this.storedExp);
        world.addFreshEntity(entity);
        this.storedExp = 0;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new FeedingTroughScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, this.inventory);
        this.storedExp = tag.getInt("StoredExp");
    }

    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.inventory);
        tag.putInt("StoredExp", this.storedExp);
    }

}