package cofh.lib.util.raytracer;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Represents a RayTraceResult generated by a {@link IndexedVoxelShape} or {@link MultiIndexedVoxelShape}.
 * <p>
 * Provides easy access to the distance, as well as the {@link IndexedVoxelShape} which the ray hit.
 * <p>
 * Copied from CCL with permission :)
 * <p>
 * Created by covers1624 on 8/9/2016.
 */
public class VoxelShapeRayTraceResult extends DistanceRayTraceResult {

    public IndexedVoxelShape shape;

    public VoxelShapeRayTraceResult(BlockRayTraceResult other, IndexedVoxelShape shape, double dist) {

        super(other.getLocation(), other.getDirection(), other.getBlockPos(), other.isInside(), shape.getData(), dist);
        this.shape = shape;
    }

    public VoxelShapeRayTraceResult(Vector3d hit, Direction side, BlockPos pos, boolean isInside, IndexedVoxelShape shape, double dist) {

        super(hit, side, pos, isInside, shape.getData(), dist);
        this.shape = shape;
    }

    public VoxelShapeRayTraceResult(Vector3d hit, Direction side, boolean isInside, IndexedVoxelShape shape, double dist) {

        super(hit, side, BlockPos.ZERO, isInside, shape.getData(), dist);
        this.shape = shape;
    }

    protected VoxelShapeRayTraceResult(boolean isMissIn, Vector3d hit, Direction side, BlockPos pos, boolean isInside, IndexedVoxelShape shape, double dist) {

        super(isMissIn, hit, side, pos, isInside, shape.getData(), dist);
        this.shape = shape;
    }

    @Override
    public DistanceRayTraceResult withDirection(Direction newFace) {

        return new VoxelShapeRayTraceResult(getType() == Type.MISS, getLocation(), newFace, getBlockPos(), isInside(), shape, dist);
    }

    public DistanceRayTraceResult getAsDistanceResult() {

        return new DistanceRayTraceResult(getType() == Type.MISS, getLocation(), getDirection(), getBlockPos(), isInside(), hitInfo, dist);
    }

    @Override
    public String toString() {

        return super.toString().replace("}", "") + ", cuboid=" + shape.toString() + "}";
    }

}
