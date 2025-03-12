public class IceSpell extends Spell {
	public IceSpell(int damage, int mana) {
		super(damage, mana);
	}
	@Override
	public String toString() {
		return "IceSpell: Damage - " + damage + "  Mana - " + mana;
	}
}
