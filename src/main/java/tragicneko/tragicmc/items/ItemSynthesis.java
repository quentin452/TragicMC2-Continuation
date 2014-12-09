package tragicneko.tragicmc.items;

import java.util.UUID;

import tragicneko.tragicmc.TragicNewConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSynthesis extends ItemTalisman {
	
	public static AttributeModifier mod = new AttributeModifier(UUID.fromString("c77b57e3-fbb3-4f31-a26e-3e614c57d7ef"), "synthesisModifier", TragicNewConfig.modifierAmts[22], 0);

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int numb, boolean flag)
	{
		if (world.isRemote || !(entity instanceof EntityPlayer)) return;
		
		EntityPlayer player = (EntityPlayer) entity;
		player.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(mod);
		
		if (world.isDaytime() && !world.isRaining() && !world.isThundering())
		{
			player.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(mod);
			if (player.ticksExisted % 400 == 0) stack.damageItem(1, player);
		}
	}
}
