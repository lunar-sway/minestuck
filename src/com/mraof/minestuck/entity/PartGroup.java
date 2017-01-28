package com.mraof.minestuck.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by mraof on 2017 January 26 at 9:55 PM.
 */
public class PartGroup
{
    private ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
    private ArrayList<Vec2f> sizes = new ArrayList<Vec2f>();
    public ArrayList<EntityBigPart> parts = new ArrayList<EntityBigPart>();
    EntityLivingBase parent;

    public PartGroup(EntityLivingBase parent)
    {
        this.parent = parent;
    }


    //x, y, z assuming no rotation
    public void addBox(double xOffset, double yOffset, double zOffset, double xSize, double ySize, double zSize)
    {
        Vec3d offset = new Vec3d(xOffset, yOffset, zOffset);
        for(int x = 0; x < xSize; x++)
        {
            positions.add(offset.addVector(x + 0.5, 0, 0.5));
            sizes.add(new Vec2f(1, (float) ySize));
            positions.add(offset.addVector(x + 0.5, 0, zSize - 0.5));
            sizes.add(new Vec2f(1, (float) ySize));
        }

        for(int z = 1; z < zSize - 1; z++)
        {
            positions.add(offset.addVector(0.5, 0, z + 0.5));
            sizes.add(new Vec2f(1, (float) ySize + 10));
            positions.add(offset.addVector(xSize - 0.5, 0, z + 0.5));
            sizes.add(new Vec2f(1, (float) ySize + 10));
        }
    }

    public void createEntities(World world)
    {
        for(int i = 0; i < positions.size(); i++)
        {
            EntityBigPart part = new EntityBigPart(world, this, sizes.get(i));
            Vec3d position = positions.get(i);
            part.setPosition(parent.posX + position.xCoord, parent.posY + position.yCoord, parent.posZ + position.zCoord);
            part.setPartId(parts.size());
            parts.add(part);
            world.spawnEntity(part);
        }
    }

    public void updatePositions()
    {
        float yaw = -parent.renderYawOffset * 3.141592f / 180f;
        if(parts.size() != positions.size())
        {
            System.out.println("Size mismatch: " + parts.size() + " parts, " + positions.size() + " positions (remote: " + parent.world.isRemote + ")");
        }
        for(int i = 0; i < parts.size(); i++)
        {
            EntityBigPart part = parts.get(i);
            Vec3d position = positions.get(i).rotateYaw(yaw);
            part.setPosition(parent.posX + position.xCoord, parent.posY + position.yCoord, parent.posZ + position.zCoord);
            part.isDead = parent.isDead;
        }
    }

    boolean attackFrom(DamageSource damageSource, float amount)
    {
        return parent != null && parent.attackEntityFrom(damageSource, amount);
    }
}
