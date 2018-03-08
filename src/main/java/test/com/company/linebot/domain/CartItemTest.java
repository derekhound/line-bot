package test.com.company.linebot.domain; 

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import com.company.linebot.domain.CartItem;
import com.company.linebot.domain.Product;

public class CartItemTest {

    private CartItem cartItem;

    @Before
    public void before() throws Exception {
        cartItem = new CartItem("1");
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void cartItem_total_price_should_be_equal_to_product_unit_price_in_case_of_single_quantity() {
        // Arrange
        Product iphone = new Product("P1234", "iPhone 5s", new BigDecimal(500));
        cartItem.setProduct(iphone);

        // Act
        BigDecimal totalPrice = cartItem.getTotalPrice();

        // Assert
        Assert.assertEquals(iphone.getUnitPrice(), totalPrice);
    }

    @Test
    public void cartItem_total_price_should_be_equal_to_product_unit_price_in_case_of_single_quantity2() {
        // Arrange
        Product iphone = new Product("P1234", "iPhone 5s", new BigDecimal(500));
        cartItem.setProduct(iphone);

        // Act
        BigDecimal totalPrice = cartItem.getTotalPrice();

        // Assert
        Assert.assertEquals(iphone.getUnitPrice(), totalPrice);
    }
}
