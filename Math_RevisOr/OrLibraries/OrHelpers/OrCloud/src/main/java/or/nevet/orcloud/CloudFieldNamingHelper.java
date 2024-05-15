package or.nevet.orcloud;

public class CloudFieldNamingHelper {

    public static String replaceDotsWithSpaces(String identifier) {
        return identifier.replaceAll("\\.", " ");
    }

    public static String restoreDotsAfterReplacementWithSpaces(String identifier) {
        return identifier.replaceAll(" ", "\\.");
    }

}
