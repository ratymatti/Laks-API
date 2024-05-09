package com.of.scraper.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.of.scraper.entity.Data;

@Service
public class ScraperService {

    public Data scrapeData() {

        // Create a ChromeOptions object
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        // Create a new instance of the Chrome driver with the options
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.scanatura.no/fangstrapport/?type=0&lang=2");

        // Find the select element
        WebElement selectElement = driver.findElement(By.id("ELV"));

        // Create a new Select object
        Select select = new Select(selectElement);

        // Select an option
        select.selectByValue("1714");

        // Wait for the page to load
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get the current year
        int currentYear = LocalDate.now().getYear();

        WebElement yearSelectElement = driver.findElement(By.id("AAR"));
        Select yearSelect = new Select(yearSelectElement);
        List<WebElement> yearOptions = yearSelect.getOptions();
        int numOfYears = yearOptions.size();

        // If the current date is before November, subtract one from the current year
        if (LocalDate.now().getMonthValue() < 11) {
            currentYear--;
            numOfYears--;
        }

        for (int i = numOfYears; i > 0; i--) {
            // Re-fetch the year options
            yearSelectElement = driver.findElement(By.id("AAR"));
            yearSelect = new Select(yearSelectElement);
            yearOptions = yearSelect.getOptions();

            // Get the year value from the option
            int yearValue = Integer.parseInt(yearOptions.get(i).getAttribute("value"));

            // Ignore the year 2024 and any years greater than the current year minus one
            if (yearValue <= currentYear) {
                // Select the year
                yearSelect.selectByValue(String.valueOf(yearValue));
                // Wait for the page to load
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Duration timeout = Duration.ofSeconds(10);
                WebDriverWait wait = new WebDriverWait(driver, timeout); // wait for up to 10 seconds
                WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn_detalj")));
                button.click();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // The rest of your code goes here...

                WebElement weekSelectElement = driver.findElement(By.id("Week"));
                Select weekSelect = new Select(weekSelectElement);
                List<WebElement> weekOptions = weekSelect.getOptions();

                for (int j = 2; j <= weekOptions.size(); j++) {
                    // Re-find the select element and re-get the options
                    weekSelectElement = driver.findElement(By.id("Week"));
                    weekSelect = new Select(weekSelectElement);
                    weekOptions = weekSelect.getOptions();

                    int weekNumber = Integer.parseInt(weekOptions.get(j).getAttribute("value"));

                    for (int k = 2; k < weekOptions.size(); k++) {
                        WebElement option = weekOptions.get(k);
                        String value = option.getAttribute("value");

                        // Convert the value to an integer
                        int optionWeekNumber = Integer.parseInt(value);

                        // Check if the week number is the current week number
                        if (optionWeekNumber == weekNumber) {
                            // Select the option
                            weekSelect.selectByValue(value);

                            // Wait for the page to load
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // Now the page should be loaded with the selected option
                            String text = driver.findElement(By.id("DataGrid3")).getText();
                            System.out.println(text);

                            // Break out of the inner loop
                            break;
                        }
                    }
                }
                // Close the browser
                driver.quit();
            }
        }
        return new Data();
    }
}