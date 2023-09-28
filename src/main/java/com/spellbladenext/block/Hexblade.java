package com.spellbladenext.block;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class Hexblade extends HorizontalFacingBlock {
    private final VoxelShape[] occlusionByIndex;
    private final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap();
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final DirectionProperty FACING;

    public Hexblade(Settings properties) {
        super(properties);
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(NORTH, false)).with(EAST, false)).with(SOUTH, false)).with(WEST, false)));
        this.occlusionByIndex = this.makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
    }

    private static int indexFor(Direction direction) {
        return 1 << direction.getHorizontal();
    }
    protected VoxelShape[] makeShapes(float f, float g, float h, float i, float j) {
        float k = 8.0F - f;
        float l = 8.0F + f;
        float m = 8.0F - g;
        float n = 8.0F + g;
        VoxelShape voxelShape = Block.createCuboidShape((double)k, 0.0D, (double)k, (double)l, (double)h, (double)l);
        VoxelShape voxelShape2 = Block.createCuboidShape((double)m, (double)i, 0.0D, (double)n, (double)j, (double)n);
        VoxelShape voxelShape3 = Block.createCuboidShape((double)m, (double)i, (double)m, (double)n, (double)j, 16.0D);
        VoxelShape voxelShape4 = Block.createCuboidShape(0.0D, (double)i, (double)m, (double)n, (double)j, (double)n);
        VoxelShape voxelShape5 = Block.createCuboidShape((double)m, (double)i, (double)m, 16.0D, (double)j, (double)n);
        VoxelShape voxelShape6 = VoxelShapes.union(voxelShape2, voxelShape5);
        VoxelShape voxelShape7 = VoxelShapes.union(voxelShape3, voxelShape4);
        VoxelShape[] voxelShapes = new VoxelShape[]{VoxelShapes.empty(), voxelShape3, voxelShape4, voxelShape7, voxelShape2, VoxelShapes.union(voxelShape3, voxelShape2), VoxelShapes.union(voxelShape4, voxelShape2), VoxelShapes.union(voxelShape7, voxelShape2), voxelShape5, VoxelShapes.union(voxelShape3, voxelShape5), VoxelShapes.union(voxelShape4, voxelShape5), VoxelShapes.union(voxelShape7, voxelShape5), voxelShape6, VoxelShapes.union(voxelShape3, voxelShape6), VoxelShapes.union(voxelShape4, voxelShape6), VoxelShapes.union(voxelShape7, voxelShape6)};

        for(int o = 0; o < 16; ++o) {
            voxelShapes[o] = VoxelShapes.union(voxelShape, voxelShapes[o]);
        }

        return voxelShapes;
    }
    public BlockState getPlacementState(ItemPlacementContext blockPlaceContext) {
        return (BlockState)this.getDefaultState().with(FACING, blockPlaceContext.getPlayerLookDirection().getOpposite());
    }
    protected int getAABBIndex(BlockState blockState) {
        return this.stateToIndex.computeIfAbsent(blockState, (blockStatex) -> {
            int i = 0;
            if ((Boolean)blockStatex.equals(NORTH)) {
                i |= indexFor(Direction.NORTH);
            }

            if ((Boolean)blockStatex.equals(EAST)) {
                i |= indexFor(Direction.EAST);
            }

            if ((Boolean)blockStatex.equals(SOUTH)) {
                i |= indexFor(Direction.SOUTH);
            }

            if ((Boolean)blockStatex.equals(WEST)) {
                i |= indexFor(Direction.WEST);
            }

            return i;
        });
    }
    static {
        FACING = HorizontalFacingBlock.FACING;
        NORTH = ConnectingBlock.NORTH;
        EAST = ConnectingBlock.EAST;
        SOUTH = ConnectingBlock.SOUTH;
        WEST = ConnectingBlock.WEST;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable BlockView blockGetter, List<Text> list, TooltipContext tooltipFlag) {
        list.add(Text.translatable("You have triumphed over Magus. Place to ward off Hexblade invasions "));
        list.add(Text.translatable("in a 64 block radius, or carry to prevent yourself from being Hexed."));
        list.add(Text.translatable("Right Click on air to create a Hexblade Portal you can right click with this sword to enter the Glass Ocean"));

        super.appendTooltip(itemStack, blockGetter, list, tooltipFlag);
    }

    @Override
    public VoxelShape getCullingShape(BlockState blockState, BlockView blockGetter, BlockPos blockPos) {
        return this.occlusionByIndex[this.getAABBIndex(blockState)];
    }

    @Override
    protected ImmutableMap<BlockState, VoxelShape> getShapesForStates(Function<BlockState, VoxelShape> function) {
        return super.getShapesForStates(function);
    }

    public VoxelShape getCameraCollisionShape(BlockState blockState, BlockView blockGetter, BlockPos blockPos, ShapeContext collisionContext) {
        return this.getOutlineShape(blockState, blockGetter, blockPos, collisionContext);
    }
    protected void appendProperties(StateManager.Builder<Block, BlockState> p_53334_) {
        p_53334_.add(FACING,NORTH, EAST, WEST, SOUTH);
    }
}
