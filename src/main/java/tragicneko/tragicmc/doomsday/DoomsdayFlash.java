package tragicneko.tragicmc.doomsday;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import tragicneko.tragicmc.TragicBlocks;
import tragicneko.tragicmc.doomsday.Doomsday.IExtendedDoomsday;
import tragicneko.tragicmc.properties.PropertyDoom;
import tragicneko.tragicmc.util.WorldHelper;

public class DoomsdayFlash extends Doomsday implements IExtendedDoomsday {

	public DoomsdayFlash(int id) {
		super(id, EnumDoomType.WORLDSHAPER);
	}

	@Override
	public void doInitialEffects(DoomsdayEffect effect, PropertyDoom doom, EntityPlayer player, boolean crucMoment) {
		double d0 = crucMoment ? 8.25D : 4.0D;
		effect.utilityList = WorldHelper.getBlocksInSphericalRange(player.worldObj, d0, player.posX, player.posY, player.posZ);

		player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "You have used Flash!"));
		if (crucMoment) player.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Crucial Moment!"));
	}

	@Override
	public void useDoomsday(DoomsdayEffect effect, PropertyDoom doom, EntityPlayer player, boolean crucMoment) {

		for (int i = 0; i < effect.utilityList.size(); i++)
		{
			int[] coord = (int[]) effect.utilityList.get(i);
			Block block = player.worldObj.getBlock(coord[0], coord[1], coord[2]);
			if (block == Blocks.air || block == TragicBlocks.Luminescence) player.worldObj.setBlock(coord[0], coord[1], coord[2], TragicBlocks.Luminescence);
		}
	}

	@Override
	public void doBacklashEffect(PropertyDoom doom, EntityPlayer player) {
		
	}

}
