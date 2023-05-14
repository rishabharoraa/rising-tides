package terrain;

import point.Point2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerrainBuilder {
    public static Terrain build(String path) throws Exception {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // local keyword
        String local = bufferedReader.readLine().trim();
        if(!local.equals("local")) {
            throw new Exception("TerrainBuilder - invalid terrain file format. \"local\" keyword not found.");
        }

        // number of rows
        int rows;
        try {
            rows = Integer.parseInt(bufferedReader.readLine().trim());
        } catch (NumberFormatException e) {
            throw new Exception("TerrainBuilder - invalid terrain row size input.");
        }

        // number of columns
        int columns;
        try {
            columns = Integer.parseInt(bufferedReader.readLine().trim());
        } catch (NumberFormatException e) {
            throw new Exception("TerrainBuilder - invalid terrain column size input.");
        }

        // number of water sources
        int numberOfWaterSources;
        try {
            numberOfWaterSources = Integer.parseInt(bufferedReader.readLine().trim());
        } catch (NumberFormatException e) {
            throw new Exception("TerrainBuilder - invalid number of water sources.");
        }

        // water sources location
        List<Point2D> waterSources = new ArrayList<>();
        for(int i = 0; i < numberOfWaterSources; i++) {
            String waterSourcesLine = bufferedReader.readLine().trim();
            List<Integer> locationLine = Arrays.stream(waterSourcesLine.split(" "))
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .toList();
            if(locationLine.size() != 2) {
                throw new Exception("TerrainBuilder - invalid water sources");
            }
            waterSources.add(new Point2D(locationLine.get(1), locationLine.get(0)));
        }

        // terrain grid
        List<List<Double>> grid = new ArrayList<>();
        for(int i = 0; i < rows; i++) {
            String rowLine = bufferedReader.readLine().trim();
            List<Double> rowData = Arrays.stream(rowLine.split(" "))
                    .mapToDouble(Double::parseDouble)
                    .boxed()
                    .toList();
            grid.add(rowData);
        }

        return new Terrain(columns, rows, numberOfWaterSources, waterSources, grid);
    }
}
