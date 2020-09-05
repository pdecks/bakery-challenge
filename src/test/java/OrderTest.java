import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public class OrderTest {
    private static Bakeshop bakeshop;
    private static boolean setUpComplete = false;

    // run via JUnit tests
    // @Before to setup bakeshop
    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        if (setUpComplete) return;
        System.out.println("Instantiating bakeshop ...\n");
        bakeshop = new Bakeshop();

        System.out.println("Generating inventory from JSON file ... ");
        bakeshop.generateInventoryFromJsonFile();

        bakeshop.printInventory();

        System.out.println("Setting promotional offers ... ");
        bakeshop.setPromotionalOffers();

        System.out.println("\nSetup complete.\n");
    }

    // Case 1: Without any sales active, order 1 x Cookie, 4 x Brownie, 1 x Cheesecake.
    // Verify the total is $16.25.
    @Test
    public void testNoSalesCookieBrownieCheesecake() {
        Order order = bakeshop.startNewOrder(LocalDate.of(2020, 9, 3)); // Thursday

        // Treat Ids -> Brownie -> 1, Cheesecake ->2 , Cookie -> 3, Donut -> 4
        order.addTreat(bakeshop.getInventory().getTreatById(3), 1); // 1 x Cookie -> $1.25 (regular)
        order.addTreat(bakeshop.getInventory().getTreatById(1), 4); // 4 x Brownie -> $7.00 (bulk)
        order.addTreat(bakeshop.getInventory().getTreatById(2), 1); // 1 x Cheesecake -> $8.00 (regular)

        float actual = order.getOrderTotal();
        float expected = 16.25f;
        Assert.assertTrue(expected == actual);
    }

    // Case 2: Without any sales active, order 8 x Cookie.
    // Verify the total is $8.50
    @Test
    public void testNoSalesCookiesBulkAndRegular() {
        Order order = bakeshop.startNewOrder(LocalDate.of(2020, 9, 3)); // Thursday

        // Treat Ids -> Brownie -> 1, Cheesecake ->2 , Cookie -> 3, Donut -> 4
        order.addTreat(bakeshop.getInventory().getTreatById(3), 8); // 1 x Cookie -> $1.25 (regular)

        float actual = order.getOrderTotal();
        float expected = 8.50f;
        Assert.assertTrue(expected == actual);
    }

    // Case 3: Without any sales active, order 1 x Cookie, 1 x Brownie, 1 x Cheesecake, 2 x Donut.
    // Verify the total is $12.25
    // expect the donuts to be regularly priced, not the 2-for-1 on Tuesdays price
    @Test
    public void testAllItemsRegularlyPriced() {
        Order order = bakeshop.startNewOrder(LocalDate.of(2020, 9, 3)); // Thursday

        // Treat Ids -> Brownie -> 1, Cheesecake ->2 , Cookie -> 3, Donut -> 4
        order.addTreat(bakeshop.getInventory().getTreatById(3), 1); // 1 x Cookie -> $1.25 (regular)
        order.addTreat(bakeshop.getInventory().getTreatById(1), 1); // 1 x Brownie -> $6.00 (regular)
        order.addTreat(bakeshop.getInventory().getTreatById(2), 1); // 1 x Cheesecake -> $8.00 (regular)
        order.addTreat(bakeshop.getInventory().getTreatById(4), 2); // 2 x Donut -> $1.00 (regular)


        float actual = order.getOrderTotal();
        float expected = 12.25f;
        Assert.assertTrue(expected == actual);
    }

    // Case 4: On 10/1/2021, order 8 x Cookies, 4 x Cheesecakes.
    // Verify the total is $30.00
    // -> date falls on a Friday AND is an annual promotion,
    //    so we expect the cookie AND cheesecake date promotions to apply
    @Test
    public void testCookiePromoAndCheesecakePromo() {
        Order order = bakeshop.startNewOrder(LocalDate.of(2021, 10, 1)); // Friday

        // Treat Ids -> Brownie -> 1, Cheesecake ->2 , Cookie -> 3, Donut -> 4
        order.addTreat(bakeshop.getInventory().getTreatById(3), 8); // 8 x Cookie on Friday -> $6.00 (day-of-week promo)
        order.addTreat(bakeshop.getInventory().getTreatById(2), 4); // 4 x Cheesecake -> $24.00 (annual 25% off)

        float actual = order.getOrderTotal();
        float expected = 30.00f;
        Assert.assertTrue(expected == actual);
    }

    // ** Additional Case for Donuts on Tuesdays: On 9/1/2020 order 4 x donuts **
    // Verify the total is $1.00
    @Test
    public void testDonutsHalfOffOnTuesdaysPromo() {
        Order order = bakeshop.startNewOrder(LocalDate.of(2020, 9, 1)); // Tuesday

        // Treat Ids -> Brownie -> 1, Cheesecake ->2 , Cookie -> 3, Donut -> 4
        order.addTreat(bakeshop.getInventory().getTreatById(4), 4); // 8 x Donutes on Tuesday -> $4.00 (day-of-week promo)

        float actual = order.getOrderTotal();
        float expected = 1.00f;
        Assert.assertTrue(expected == actual);
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(OrderTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        if (result.wasSuccessful()) {
            System.out.println("All tests passed.");
        }
    }
}
