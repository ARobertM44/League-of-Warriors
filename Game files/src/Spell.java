public abstract class Spell {
	protected int damage;
	protected int mana;
	public Spell(int damage, int mana) {
		this.damage = damage;
		this.mana = mana;
	}
	public abstract String toString();
	public void setSpellDamage(int damage) {
		this.damage = damage;
	}
	public void setSpellMana(int mana) {
		this.mana = mana;
	}

}
