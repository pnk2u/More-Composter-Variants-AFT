package de.pnku.mcmvaft.ai;

import de.pnku.mcmvaft.block.MoreFeedingTroughBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import slexom.animal_feeding_trough.platform.common.block.entity.FeedingTroughBlockEntity;

import java.util.function.Predicate;

public class VariantSelfFeedGoal extends MoveToBlockGoal {
    protected final Animal mob;
    private final Predicate<ItemStack> foodPredicate;
    private MoreFeedingTroughBlockEntity feeder;

    public VariantSelfFeedGoal(Animal mob, double speed, Predicate<ItemStack> foodPredicate) {
        super(mob, speed, 8);
        this.mob = mob;
        this.foodPredicate = foodPredicate;
    }

    public boolean canUse() {
        return this.mob.canFallInLove() && this.mob.getAge() == 0 && super.canUse();
    }

    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.feeder != null && this.mob.canFallInLove() && this.mob.getAge() == 0;
    }

    public double acceptedDistance() {
        return 2.0;
    }

    private boolean hasCorrectFood(ItemStack itemStack) {
        return this.foodPredicate.test(itemStack);
    }

    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MoreFeedingTroughBlockEntity moreFeedingTroughBlockEntity) {
            ItemStack itemStack = (ItemStack)moreFeedingTroughBlockEntity.getItems().get(0);
            if (!itemStack.isEmpty() && this.hasCorrectFood(itemStack)) {
                this.feeder = moreFeedingTroughBlockEntity;
                return true;
            }
        }

        return false;
    }

    public void tick() {
        Level world = this.mob.level();
        if (!world.isClientSide && this.feeder != null && this.mob.canFallInLove()) {
            if (!((ItemStack)this.feeder.getItems().get(0)).isEmpty()) {
                this.mob.getLookControl().setLookAt((double)this.blockPos.getX() + 0.5, (double)this.blockPos.getY(), (double)this.blockPos.getZ() + 0.5, 10.0F, (float)this.mob.getMaxHeadXRot());
                if (this.isReachedTarget()) {
                    ((ItemStack)this.feeder.getItems().get(0)).shrink(1);
                    this.mob.setInLove((Player)null);
                }
            }

            this.feeder = null;
        }

        super.tick();
    }
}
