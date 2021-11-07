package TeXCalc.util;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {

    /**
     * Examples
     */
    public static void main(String[] args) {
        System.out.println(diff("this is ax example", "th2is i1s a eaaxamp")); // prints (le,)
        System.out.println(diff("Honda", "Hyundai")); // prints (o,yui)
        System.out.println(diff("Toyota", "Coyote")); // prints (Ta,Ce)
        System.out.println(diff("Flomax", "Volmax")); // prints (Fo,Vo)
    }

    /**
     * Returns a minimal set of characters that have to be removed from (or added to) the respective
     * strings to make the strings equal.
     */
    public static Pair<String> diff(String a, String b) {
        return diffHelper(a, b, new HashMap<>());
    }

    /**
     * Recursively compute a minimal set of characters while remembering already computed substrings.
     * Runs in O(n^2).
     */
    private static Pair<String> diffHelper(String a, String b, Map<Long, Pair<String>> lookup) {
        long key = ((long) a.length()) << 32 | b.length();
        if (!lookup.containsKey(key)) {
            Pair<String> value;
            if (a.isEmpty() || b.isEmpty()) {
                value = new Pair<>(a, b);
            } else if (a.charAt(0) == b.charAt(0)) {
                value = diffHelper(a.substring(1), b.substring(1), lookup);
            } else {
                Pair<String> aa = diffHelper(a.substring(1), b, lookup);
                Pair<String> bb = diffHelper(a, b.substring(1), lookup);
                if (aa.first.length() + aa.second.length() < bb.first.length() + bb.second.length()) {
                    value = new Pair<>(a.charAt(0) + aa.first, aa.second);
                } else {
                    value = new Pair<>(bb.first, b.charAt(0) + bb.second);
                }
            }
            lookup.put(key, value);
        }
        return lookup.get(key);
    }

    public static class Pair<T> {
        public Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }

        public final T first, second;

        public String toString() {
            return "(" + first + "," + second + ")";
        }
    }
    public static int count(
    		  String someString, char searchedChar, int index) {
    		    if (index >= someString.length()) {
    		        return 0;
    		    }
    		    
    		    int count = someString.charAt(index) == searchedChar ? 1 : 0;
    		    return count + count(
    		      someString, searchedChar, index + 1);
    		}
}