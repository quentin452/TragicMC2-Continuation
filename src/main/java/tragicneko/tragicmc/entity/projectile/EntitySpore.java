package tragicneko.tragicmc.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySpore extends EntityProjectile {

	public EntitySpore(World par1World) {
		super(par1World);
	}
	
	public EntitySpore(World par1World, EntityLivingBase par2EntityLivingBase, double par3, double par5, double par7)
	{
		super(par1World, par2EntityLivingBase, par3, par5, par7);
	}

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if (mop.entityHit != null && !inGround) 
		{			
			mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), 5.0F);
			
			if (mop.entityHit instanceof EntityLivingBase)
			{
				((EntityLivingBase) mop.entityHit).addPotionEffect(new PotionEffect(Potion.poison.id, 600, 1));
			}
		}

		this.setDead();
		}
	
	

}