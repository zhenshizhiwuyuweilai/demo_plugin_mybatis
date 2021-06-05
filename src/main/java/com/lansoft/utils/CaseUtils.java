package com.lansoft.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author guowd
 * @date 2021/6/6  2:29
 */
public class CaseUtils {

    public CaseUtils() {
    }

    public static String toCamelCase(String str, boolean capitalizeFirstLetter, char... delimiters) {
        if (StringUtils.isEmpty(str)) {
            return str;
        } else {
            str = str.toLowerCase();
            int strLen = str.length();
            int[] newCodePoints = new int[strLen];
            int outOffset = 0;
            Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
            boolean capitalizeNext = false;
            if (capitalizeFirstLetter) {
                capitalizeNext = true;
            }

            int index = 0;

            while(true) {
                while(index < strLen) {
                    int codePoint = str.codePointAt(index);
                    if (delimiterSet.contains(codePoint)) {
                        capitalizeNext = outOffset != 0;
                        index += Character.charCount(codePoint);
                    } else if (capitalizeNext || outOffset == 0 && capitalizeFirstLetter) {
                        int titleCaseCodePoint = Character.toTitleCase(codePoint);
                        newCodePoints[outOffset++] = titleCaseCodePoint;
                        index += Character.charCount(titleCaseCodePoint);
                        capitalizeNext = false;
                    } else {
                        newCodePoints[outOffset++] = codePoint;
                        index += Character.charCount(codePoint);
                    }
                }

                if (outOffset != 0) {
                    return new String(newCodePoints, 0, outOffset);
                }

                return str;
            }
        }
    }

    private static Set<Integer> generateDelimiterSet(char[] delimiters) {
        Set<Integer> delimiterHashSet = new HashSet();
        delimiterHashSet.add(Character.codePointAt(new char[]{' '}, 0));
        if (ArrayUtils.isEmpty(delimiters)) {
            return delimiterHashSet;
        } else {
            for(int index = 0; index < delimiters.length; ++index) {
                delimiterHashSet.add(Character.codePointAt(delimiters, index));
            }

            return delimiterHashSet;
        }
    }
}
