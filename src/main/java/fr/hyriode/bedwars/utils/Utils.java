package fr.hyriode.bedwars.utils;

import java.util.Set;

public class Utils {

    public static int getFirstElementOfInt(Set<Integer> set){
        for (Integer i : set) {
            return i;
        }
        return -1;
    }


}
