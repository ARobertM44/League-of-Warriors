public class Cell {
	private int x;
	private int y;
	private CellEntityType type;
	private boolean visited;

	public Cell(int x, int y, CellEntityType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.visited = false;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public CellEntityType getType() {
		return type;
	}

	public void setType(CellEntityType type) {
		this.type = type;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited() {
		this.visited = true;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
