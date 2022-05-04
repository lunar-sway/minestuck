package com.mraof.minestuck.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by mraof on 2017 January 26 at 9:55 PM.
 */
public class PartGroup
{
    private ArrayList<Vector3d> positions = new ArrayList<>();
    private ArrayList<Vector3d> sizes = new ArrayList<>();
    public ArrayList<EntityBigPart> parts = new ArrayList<>();
    private ArrayList<AxisAlignedBB> boxes = new ArrayList<>();
    LivingEntity parent;

    public PartGroup(LivingEntity parent)
    {
        this.parent = parent;
    }


    //x, y, z assuming no rotation
    public void addBox(double xOffset, double yOffset, double zOffset, double xSize, double ySize, double zSize)
    {
        Vector3d offset = new Vector3d(xOffset, yOffset, zOffset);
        Vector3d max = offset.add(xSize, ySize, zSize);
        //I know AxisAlignedBB has a constructor for two Vector3d but that doesn't work on dedicated servers
        boxes.add(new AxisAlignedBB(offset.x, offset.y, offset.z, max.x, max.y, max.z));
        for(int x = 0; x < xSize; x++)
        {
            positions.add(offset.add(x + 0.5, 0, 0.5));
            sizes.add(new Vector3d(1, ySize, 1));
            positions.add(offset.add(x + 0.5, 0, zSize - 0.5));
            sizes.add(new Vector3d(1, ySize, 1));
        }

        for(int z = 1; z < zSize - 1; z++)
        {
            positions.add(offset.add(0.5, 0, z + 0.5));
            sizes.add(new Vector3d(1, ySize + 10, 1));
            positions.add(offset.add(xSize - 0.5, 0, z + 0.5));
            sizes.add(new Vector3d(1, ySize + 10, 1));
        }
    }

    public void createEntities(World world)
    {
        for(int i = 0; i < positions.size(); i++)
        {
            EntityBigPart part = new EntityBigPart(parent.getType(), world, this, (float) sizes.get(i).x, (float) sizes.get(i).y);
            Vector3d position = positions.get(i);
            part.setPos(parent.getX() + position.x, parent.getY() + position.y, parent.getZ() + position.z);
            part.setPartId(parts.size());
            parts.add(part);
            //world.addEntity(part); TODO Not safe to add entities to world on creation. A different solution is needed
        }
    }

    public void updatePositions()
    {
        float yaw = -parent.yBodyRot * 3.141592f / 180f;
        if(parts.size() != positions.size())
        {
            System.out.println("Size mismatch: " + parts.size() + " parts, " + positions.size() + " positions (remote: " + parent.level.isClientSide + ")");
        }
        for(int i = 0; i < parts.size(); i++)
        {
            EntityBigPart part = parts.get(i);
            Vector3d position = positions.get(i).yRot(yaw);
            part.setPos(parent.getX() + position.x, parent.getY() + position.y, parent.getZ() + position.z);
            if(parent.removed != part.removed)
            {
                if(parent.removed)
                    part.remove(false);
                else part.revive();
            }
        }
    }

    public void applyCollision(Entity entity)
    {
        parent.level.getProfiler().push("partGroupCollision");
        float yaw = -parent.yBodyRot * 3.141592f / 180f;
        boolean positionChanged = false;
        Vector3d position = new Vector3d(entity.getX() - parent.getX(), entity.getY() - parent.getY(), entity.getZ() - parent.getZ()).yRot(yaw);
        for (AxisAlignedBB box : boxes)
        {
            AxisAlignedBB relativeBox = new AxisAlignedBB(position.x, position.y, position.z, entity.getBbWidth(), entity.getBbHeight(), entity.getBbWidth());
            if(box.intersects(relativeBox))
            {
                positionChanged = true;
                double centerX = box.maxX / 2 + box.minX;
                double centerZ = box.maxZ / 2 + box.minZ;
                double differenceX = position.x - centerX;
                double differenceZ = position.z - centerZ;
                if(Math.abs(differenceX) > Math.abs(differenceZ))
                {
                    position = new Vector3d(position.x, position.y, differenceZ > 0 ? box.maxZ : box.minZ);
                }
                else
                {
                    position = new Vector3d(differenceX > 0 ? box.maxX : box.minX, position.y, position.z);
                }
            }
            if(positionChanged)
            {
                position = position.yRot(-yaw);
                entity.move(MoverType.SELF, position.add(parent.position()));	//TODO change to velocity, or lookup MoverType?
            }
        }
        parent.level.getProfiler().pop();
    }

    boolean attackFrom(DamageSource damageSource, float amount)
    {
        return parent != null && parent.hurt(damageSource, amount);
    }
}
