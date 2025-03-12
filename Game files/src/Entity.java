import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Entity implements Battle{
	private List<Spell> abilities;
	private int currentHealth;
	private int maxHealth;
	private int currentMana;
	private int maxMana;
	private boolean immunityFire;
	private boolean immunityIce;
	private boolean immunityEarth;
	public Entity(int maxHealth, int maxMana, boolean immunityFire, boolean immunityIce, boolean immunityEarth) {
		this.abilities = new ArrayList<>();
		this.maxHealth = maxHealth;
		this.currentHealth = maxHealth;
		this.maxMana = maxMana;
		this.currentMana = maxMana;
		this.immunityEarth = immunityEarth;
		this.immunityFire = immunityFire;
		this.immunityIce = immunityIce;
	}
	public void regenerateHealth(int health){
		this.currentHealth = currentHealth + health;
		if (this.currentHealth > maxHealth) {
			this.currentHealth = maxHealth;
		}
	}
	public void regenerateMana(int mana) {
		this.currentMana = currentMana + mana;
		if (this.currentMana > maxMana) {
			this.currentMana = maxMana;
		}
	}

	public void addAbility(Spell ability) {
		abilities.add(ability);
	}

	public List<Spell> getAbilities() {
		return abilities;
	}
	public boolean getImmunityIce() {
		return immunityIce;
	}
	public boolean getImmunityFire() {
		return immunityFire;
	}
	public boolean getImmunityEarth() {
		return immunityEarth;
	}
	@Override
	public abstract void receiveDamage(int damage);

	@Override
	public abstract int getDamage();

	public int useAbility(Spell ability, Entity enemy) {
		int bruteDamage = 0;
		int mana = this.getCurrentMana();
		this.setCurrentMana(getCurrentMana() - ability.mana);
		if (this.getCurrentMana() < 0) {
			System.out.println("Oh! Not enough mana for that! Used default attack and lost an ability");
			this.setCurrentMana(mana);
			bruteDamage = this.getDamage();
		} else {
			if (ability instanceof FireSpell) {
				if (enemy.immunityFire) {
					System.out.println("Watch out! Opponent has immunity to fire!");
					bruteDamage = this.getDamage();
				} else {
					bruteDamage = this.getDamage() + ability.damage;
				}
			} else if (ability instanceof IceSpell) {
				if (enemy.immunityIce) {
					System.out.println("Watch out! Opponent has immunity to ice!");
					bruteDamage = this.getDamage();
				} else {
					bruteDamage = this.getDamage() + ability.damage;
				}
			} else if (ability instanceof EarthSpell) {
				if (enemy.immunityEarth) {
					System.out.println("Watch out! Opponent has immunity to earth!");
					bruteDamage = this.getDamage();
				} else {
					bruteDamage = this.getDamage() + ability.damage;
				}
			}
		}
		return bruteDamage;
	}	
	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxMana(int level) {
		this.maxMana = (int) (Math.pow(1.03, level) * this.maxHealth);
	}

	public void setMaxHealth(int level) {
		this.maxHealth = (int) (Math.pow(1.1, level) * this.maxHealth);
	}

	public int getCurrentMana() {
		return currentMana;
	}

	public void setCurrentHealth(int health) {
		this.currentHealth = health;
	}

	public void setCurrentMana(int currentMana) {
		this.currentMana = currentMana;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public void setNewMaxMana() {
		this.maxMana = (int) (this.maxMana * 1.03);
	}

	public void setNewMaxHealth() {
		this.maxHealth = (int) (this.maxHealth * 1.1);
	}

	public void setAbilities() {
		Random random = new Random();
		int numberAbilities = random.nextInt(4) + 3;
		int damage = random.nextInt(41) + 10;
		int mana = random.nextInt(81) + 20;
		FireSpell abilityFire = new FireSpell(damage, mana);
		this.addAbility(abilityFire);
		damage = random.nextInt(41) + 10;
		mana = random.nextInt(81) + 20;
		IceSpell abilityIce = new IceSpell(damage, mana);
		this.addAbility(abilityIce);
		damage = random.nextInt(41) + 10;
		mana = random.nextInt(81) + 20;
		EarthSpell abilityEarth = new EarthSpell(damage, mana);
		this.addAbility(abilityEarth);
		for (int i = 3; i < numberAbilities; i++) {
			int whatAbility = random.nextInt(3);
			if (whatAbility == 0) {
				damage = random.nextInt(41) + 10;
				mana = random.nextInt(81) + 20;
				FireSpell ability = new FireSpell(damage, mana);
				this.addAbility(ability);
			}
			if (whatAbility == 1) {
				damage = random.nextInt(41) + 10;
				mana = random.nextInt(81) + 20;
				IceSpell ability = new IceSpell(damage, mana);
				this.addAbility(ability);
			}
			if (whatAbility == 2) {
				damage = random.nextInt(41) + 10;
				mana = random.nextInt(81) + 20;
				EarthSpell ability = new EarthSpell(damage, mana);
				this.addAbility(ability);
			}
		}
	}
}
