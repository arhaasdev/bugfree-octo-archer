import java.util.ArrayList;

public class Board {
	private final int[][] blocks; // numbered blocks (1 to N^2-1 plus a blank)

	// construct a board from an N-by-N grid of blocks
	public Board(int[][] blocks) {
		this.blocks = getCopy(blocks);
	}

	// find coordinates (row i, column j) of the blank block represented as 0
	private int[] getCoordsOfBlank() {
		int N = dimension();
		int[] coords = new int[2];
		boolean found = false;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (blocks[i][j] == 0) {
					found = true;
					coords[0] = i;
					coords[1] = j;
					break;
				}
			}
			if (found)
				break;
		}
		return coords;
	}

	// board dimension N
	public int dimension() {
		return blocks.length;
	}

	// number of blocks out of place
	public int hamming() {
		int N = dimension();
		int count = 0;
		int a;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				a = blocks[i][j];
				if (a == 0)
					continue;
				if (i != (a - 1) / N) {
					count++;
				} else if (j != (a - 1) % N) {
					count++;
				}
			}
		return count;
	}

	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		int N = dimension();
		int sum = 0;
		int a;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				a = blocks[i][j];
				if (a == 0)
					continue;
				sum += Math.abs((a - 1) / N - i);
				sum += Math.abs((a - 1) % N - j);
			}
		return sum;
	}

	// is this board the goal board?
	public boolean isGoal() {
		return hamming() == 0;
	}

	// a board obtained by exchanging two adjacent blocks in the same row
	public Board twin() {
		int[][] twinBlocks = getCopy(blocks);
		if (twinBlocks[0][0] == 0 || twinBlocks[0][1] == 0) // block is blank
			exch(twinBlocks, 1, 0, 1, 1); // use second row for exchange
		else // it is okay to make exchange in first row
			exch(twinBlocks, 0, 0, 0, 1); // use first row for exchange
		return new Board(twinBlocks);
	}

	// return an independent copy of N-by-N array of blocks
	private static int[][] getCopy(int[][] blocks) {
		int N = blocks.length;
		int[][] copy = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				copy[i][j] = blocks[i][j];
		return copy;
	}

	// exhange two blocks with one another
	private static void exch(int[][] a, int i1, int j1, int i2, int j2) {
		int temp = a[i1][j1];
		a[i1][j1] = a[i2][j2];
		a[i2][j2] = temp;
	}

	// does this board equal y?
	public boolean equals(Object y) {
		if (this == y) // this refers to the same object as y
			return true;
		if (y == null) // null references are not regarded as equal
			return false;
		if (this.getClass() != y.getClass()) // are both of the same class?
			return false;
		Board that = (Board) y; // safely cast y to type Board
		int N = dimension();
		if (N != that.dimension()) // do the dimensions of each match?
			return false;
		for (int i = 0; i < N; i++) { // are blocks in precise correspondence?
			for (int j = 0; j < N; j++)
				if (this.blocks[i][j] != that.blocks[i][j])
					return false;
		}
		return true;
	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		int N = dimension();
		ArrayList<Board> neighbors = new ArrayList<Board>();
		int[][] nextBlocks; // new arrangement of blocks representing neighbor
		int[] coordsOfBlank = getCoordsOfBlank();
		int zeroY = coordsOfBlank[0];
		int zeroX = coordsOfBlank[1];
		if (zeroY != N - 1) {
			nextBlocks = getCopy(blocks); // create and enqueue bottom neighbor
			exch(nextBlocks, zeroY, zeroX, zeroY + 1, zeroX);
			neighbors.add(new Board(nextBlocks));
		}
		if (zeroX != N - 1) {
			nextBlocks = getCopy(blocks); // create and enqueue right neighbor
			exch(nextBlocks, zeroY, zeroX, zeroY, zeroX + 1);
			neighbors.add(new Board(nextBlocks));
		}
		if (zeroX != 0) {
			nextBlocks = getCopy(blocks); // create and enqueue left neighbor
			exch(nextBlocks, zeroY, zeroX, zeroY, zeroX - 1);
			neighbors.add(new Board(nextBlocks));
		}
		if (zeroY != 0) {
			nextBlocks = getCopy(blocks); // create and enqueue top neighbor
			exch(nextBlocks, zeroY, zeroX, zeroY - 1, zeroX);
			neighbors.add(new Board(nextBlocks));
		}
		return neighbors;
	}

	// string representation of this board in the required format
	public String toString() {
		int N = dimension();
		StringBuilder s = new StringBuilder();
		s.append(N + "\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++)
				s.append(String.format("%2d ", blocks[i][j]));
			s.append("\n");
		}
		return s.toString();
	}

	// see class Solver for unit tests
	public static void main(String[] args) {
	}
}
