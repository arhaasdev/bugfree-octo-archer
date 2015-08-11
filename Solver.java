public class Solver {
	private final boolean isSolvable;
	private final int moves;
	private final Stack<Board> solution;

	// find a solution to the initial board using the A* algorithm
	public Solver(Board initial) {
		if (initial == null)
			throw new NullPointerException();
		Board twin = initial.twin(); // get a board that is a twin of initial
		MinPQ<SearchNode> mpq = new MinPQ<SearchNode>();
		mpq.insert(new SearchNode(null, initial, 0)); // insert search node
		mpq.insert(new SearchNode(null, twin, 0)); // insert twin search node
		boolean isSolved = false;
		Iterable<Board> neighbors; // contains boards one move from the current
		SearchNode curNode = mpq.delMin(); // remove search node and do check
		if (curNode.board.isGoal())
			isSolved = true;
		else {
			neighbors = curNode.board.neighbors(); // get neighboring boards
			for (Board b : neighbors)
				mpq.insert(new SearchNode(curNode, b, curNode.moves + 1));
		}
		while (!isSolved) { // neither initial nor twin board is yet solved
			curNode = mpq.delMin(); // remove a reference from priority queue
			if (curNode.board.isGoal()) { // initial or twin is solved
				isSolved = true;
				break; // goal found, skip to reconstructing the solution
			}
			neighbors = curNode.board.neighbors(); // get neighboring boards
			for (Board b : neighbors) {
				if (curNode.previous == null) // optimzation must be skipped
					mpq.insert(new SearchNode(curNode, b, curNode.moves + 1));
				else if (!b.equals(curNode.previous.board)) { // optimization
					mpq.insert(new SearchNode(curNode, b, curNode.moves + 1));
				}
			}
		}
		Stack<Board> solutionStack = getSolution(curNode);
		if (solutionStack.peek().equals(initial)) { // initial board is solved
			isSolvable = true;
			solution = solutionStack;
			moves = solution.size() - 1;
		} else { // twin is solved and initial board is unsolvable
			isSolvable = false;
			solution = null;
			moves = -1;
		}
	}

	// reconstruct solution by following pointers back to the initial board
	private static Stack<Board> getSolution(SearchNode node) {
		Stack<Board> solutionStack = new Stack<Board>(); // initial to goal
		while (node.previous != null) {
			solutionStack.push(node.board);
			node = node.previous;
		}
		solutionStack.push(node.board);
		return solutionStack; // return all boards involved in the solution
	}

	// is the initial board solvable?
	public boolean isSolvable() {
		return isSolvable;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		return moves;
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution() {
		return solution;
	}

	// This class encapsulates a search node of the A* algorithm
	private class SearchNode implements Comparable<SearchNode> {
		final Board board; // configuration of blocks
		final SearchNode previous; // parent search node
		final int moves; // number of moves made to reach this board
		final int priority; // priority based on hamming priority and moves

		// construct a new immutable search node
		SearchNode(SearchNode previous, Board board, int moves) {
			this.previous = previous;
			this.board = board;
			this.moves = moves;
			priority = this.board.manhattan() + this.moves; // priority of node
		}

		// compare nodes by priority
		public int compareTo(SearchNode that) {
			return this.priority - that.priority;
		}
	}

	// create initial board and solve puzzle for each filename passed
	public static void main(String[] args) {
		for (String filename : args) {
			In in = new In(filename); // read in board data filename
			int N = in.readInt();
			int[][] blocks = new int[N][N];
			for (int i = 0; i < N; i++) // read in the board data
				for (int j = 0; j < N; j++)
					blocks[i][j] = in.readInt();
			Board initial = new Board(blocks); // create initial board
			Solver solver = new Solver(initial); // generate solution
			if (!solver.isSolvable()) // print solution to standard output
				StdOut.println("No solution possible");
			else {
				StdOut.println("Minimum number of moves = " + solver.moves());
				for (Board b : solver.solution())
					StdOut.println(b);
			}
		}
	}
}
