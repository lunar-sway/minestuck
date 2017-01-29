package com.mraof.minestuck.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by mraof on 2017 January 26 at 9:31 PM.
 */
public class EntityBigPart extends EntityLivingBase implements IEntityAdditionalSpawnData
{
    private PartGroup group;
    private int partId;

    public EntityBigPart(World world)
    {
        super(world);
        this.noClip = true;
    }

    EntityBigPart(World worldIn, PartGroup group, Vec3d size)
    {
        this(worldIn);
        this.group = group;
        this.setSize((float) size.xCoord, (float) size.yCoord);
    }

    void setPartId(int id)
    {
        this.partId = id;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
    }

    @Override
    public void onEntityUpdate()
    {
        if(this.group == null || this.group.parent == null || this.group.parent.isDead)
        {
            this.setDead();
        }
        super.onEntityUpdate();
        world.getHeight(this.getPosition());
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float amount)
    {
        return this.group != null && this.group.attackFrom(damageSource, amount);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList()
    {
        return new ArrayList<ItemStack>();
    }

    @Nullable
    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
    {
        return null;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, @Nullable ItemStack stack)
    {

    }

    @Override
    public EnumHandSide getPrimaryHand()
    {
        return EnumHandSide.RIGHT;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(this.group.parent.getEntityId());
        buffer.writeFloat(this.width);
        buffer.writeFloat(this.height);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        Entity entity = world.getEntityByID(additionalData.readInt());
        if(entity instanceof IBigEntity)
        {
            ArrayList<EntityBigPart> parts = ((IBigEntity) entity).getGroup().parts;
            int index = parts.size() - 1;
            while(index > 0 && parts.get(index).partId > this.partId)
            {
                index--;
            }
            parts.add(index, this);
        }
        this.setSize(this.width, this.height);
    }

    @Override
    public boolean isEntityEqual(Entity entityIn)
    {
        return entityIn == this || this.group != null && (entityIn == this.group.parent || entityIn instanceof EntityBigPart && ((EntityBigPart) entityIn).group == this.group);
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }
}
