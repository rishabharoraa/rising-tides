import terrain.Terrain;
import terrain.TerrainBuilder;
import terrain.TerrainImageFileType;

public class RisingTides {
    public static void main(String[] args) throws Exception {
        Terrain terrain = TerrainBuilder.build("terrain-files/VelingaraSenegal.terrain");

        for(int i = 0; i <= 25; i++) {
            terrain.floodFill(
                    String.format("velingara-senegal-report/VelingaraSenegal%dm", i),
                    TerrainImageFileType.PNG,
                    i
            );
        }
    }
}
