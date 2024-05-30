package com.of.scraper.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import com.of.scraper.dto.WeekDTO;
import com.of.scraper.entity.Fish;

public class FilteringUtils {

    /**
     * Filters out the fishes that were caught out of season.
     * Currently by the salmon fishing season is from June 15th to August 31st.
     * Could be refactored to take the season as a parameter.
     *
     * @param fishData A list of fish data.
     * @return A list of fish data that were caught in season.
     */
    public static List<Fish> filterOutOffSeasonFishes(List<Fish> fishData) {
        return fishData.stream()
                .filter(fish -> {
                    LocalDate date = fish.getDate();
                    int month = date.getMonthValue();
                    int day = date.getDayOfMonth();
                    return (month > 6 || (month == 6 && day >= 15)) && (month < 8 || (month == 8 && day <= 31));
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns the three best weeks from the list of weekly statistics.
     * 
     * @param weeklyStats A list of WeekDTOs.
     * @return A list of the three best weeks.
     */

    /**
     * Returns the three best weeks from the list of weekly statistics.
     *
     * @param weeklyStats A list of WeekDTOs.
     * @return A list of the three best weeks sorted in descending order by count.
     */
    public static List<WeekDTO> getBestWeeks(List<WeekDTO> weeklyStats) {
        PriorityQueue<WeekDTO> queue = new PriorityQueue<>(Comparator.comparing(WeekDTO::getCount));
        for (WeekDTO week : weeklyStats) {
            queue.add(week);
            if (queue.size() > 3) {
                queue.poll();
            }
        }
        List<WeekDTO> result = new ArrayList<>(queue);
        result.sort(Comparator.comparing(WeekDTO::getCount, Comparator.reverseOrder()));
        return result;
    }
}
