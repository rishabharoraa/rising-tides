import terrain.Terrain;
import terrain.TerrainBuilder;
import terrain.TerrainImageFileType;

public class RisingTides {
    private static final int HEIGHT = 100;
    private static final int WIDTH = 360;

    public static void main(String[] args) throws Exception {
        Terrain terrain = TerrainBuilder.build("terrain-files/PearlRiverDelta.terrain");
        terrain.generateTerrainFile("output-images/PearlRiverDelta", TerrainImageFileType.PPM);
    }
}
