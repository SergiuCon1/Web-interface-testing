package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldGetSuccessMessage() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Концевич-Иванов Сергей");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79245632145");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=\"order-success\"]")).getText();
        assertEquals("  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);
    }

    @Test
    void shouldGetValidationMessageWhenFioIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79245632145");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Концевич-Иванов Сергей");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void shouldGetValidationMessageWhenCheckboxDisabled() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Концевич-Иванов Сергей");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79245632145");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text);
    }

    @Test
    void shouldGetValidationMessageWhenFioWithNumbers() {
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79245632145");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Концевич Сергей123");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void shouldGetValidationMessageWhenFioWithSymbols() {
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79245632145");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Концевич Сергей@#@$");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void shouldGetValidationMessageWhenFioWithLatin() {
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79245632145");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Contevici Sergiu");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Концевич-Иванов Сергей");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("79245632145");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneWhenLessThan11() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Концевич-Иванов Сергей");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+7924563214");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneWhenMoreThan11() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Концевич-Иванов Сергей");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+792456321456");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneWhenAnotherSymbol() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Концевич-Иванов Сергей");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("*79245632145");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldGetValidationMessageWhenPhoneWhenPlusInTheEnd() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Концевич-Иванов Сергей");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("79245632145+");
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }
}
