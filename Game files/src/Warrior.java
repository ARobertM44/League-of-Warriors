import java.util.Random;

public class Warrior extends Character {
	public Warrior(String name, int experience, int level) {
		super(name, "Warrior", true, 200, 50, experience, level,
		6, 0, 0, false, true, false);
	}

	@Override
	public int getDamage() {
		int damage = 60;
		int strength = getStrength();
		int charisma = getCharisma();
		if (strength + charisma >= 10) {
			Random random = new Random();
			if (random.nextBoolean()) { 
				damage *= 2;
			}
		}
		return damage;
	}

	@Override
	public void receiveDamage(int damage) {
		int charisma = getCharisma();
		int dexterity = getDexterity();
		int currentHealth = getCurrentHealth();
		if (dexterity + charisma >= 10) {
			Random random = new Random();
			if (random.nextBoolean()) { 
				damage /= 2;
			}
		}
		System.out.println("Your enemy dealt " + damage + " damage");
		setCurrentHealth(currentHealth - damage);
	}
}