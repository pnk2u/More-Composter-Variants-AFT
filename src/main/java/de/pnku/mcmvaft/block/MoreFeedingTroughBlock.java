package de.pnku.mcmvaft.block;

import com.mojang.serialization.MapCodec;
import de.pnku.mcmvaft.MoreFeedingTroughVariants;
import de.pnku.mcmvaft.init.McmvaftBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import slexom.animal_feeding_trough.platform.common.AnimalFeedingTroughMod;
import slexom.animal_feeding_trough.platform.common.block.FeedingTroughBlock;
import slexom.animal_feeding_trough.platform.common.block.entity.FeedingTroughBlockEntity;

public class MoreFeedingTroughBlock extends FeedingTroughBlock {
    public final String feedingTroughWoodType;
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);

    public MoreFeedingTroughBlock(MapColor colour, String feedingTroughWoodType) {
        super(BlockBehaviour.Properties.of().mapColor(colour).instrument(NoteBlockInstrument.BASS).strength(0.2f).sound(SoundType.WOOD).ignitedByLava().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, MoreFeedingTroughVariants.asId(feedingTroughWoodType + "_feeding_trough"))));
        this.feedingTroughWoodType = feedingTroughWoodType;
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0));
    }

    public MoreFeedingTroughBlock(MapColor colour, SoundType sound, String feedingTroughWoodType) {
        super(BlockBehaviour.Properties.of().mapColor(colour).instrument(NoteBlockInstrument.BASS).strength(0.2f).sound(sound).ignitedByLava().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, MoreFeedingTroughVariants.asId(feedingTroughWoodType + "_feeding_trough"))));
        this.feedingTroughWoodType = feedingTroughWoodType;
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0));
    }

    public MapCodec<FeedingTroughBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(LEVEL);
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MoreFeedingTroughBlockEntity moreFeedingTroughBlockEntity) {
            if (player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty()) {
                moreFeedingTroughBlockEntity.dropStoredXp(world, player);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(world, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MoreFeedingTroughBlockEntity) {
                Containers.dropContents(world, pos, (MoreFeedingTroughBlockEntity) blockEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MoreFeedingTroughBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (world.isClientSide) {
            return null;
        }

        return createTickerHelper(type, McmvaftBlockInit.MORE_FEEDING_TROUGH_BLOCK_ENTITY, (Level wld, BlockPos pos, BlockState st, MoreFeedingTroughBlockEntity blockEntity) -> {
            MoreFeedingTroughBlockEntity.tick(wld, pos, st, blockEntity);
            blockEntity.gatherExperienceOrbs(wld, pos);
        });
    }
}