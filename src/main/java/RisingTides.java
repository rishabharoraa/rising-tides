import terrain.Terrain;
import terrain.TerrainBuilder;
import terrain.TerrainImageFileType;

public class RisingTides {
    public static void main(String[] args) throws Exception {
        Terrain terrain = TerrainBuilder.build("terrain-files/RioDeJaneiro.terrain");
        terrain.floodFill(
                "output-images/RioDeJaneiro2",
                TerrainImageFileType.PNG,
                0
        );
    }
}
