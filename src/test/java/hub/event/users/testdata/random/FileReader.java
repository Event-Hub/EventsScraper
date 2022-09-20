package hub.event.users.testdata.random;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {


    public List<String> readFile(String fileName){

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            return stream.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPathForResourceFile(String citiesFile) {
        ClassLoader classLoader = FileReader.class.getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(citiesFile)).getFile()).getAbsolutePath();

    }

}
