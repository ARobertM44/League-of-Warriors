import java.util.ArrayList;
import java.util.Random;
public class Grid extends ArrayList<ArrayList<Cell>> {
	private int length;
	private int width;
	private Character character;
	private Cell cellPlayer;
	private Grid(int length, int width){
		this.length = length;
		this.width = width;
		for (int i = 0; i < length; i++) {
			this.add(new ArrayList<>());
			for (int j = 0; j < width; j++) {
				this.get(i).add(new Cell(i, j, CellEntityType.VOID));
			}
		}
	}
	public static Grid randomGrid(int length, int width) {
		Grid grid = new Grid(length, width);
		Random random = new Random();
		int x = random.nextInt(length);
		int y = random.nextInt(width);
		grid.get(x).get(y).setType(CellEntityType.PLAYER);
		grid.cellPlayer = new Cell(x, y, CellEntityType.PLAYER);
		while(grid.get(x).get(y).getType() != CellEntityType.VOID) {
			x = random.nextInt(length);
			y = random.nextInt(width);
		}
		grid.get(x).get(y).setType(CellEntityType.PORTAL);
		while(grid.get(x).get(y).getType() != CellEntityType.VOID) {
			x = random.nextInt(length);
			y = random.nextInt(width);
		}
		grid.get(x).get(y).setType(CellEntityType.SANCTUARY);
		while(grid.get(x).get(y).getType() != CellEntityType.VOID) {
			x = random.nextInt(length);
			y = random.nextInt(width);
		}
		grid.get(x).get(y).setType(CellEntityType.SANCTUARY);
		while(grid.get(x).get(y).getType() != CellEntityType.VOID) {
			x = random.nextInt(length);
			y = random.nextInt(width);
		}
		grid.get(x).get(y).setType(CellEntityType.ENEMY);
		while(grid.get(x).get(y).getType() != CellEntityType.VOID) {
			x = random.nextInt(length);
			y = random.nextInt(width);
		}
		grid.get(x).get(y).setType(CellEntityType.ENEMY);
		while(grid.get(x).get(y).getType() != CellEntityType.VOID) {
			x = random.nextInt(length);
			y = random.nextInt(width);
		}
		grid.get(x).get(y).setType(CellEntityType.ENEMY);
		while(grid.get(x).get(y).getType() != CellEntityType.VOID) {
			x = random.nextInt(length);
			y = random.nextInt(width);
		}
		grid.get(x).get(y).setType(CellEntityType.ENEMY);
		for (ArrayList<Cell> row : grid) {
			for (Cell cell : row) {
				if(cell.getType() == CellEntityType.VOID) {
					cell.setType(getRandomType());
				}
			}
		}
		return grid;
	}
	public static Grid hardGrid() {
		Grid grid = new Grid(5, 5);
		grid.getFirst().getFirst().setType(CellEntityType.PLAYER);
		grid.cellPlayer = new Cell(0, 0, CellEntityType.PLAYER);
		grid.getFirst().get(3).setType(CellEntityType.SANCTUARY);
		grid.get(1).get(3).setType(CellEntityType.SANCTUARY);
		grid.get(2).getFirst().setType(CellEntityType.SANCTUARY);
		grid.get(3).get(4).setType(CellEntityType.ENEMY);
		grid.get(4).get(3).setType(CellEntityType.SANCTUARY);
		grid.get(4).get(4).setType(CellEntityType.PORTAL);
		return grid;
	}

	public static CellEntityType getRandomType() {
		Random random = new Random();
		CellEntityType[] values = CellEntityType.values();
		int index = random.nextInt(values.length - 2);
		return values[index];
	}

	public CellEntityType goNorth() throws ImpossibleMove{
		int x = cellPlayer.getX();
		int y = cellPlayer.getY();
		if(x == 0) {
			throw new ImpossibleMove("Impossible move, you have hit a wall!\n");
		} 
		this.get(x).get(y).setVisited();
		this.get(x).get(y).setType(CellEntityType.VOID);
		Cell newCell = this.get(x - 1).get(y);
		cellPlayer.setX(x - 1);
		CellEntityType type = newCell.getType();
		newCell.setType(CellEntityType.PLAYER);
		return type;
	}

	public CellEntityType goSouth() throws ImpossibleMove{
		int x = cellPlayer.getX();
		int y = cellPlayer.getY();
		if(x == length - 1) {
			throw new ImpossibleMove("Impossible move, you have hit a wall!\n");
		}
		this.get(x).get(y).setVisited();
		this.get(x).get(y).setType(CellEntityType.VOID);
		Cell newCell = this.get(x + 1).get(y);
		cellPlayer.setX(x + 1);
		CellEntityType type = newCell.getType();
		newCell.setType(CellEntityType.PLAYER);
		return type;
	}

	public CellEntityType goEast() throws ImpossibleMove{
		int x = cellPlayer.getX();
		int y = cellPlayer.getY();
		if(y == width - 1) {
			throw new ImpossibleMove("Impossible move, you have hit a wall!\n");
		}
		this.get(x).get(y).setVisited();
		this.get(x).get(y).setType(CellEntityType.VOID);
		Cell newCell = this.get(x).get(y + 1);
		cellPlayer.setY(y + 1);
		CellEntityType type = newCell.getType();
		newCell.setType(CellEntityType.PLAYER);
		return type;
	}
	public CellEntityType goWest() throws ImpossibleMove{
		int x = cellPlayer.getX();
		int y = cellPlayer.getY();
		if(y == 0) {
			throw new ImpossibleMove("Impossible move, you have hit a wall!\n");
		}

		this.get(x).get(y).setVisited();
		this.get(x).get(y).setType(CellEntityType.VOID);
		Cell newCell = this.get(x).get(y - 1);
		cellPlayer.setY(y - 1);
		CellEntityType type = newCell.getType();
		newCell.setType(CellEntityType.PLAYER);
		return type;
	}

	public void printGrid() {
		for (ArrayList<Cell> row : this) { 
			for (Cell cell : row) {
				if (cell.getType() == CellEntityType.PLAYER) {
					System.out.print("P ");
				} else {
					if (cell.isVisited()) {
						System.out.print("V ");
					} else {
						System.out.print("N ");
					}
				}
			}
			System.out.println();
		}
	}
}
