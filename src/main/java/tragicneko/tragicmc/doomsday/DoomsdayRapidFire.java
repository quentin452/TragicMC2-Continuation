package tragicneko.tragicmc.doomsday;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import tragicneko.tragicmc.TragicItems;
import tragicneko.tragicmc.doomsday.Doomsday.IExtendedDoomsday;
import tragicneko.tragicmc.properties.PropertyDoom;

public class DoomsdayRapidFire extends Doomsday implements IExtendedDoomsday {

	public DoomsdayRapidFire(int id) {
		super(id, EnumDoomType.OVERFLOW);
		this.waitTime = 2;
		this.maxIterations = 30;
	}

	@Override
	public void doInitialEffects(DoomsdayEffect effect, PropertyDoom doom, EntityPlayer player, boolean crucMoment) {
		player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "You have used Rapid Fire!"));
	}

	@Override
	public void useDoomsday(DoomsdayEffect effect, PropertyDoom doom, EntityPlayer player, boolean crucMoment)
	{
		float f = 1.0F;
		EntityArrow entityarrow = new EntityArrow(player.worldObj, player, f * 2.0F);
		ItemStack stack = player.getCurrentEquippedItem();
		double damage = 3.0;

		if (crucMoment)
		{
			damage += 3.0;
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Crucial Moment!"));
		}

		entityarrow.setDamage(entityarrow.getDamage() + damage);
		entityarrow.motionX *= 1.3;
		entityarrow.motionZ *= 1.3;
		entityarrow.motionY *= 1.1;

		if (f == 1.0F)
		{
			entityarrow.setIsCritical(true);
		}

		int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);

		if (l > 0)
		{
			entityarrow.setKnockbackStrength(l);
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
		{
			entityarrow.setFire(200);
		}

		player.worldObj.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (Doomsday.rand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
		entityarrow.canBePickedUp = 2;

		if (!player.worldObj.isRemote)
		{
			player.worldObj.spawnEntityInWorld(entityarrow);
		}
	}
	
	@Override
	public void doBacklashEffect(PropertyDoom doom, EntityPlayer player) {
		if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == TragicItems.HuntersBow)
		{
			player.destroyCurrentEquippedItem();
			player.playSound("random.break", rand.nextFloat(), rand.nextFloat());
		}
	}

}
