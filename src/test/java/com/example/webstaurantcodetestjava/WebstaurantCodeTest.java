package com.example.webstaurantcodetestjava;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebstaurantCodeTest {
    static WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://www.webstaurantstore.com/");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void searchForTablesAndEmptyCart() {
        // Search for stainless work table
        searchForProduct("stainless work table");

        assertAllSearchResultsOnPageAreTables();

        addLastItemToCart();

        boolean cartIsEmpty = emptyCart();

        assertTrue(cartIsEmpty, "The test failed because the cart is not empty");
    }

    private static void assertAllSearchResultsOnPageAreTables() {
        int currentPage = 1;
        boolean finalPage = false;

        while(!finalPage) {
            WebElement searchResults = driver.findElement(By.id("product_listing"));

            List<WebElement> descriptions = searchResults.findElements(By.cssSelector("[data-testid='itemDescription']"));

            for(WebElement description : descriptions) {
                assertTrue(description.getText().contains("Table"), description.getText() + " is not a table");
            }

            currentPage++;
            String nextPageButtonSelector = "[aria-label='go to page " + currentPage + "']";
            finalPage = driver.findElements(By.cssSelector(nextPageButtonSelector)).isEmpty();

            if(!finalPage) {
                WebElement nextPage = driver.findElement(By.cssSelector(nextPageButtonSelector));
                nextPage.click();
            }
        }
    }

    private static void searchForProduct(String productName)
    {
        WebElement searchBar = driver.findElement(By.id("searchval"));
        WebElement searchBarSubmit = driver.findElement(By.cssSelector("[value='Search']"));

        searchBar.sendKeys(productName);
        searchBarSubmit.click();
    }

    private  static void addLastItemToCart() {
        List <WebElement> products = driver.findElements(By.className("product-box-container"));

        WebElement addToCartButton = products.getLast().findElement(By.name("addToCartButton"));

        addToCartButton.click();
    }

    private static boolean emptyCart() {
        WebElement cartButton = driver.findElement(By.cssSelector("[data-testid='cart-button']"));
        cartButton.click();

        WebElement emptyCartButton = driver.findElement(By.className("emptyCartButton"));
        emptyCartButton.click();

        WebElement confirmEmptyCartButton = driver.findElement(By.cssSelector("button[class*='mr-2']"));
        confirmEmptyCartButton.click();

        return driver.findElements(By.className("cartEmpty")).isEmpty();
    }
}
