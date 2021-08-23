package io.behrenle;

public class Edge {
    private final int firstNode;
    private final int secondNode;

    Edge(int a, int b) {
        firstNode = a;
        secondNode = b;
    }

    public int getFirstNode() {
        return firstNode;
    }

    public int getSecondNode() {
        return secondNode;
    }

    public boolean containsNode(int node) {
        return firstNode == node || secondNode == node;
    }

    public boolean equals(Edge e) {
        return e.getFirstNode() == firstNode && e.getSecondNode() == secondNode
                || e.getFirstNode() == secondNode && e.getSecondNode() == firstNode;
    }
}
