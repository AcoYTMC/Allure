package net.acoyt.allure.impl.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AcoYT
 */
public class AllureUtil {
    public static <T> List<T> flattenList(List<List<T>> lists) {
        List<T> tList = new ArrayList<>();
        for (List<T> list : lists) {
            tList.addAll(list);
        }

        return tList;
    }
}
