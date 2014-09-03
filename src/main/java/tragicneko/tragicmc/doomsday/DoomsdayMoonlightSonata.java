package tragicneko.tragicmc.doomsday;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import tragicneko.tragicmc.properties.PropertyDoom;

public class DoomsdayMoonlightSonata extends Doomsday {

	public DoomsdayMoonlightSonata(int id, int cd, int reqDoom) {
		super(id, cd, reqDoom, EnumDoomType.ULTIMATE);
	}

	@Override
	public void useDoomsday(PropertyDoom doom, EntityPlayer player, boolean crucMoment, boolean griefCheck) {
		
		long time = player.worldObj.getWorldTime();
		if (time >= 15000 && time <= 17000 && player.worldObj.canBlockSeeTheSky((int) player.posX, (int) player.posY, (int) player.posZ))
		{
			doom.fillDoom();
			if (!player.capabilities.isCreativeMode || crucMoment) this.applyDoomAndCooldown(doom);
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_BLUE + "You have used Moonlight Sonata!"));
			
			if (crucMoment)
			{
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Crucial Moment!"));
			}
		}
		else
		{
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "Not the proper time to use that..."));
		}
		
	}

	@Override
	public void useDoomsdayThroughCommand(PropertyDoom doom, EntityPlayer player, boolean crucMoment, boolean griefCheck) {
		long time = player.worldObj.getWorldTime();
		if (time >= 15000 && time <= 17000 && player.worldObj.canBlockSeeTheSky((int) player.posX, (int) player.posY - 1, (int) player.posZ))
		{
			doom.fillDoom();
			if (!player.capabilities.isCreativeMode) this.applyDoomAndCooldown(doom);
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_BLUE + "You have used Moonlight Sonata!"));
			
			if (crucMoment)
			{
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Crucial Moment!"));
			}
		}
		else
		{
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "Not the proper time to use that..."));
		}
	}

	@Override
	public void doBacklashEffect(PropertyDoom doom, EntityPlayer player, boolean griefCheck) {
		
	}

}
