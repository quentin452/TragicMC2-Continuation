package tragicneko.tragicmc.items.armor;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tragicneko.tragicmc.TragicEnchantments;
import tragicneko.tragicmc.TragicItems;
import tragicneko.tragicmc.doomsday.Doomsday;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArmorHunter extends TragicArmor {

	public ArmorHunter(ArmorMaterial material, int armorType, Doomsday dday) {
		super(material, armorType, dday);
	}

	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		if (stack.getItem() == TragicItems.HuntersLegs)
		{
			return "tragicmc:textures/armor/Hunter2_lowRes.png";
		}
		return "tragicmc:textures/armor/Hunter1_lowRes.png";
	}
}
