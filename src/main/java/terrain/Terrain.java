package terrain;

import color.ColorUtils;
import color.HSL;
import color.RGB;
import floodfill.FloodFill;
import point.Point2D;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private final int height;
    private final int width;
    private final int numberOfWaterResources;
    private final List<Point2D> waterSources;
    private final List<List<Double>> grid;

    private double minHeight = Double.MAX_VALUE;
    private double maxHeight = Double.MIN_VALUE;
    private double maxDepth = Double.MAX_VALUE;

    private final static TerrainImageFileType DEFAULT_TERRAIN_IMAGE_TYPE = TerrainImageFileType.PPM;

    public Terrain(int width,
                   int height,
                   int numberOfWaterResources,
                   List<Point2D> waterSources,
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
        this.generateTerrainFile(fileName, fileType, this.grid, 0.0f);
    }

    public void generateTerrainFile(
            String fileName,
            TerrainImageFileType fileType,
            List<List<Double>> grid,
            double offset) throws Exception
    {
        switch (fileType) {
            case PNG -> this.generateTerrainPNG(fileName, grid, offset);
            case JPEG -> this.generateTerrainJPEG(fileName, grid, offset);
            case PPM -> this.generateTerrainPPM(fileName, grid, offset);
        }

    }

    public void generateTerrainFile(String fileName) throws Exception {
        this.generateTerrainFile(fileName, DEFAULT_TERRAIN_IMAGE_TYPE);
    }

    private void generateTerrainPNG(String fileName, List<List<Double>> grid, double offset) throws Exception {
        List<List<RGB>> pixelGrid = this.getPixelGrid(grid, offset);
        List<Integer> rgbs = pixelGrid
                .stream()
                .flatMap(List::stream)
                .map(rgb -> {
                    int rgbCode = rgb.getR();
                    rgbCode = (rgbCode << 8) + rgb.getG();
                    rgbCode = (rgbCode << 8) + rgb.getB();
                    return rgbCode;
                })
                .toList();

        DataBuffer rgbData = new DataBufferInt(rgbs.stream().mapToInt(i->i).toArray(), rgbs.size());

        BufferedImage img = new BufferedImage(
                new DirectColorModel(24, 0xff0000, 0xff00, 0xff),
                Raster.createPackedRaster(rgbData, this.width, this.height, this.width,
                        new int[]{0xff0000, 0xff00, 0xff},
                        null),
                false,
                null
        );

        ImageIO.write(img, "png", new File(fileName + "." + TerrainImageFileType.PNG.extension));
    }

    private void generateTerrainJPEG(String fileName, List<List<Double>> grid, double offset) throws Exception {
        throw new Exception("Method not implemented.");
    }

    public void floodFill(String fileName, TerrainImageFileType fileType, double targetAltitude) throws Exception {
        List<List<Double>> newGrid = FloodFill.applyFloodFill(
                this.grid,
                this.waterSources.get(0),
                (Double altitude) -> altitude < targetAltitude,
                (Double altitude) -> altitude - targetAltitude
        );
        this.generateTerrainFile(fileName, fileType, newGrid, targetAltitude);
    }

    private void generateTerrainPPM(String fileName, List<List<Double>> grid, double offset) throws Exception {
        FileWriter fileWriter = new FileWriter(fileName + "." + TerrainImageFileType.PPM.extension);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("P3\n");
        bufferedWriter.write(String.format("%d %d\n", this.width, this.height));
        bufferedWriter.write("255\n");

        List<List<RGB>> pixelGrid = this.getPixelGrid(grid, offset);
        for(int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                bufferedWriter.write(pixelGrid.get(y).get(x).toStringForPPM());
                bufferedWriter.newLine();
            }
        }

        bufferedWriter.close();
    }

    private List<List<RGB>> getPixelGrid(List<List<Double>> grid, double offset) throws Exception {
        List<List<RGB>> pixelGrid = new ArrayList<>();

        for(int y = 0; y < this.height; y++) {
            List<RGB> currentRow = new ArrayList<>();
            for(int x = 0; x < this.width; x++) {
                HSL hsl;
                if(grid.get(y).get(x) < 0) {
                    int lightness = ColorUtils.mapToRange(
                            0,
                            this.maxDepth - offset,
                            50,
                            0,
                            grid.get(y).get(x)
                    );
                    hsl = new HSL(240, 65, lightness);
                }
                else {
                    int hue = ColorUtils.mapToRange(
                            this.minHeight,
                            this.maxHeight + offset,
                            80,
                            0,
                            this.grid.get(y).get(x)
                    );
                    hsl = new HSL(hue,65,50);
                }
                currentRow.add(RGB.fromHSL(hsl));
            }
            pixelGrid.add(currentRow);
        }

        return pixelGrid;
    }
}
