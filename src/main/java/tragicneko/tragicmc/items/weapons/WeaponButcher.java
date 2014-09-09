package tragicneko.tragicmc.items.weapons;

import java.util.UUID;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import tragicneko.tragicmc.doomsday.Doomsday;
import tragicneko.tragicmc.entity.projectile.EntityDarkEnergy;
import tragicneko.tragicmc.main.TragicEnchantments;
import tragicneko.tragicmc.main.TragicNewConfig;
import tragicneko.tragicmc.properties.PropertyDoom;

public class WeaponButcher extends EpicWeapon {

	public WeaponButcher(Doomsday dday) {
		super(dday);
		this.lores = new Lore[] {new Lore("ButcherLore1"), new Lore("ButcherLore2"), new Lore("ButcherLore3")};
		this.uncommonEnchants = new Enchantment[] {Enchantment.unbreaking};
		this.uncommonLevels = new int[] {1};
		this.rareEnchants = new Enchantment[] {Enchantment.unbreaking, TragicEnchantments.RuneBreak};
		this.rareLevels = new int[] {3, 1};
		this.epicEnchants = new Enchantment[] {Enchantment.unbreaking, TragicEnchantments.RuneBreak};
		this.epicLevels = new int[] {5, 3};
	}

	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		PropertyDoom doom = PropertyDoom.get(player);

		if (!super.onLeftClickEntity(stack, player, entity) && entity instanceof EntityLivingBase && cooldown == 0 && doom != null && doom.getCurrentDoom() >= 1
				&& TragicNewConfig.allowNonDoomsdayAbilities)
		{
			if (itemRand.nextBoolean()) ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.weakness.id, 200, 1));
			cooldown = 10;
		}
		return super.onLeftClickEntity(stack, player, entity);
	} 
	

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int numb, boolean flag)
	{
		if (world.isRemote || !(entity instanceof EntityPlayer) || !(TragicNewConfig.allowNonDoomsdayAbilities)) return;
		
		UUID uuidForMod = UUID.fromString("040d7d22-6b19-498b-8216-4316cf39387e");
		AttributeModifier mod = new AttributeModifier(uuidForMod, "butcherModifier", 1.0, 0);
		EntityPlayer player = (EntityPlayer) entity;
		PropertyDoom doom = PropertyDoom.get(player);
		
		player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(mod);
		
		if (flag && doom != null && doom.getCurrentDoom() > 0)
		{
			player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(mod);
		}
	}
}
