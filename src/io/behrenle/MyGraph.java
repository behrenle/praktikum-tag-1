package io.behrenle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class MyGraph implements Graph {

    private Map<Integer, Set<Integer>> adjacencyList;

    MyGraph() {
        adjacencyList = new HashMap<>();
    }

    MyGraph(Map<Integer, Set<Integer>> al) {
        adjacencyList = new HashMap<>();
        al.keySet().forEach(key -> adjacencyList.put(key, new HashSet<>(al.get(key))));
    }

    public MyGraph(String filename) {
        this();
        File file = new File(filename);
        try {
            Scanner reader = new Scanner(file);
            List<String> lines = new ArrayList<>();

            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }

            List<List<String>> splittedLines = lines.stream()
                    .map(line -> Arrays.stream(line.split(" ")).toList())
                    .collect(Collectors.toList());

            List<String> vertexIds = new ArrayList<>();
            splittedLines.forEach(line -> line.forEach(vertexName -> {
                if (!vertexIds.contains(vertexName))
                    vertexIds.add(vertexName);
            }));

            vertexIds.forEach((ignored) -> this.addVertex(adjacencyList.size()));
            splittedLines.forEach(edge -> {
                if (edge.size() == 2)
                    this.addEdge(
                            vertexIds.indexOf(edge.get(0)),
                            vertexIds.indexOf(edge.get(1))
                    );
            });
        } catch (FileNotFoundException ignored) {

        }
    }

    @Override
    public void addVertex(Integer v) {
        if (adjacencyList.containsKey(v))
            throw new RuntimeException("Vertex already exists");

        adjacencyList.put(v, new HashSet<>());
    }

    @Override
    public void addEdge(Integer v, Integer w) {
        if (contains(v) && contains(w)) {
            adjacencyList.get(v).add(w);
            adjacencyList.get(w).add(v);
        } else {
            throw new RuntimeException("v or w does not exist");
        }
    }

    @Override
    public void deleteVertex(Integer v) {
        if (!contains(v))
            throw new RuntimeException("Vertex does not exist");

        Set<Integer> neighbours = adjacencyList.get(v);
        adjacencyList.remove(v);

        neighbours.forEach(neighbour -> {
            if (!contains(neighbour))
                throw new RuntimeException("Neighbour does not exist " + neighbour);

            adjacencyList.get(neighbour).remove(v);
        });
    }

    @Override
    public void deleteEdge(Integer u, Integer v) {
        if (contains(u) && contains(v)) {
            adjacencyList.get(u).remove(v);
            adjacencyList.get(v).remove(u);
        } else
            throw new RuntimeException("Edge doesn't exist!");
    }

    @Override
    public boolean contains(Integer v) {
        return adjacencyList.containsKey(v);
    }

    @Override
    public int degree(Integer v) {
        if (!contains(v))
            throw new RuntimeException("Vertex doesn't exist!");

        return adjacencyList.get(v).size();
    }

    @Override
    public boolean adjacent(Integer v, Integer w) {
        if (!contains(v) && !contains(w))
            throw new RuntimeException("One of the vertex doesn't exist or both dont't exit!");

        return adjacencyList.get(v).contains(w)
                && adjacencyList.get(w).contains(v);
    }

    @Override
    public Graph getCopy() {
        return new MyGraph(new HashMap<>(adjacencyList));
    }

    @Override
    public Set<Integer> getNeighbors(Integer v) {
        if (!contains(v))
            throw new RuntimeException("Vertex does not exist");

        return adjacencyList.get(v);
    }

    @Override
    public int size() {
        return adjacencyList.size();
    }

    @Override
    public int getEdgeCount() {
        var wrapper = new Object(){ int counter = 0; };
        adjacencyList.keySet()
                .forEach(vertexId -> {
                    Set<Integer> neighbours = getNeighbors(vertexId);
                    neighbours.forEach(neighbourId -> {
                        if (neighbourId > vertexId)
                            wrapper.counter++;
                    });
                });

        return wrapper.counter;
    }

    @Override
    public Set<Integer> getVertices() {
        return adjacencyList.keySet();
    }
}
