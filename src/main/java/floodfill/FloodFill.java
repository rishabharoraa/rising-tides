package floodfill;

import point.Point2D;
import terrain.TerrainGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class FloodFill {
    public static <T> List<List<T>> applyFloodFill(
            List<List<T>> _grid,
            Point2D startingPosition,
            Function<T, Boolean> fillPredicate,
            Function<T, T> replacementValueCreator
    ) {
        Stack<Point2D> stack = new Stack<>();
        stack.push(startingPosition);

        List<List<T>> grid = deepCopy(_grid);

        List<List<Boolean>> visited =
                new ArrayList<>(
                        Collections.nCopies(
                                grid.size(),
                                new ArrayList<>(
                                        Collections.nCopies(grid.get(0).size(), false)
                                )
                        )
                );

        while(!stack.isEmpty()) {
            Point2D currentPoint = stack.pop();

            T node;
            try {
                // if out of index, we can just continue
                node = grid.get(currentPoint.getY()).get(currentPoint.getX());
            } catch (Exception e) {
                continue;
            }

            if (fillPredicate.apply(node) && !visited.get(currentPoint.getY()).get(currentPoint.getX())) {
                List<T> row = new ArrayList<>(grid.get(currentPoint.getY()));
                row.set(currentPoint.getX(), replacementValueCreator.apply(node));
                grid.set(currentPoint.getY(), row);

                List<Boolean> visitedRow = new ArrayList<>(visited.get(currentPoint.getY()));
                visitedRow.set(currentPoint.getX(), true);
                visited.set(currentPoint.getY(), visitedRow);

                stack.push(new Point2D(currentPoint.getX() + 1, currentPoint.getY()));
                stack.push(new Point2D(currentPoint.getX() - 1, currentPoint.getY()));
                stack.push(new Point2D(currentPoint.getX(), currentPoint.getY() + 1));
                stack.push(new Point2D(currentPoint.getX(), currentPoint.getY() - 1));
            }
        }
        return grid;
    }

    private static <T> List<List<T>> deepCopy(List<List<T>> src) {
        List<List<T>> newList = new ArrayList<>();
        for (List<T> temp : src) {
            List<T> tempCopy = new ArrayList<>(temp);
            newList.add(tempCopy);
        }
        return newList;
    }
}
