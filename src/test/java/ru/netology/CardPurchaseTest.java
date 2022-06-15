package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardPurchaseTest {
    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldGetSuccessMessage() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Концевич Сергей");
        fieldElements.get(1).sendKeys("+79245632145");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.className("paragraph")).getText();
        assertEquals("  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);
    }

    @Test
    void shouldGetValidationMessageWhenFioIsEmpty() {
        driver.get("http://localhost:9999");
        driver.findElement(By.className("button")).click();
        List<WebElement> fieldElements = driver.findElements(By.className("input__sub"));
        String text = fieldElements.get(0).getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneIsEmpty() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Концевич Сергей");
        driver.findElement(By.className("button")).click();
        List<WebElement> valMessagesElements = driver.findElements(By.className("input__sub"));
        String text = valMessagesElements.get(1).getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void shouldGetValidationMessageWhenCheckboxDisabled() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Концевич Сергей");
        fieldElements.get(1).sendKeys("+79245632145");
        driver.findElement(By.className("button")).click();
        String color = driver.findElement(By.className("input_invalid")).getCssValue("color");
        assertEquals("rgba(255, 92, 92, 1)", color);
    }

    @Test
    void shouldGetValidationMessageWhenFioWithNumbers() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Концевич Сергей123");
        driver.findElement(By.className("button")).click();
        List<WebElement> valMessagesElements = driver.findElements(By.className("input__sub"));
        String text = valMessagesElements.get(0).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void shouldGetValidationMessageWhenFioWithSymbols() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Концевич Сергей@#@$");
        driver.findElement(By.className("button")).click();
        List<WebElement> valMessagesElements = driver.findElements(By.className("input__sub"));
        String text = valMessagesElements.get(0).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void shouldGetValidationMessageWhenFioWithLatin() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Contevici Sergiu");
        driver.findElement(By.className("button")).click();
        List<WebElement> valMessagesElements = driver.findElements(By.className("input__sub"));
        String text = valMessagesElements.get(0).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneWithoutPlus() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Концевич Сергей");
        fieldElements.get(1).sendKeys("79245632145");
        driver.findElement(By.className("button")).click();
        List<WebElement> valMessagesElements = driver.findElements(By.className("input__sub"));
        String text = valMessagesElements.get(1).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneWhenLessThan11() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Концевич Сергей");
        fieldElements.get(1).sendKeys("+7924563214");
        driver.findElement(By.className("button")).click();
        List<WebElement> valMessagesElements = driver.findElements(By.className("input__sub"));
        String text = valMessagesElements.get(1).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneWhenMoreThan11() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Концевич Сергей");
        fieldElements.get(1).sendKeys("+792456321456");
        driver.findElement(By.className("button")).click();
        List<WebElement> valMessagesElements = driver.findElements(By.className("input__sub"));
        String text = valMessagesElements.get(1).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneWhenAnotherSymbol() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Концевич Сергей");
        fieldElements.get(1).sendKeys("*79245632145");
        driver.findElement(By.className("button")).click();
        List<WebElement> valMessagesElements = driver.findElements(By.className("input__sub"));
        String text = valMessagesElements.get(1).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneWhenPlusInTheEnd() {
        driver.get("http://localhost:9999");
        List<WebElement> fieldElements = driver.findElements(By.className("input__control"));
        fieldElements.get(0).sendKeys("Концевич Сергей");
        fieldElements.get(1).sendKeys("79245632145+");
        driver.findElement(By.className("button")).click();
        List<WebElement> valMessagesElements = driver.findElements(By.className("input__sub"));
        String text = valMessagesElements.get(1).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }
}
