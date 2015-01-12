package tragicneko.tragicmc.items.weapons;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import tragicneko.tragicmc.TragicEnchantments;
import tragicneko.tragicmc.TragicConfig;
import tragicneko.tragicmc.TragicPotion;
import tragicneko.tragicmc.doomsday.Doomsday;
import tragicneko.tragicmc.properties.PropertyDoom;

public class WeaponGuiltyThorn extends TragicWeapon {
	
	//private final Lore[] uniqueLores = new Lore[] {new Lore("Kill...", EnumRarity.uncommon), new Lore("Your pain = <3", EnumRarity.epic), new Lore("Your happiness hurts me.", EnumRarity.epic),
	//	new Lore("Die.", EnumRarity.epic), new Lore("I love your screams of terror~", EnumRarity.rare), new Lore("Your smiles kill me inside."), new Lore("Killing you softly~", EnumRarity.uncommon),
	//	new Lore("The voices in my head agree I'm not crazy!")};

	public WeaponGuiltyThorn(ToolMaterial p_i45356_1_, Doomsday dday) {
		super(p_i45356_1_, dday);
		//this.lores = uniqueLores;
		this.uncommonEnchants = new Enchantment[] {Enchantment.unbreaking};
		this.uncommonLevels = new int[] {1};
		this.rareEnchants = new Enchantment[] {Enchantment.unbreaking, TragicEnchantments.Leech};
		this.rareLevels = new int[] {3, 1};
		this.epicEnchants = new Enchantment[] {Enchantment.unbreaking, TragicEnchantments.Leech, Enchantment.sharpness};
		this.epicLevels = new int[] {5, 3, 3};
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		PropertyDoom doom = PropertyDoom.get(player);
		
		if (!super.onLeftClickEntity(stack, player, entity) && entity instanceof EntityLivingBase && itemRand.nextInt(8) == 0 && canUseAbility(doom, TragicConfig.nonDoomsdayAbilityCosts[16]) && getStackCooldown(stack) == 0 && TragicConfig.nonDoomsdayAbilities[16])
		{
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.poison.id, 60, itemRand.nextInt(4)));
			if (TragicConfig.allowStun && itemRand.nextInt(6) == 0) ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TragicPotion.Stun.id, 40, 0));
			if (!player.capabilities.isCreativeMode) doom.increaseDoom(-TragicConfig.nonDoomsdayAbilityCosts[16]);
			setStackCooldown(stack, 5);
		}
		return super.onLeftClickEntity(stack, player, entity);
	} 
}
