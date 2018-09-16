package casm.gis.util;

public class CharacterSimilarityUtils {
    public static int compare(String str, String target) {
        int d[][]; // matrix
        int n = str.length();
        int m = target.length();
        int i; // Traversing str
        int j; // Traversing target
        char ch1; // str
        char ch2; // target
        int temp; // Record the same character, the increment of the value in a matrix position, not 0 or 1
        
        if (n == 0) {
            return m;
        }
        
        if (m == 0) {
            return n;
        }
        
        d = new int[n + 1][m + 1];
        
        for (i = 0; i <= n; i++) { // Initialize the first line
            d[i][0] = i;
        }
        
        for (j = 0; j <= m; j++) { // Initialize the first line
            d[0][j] = j;
        }
        
        for (i = 1; i <= n; i++) { // Traversing str
            ch1 = str.charAt(i - 1);
            // Go to match target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                
                // +1 on the left, +1 on the top, +temp in the upper left corner
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        
        return d[n][m];
    }
    
    public static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }
    
    /**
     * Get the similarity of two strings
     * 
     * @param str
     * @param target
     * 
     * @return
     */
    
    public static float getSimilarityRatio(String str, String target) {
        return 1 - (float) compare(str, target) / Math.max(str.length(), target.length());
        
    }
}
