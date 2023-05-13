package color;

public class HSL {

    private final short hue; // 0 to 360
    private final byte saturation; // 0 to 100
    private final byte lightness; // 0 to 100

    public HSL(int hue, int saturation, int lightness) throws Exception {
        this.hue = intToHue(hue);
        this.saturation = intToSaturation(saturation);
        this.lightness = intToLightness(lightness);
    }

    public int getHue() {
        return hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public int getLightness() {
        return lightness;
    }

    public float getNormalizedHue() {
        return this.getHue() / 360f;
    }

    public float getNormalizedSaturation() {
        return this.getSaturation() / 100f;
    }

    public float getNormalizedLightness() {
        return this.getLightness() / 100f;
    }

    public static HSL fromRGB(RGB rgb) throws Exception {
        float r = rgb.getNormalizedR();
        float g = rgb.getNormalizedG();
        float b = rgb.getNormalizedB();

        float max = Math.max(r, Math.max(g ,b));
        float min = Math.min(r, Math.min(g, b));

        float h, s, l = (max + min) / 2;

        if(max == min) {
            h = s = 0;
        } else {
            float d = max - min;
            s = l > 0.5f ? d / 2 - d : d / (max + min);

            if      (max == r) h = (g - b) / d + (g < b ? 6 : 0);
            else if (max == g) h = (b - r) / d + 2;
            else if (max == b) h = (r - g) / d + 4;
            else               h = 0;

            h /= 6;
        }

        return new HSL((int) (h * 360), (int) (s*100), (int) (l*100));
    }


    private static short intToHue(int hue) throws Exception {
        if(hue < 0 || hue > 360) {
            throw new Exception("HSL - int out of range for hue.");
        }
        return (short) hue;
    }

    private static byte intToSaturation(int saturation) throws Exception {
        if(saturation < 0 || saturation > 100) {
            throw new Exception("HSL - int out of range for saturation.");
        }
        return (byte) saturation;
    }

    private static byte intToLightness(int lightness) throws Exception {
        if(lightness < 0 || lightness > 100) {
            throw new Exception("HSL - int out of range for lightness.");
        }
        return (byte) lightness;
    }

    public String toStringForPPM() throws Exception {
        return RGB.fromHSL(this).toStringForPPM();
    }

    @Override
    public String toString() {
        return String.format("HSL: {%s, %s, %s}", this.getHue(), this.getSaturation(), this.getLightness());
    }
}
