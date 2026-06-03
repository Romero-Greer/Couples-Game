package game.logic;

public class StringSimilarity {

    public static boolean isSimilarEnough(String a, String b) {
        String normA = normalize(a);
        String normB = normalize(b);

        if (normA.equals(normB)) {
            return true;
        }

        int maxLength = Math.max(normA.length(), normB.length());
        if (maxLength == 0) {
            return true;
        }

        int distance = levenshteinDistance(normA, normB);
        double similarity = 1.0 - ((double) distance / maxLength);
        return similarity >= 0.50;
    }

    private static String normalize(String s) {
        return s.toLowerCase().trim().replaceAll("\\s+", " ");
    }

    private static int levenshteinDistance(String a, String b) {
        int lenA = a.length();
        int lenB = b.length();
        int[][] dp = new int[lenA + 1][lenB + 1];

        for (int i = 0; i <= lenA; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= lenB; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= lenA; i++) {
            for (int j = 1; j <= lenB; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                                   Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        return dp[lenA][lenB];
    }
}
