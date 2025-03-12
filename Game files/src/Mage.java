import java.util.Random;

public class Mage extends Character {
	public Mage(String name, int experience, int level) {
		super(name, "Mage", true, 100, 150, experience, level,
		0, 4, 0, false, false, true);
	}

	@Override
	public int getDamage() {
		int damage = 30;
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