package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraftforge.entity.PartEntity;

public class BasiliskPartEntity extends PartEntity<BasiliskEntity> {
    public final BasiliskEntity parentMob;
    public final String name;
    private final EntitySize size;

    public BasiliskPartEntity(BasiliskEntity parent, String name, float width, float height) {
        super(parent);
        this.size = EntitySize.scalable(width, height);
        this.refreshDimensions();
        this.parentMob = parent;
        this.name = name;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT pCompound) {
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return this.isInvulnerableTo(pSource) ? false : this.parentMob.hurt(pSource, pAmount);
    }

    public boolean is(Entity pEntity) {
        return this == pEntity || this.parentMob == pEntity;
    }

    public IPacket<?> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    public EntitySize getDimensions(Pose pPose) {
        return this.size;
    }
}
