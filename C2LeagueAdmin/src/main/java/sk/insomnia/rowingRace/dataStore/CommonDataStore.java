package sk.insomnia.rowingRace.dataStore;

import sk.insomnia.rowingRace.dto.EnumEntityDto;
import sk.insomnia.rowingRace.so.EnumEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bobek on 7/16/2014.
 */
public final class CommonDataStore {

    private static final Map<Class<? extends EnumEntity>, List<? extends EnumEntityDto>> data = new HashMap();

    public static List<? extends EnumEntityDto> getValuesForClass(Class<? extends EnumEntity> clazz) throws NoDataForKeyException {
        if (data.containsKey(clazz)) {
            return data.get(clazz);
        }
        throw new NoDataForKeyException(String.format("Data store doesn't contain values for class %s", clazz.getCanonicalName()));
    }

    public static void registerValuesForClass(Class<? extends EnumEntity> clazz, List<? extends EnumEntityDto> values) {
        data.put(clazz, values);
    }
}
