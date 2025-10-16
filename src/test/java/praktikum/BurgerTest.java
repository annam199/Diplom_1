package praktikum;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class BurgerTest {
    @RunWith(Parameterized.class)
    public static class BurgerParamTest {
        float price;
        ArrayList<Ingredient> ingredients;
        Bun bun;
        Burger burger;

        @Parameterized.Parameters
        public static Object[][] getParams() {
            Ingredient ingredientMock = mock(Ingredient.class);
            when(ingredientMock.getPrice()).thenReturn(250f);
            when(ingredientMock.getType()).thenReturn(IngredientType.FILLING);
            return new Object[][] {
                    {new Bun("Розовая", 100f), new ArrayList<>(), 200f},
                    {new Bun("Синяя", 100f), new ArrayList<>(List.of(ingredientMock, ingredientMock)), 700f}
            };
        }

        public BurgerParamTest(Bun bun, ArrayList<Ingredient> ingredients, float price) {
            this.bun = bun;
            this.ingredients = ingredients;
            this.price = price;
        }

        @Before
        public void prepareBurger() {
            burger = new Burger();
            burger.setBuns(this.bun);
            for (Ingredient ingredient : this.ingredients) {
                burger.addIngredient(ingredient);
            }
        }

        @Test
        public void testGetPrice() {
            assertEquals(this.price, burger.getPrice(), 0);
        }

        @Test
        public void testGetReceipt() {
            StringBuilder receipt = new StringBuilder(String.format("(==== %s ====)%n", bun.getName()));

            for (Ingredient ingredient : ingredients) {
                receipt.append(String.format("= %s %s =%n", ingredient.getType().toString().toLowerCase(),
                        ingredient.getName()));
            }

            receipt.append(String.format("(==== %s ====)%n", bun.getName()));
            receipt.append(String.format("%nPrice: %f%n", burger.getPrice()));

            assertEquals(receipt.toString(), burger.getReceipt());
        }
    }

    public static class BurgerSingleTest {
        Bun bunMock = mock(Bun.class);
        Ingredient ingredientObj = mock(Ingredient.class);

        @Test
        public void testSetBuns() {
            Burger burgerObj = new Burger();
            burgerObj.setBuns(bunMock);
            assertEquals(bunMock, burgerObj.bun);
        }

        @Test
        public void testAddIngredient() {
            Burger burgerObj = new Burger();
            burgerObj.addIngredient(ingredientObj);
            assertEquals(1, burgerObj.ingredients.size());
            burgerObj.addIngredient(ingredientObj);
            assertEquals(2, burgerObj.ingredients.size());
        }

        @Test
        public void testRemoveIngredient() {
            Burger burgerObj = new Burger();
            Ingredient ingredientFirstObj = mock(Ingredient.class);
            Ingredient ingredientSecondObj = mock(Ingredient.class);
            burgerObj.addIngredient(ingredientFirstObj);
            burgerObj.addIngredient(ingredientSecondObj);
            assertEquals(2, burgerObj.ingredients.size());
            burgerObj.removeIngredient(0);
            assertEquals(ingredientSecondObj, burgerObj.ingredients.get(0));
            assertEquals(1, burgerObj.ingredients.size());
            burgerObj.removeIngredient(0);
            assertEquals(0, burgerObj.ingredients.size());
        }

        @Test
        public void testMoveIngredient() {
            Burger burgerObj = new Burger();
            Ingredient ingredientFirstObj = mock(Ingredient.class);
            Ingredient ingredientSecondObj = mock(Ingredient.class);
            burgerObj.addIngredient(ingredientFirstObj);
            burgerObj.addIngredient(ingredientSecondObj);
            assertEquals(ingredientFirstObj, burgerObj.ingredients.get(0));
            assertEquals(ingredientSecondObj, burgerObj.ingredients.get(1));
            burgerObj.moveIngredient(1, 0);
            assertEquals(ingredientFirstObj, burgerObj.ingredients.get(1));
            assertEquals(ingredientSecondObj, burgerObj.ingredients.get(0));
        }
    }
}