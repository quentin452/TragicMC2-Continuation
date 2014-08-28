package tragicneko.tragicmc.entity.projectile;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tragicneko.tragicmc.entity.boss.EntityMegaCryse;
import tragicneko.tragicmc.entity.boss.EntityYeti;
import tragicneko.tragicmc.entity.mob.EntityAbomination;
import tragicneko.tragicmc.entity.mob.EntityCryse;

public class EntityIcicle extends EntityProjectile {

	public EntityIcicle(World world) {
		super(world);
	}

	public EntityIcicle(World par1World, EntityLivingBase par2EntityLivingBase, double par3, double par5, double par7)
	{
		super(par1World, par2EntityLivingBase, par3, par5, par7);
	}
	
	protected float getMotionFactor()
	{
		return 0.865F;
	}

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if (mop.entityHit != null && !inGround) 
		{			
			if (!(mop.entityHit instanceof EntityLivingBase)) return;
			if (mop.entityHit instanceof EntityYeti || mop.entityHit instanceof EntityAbomination || mop.entityHit instanceof EntityCryse || mop.entityHit instanceof EntityMegaCryse) return;
			
			mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), 2.0F);

			if (this.rand.nextBoolean())
			{
				((EntityLivingBase) mop.entityHit).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 60, 0 + rand.nextInt(2)));
			}
			
			this.setDead();
		}
	}
	
	@Override
	protected String getParticleString()
	{
		return "snowshovel";
	}

}