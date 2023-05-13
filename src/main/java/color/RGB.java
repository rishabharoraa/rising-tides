package color;

public class RGB {
    private final byte r, g ,b;

    public RGB(int r, int g, int b) throws Exception {
        this.r = intToByte(r);
        this.g = intToByte(g);
        this.b = intToByte(b);

    }

    public int getR() {
        return byteToInt(this.r);
    }

    public int getG() {
        return byteToInt(this.g);
    }

    public int getB() {
        return byteToInt(this.b);
    }

    public float getNormalizedR() {
        return this.getR() / 255f;
    }

    public float getNormalizedG() {
        return this.getG() / 255f;
    }

    public float getNormalizedB() {
        return this.getB() / 255f;
    }

    public static RGB fromHSL(HSL hsl) throws Exception {
        int r, g, b;

        if(hsl.getSaturation() == 0) {
            r = g = b = (int) (hsl.getNormalizedLightness() * 255);
        } else {
            float l = hsl.getNormalizedLightness();
            float s = hsl.getNormalizedSaturation();
            float h = hsl.getNormalizedHue();
            float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRGB(p, q, h + 0.333333f);
            g = hueToRGB(p, q, h);
            b = hueToRGB(p, q, h - 0.333333f);
        }
        return new RGB(r, g, b);
    }

    // TODO refactor to use byte
    private static int hueToRGB(float p, float q, float t) throws Exception {
        float factor;
        
        if(t < 0) t++;
        else if(t > 1) t--;

        if      (t < 0.166666f) factor = p + (q - p) * 6 * t;
        else if (t < 0.5f)      factor = q;
        else if (t < 0.666666f) factor = p + (q - p) * (0.666666f - t) * 6;
        else                    factor = p;

        return Math.round(factor * 255);
    }

    private static int byteToInt(byte b) {
        return (int) (b + 128);
    }

    private static byte intToByte(int i) throws Exception {
        if(i < 0 || i > 255) {
            throw new Exception(String.format("RGB - int out of range for unsigned byte. Found value: %d", i));
        }
        return (byte) (i - 128);
    }

    public String toStringForPPM() {
        return String.format("%d %d %d", this.getR(), this.getG(), this.getB());
    }
}
