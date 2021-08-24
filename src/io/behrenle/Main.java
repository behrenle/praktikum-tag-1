package io.behrenle;

public class Main {

    public static void main(String[] args) {
        SearchTree vertexCoverSolver = new SearchTree();

        MyGraph test1 = new MyGraph("data/outmoreno_zebra_zebra");
        System.out.println(test1.getEdgeCount());
        System.out.println(test1.getVertices().size());
        System.out.println(vertexCoverSolver.solve(test1) == 20);

        MyGraph test2 = new MyGraph("data/outcontiguous-usa");
        System.out.println(test2.getEdgeCount());
        System.out.println(test2.getVertices().size());
        System.out.println(vertexCoverSolver.solve(test2) == 30);
    }

}
