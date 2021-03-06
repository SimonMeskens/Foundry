package exter.foundry.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.integration.ModIntegrationCrafttweaker;
import exter.foundry.recipes.BurnerHeaterFuel;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.BurnerHeater")
public class CrTBurnerFuelHandler
{
    public static class BurnerFuelAction extends AddRemoveAction
    {

        IBurnerHeaterFuel fuel;

        public BurnerFuelAction(IBurnerHeaterFuel fuel)
        {
            this.fuel = fuel;
        }

        @Override
        protected void add()
        {
            BurnerHeaterFuelManager.INSTANCE.addFuel(fuel);
        }

        @Override
        public String getDescription()
        {
            return String.format("%s -> (Time: %s | Heat: %s)", CrTHelper.getItemDescription(fuel.getFuel()),
                    fuel.getBurnTime(), fuel.getHeat());
        }

        @Override
        public String getRecipeType()
        {
            return "burner heater fuel";
        }

        @Override
        protected void remove()
        {
            BurnerHeaterFuelManager.INSTANCE.removeFuel(fuel);
        }
    }

    @ZenMethod
    public static void addFuel(IIngredient fuel, int time, int heat)
    {
        ModIntegrationCrafttweaker.queueAdd(() -> {
            CraftTweakerAPI.apply(
                    new BurnerFuelAction(new BurnerHeaterFuel(CrTHelper.getIngredient(fuel), time, heat)).action_add);
        });
    }

    @ZenMethod
    public static void removeFuel(IItemStack stack)
    {
        ModIntegrationCrafttweaker.queueRemove(() -> {
            IBurnerHeaterFuel fuel = null;
            for (IBurnerHeaterFuel f : BurnerHeaterFuelManager.INSTANCE.getFuels())
            {
                if (f.getFuel().apply(CraftTweakerMC.getItemStack(stack)))
                {
                    fuel = f;
                    break;
                }
            }

            if (fuel != null)
                CraftTweakerAPI.apply(new BurnerFuelAction(fuel).action_remove);
            else
                CrTHelper.printCrt("No burner fuel found for " + stack);
        });
    }

    @ZenMethod
    public static void clear()
    {
        ModIntegrationCrafttweaker.queueClear(BurnerHeaterFuelManager.INSTANCE.getFuels());
    }
}
