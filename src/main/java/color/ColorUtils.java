package color;

public class ColorUtils {
    public static int mapToRange(int inputStart, int inputEnd, int outputStart, int outputEnd, int input) {
        return outputStart + ((outputEnd - outputStart) / (inputEnd - inputStart)) * (input - inputStart);
    }

    public static int mapToRange(double inputStart, double inputEnd, int outputStart, int outputEnd, double input) {
        return (int) (outputStart + ((outputEnd - outputStart) / (inputEnd - inputStart)) * (input - inputStart));
    }

    public static int mapToRange(float inputStart, float inputEnd, int outputStart, int outputEnd, float input) {
        return (int) (outputStart + ((outputEnd - outputStart) / (inputEnd - inputStart)) * (input - inputStart));
    }
}
