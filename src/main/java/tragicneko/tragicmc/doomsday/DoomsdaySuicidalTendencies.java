package tragicneko.tragicmc.doomsday;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import tragicneko.tragicmc.entity.projectile.EntityNekoRocket;
import tragicneko.tragicmc.entity.projectile.EntityNekoStickyBomb;
import tragicneko.tragicmc.items.weapons.TragicWeapon;
import tragicneko.tragicmc.properties.PropertyDoom;

public class DoomsdaySuicidalTendencies extends Doomsday implements IExtendedDoomsday {

	public DoomsdaySuicidalTendencies(int id, int cd, int reqDoom) {
		super(id, cd, reqDoom, EnumDoomType.WORLDSHAPER);
		this.waitTime = 10;
		this.maxIterations = 5;
	}

	@Override
	public void doInitialEffects(PropertyDoom doom, EntityPlayer player, boolean crucMoment) {
		Vec3 vec = TragicWeapon.getVecFromPlayer(player, 30.0);

		if (vec == null)
		{
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "Doomsday needs to be aimed..."));
		}
		else
		{
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "You have used Suicidal Tendencies!"));

			if (crucMoment)
			{
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Crucial Moment!"));
			}
		}
	}

	@Override
	public void useDoomsday(PropertyDoom doom, EntityPlayer player,	boolean crucMoment) 
	{
		Vec3 vec = TragicWeapon.getVecFromPlayer(player, 30.0);
		if (vec == null) return;

		double d4 = vec.xCoord - player.posX;
		double d5 = vec.yCoord - (player.posY + (double)(player.height / 2.0F));
		double d6 = vec.zCoord - player.posZ;

		EntityNekoRocket rocket = new EntityNekoRocket(player.worldObj, player, d4, d5, d6);
		rocket.posX = player.posX + (d4 * 0.115D);
		rocket.posY = player.posY + player.getEyeHeight();
		rocket.posZ = player.posZ + (d6 * 0.115D);

		if (!player.worldObj.isRemote)
		{
			player.worldObj.spawnEntityInWorld(rocket);
		}
	}

	@Override
	public void doBacklashEffect(PropertyDoom doom, EntityPlayer player) {
		EntityNekoStickyBomb bomb = new EntityNekoStickyBomb(player.worldObj, player);
		bomb.setPosition(player.posX, player.posY, player.posZ);
		player.worldObj.spawnEntityInWorld(bomb);
	}

}
