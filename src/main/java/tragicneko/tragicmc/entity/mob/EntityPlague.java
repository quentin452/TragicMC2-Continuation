package tragicneko.tragicmc.entity.mob;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import tragicneko.tragicmc.main.TragicNewConfig;
import tragicneko.tragicmc.main.TragicPotions;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityPlague extends TragicMob {

	public EntityPlague(World par1World) {
		super(par1World);
		this.setSize(0.625F, 0.725F);
		this.stepHeight = 1.0F;
		this.experienceValue = 5;
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
		this.tasks.addTask(3, new EntityAIMoveTowardsTarget(this, 1.0D, 32.0F));
		this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 32.0F));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.canCorrupt = true;
		this.isCorruptible = true;
		this.isChangeable = false;
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2);
	}

	public boolean isAIEnabled()
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float par1)
	{
		return 0;
	}

	public float getBrightness(float par1)
	{
		return 0.0F;
	}

	public boolean canRenderOnFire()
	{
		return false;
	}

	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		if (this.ticksExisted % 120 == 0 && TragicNewConfig.allowCorruption)
		{
			this.addPotionEffect(new PotionEffect(TragicPotions.Corruption.id, 200, 0));
		}

		this.motionY = -rand.nextDouble() + 0.2;

		if (this.ticksExisted % 5 == 0)
		{
			this.motionY += rand.nextDouble() + 0.8;
		}

		this.motionX = rand.nextDouble() * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
		this.motionZ = rand.nextDouble() * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);

		for (int k = 0; k < 3; ++k)
		{
			this.worldObj.spawnParticle("townaura",
					this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D,
					this.posY + this.rand.nextDouble() * (double)this.height + 0.4D,
					this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D,
					this.rand.nextDouble(),
					this.rand.nextDouble() - 0.6D,
					this.rand.nextDouble());
		}

		for (int l = 0; l < 3; ++l)
		{
			this.worldObj.spawnParticle("witchMagic",
					this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.5D,
					this.posY + this.rand.nextDouble() * (double)this.height,
					this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.5D,
					(this.rand.nextDouble() - 0.6D) * 0.1D,
					this.rand.nextDouble() * 0.1D,
					(this.rand.nextDouble() - 0.6D) * 0.1D);
		}

		for (int l = 0; l < 2; ++l)
		{
			this.worldObj.spawnParticle("portal",
					this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.5D,
					this.posY + this.rand.nextDouble() * (double)this.height,
					this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.5D,
					(this.rand.nextDouble() - 0.6D) * 0.1D,
					this.rand.nextDouble() * 0.1D,
					(this.rand.nextDouble() - 0.6D) * 0.1D);
		}

		if (!this.worldObj.isRemote && TragicNewConfig.allowCorruption && this.ticksExisted % 60 == 0)
		{
			double d0 = 10.0;
			EnumDifficulty dif = this.worldObj.difficultySetting;

			if (dif == EnumDifficulty.EASY)
			{
				d0 = 6.0;
			}

			if (dif == EnumDifficulty.HARD)
			{
				d0 = 16.0;
			}
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(d0, d0, d0));

			for (int i = 0; i < list.size(); i++)
			{
				Entity entity = list.get(i);

				if (this.canEntityBeSeen(entity))
				{
					if (entity instanceof TragicMob)
					{
						((TragicMob) entity).addPotionEffect(new PotionEffect(TragicPotions.Corruption.id, 800));
					}
					else if (entity instanceof EntityCreature)
					{
						((EntityCreature) entity).addPotionEffect(new PotionEffect(TragicPotions.Corruption.id, 200));
					}
					else if (entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.isCreativeMode)
					{
						((EntityPlayer) entity).addPotionEffect(new PotionEffect(TragicPotions.Corruption.id, 200));

						if (this.rand.nextInt(16) == 0)
						{
							((EntityPlayer) entity).addPotionEffect(new PotionEffect(Potion.confusion.id, 100 + rand.nextInt(100)));
						}

						if (this.rand.nextInt(16) == 0)
						{
							((EntityPlayer) entity).addPotionEffect(new PotionEffect(Potion.blindness.id, 160 + rand.nextInt(140)));
						}

						if (TragicNewConfig.allowSubmission && this.rand.nextInt(32) == 0)
						{
							((EntityPlayer) entity).addPotionEffect(new PotionEffect(TragicPotions.Submission.id, 160 + rand.nextInt(160)));
						}

						if (TragicNewConfig.allowDisorientation && this.rand.nextInt(16) == 0)
						{
							((EntityPlayer) entity).addPotionEffect(new PotionEffect(TragicPotions.Disorientation.id, 180 + rand.nextInt(80)));
						}
					}
				}
			}
		}
		
		if (this.ticksExisted >= 1200 && !this.hasCustomNameTag())
		{
			this.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
		}

	}

	public void fall(float par1){}
	
	public void updateFallState(double par1, boolean par2) {}

}