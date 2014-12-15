package tragicneko.tragicmc.blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tragicneko.tragicmc.TragicBlocks;
import tragicneko.tragicmc.TragicItems;
import tragicneko.tragicmc.TragicTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockStarCrystal extends Block {

	private String[] subNames = new String[] {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "DarkGray", "Pink", "LimeGreen", "Yellow", "LightBlue",
			"Magenta", "Orange", "White"};
	private IIcon[] iconArray = new IIcon[subNames.length];

	public BlockStarCrystal() {
		super(Material.glass);
		this.setCreativeTab(TragicTabs.Survival);
		this.setResistance(10.0F);
		this.setHardness(2.0F);
		this.setStepSound(soundTypeGlass);
		this.setBlockName("tragicmc.starCrystal");
		this.lightValue = 15;
		this.lightOpacity = 2;
		this.setHarvestLevel("pickaxe", 0);
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random rand)
	{
		if (fortune > 0 && this.getItemDropped(0, rand, fortune) == Items.dye)
		{
			int j = rand.nextInt(fortune + 2) - 1;
			if (j < 0) j = 0;

			return this.quantityDropped(rand) * (j + 1);
		}
		else
		{
			return this.quantityDropped(rand);
		}
	}
	
	@Override
	public int quantityDropped(Random rand)
	{
		return 1 + rand.nextInt(4);
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int level)
	{
		return Items.dye;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta >= this.iconArray.length)
		{
			meta = this.iconArray.length - 1;
		}
		return this.iconArray[meta];
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		for (int i = 0; i < subNames.length; i++)
		{
			this.iconArray[i] = par1IconRegister.registerIcon("tragicmc:" + subNames[i] + "StarCrystal_lowRes");
		}
	}

	public int damageDropped(int par1)
	{
		return par1;
	}

	public void getSubBlocks(Item par1, CreativeTabs par2, List par3)
	{
		for (int i = 0; i < this.subNames.length; i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}

	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 1;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
	{
		return super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, 1 - p_149646_5_);
	}

}
