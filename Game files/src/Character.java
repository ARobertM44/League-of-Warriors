import java.util.Random;

public abstract class Character extends Entity {
	private String name;
	private int experience;
	private int level;
	private int strength;
	private int charisma;
	private int dexterity;
	private String profession;
	private boolean firstGame;

	public Character(String name, String profession, boolean firstGame, int maxHealth, int maxMana, int experience,
					 int level, int strength, int charisma, int dexterity, boolean immunityEarth,
					 boolean immunityFire, boolean immunityIce) {
		super(maxHealth, maxMana, immunityFire, immunityIce, immunityEarth);
		this.name = name;
		this.profession = profession;
		this.experience = experience;
		this.level = level;
		this.charisma = charisma;
		this.strength = strength;
		this.dexterity = dexterity;
		this.firstGame = firstGame;
	}

	public int getStrength() {
		return strength;
	}

	public int getCharisma() {
		return charisma;
	}

	public int getDexterity() {
		return dexterity;
	}

	public String getName() {
		return name;
	}

	public int getExperience() {
		return experience;
	}

	public int getLevel() {
		return level;
	}

	public String getProfession() {
		return profession;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public void incLevel() {
		this.level++;
	}

	public void incDexterity() {
		this.dexterity++;
	}

	public void incStrength() {
		this.strength++;
	}

	public void incCharisma() {
		this.charisma++;
	}
	public boolean getFirstGame() {
		return firstGame;
	}
	public void setNotFirstGame() {
		this.firstGame = false;
	}


	public void evolve() {
		int level = this.getLevel();
		int experience = this.getExperience();
		experience = experience + 5 * level;
		while (experience > (int) (50 * Math.pow(1.1, level))) {
			experience -= (int) (50 * Math.pow(1.1, level));
			this.incLevel();
			Random random = new Random();
			for (int i = 0; i < 3; i++) {
				int j = random.nextInt(3);
				if(j == 0) {
					incCharisma();
				}
				if(j == 1) {
					incDexterity();
				}
				if(j == 2) {
					incStrength();
				}
			}
			this.setNewMaxHealth();
			this.setNewMaxMana();
		}
		this.setExperience(experience);
		this.setCurrentHealth(this.getMaxHealth());
		this.setCurrentMana(this.getMaxMana());
	}
}
 