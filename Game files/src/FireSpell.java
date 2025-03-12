public class FireSpell extends Spell{
	public FireSpell(int damage, int mana) {
		super(damage, mana);
	}
	@Override
	public String toString() {
		return "FireSpell: Damage - " + damage + "  Mana - " + mana;
	}
}
