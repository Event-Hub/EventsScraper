package hub.event.users.testdata.random;

import hub.event.users.filter.dto.FilterDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

    private final FileReader fileReader;
    private final List<String> musicTypes;

    public Random(FileReader fileReader) {
        this.fileReader = fileReader;
        musicTypes = Arrays.asList("Rock", "Pop", "Jazz | Blues", "Metal", "Elektro | Techno", "Recital | Poezja śpiewana", "Hip Hop | Rap", "Disco Polo", "Etno");
    }

    public int getRandomNum(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }


    public String getRandomCity(String citiesFile) {

        String path = fileReader.getPathForResourceFile(citiesFile);
        List<String> cities = fileReader.readFile(path);
        int min = 0;
        int max = cities.size() - 1;
        int randomNum = getRandomNum(min, max);
        return cities.get(randomNum);
    }

    public String getRandomMusicType() {
        int randomNum = getRandomNum(0, musicTypes.size() - 1);
        return musicTypes.get(randomNum);
    }


        public LocalDateTime getRandomLocalDateTime() {
        int year = getRandomNum(2020, 2025);
        int month = getRandomNum(1, 12);
        int dayOfMonth = getRandomNum(1, 28);
        int hour = getRandomNum(0, 23);
        int minute = getRandomNum(0, 59);
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute);

    }

    public FilterDto randomFilter() {
//        Long filterId = (long) getRandomNum(1, 100);
        Long filterId = null;
        Long cityId = (long) getRandomNum(1,100);
        Long userId = (long) getRandomNum(1,100);
        String city = getRandomCity("cities.txt");
        String musicType = getRandomMusicType();
        String name = city + " - " + musicType;
        LocalDateTime hour1 = getRandomLocalDateTime();
        LocalDateTime hour2 = getRandomLocalDateTime();
        LocalDateTime fromHour;
        LocalDateTime toHour;
        if (hour1.isAfter(hour2)){
            toHour = hour1;
            fromHour = hour2;
        } else {
            fromHour = hour1;
            toHour = hour2;
        }

        return new FilterDto(filterId, cityId, userId, name, fromHour, toHour);

    }
}
