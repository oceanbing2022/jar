package com.ohx.util;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author 贾榕福
 * @date 2022/12/19
 */
public class SortUtils {
    public static void sort(ArrayList list, String filedName, boolean ascFlag) {
        if (list.size() == 0 || filedName.equals("")) {
            return;
        }
        Comparator<?> cmp = ComparableComparator.getInstance();
        if (ascFlag) {
            cmp = ComparatorUtils.nullLowComparator(cmp);
        } else {
            cmp = ComparatorUtils.reversedComparator(cmp);

        }
        Collections.sort(list, new BeanComparator(filedName, cmp));
    }
}
