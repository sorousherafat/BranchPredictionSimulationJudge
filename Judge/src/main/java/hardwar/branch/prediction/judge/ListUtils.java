package hardwar.branch.prediction.judge;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class ListUtils {
    public static <T> long countEqualElements(List<T> firstList, List<T> secondList) {
        return IntStream.range(0, Math.min(firstList.size(), secondList.size()))
                .filter(i -> firstList.get(i).equals(secondList.get(i)))
                .count();
    }

    public static <T> List<T> readListFromFile(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<T> list = null;
        try {
            list = objectMapper.readValue(file, new TypeReference<List<T>>() {
            });
        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        return list;
    }
}
