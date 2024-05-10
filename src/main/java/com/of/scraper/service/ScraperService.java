package com.of.scraper.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import com.of.scraper.entity.Data;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ScraperService {

    DataService dataService;

    public List<Data> scrapeData() {
        List<Data> dataList = new ArrayList<>();
        WebDriver driver = initializeWebDriver();
        navigateToPage(driver, "https://www.scanatura.no/fangstrapport/?type=0&lang=2");

        Select select = initializeSelect(driver, "ELV");
        select.selectByValue("1714");
        sleep(5000);

        // Get the current year
        int currentYear = getCurrentYear();

        Select yearSelect = initializeSelect(driver, "AAR");
        List<WebElement> yearOptions = yearSelect.getOptions();

        for (int i = (yearOptions.size() - 1); i >= 0; i--) {
            // Re-fetch the year options
            yearSelect = initializeSelect(driver, "AAR");
            yearOptions = yearSelect.getOptions();

            // Get the year value from the option
            int yearValue = Integer.parseInt(yearOptions.get(i).getAttribute("value"));

            if (yearValue <= currentYear) {
                yearSelect.selectByValue(String.valueOf(yearValue));

                clickButton(driver, "btn_detalj");

                Select weekSelect = initializeSelect(driver, "Week");
                List<WebElement> weekOptions = weekSelect.getOptions();

                for (int j = 2; j < weekOptions.size(); j++) {
                    // Re-find the select element and re-get the options
                    weekSelect = initializeSelect(driver, "Week");
                    weekOptions = weekSelect.getOptions();

                    String value = weekOptions.get(j).getAttribute("value");
                    weekSelect.selectByValue(value);
                    sleep(5000);

                    // Now the page should be loaded with the selected option
                    WebElement table = driver.findElement(By.id("DataGrid3"));

                    List<WebElement> rows = table.findElements(By.tagName("tr"));

                    for (int k = 1; k < rows.size(); k++) {
                        WebElement row = rows.get(k);

                        List<WebElement> cells = row.findElements(By.tagName("td"));

                        Data data = createData(cells);

                        Data savedData = dataService.saveData(data);
                        dataList.add(savedData);
                    }
                }
            }
        }
        // Close the browser
        driver.quit();

        return dataList;
    }

    private WebDriver initializeWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }

    private void navigateToPage(WebDriver driver, String url) {
        try {
            driver.get(url);
        } catch (WebDriverException e) {
            System.out.println("Failed to load page: " + url);
            e.printStackTrace();
        }
    }

    private Select initializeSelect(WebDriver driver, String id) {
        Duration timeout = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, timeout); // wait up to 10 seconds
        WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
        return new Select(selectElement);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void clickButton(WebDriver driver, String id) {
        Duration timeout = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
        button.click();
    }

    private int getCurrentYear() {
        int currentYear = LocalDate.now().getYear();
        if (LocalDate.now().getMonthValue() < 11) {
            currentYear--;
        }
        return currentYear;
    }

    private Data createData(List<WebElement> cells) {
        Data data = new Data();

        String date = cells.get(0).getText();
        if (date != null && !date.isEmpty()) {
            data.setDate(date);
        }

        String input = cells.get(1).getText();
        double weight = Double.parseDouble(input.replace(",", "."));
        if (!Double.isNaN(weight)) {
            data.setWeight(weight);
        }

        String species = cells.get(2).getText();
        if (species != null && !species.isEmpty()) {
            data.setSpecies(species);
        }

        String gear = cells.get(3).getText();
        if (gear != null && !gear.isEmpty()) {
            data.setGear(gear);
        }

        String zone = cells.get(4).getText();
        if (zone != null && !zone.isEmpty()) {
            data.setZone(zone);
        }

        String name = cells.get(5).getText();
        if (name != null && !name.isEmpty()) {
            data.setName(name);
        }
        return data;
    }
}