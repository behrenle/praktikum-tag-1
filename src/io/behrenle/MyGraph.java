package io.behrenle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class MyGraph implements Graph {
    private Set<Integer> vertices;
    private List<Edge> edges;

    public MyGraph() {
        edges = new ArrayList<Edge>();
        vertices = new HashSet<Integer>();
    }

    public MyGraph(Set<Integer> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public MyGraph(String filename) {
        edges = new ArrayList<Edge>();
        vertices = new HashSet<Integer>();
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

            vertexIds.forEach((ignored) -> this.addVertex(this.vertices.size() - 1));
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
        vertices.add(v);
    }

    @Override
    public void addEdge(Integer v, Integer w) {
        Edge newEdge = new Edge(v, w);
        boolean duplicate = edges.stream()
                .anyMatch(edge -> edge.equals(newEdge));

        if (!duplicate)
            edges.add(newEdge);
    }

    @Override
    public void deleteVertex(Integer v) {
        vertices.remove(v);
        edges = edges.stream()
                .filter(edge -> !edge.containsNode(v))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEdge(Integer u, Integer v) {
        Edge deletedEdge = new Edge(u, v);
        edges = edges.stream()
                .filter(e -> e.equals(deletedEdge))
                .collect(Collectors.toList());
    }

    @Override
    public boolean contains(Integer v) {
        return vertices.contains(v);
    }

    @Override
    public int degree(Integer v) {
        return (int) edges.stream()
                .filter(e -> e.containsNode(v))
                .count();
    }

    @Override
    public boolean adjacent(Integer v, Integer w) {
        return edges.stream()
                .filter(e -> e.containsNode(v))
                .anyMatch(e -> e.containsNode(w));
    }

    @Override
    public Graph getCopy() {
        return new MyGraph(this.vertices, this.edges);
    }

    @Override
    public Set<Integer> getNeighbors(Integer v) {
        return edges.stream()
                .filter(e -> e.containsNode(v))
                .map(e -> {
                    if (e.getFirstNode() == v)
                        return e.getSecondNode();
                    return e.getFirstNode();
                }).collect(Collectors.toSet());
    }

    @Override
    public int size() {
        return vertices.size();
    }

    @Override
    public int getEdgeCount() {
        return edges.size();
    }

    @Override
    public Set<Integer> getVertices() {
        return vertices;
    }

    /*
    public List<Edge> getEdges() {
        return edges;
    }
     */
}
