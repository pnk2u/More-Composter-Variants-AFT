package de.pnku.mcmvaft.mixin;

import de.pnku.mcmvaft.ai.VariantSelfFeedGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slexom.animal_feeding_trough.platform.common.goal.entity.ai.SelfFeedGoal;
import slexom.animal_feeding_trough.platform.common.mixin.GoalSelectorAccessor;
import slexom.animal_feeding_trough.platform.common.mixin.TemptGoalAccessor;

import java.util.Arrays;
import java.util.function.Predicate;

@Mixin(Animal.class)
public class AnimalMixin extends Mob {

    @Unique
    private static final ItemStack[] FORBIDDEN_ITEMS;

    protected AnimalMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = {"<init>"},
            at = {@At("TAIL")}
    )
    private void AFTAddSelfFeedingGoal(EntityType<? extends Mob> entityType, Level world, CallbackInfo ci) {
        if (world != null) {
            if (!world.isClientSide) {
                ((GoalSelectorAccessor)this.goalSelector).getGoals().stream().filter((prioritizedGoal) -> {
                    return prioritizedGoal.getGoal().getClass().equals(TemptGoal.class);
                }).toList().forEach((prioritizedGoal) -> {
                    TemptGoal goal = (TemptGoal)prioritizedGoal.getGoal();
                    PathfinderMob mob = ((TemptGoalAccessor)goal).getMob();
                    double speed = ((TemptGoalAccessor)goal).getSpeed();
                    Predicate<ItemStack> foodPredicate = ((TemptGoalAccessor)goal).getFood();
                    boolean hasForbiddenFood = false;
                    ItemStack[] var8 = FORBIDDEN_ITEMS;
                    int var9 = var8.length;

                    for(int var10 = 0; var10 < var9; ++var10) {
                        ItemStack itemStack = var8[var10];
                        if (foodPredicate.test(itemStack)) {
                            hasForbiddenFood = true;
                            break;
                        }
                    }

                    if (!hasForbiddenFood) {
                        this.goalSelector.addGoal(prioritizedGoal.getPriority() + 1, new VariantSelfFeedGoal((Animal)mob, speed, foodPredicate));
                    }

                });
            }
        }
    }

    static {
        FORBIDDEN_ITEMS = (ItemStack[]) Arrays.stream(new Item[]{Items.CARROT_ON_A_STICK, Items.WARPED_FUNGUS_ON_A_STICK}).map(ItemStack::new).toArray((x$0) -> {
            return new ItemStack[x$0];
        });
    }

}
