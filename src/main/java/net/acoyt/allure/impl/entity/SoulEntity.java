package net.acoyt.allure.impl.entity;

import net.acoyt.allure.impl.cca.entity.SoulsComponent;
import net.acoyt.allure.impl.index.AllureEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * @author AcoYT
 */
public class SoulEntity extends Entity {
    private static final int LIFETIME = 6000;

    public static final EntityDataAccessor<Integer> DATA_VALUE
            = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.INT);

    @Nullable private Player followingPlayer;
    private final InterpolationHandler interpolation = new InterpolationHandler(this);
    private int age = 0;

    public SoulEntity(Level level, double x, double y, double z, int value, Player followingPlayer) {
        this(level, new Vec3(x, y, z), value, followingPlayer);
    }

    public SoulEntity(Level level, Vec3 pos, int value, Player followingPlayer) {
        this(AllureEntities.SOUL, level);
        this.setPos(pos);
        this.setValue(value);
        this.followingPlayer = followingPlayer;
    }

    public SoulEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public boolean isNoGravity() {
        return true;
    }

    public void tick() {
        this.noPhysics = true;
        if (followingPlayer != null) this.interpolation.interpolateTo(followingPlayer.position(), 0.0F, 0.0F);
        super.tick();

        age++;
        if (age > LIFETIME) this.discard();
    }

    public void playerTouch(Player player) {
        SoulsComponent.KEY.get(player).addSouls(getValue());
        super.playerTouch(player);
    }

    public void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(DATA_VALUE, 1);
    }

    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return source.is(DamageTypes.GENERIC_KILL);
    }

    public void readAdditionalSaveData(ValueInput input) {
        age = input.getIntOr("Age", 0);
        setValue(input.getIntOr("Value", 1));
    }

    public void addAdditionalSaveData(ValueOutput output) {
        output.putInt("Age", age);
        output.putInt("Value", getValue());
    }

    public int getValue() {
        return entityData.get(DATA_VALUE);
    }

    public void setValue(int value) {
        entityData.set(DATA_VALUE, value);
    }
}
