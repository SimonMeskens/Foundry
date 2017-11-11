package exter.foundry.integration.minetweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.Infuser")
public class MTInfuserHandler {
	public static class InfuserAction extends AddRemoveAction {

		IInfuserRecipe recipe;

		public InfuserAction(IInfuserRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		protected void add() {
			InfuserRecipeManager.instance.recipes.add(recipe);
		}

		@Override
		public String getDescription() {
			return String.format("( %s, %s ) -> %s", MTHelper.getFluidDescription(recipe.getInputFluid()), MTHelper.getItemDescription(recipe.getInput()), MTHelper.getFluidDescription(recipe.getOutput()));
		}

		@Override
		public String getRecipeType() {
			return "infuser";
		}

		@Override
		protected void remove() {
			InfuserRecipeManager.instance.recipes.remove(recipe);
		}
	}

	@ZenMethod
	static public void addRecipe(ILiquidStack output, ILiquidStack input, IIngredient substance, int energy) {
		IInfuserRecipe recipe = null;
		try {
			recipe = new InfuserRecipe(CraftTweakerMC.getLiquidStack(output), CraftTweakerMC.getLiquidStack(input), MTHelper.getIngredient(substance), energy);
		} catch (IllegalArgumentException e) {
			CraftTweakerAPI.logError("Invalid infuser recipe: " + e.getMessage());
			return;
		}
		CraftTweakerAPI.apply(new InfuserAction(recipe).action_add);
	}

	@ZenMethod
	static public void removeRecipe(ILiquidStack input, IItemStack substance) {
		IInfuserRecipe recipe = InfuserRecipeManager.instance.findRecipe(CraftTweakerMC.getLiquidStack(input), CraftTweakerMC.getItemStack(substance));
		if (recipe == null) {
			CraftTweakerAPI.logWarning("Infuser recipe not found.");
			return;
		}
		CraftTweakerAPI.apply(new InfuserAction(recipe).action_remove);
	}
}
