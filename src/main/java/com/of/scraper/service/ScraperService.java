package com.of.scraper.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.of.scraper.entity.Fish;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ScraperService {

    FishDataService dataService;

    /**
     * Scrapes data from scanatura.no catch reports page and stores it in a list
     * of Fish entities (each representing one fish that is submitted to page).
     * 
     * The function navigates to a webpage, selects specific options from dropdown
     * menus,
     * waits for the page to load, and then scrapes data from a table on the page.
     * The scraped data is stored in a list and saved to a database.
     * Currently hardcoded to scrape data for Beiarelva river.
     * 
     * @return A list of Data objects containing the scraped data.
     */

    public List<Fish> scrapeData() {
        // Initialize a list to store the scraped data
        List<Fish> dataList = new ArrayList<>();

        // Initialize the WebDriver
        WebDriver driver = initializeWebDriver();

        // Navigate to the target page
        navigateToPage(driver, "https://www.scanatura.no/fangstrapport/?type=0&lang=2");

        // Initialize the select element for "ELV" and select the value "1714"
        Select select = initializeSelect(driver, "ELV");
        select.selectByValue("1714");

        sleep(5000);

        // Get the current year
        int currentYear = getCurrentYear();

        // Initialize the select element for "AAR" and get its options
        Select yearSelect = initializeSelect(driver, "AAR");
        List<WebElement> yearOptions = yearSelect.getOptions();

        // Iterate over the year options in reverse order
        for (int i = (yearOptions.size() - 1); i >= 0; i--) {
            // Re-fetch the year options
            yearSelect = initializeSelect(driver, "AAR");
            yearOptions = yearSelect.getOptions();

            // Get the year value from the option
            int yearValue = Integer.parseInt(yearOptions.get(i).getAttribute("value"));

            // If the year value is less than or equal to the current year
            if (yearValue <= currentYear) {
                // Select the year value
                yearSelect.selectByValue(String.valueOf(yearValue));

                // Click the "btn_detalj" button
                clickButton(driver, "btn_detalj");

                // Initialize the select element for "Week" and get its options
                Select weekSelect = initializeSelect(driver, "Week");
                List<WebElement> weekOptions = weekSelect.getOptions();

                // Iterate over the week options starting from the third option
                // First two option contains summary data that we are not interested in
                for (int j = 2; j < weekOptions.size(); j++) {
                    // Re-find the select element and re-get the options
                    weekSelect = initializeSelect(driver, "Week");
                    weekOptions = weekSelect.getOptions();

                    // Get the value of the week option
                    String value = weekOptions.get(j).getAttribute("value");

                    // Select the week value
                    weekSelect.selectByValue(value);

                    sleep(5000);

                    // Now the page should be loaded with the selected option
                    // Find the "DataGrid3" table
                    Duration timeout = Duration.ofSeconds(10);
                    WebDriverWait wait = new WebDriverWait(driver, timeout); // wait up to 10 seconds
                    WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("DataGrid3")));

                    // Get all the rows in the table
                    List<WebElement> rows = table.findElements(By.tagName("tr"));

                    // Initialize a list to store the data for the week
                    List<Fish> weekData = new ArrayList<>();

                    // Iterate over the rows starting from the second row
                    // Ignore first row since its the header
                    for (int k = 1; k < rows.size(); k++) {
                        // Get the current row
                        WebElement row = rows.get(k);

                        // Get all the cells in the row
                        List<WebElement> cells = row.findElements(By.tagName("td"));

                        // Create a Data object from the cells
                        // and add it to the weekData list
                        Fish data = createData(cells);
                        weekData.add(data);
                    }
                    // Save the weekData list to the database
                    // and add it to the dataList list
                    dataService.saveAll(weekData);
                    dataList.addAll(weekData);
                }
            }
        }

        // Close the browser
        driver.quit();

        // Return the list of scraped data
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
            // If the current month is before November, consider the previous year.
            // Data at the webpage is usually for the previous year.
            currentYear--;
        }
        return currentYear;
    }

    private Fish createData(List<WebElement> cells) {
        Fish data = new Fish();

        data.setLocation("Beiarelva");

        String date = cells.get(0).getText();
        data.setDate(transformToLocalDate(date));

        String input = cells.get(1).getText();
        double weight = Double.parseDouble(input.replace(",", "."));
        data.setWeight((!Double.isNaN(weight)) ? weight : 0.0);

        String species = cells.get(2).getText();
        data.setSpecies((species != null && !species.isEmpty()) ? species : "N/A");

        String gear = cells.get(3).getText();
        data.setGear((gear != null && !gear.isEmpty()) ? gear : "N/A");

        String zone = cells.get(4).getText();
        data.setZone((zone != null && !zone.isEmpty()) ? zone : "N/A");

        String name = cells.get(5).getText();
        data.setName((name != null && !name.isEmpty()) ? name : "N/A");

        return data;
    }

    private LocalDate transformToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate;
        if (date != null && !date.isEmpty()) {
            localDate = LocalDate.parse(date, formatter);
        } else {
            localDate = LocalDate.of(1900, 01, 01); // default date
        }
        return localDate;
    }
}