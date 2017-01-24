import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class A {

	private int[] vertices;
	private boolean[][] edges;
	private int edgeCount;
	
	public A(File file) throws IOException {
		readGraph(file);
	}

	private void readGraph(File file) {
		// TODO
	}
	
	public int[] getVertices() {
		return this.vertices;
	}

	public boolean[][] getEdges() {
		return this.edges;
	}
}
