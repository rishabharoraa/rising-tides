package terrain;

import color.ColorUtils;
import color.HSL;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class Terrain {
    private final int height;
    private final int width;
    private final int numberOfWaterResources;
    private final List<List<Integer>> waterSources;
    private final List<List<Double>> grid;

    private double minHeight = Double.MAX_VALUE;
    private double maxHeight = Double.MIN_VALUE;
    private double maxDepth = Double.MAX_VALUE;

    private final static TerrainImageFileType DEFAULT_TERRAIN_IMAGE_TYPE = TerrainImageFileType.PPM;

    public Terrain(int width,
                   int height,
                   int numberOfWaterResources,
                   List<List<Integer>> waterSources,
                   List<List<Double>> grid
    ) throws Exception {
        // preliminary checks
        if(numberOfWaterResources != waterSources.size()) {
            throw new Exception(
                    String.format(
                            "Terrain - Water sources do not match up. Expected: %d, got %d.",
                            numberOfWaterResources,
                            waterSources.size()
                    )
            );
        }
        for(int i = 0; i < numberOfWaterResources; i++) {
            if(waterSources.get(i).size() != 2) {
                throw new Exception("Terrain - invalid water sources.");
            }
        }
        if(grid.size() != height) {
            throw new Exception(
                    String.format(
                            "Terrain - Height do not match up. Expected %d, got %d.",
                            height,
                            grid.size()
                    )
            );
        }
        for(int y = 0; y < height; y++) {
            if (grid.get(y).size() != width) {
                throw new Exception(
                        String.format(
                                "Terrain - Row number %d does not match up. Expected %d, got %d.",
                                y + 1,
                                width,
                                grid.get(y).size()
                        )
                );
            }
            for(int x = 0; x < width; x++) {
                this.maxHeight = Math.max(this.maxHeight, grid.get(y).get(x));
                this.maxDepth = Math.min(this.maxDepth, grid.get(y).get(x));
                this.minHeight = Math.max(0, this.maxDepth);
            }
        }

        this.height = height;
        this.width = width;
        this.numberOfWaterResources = numberOfWaterResources;
        this.waterSources = waterSources;
        this.grid = grid;
    }

    public void generateTerrainFile(String fileName, TerrainImageFileType fileType) throws Exception {
        FileWriter fileWriter = new FileWriter(fileName + "." + fileType.extension);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        switch (fileType) {
            case PNG -> this.generateTerrainPNG(bufferedWriter);
            case JPEG -> this.generateTerrainJPEG(bufferedWriter);
            case PPM -> this.generateTerrainPPM(bufferedWriter);
        }

        bufferedWriter.close();
    }

    public void generateTerrainFile(String fileName) throws Exception {
        this.generateTerrainFile(fileName, DEFAULT_TERRAIN_IMAGE_TYPE);
    }

    private void generateTerrainPNG(BufferedWriter bufferedWriter) throws Exception {
        throw new Exception("Method not implemented.");
    }

    private void generateTerrainJPEG(BufferedWriter bufferedWriter) throws Exception {
        throw new Exception("Method not implemented.");
    }

    private void generateTerrainPPM(BufferedWriter bufferedWriter) throws Exception {
        bufferedWriter.write("P3\n");
        bufferedWriter.write(String.format("%d %d\n", this.width, this.height));
        bufferedWriter.write("255\n");
        for(int y = 0; y < this.height; y++) {
            for(int x = 0; x < this.width; x++) {
                HSL hsl;
                if(this.grid.get(y).get(x) < 0) {
                    int lightness = ColorUtils.mapToRange(
                            0,
                            this.maxDepth,
                            50,
                            0,
                            this.grid.get(y).get(x)
                    );
                    hsl = new HSL(240, 100, lightness);
                }
                else {
                    int hue = ColorUtils.mapToRange(
                            this.minHeight,
                            this.maxHeight,
                            120,
                            0,
                            this.grid.get(y).get(x)
                    );
                    hsl = new HSL(hue,85,50);
                }
//                System.out.printf("%s -> %s%n",this.grid.get(y).get(x),hue);
                bufferedWriter.write(hsl.toStringForPPM());
                bufferedWriter.newLine();
            }
        }
    }
}
