package tragicneko.tragicmc.entity.boss;

import static tragicneko.tragicmc.TragicConfig.overlordCoreStats;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tragicneko.tragicmc.TragicBlocks;
import tragicneko.tragicmc.TragicItems;
import tragicneko.tragicmc.TragicMC;
import tragicneko.tragicmc.TragicConfig;
import tragicneko.tragicmc.TragicPotion;
import tragicneko.tragicmc.entity.mob.EntityNanoSwarm;
import tragicneko.tragicmc.entity.projectile.EntityOverlordMortor;

import com.google.common.collect.Sets;

public class EntityOverlordCore extends TragicBoss {

	public double targetX;
	public double targetY;
	public double targetZ;

	public boolean forceNewTarget;
	public boolean slowed;
	private Entity target;

	private int hoverTicks;
	private int hoverBuffer;
	private int aggregate;

	private static final Set ignoredBlocks = Sets.newHashSet(new Block[] {TragicBlocks.OverlordBarrier, Blocks.air, TragicBlocks.Luminescence});

	public EntityOverlordCore(World par1World) {
		super(par1World);
		this.setSize(6.0F, 6.0F);
		this.targetY = 100.0D;
	}

	@Override
	public boolean handleWaterMovement()
	{
		return false;
	}

	@Override
	public void setAir(int i){}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(overlordCoreStats[0]);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(overlordCoreStats[1]);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(overlordCoreStats[2]);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(overlordCoreStats[3]);
		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(overlordCoreStats[4]);
	}

	@Override
	public int getTotalArmorValue()
	{
		return (int) overlordCoreStats[5];
	}

	@Override
	public void onLivingUpdate()
	{		
		double d0;
		double d1;
		double d2;
		double d10;
		float f12;

		if (this.worldObj.isRemote)
		{
			if (this.newPosRotationIncrements > 0)
			{
				d10 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
				d0 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
				d1 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
				d2 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
				this.rotationYaw = (float)(this.rotationYaw + d2 / this.newPosRotationIncrements);
				this.rotationPitch = (float)(this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
				--this.newPosRotationIncrements;
				this.setPosition(d10, d0, d1);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			}
			
			return;
		}

		d10 = this.targetX - this.posX;
		d0 = this.targetY - this.posY;
		d1 = this.targetZ - this.posZ;
		d2 = d10 * d10 + d0 * d0 + d1 * d1;

		if (this.target != null)
		{
			this.targetX = this.target.posX;
			this.targetZ = this.target.posZ;
			double d3 = this.targetX - this.posX;
			double d5 = this.targetZ - this.posZ;
			double d7 = Math.sqrt(d3 * d3 + d5 * d5);
			double d8 = 0.4000000059604645D + d7 / 80.0D - 1.0D;

			if (d8 > 10.0D)
			{
				d8 = 10.0D;
			}

			this.targetY = this.target.boundingBox.minY + d8;

			if (this.rand.nextInt(512) == 0 && this.hoverBuffer == 0 || this.aggregate >= 10 && this.hoverBuffer == 0) this.hoverTicks = 300 + rand.nextInt(120);
		}
		else
		{
			this.targetX += this.rand.nextGaussian() * 2.0D;
			this.targetZ += this.rand.nextGaussian() * 2.0D;
		}

		if (this.forceNewTarget || d2 < 100.0D || d2 > 22500.0D || this.isCollidedHorizontally || this.isCollidedVertically)
		{
			this.setNewTarget();
		}

		d0 /= MathHelper.sqrt_double(d10 * d10 + d1 * d1);
		f12 = 0.6F;

		if (d0 < (-f12))
		{
			d0 = (-f12);
		}

		if (d0 > f12)
		{
			d0 = f12;
		}

		this.motionY += d0 * 0.10000000149011612D;
		this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
		double d4 = 180.0D - Math.atan2(d10, d1) * 180.0D / Math.PI;
		double d6 = MathHelper.wrapAngleTo180_double(d4 - this.rotationYaw);

		if (d6 > 50.0D)
		{
			d6 = 50.0D;
		}

		if (d6 < -50.0D)
		{
			d6 = -50.0D;
		}

		Vec3 vec3 = Vec3.createVectorHelper(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
		Vec3 vec32 = Vec3.createVectorHelper(MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), this.motionY, (-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F))).normalize();
		float f5 = (float)(vec32.dotProduct(vec3) + 0.5D) / 1.5F;

		if (f5 < 0.0F)
		{
			f5 = 0.0F;
		}

		this.randomYawVelocity *= 0.8F;
		float f6 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
		double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;

		if (d9 > 40.0D)
		{
			d9 = 40.0D;
		}

		this.randomYawVelocity = (float)(this.randomYawVelocity + d6 * (0.699999988079071D / d9 / f6));
		this.rotationYaw += this.randomYawVelocity * 0.1F;
		float f7 = (float)(2.0D / (d9 + 1.0D));
		float f8 = 0.06F;
		this.moveFlying(0.0F, -1.0F, f8 * (f5 * f7 + (1.0F - f7)));

		if (this.slowed)
		{
			this.moveEntity(this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
		}
		else
		{
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
		}

		Vec3 vec31 = Vec3.createVectorHelper(this.motionX, this.motionY, this.motionZ).normalize();
		float f9 = (float)(vec31.dotProduct(vec32) + 1.0D) / 2.0F;
		f9 = 0.8F + 0.15F * f9;
		this.motionX *= f9;
		this.motionZ *= f9;
		this.motionY *= 0.9100000262260437D;

		if (this.hoverBuffer > 0) --this.hoverBuffer;

		if (this.hoverTicks > 0) 
		{
			--this.hoverTicks;
			this.motionX = this.motionZ = this.motionY = 0.0F;

			if (this.target != null && this.hoverTicks > 60 && this.hoverTicks % 10 == 0) this.createMortors();
			if (this.ticksExisted % 5 == 0 && this.getHealth() < this.getMaxHealth()) this.healByFactorRanged(5.0F, 5.0F, 30.0F);

			if (this.hoverTicks == 0) this.hoverBuffer = 200;
			this.aggregate = 0;
		}

		if (this.hurtTime == 0 && this.hoverTicks == 0) this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0D, 1.0D, 1.0D)));

		this.slowed = this.destroyBlocksInAABB(this.boundingBox);
	}

	private boolean destroyBlocksInAABB(AxisAlignedBB bb)
	{
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.minY);
		int k = MathHelper.floor_double(bb.minZ);
		int l = MathHelper.floor_double(bb.maxX);
		int i1 = MathHelper.floor_double(bb.maxY);
		int j1 = MathHelper.floor_double(bb.maxZ);
		boolean flag = false;
		boolean flag1 = false;

		for (int k1 = i; k1 <= l; ++k1)
		{
			for (int l1 = j; l1 <= i1; ++l1)
			{
				for (int i2 = k; i2 <= j1; ++i2)
				{
					Block block = this.worldObj.getBlock(k1, l1, i2);

					if (!block.isAir(worldObj, k1, l1, i2))
					{
						if (this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") && !EntityOverlordCore.ignoredBlocks.contains(block))
						{
							flag1 = this.worldObj.setBlockToAir(k1, l1, i2) || flag1;
						}
						else
						{
							flag = true;
						}
					}
				}
			}
		}

		if (flag1)
		{
			double d1 = bb.minX + (bb.maxX - bb.minX) * this.rand.nextFloat();
			double d2 = bb.minY + (bb.maxY - bb.minY) * this.rand.nextFloat();
			double d0 = bb.minZ + (bb.maxZ - bb.minZ) * this.rand.nextFloat();
			this.worldObj.spawnParticle("largeexplode", d1, d2, d0, 0.0D, 0.0D, 0.0D);
		}

		return flag;
	}

	private void attackEntitiesInList(List list)
	{
		for (int i = 0; i < list.size(); ++i)
		{
			Entity entity = (Entity)list.get(i);
			if (entity instanceof EntityLivingBase && !(entity instanceof EntityNanoSwarm)) entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
		}
	}

	private void setNewTarget()
	{
		this.forceNewTarget = false;

		if (this.rand.nextInt(2) == 0 && !this.worldObj.playerEntities.isEmpty())
		{
			this.target = (Entity)this.worldObj.playerEntities.get(this.rand.nextInt(this.worldObj.playerEntities.size()));
		}
		else
		{
			boolean flag = false;

			do
			{
				this.targetX = 0.0D;
				this.targetY = 70.0F + this.rand.nextFloat() * 50.0F;
				this.targetZ = 0.0D;
				this.targetX += this.rand.nextFloat() * 120.0F - 60.0F;
				this.targetZ += this.rand.nextFloat() * 120.0F - 60.0F;
				double d0 = this.posX - this.targetX;
				double d1 = this.posY - this.targetY;
				double d2 = this.posZ - this.targetZ;
				flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
			}
			while (!flag);

			this.target = null;
		}
	}

	private void createMortors() {
		double d0 = this.target.posX - this.posX;
		double d1 = rand.nextInt(4);
		double d2 = this.target.posZ - this.posZ;
		float f1 = MathHelper.sqrt_float(this.getDistanceToEntity(this.target)) * 0.975F;

		EntityOverlordMortor mortor = new EntityOverlordMortor(this.worldObj, this, d0 + this.rand.nextGaussian() * f1, d1, d2 + this.rand.nextGaussian() * f1);
		mortor.posY += (rand.nextDouble() * this.height) + d1 * 0.04335D;
		mortor.posX += d0 * 0.04335D;
		mortor.posZ += d2 * 0.04335D;
		mortor.motionY += rand.nextFloat() - rand.nextFloat();
		mortor.motionX += rand.nextFloat() - rand.nextFloat();
		mortor.motionZ += rand.nextFloat() - rand.nextFloat();
		this.worldObj.spawnEntityInWorld(mortor);
	}

	@Override
	protected void despawnEntity() {}

	@Override
	public void fall(float f) {}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		tag.setDouble("targetX", this.targetX);
		tag.setDouble("targetY", this.targetY);
		tag.setDouble("targetZ", this.targetZ);
		tag.setInteger("hoverTicks", this.hoverTicks);
		tag.setInteger("hoverBuffer", this.hoverBuffer);
		tag.setInteger("aggregate", this.aggregate);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		if (tag.hasKey("targetX")) this.targetX = tag.getDouble("targetX");
		if (tag.hasKey("targetY")) this.targetY = tag.getDouble("targetY");
		if (tag.hasKey("targetZ")) this.targetZ = tag.getDouble("targetZ");
		if (tag.hasKey("hoverTicks")) this.hoverTicks = tag.getInteger("hoverTicks");
		if (tag.hasKey("hoverBuffer")) this.hoverBuffer = tag.getInteger("hoverBuffer");
		if (tag.hasKey("aggregate")) this.aggregate = tag.getInteger("aggregate");
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float dmg)
	{
		if (src.getEntity() instanceof EntityLivingBase && !this.worldObj.isRemote)
		{
			EntityLivingBase entity = (EntityLivingBase) src.getEntity();
			++aggregate;
			if (aggregate >= 10) this.target = entity;

			if (entity.isPotionActive(TragicPotion.Divinity) || !TragicConfig.allowDivinity && !(entity instanceof EntityNanoSwarm))
			{
				if (rand.nextBoolean() && this.worldObj.getEntitiesWithinAABB(EntityNanoSwarm.class, this.boundingBox.expand(64.0, 64.0, 64.0D)).size() < 16)
				{
					EntityNanoSwarm swarm = new EntityNanoSwarm(this.worldObj);
					swarm.setPosition(this.posX, this.posY, this.posZ);
					this.worldObj.spawnEntityInWorld(swarm);
				}
				
				if (this.hoverTicks > 0)
				{
					this.hoverTicks = 0;
					this.forceNewTarget = true;
					this.hoverBuffer = 100;
				}

				return super.attackEntityFrom(src, dmg);
			}
		}

		return true;
	}

	@Override
	public void onDeath(DamageSource par1DamageSource)
	{
		super.onDeath(par1DamageSource);

		if (!this.worldObj.isRemote && TragicConfig.allowMobStatueDrops && rand.nextInt(100) <= TragicConfig.mobStatueDropChance && this.getAllowLoot()) this.entityDropItem(new ItemStack(TragicItems.MobStatue, 1, 15), 0.4F);
		if (!this.worldObj.isRemote && this.getAllowLoot()) this.entityDropItem(new ItemStack(TragicItems.Sentinel), 0.4F);
	}
}