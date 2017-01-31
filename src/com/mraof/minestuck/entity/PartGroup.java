package com.mraof.minestuck.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by mraof on 2017 January 26 at 9:55 PM.
 */
public class PartGroup
{
    private ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
    private ArrayList<Vec3d> sizes = new ArrayList<Vec3d>();
    public ArrayList<EntityBigPart> parts = new ArrayList<EntityBigPart>();
    private ArrayList<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
    EntityLivingBase parent;

    public PartGroup(EntityLivingBase parent)
    {
        this.parent = parent;
    }


    //x, y, z assuming no rotation
    public void addBox(double xOffset, double yOffset, double zOffset, double xSize, double ySize, double zSize)
    {
        Vec3d offset = new Vec3d(xOffset, yOffset, zOffset);
        Vec3d max = offset.addVector(xSize, ySize, zSize);
        //I know AxisAlignedBB has a constructor for two Vec3d but that doesn't work on dedicated servers
        boxes.add(new AxisAlignedBB(offset.xCoord, offset.yCoord, offset.zCoord, max.xCoord, max.yCoord, max.zCoord));
        for(int x = 0; x < xSize; x++)
        {
            positions.add(offset.addVector(x + 0.5, 0, 0.5));
            sizes.add(new Vec3d(1, ySize, 1));
            positions.add(offset.addVector(x + 0.5, 0, zSize - 0.5));
            sizes.add(new Vec3d(1, ySize, 1));
        }

        for(int z = 1; z < zSize - 1; z++)
        {
            positions.add(offset.addVector(0.5, 0, z + 0.5));
            sizes.add(new Vec3d(1, ySize + 10, 1));
            positions.add(offset.addVector(xSize - 0.5, 0, z + 0.5));
            sizes.add(new Vec3d(1, ySize + 10, 1));
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

    public void applyCollision(Entity entity)
    {
        parent.world.theProfiler.startSection("partGroupCollision");
        float yaw = -parent.renderYawOffset * 3.141592f / 180f;
        boolean positionChanged = false;
        Vec3d position = new Vec3d(entity.posX - parent.posX, entity.posY - parent.posY, entity.posZ - parent.posZ).rotateYaw(yaw);
        for (AxisAlignedBB box : boxes)
        {
            AxisAlignedBB relativeBox = new AxisAlignedBB(position.xCoord, position.yCoord, position.zCoord, entity.width, entity.height, entity.width);
            if(box.intersectsWith(relativeBox))
            {
                positionChanged = true;
                double centerX = box.maxX / 2 + box.minX;
                double centerZ = box.maxZ / 2 + box.minZ;
                double differenceX = position.xCoord - centerX;
                double differenceZ = position.zCoord - centerZ;
                if(Math.abs(differenceX) > Math.abs(differenceZ))
                {
                    position = new Vec3d(position.xCoord, position.yCoord, differenceZ > 0 ? box.maxZ : box.minZ);
                }
                else
                {
                    position = new Vec3d(differenceX > 0 ? box.maxX : box.minX, position.yCoord, position.zCoord);
                }
            }
            if(positionChanged)
            {
                position = position.rotateYaw(-yaw);
                entity.move(position.xCoord + parent.posX, position.yCoord + parent.posY, position.zCoord + parent.posZ);
            }
        }
        parent.world.theProfiler.endSection();
    }

    boolean attackFrom(DamageSource damageSource, float amount)
    {
        return parent != null && parent.attackEntityFrom(damageSource, amount);
    }
}
