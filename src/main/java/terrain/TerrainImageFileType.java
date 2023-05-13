package terrain;

public enum TerrainImageFileType {
    PNG("png"),
    JPEG("jpg"),
    PPM("ppm"),
    ;

    public final String extension;
    TerrainImageFileType(String extension) {
        this.extension = extension;
    }
}
