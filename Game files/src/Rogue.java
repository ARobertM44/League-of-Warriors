import java.util.Random;

public class Rogue extends Character {
	public Rogue(String name, int experience, int level) {
		super(name, "Rogue", true,150, 100, experience, level,
		0, 0, 6, true, false, false);
	}

	@Override
	public int getDamage() {
		int damage = 45;
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
