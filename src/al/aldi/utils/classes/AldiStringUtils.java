package al.aldi.utils.classes;

public class AldiStringUtils {

    public static String arrayToString(String[] strArray) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < strArray.length; i++) {
            result.append(strArray[i]);
            if (i != strArray.length - 1) {
            }
        }
        String concatString = result.toString();
        return concatString;

    }


    /**
     * Joins the array with a suffix
     *
     * @param strArray the array to be joined
     * @param separator - the suffix to be added after the token. It is not added in the last token.
     * @return joined array
     */
    public static String arrayToString(String[] strArray, String separator) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < strArray.length; i++) {
            result.append(strArray[i]);
            if (i != strArray.length - 1) {
                result.append(separator);
            }
        }
        String concatString = result.toString();
        return concatString;

    }

    /**
     * Joins the array with a prefix and suffix
     *
     * @param strArray the array to be joined
     * @param prefix - the prefix to be added before the array token before joined
     * @param separator - the suffix to be added after the token. It is not added in the last token.
     * @return joined array
     */
    public static String arrayToString(String[] strArray, String prefix, String separator) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < strArray.length; i++) {
            result.append(prefix);
            result.append(strArray[i]);
            if (i != strArray.length - 1) {
                result.append(separator);
            }
        }
        String concatString = result.toString();
        return concatString;

    }
    
    /**
     * Checks if the string starts with HTTP or HTTPS
     * 
     * Like http:// or https://
     * 
     * @param string
     * @return
     */
    public static boolean startsWithHttpS(String string) {
        return string.startsWith("http://") || string.startsWith("https://");
    }
}
