public class EarthSpell extends Spell{
	public EarthSpell(int damage, int mana) {
		super(damage, mana);
	}
	@Override
	public String toString() {
		return "EarthSpell: Damage - " + damage + "  Mana - " + mana;
	}
}
