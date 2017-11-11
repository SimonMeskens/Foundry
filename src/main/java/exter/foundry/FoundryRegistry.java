package exter.foundry;

import java.util.List;

import exter.foundry.init.InitRecipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FoundryRegistry {

	public static final List<Block> BLOCKS = NonNullList.create();
	public static final List<Item> ITEMS = NonNullList.create();
	public static final List<SoundEvent> SOUNDS = NonNullList.create();
	public static final List<IRecipe> RECIPES = NonNullList.create();

	@SubscribeEvent
	public void registerBlocks(Register<Block> e) {
		e.getRegistry().registerAll(BLOCKS.toArray(new Block[BLOCKS.size()]));
	}

	@SubscribeEvent
	public void registerItems(Register<Item> e) {
		e.getRegistry().registerAll(ITEMS.toArray(new Item[ITEMS.size()]));
	}

	@SubscribeEvent
	public void registerRecipes(Register<IRecipe> e) {
		InitRecipes.preInit();
		e.getRegistry().registerAll(RECIPES.toArray(new IRecipe[RECIPES.size()]));
	}

	@SubscribeEvent
	public void registerSounds(Register<SoundEvent> e) {
		e.getRegistry().registerAll(SOUNDS.toArray(new SoundEvent[SOUNDS.size()]));
	}

}