package cofh.lib.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static cofh.lib.util.references.CoreReferences.ELECTRIC_FIELD_ENTITY;

public class ElectricFieldEntity extends AbstractAoESpellEntity {

    public ElectricFieldEntity(EntityType<? extends ElectricFieldEntity> type, Level level) {

        super(type, level);
    }

    public ElectricFieldEntity(Level level, Vec3 pos, float radius, int duration) {

        this(ELECTRIC_FIELD_ENTITY, level);
        this.moveTo(pos);
        this.radius = radius;
        this.duration = duration;
    }

    @Override
    public void tick() {

        if (!level.isClientSide && random.nextInt(16) == 0) {
            summonArc();
        }
        super.tick();
    }

    protected void summonArc() {

        double radiusSqr = radius * radius;
        List<Entity> entities = level.getEntities(this, this.getBoundingBox().inflate(radius), EntitySelector.LIVING_ENTITY_STILL_ALIVE.and((entity) -> entity.distanceToSqr(this) < radiusSqr));
        if (random.nextInt(5) < entities.size()) {
            level.addFreshEntity((new ElectricArcEntity(level, entities.get(random.nextInt(entities.size())))).setOwner(this.getOwner()));
        } else {
            Vec3 pos = this.position().add(random.nextGaussian() * radius * 0.5F, radius, random.nextGaussian() * radius * 0.5F);
            BlockHitResult raytrace = level.clip(new ClipContext(pos, pos.add(0, -2 * radius, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, this));
            if (raytrace.getType().equals(BlockHitResult.Type.MISS)) {
                return;
            }
            level.addFreshEntity((new ElectricArcEntity(level, raytrace.getLocation())).setOwner(this.getOwner()));
        }
    }

}
