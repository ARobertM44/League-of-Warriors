import java.util.Random;

public class Enemy extends Entity{
	public Enemy() {
		super(
			getRandomValue(),
			getRandomValue(),
			new Random().nextBoolean(),
			new Random().nextBoolean(),
			new Random().nextBoolean()
		);
	}
	@Override
	public int getDamage() {
		Random random = new Random();
		int damage = 5 + random.nextInt(26);
		if (random.nextBoolean()) { 
			damage *= 2;
		}
		return damage;
	}

	@Override
	public void receiveDamage(int damage) {
	
		int currentHealth = getCurrentHealth();
		Random random = new Random();
		if (random.nextBoolean()) {
			System.out.println("Oh no! Attack dodged! You have dealt no damage!");
		} else {
			setCurrentHealth(currentHealth - damage);
			System.out.println("You have dealt " + damage + " damage!");
		}
	}
	
	private static int getRandomValue() {
		Random random = new Random();
		return random.nextInt(101) + 50;
	}
	@Override
	public int useAbility(Spell ability, Entity hero) {
		int bruteDamage = 0;
		int mana = this.getCurrentMana();
		this.setCurrentMana(getCurrentMana() - ability.mana);
		if (this.getCurrentMana() < 0) {
			System.out.println("Your enemy did not have enough mana for his ability and lost it");
			this.setCurrentMana(mana);
			bruteDamage = this.getDamage();
		} else {
			if (ability instanceof FireSpell) {
				if (hero.getImmunityFire()) {
					System.out.println("Your enemy tried to use a fire spell, but you are immune");
					bruteDamage = this.getDamage();
				} else {
					bruteDamage = this.getDamage() + ability.damage;
				}
			} else if (ability instanceof IceSpell) {
				if (hero.getImmunityIce()) {
					System.out.println("Your enemy tried to use an ice spell, but you are immune");
					bruteDamage = this.getDamage();
				} else {
					bruteDamage = this.getDamage() + ability.damage;
				}
			} else if (ability instanceof EarthSpell) {
				if (hero.getImmunityEarth()) {
					System.out.println("Your enemy tried to use an earth spell, but you are immune");
					bruteDamage = this.getDamage();
				} else {
					bruteDamage = this.getDamage() + ability.damage;
				}
			}
		}
		return bruteDamage;
	}
}
