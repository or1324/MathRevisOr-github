package or.nevet.orgeneralhelpers;

import java.util.Arrays;
import java.util.HashSet;

public class CommonArrayAlgorithms {
    //O(n)
    public static boolean arrayContainsElement(Object[] arr, Object target) {
        for (Object o : arr) {
            if (o.equals(target))
                return true;
        }
        return false;
    }

    //O(n+m) where n=parentLength and m=childLength
    public static boolean arrayContainsAllArrayValues(Object[] parent, Object[] child) {
        //O(n)
        HashSet<Object> parentHashSet = new HashSet<>(Arrays.asList(parent));
        //O(m)
        for (Object o : child)
            if (!parentHashSet.contains(o))
                return false;
        return true;
    }

    //O(n+m) where n=parentLength and m=stringLength
    public static boolean charArrayContainsAllStringCharacters(char[] parent, String str) {
        Character[] stringCharacters = new Character[str.length()];
        for (int i = 0; i < str.length(); i++)
            stringCharacters[i] = str.charAt(i);
        Character[] parentCharacters = new Character[parent.length];
        for (int i = 0; i < parent.length; i++)
            parentCharacters[i] = parent[i];
        return arrayContainsAllArrayValues(parentCharacters, stringCharacters);
    }
}
