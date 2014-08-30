package tragicneko.tragicmc.entity.projectile;

import tragicneko.tragicmc.entity.boss.EntityApis;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityDarkLightning extends EntityProjectile {

	public EntityDarkLightning(World par1World)
	{
		super(par1World);
	}
	
	public EntityDarkLightning(World par1World, EntityLivingBase entity, double par2, double par4, double par6) {
		super(par1World, entity, par2, par4, par6);
	}
	
	protected float getMotionFactor()
	{
		return 0.766736F;
	}

	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if (this.worldObj.isRemote)
		{
			//spawn particles on impact with something
		}
		else
		{
			if (var1.entityHit != null && !inGround) // && !(mop.entityHit instanceof EntityEnyvil)) 
			{			
				var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), 5.0F);
				this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, var1.entityHit.posX, var1.entityHit.posY, var1.entityHit.posZ));
				if (var1.entityHit instanceof EntityLivingBase) ((EntityLivingBase) var1.entityHit).addPotionEffect(new PotionEffect(Potion.blindness.id, 120, 1));
			}
			this.setDead();
		}
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		
		if (this.ticksInAir % 10 == 0 && this.ticksInAir > 10 && !this.inGround && !this.isDead)
		{
			this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.posX, this.worldObj.getTopSolidOrLiquidBlock((int) this.posX, (int) this.posZ), this.posZ));
		}
	}
	
	@Override
	protected String getParticleString()
	{
		return "witchMagic";
	}

}