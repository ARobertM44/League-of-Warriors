import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
	private ArrayList<Account> accounts;
	private Grid grid;
	private ArrayList<Character> characters;
	private static Game instance;

	private Game() {
		this.accounts = JsonInput.deserializeAccounts();
	}

	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}
	public void run() {
		System.out.println("Welcome to League of Warriors!");

		Account account = authenticateUser();
		if (account == null) {
			System.out.println("Authentication failed");
			return;
		}
		characters = account.getCharacters();
		while (true) {

			Character character = selectCharacter(characters);
			if (character == null) {
				System.out.println("No character selected. Fail");
				return;
			}

			if (character.getFirstGame()) {
				System.out.println("FIRST GAME!");
				for (int k = 1; k < character.getLevel(); k++) {
					Random random = new Random();
					for (int i = 0; i < 3; i++) {
						int j = random.nextInt(3);
						if (j == 0) {
							character.incCharisma();
						}
						if (j == 1) {
							character.incDexterity();
						}
						if (j == 2) {
							character.incStrength();
						}
					}
				}
				character.setMaxHealth(character.getLevel());
				character.setMaxMana(character.getLevel());
			}
			character.setCurrentMana(character.getMaxMana());
			character.setCurrentHealth(character.getMaxHealth());
			character.setNotFirstGame();
			System.out.println("Start the adventure!");
			System.out.println("Current level: " + character.getLevel());
			System.out.println("Current experience: " + character.getExperience());
			Random random = new Random();
			int length = 0;
			int width = 0;
			while (length * width < 8) {
				length = random.nextInt(10) + 1;
				width = random.nextInt(10) + 1;
			}
			grid = Grid.randomGrid(length, width);
			CellEntityType type;
			boolean notAlive = false;
			boolean exit_fight = false;
			while (true) {
				if (notAlive) {
					break;
				}
				if (exit_fight) {
					break;
				}
				grid.printGrid();
				type = movement();
				if (type == null) {
					break;
				}

				if (type == CellEntityType.PORTAL) {
					System.out.println("You have just finished a map!");
					account.incMapsCompleted();
					character.evolve();
					System.out.println("Current level: " + character.getLevel());
					System.out.println("Current experience: " + character.getExperience());
					length = 0;
					width = 0;
					while (length * width < 8) {
						length = random.nextInt(10) + 1;
						width = random.nextInt(10) + 1;
					}
					grid = Grid.randomGrid(length, width);
				}
				if (type == CellEntityType.ENEMY) {
					Enemy enemy = new Enemy();
					enemy.setMaxHealth(character.getLevel());
					enemy.setMaxMana(character.getLevel());
					enemy.setCurrentHealth(enemy.getMaxHealth());
					enemy.setCurrentMana(enemy.getMaxMana());
					enemy.setAbilities();
					List<Spell> abilitiesEnemy;
					abilitiesEnemy = enemy.getAbilities();
					character.setAbilities();
					List<Spell> abilitiesHero;
					abilitiesHero = character.getAbilities();
					System.out.println("You have encountered an enemy!\n");
					boolean firstAttack = true;
					while(true) {
						System.out.println("Enemy's stats:");
						System.out.println("Health: " + enemy.getCurrentHealth() + "  Mana: " + enemy.getCurrentMana());
						if (enemy.getImmunityIce()) {
							System.out.print("Immunity Ice: yes  ");
						} else {
							System.out.print("Immunity Ice: no  ");
						}
						if (enemy.getImmunityFire()) {
							System.out.print("Immunity Fire: yes  ");
						} else {
							System.out.print("Immunity Fire: no  ");
						}
						if (enemy.getImmunityEarth()) {
							System.out.print("Immunity Earth: yes  \n\n");
						} else {
							System.out.print("Immunity Earth: no  \n\n");
						}
						System.out.println("Enemy's abilities are: ");
						for (int i = 0; i < abilitiesEnemy.size(); i++) {
							Spell spell = abilitiesEnemy.get(i);
							if (firstAttack) {
								spell.setSpellDamage((int) (spell.damage * (Math.pow(1.05, character.getLevel()))));
								spell.setSpellMana((int) (spell.mana * (Math.pow(1.05, character.getLevel()))));
							}
							System.out.println((i + 1) + ". " + spell.toString());
						}
						System.out.println();
						System.out.println("Your stats:");
						System.out.println("Health: " + character.getCurrentHealth() + "  Mana: " + character.getCurrentMana());
						if (character.getImmunityIce()) {
							System.out.print("Immunity Ice: yes  ");
						} else {
							System.out.print("Immunity Ice: no  ");
						}
						if (character.getImmunityFire()) {
							System.out.print("Immunity Fire: yes  ");
						} else {
							System.out.print("Immunity Fire: no  ");
						}
						if (character.getImmunityEarth()) {
							System.out.print("Immunity Earth: yes  \n\n");
						} else {
							System.out.print("Immunity Earth: no  \n\n");
						}
						System.out.println("Your abilities are: ");
						for (int i = 0; i < abilitiesHero.size(); i++) {
							Spell spellHero = abilitiesHero.get(i);
							if (firstAttack) {
								spellHero.setSpellDamage((int) (spellHero.damage * (Math.pow(1.05, character.getLevel()))));
								spellHero.setSpellMana((int) (spellHero.mana * (Math.pow(1.05, character.getLevel()))));
							}
							System.out.println((i + 1) + ". " + spellHero.toString());
						}
						firstAttack = false;
						System.out.println();

						System.out.println("Choose how to attack, 0 as default");
						boolean okCommand = false;
						int number = 0;
						while(!okCommand) {
							try{
								number = chooseAttack();
								if (number == -100) {
									break;
								}
								if (number > abilitiesHero.size() || number < 0) {
									System.out.println("You are out of range, try again.");
								} else {
									okCommand = true;
								}
							} catch (InvalidCommand e) {
								System.out.println(e.getMessage());
							}
						}
						if (number == -100) {
							exit_fight = true;
							abilitiesHero.clear();
							break;
						}
						if (number == 0) {
							int damage = character.getDamage();
							damage = damage * (int) (Math.pow(1.05, character.getLevel()));
							enemy.receiveDamage(damage);


						} else {
							int damage = character.useAbility(abilitiesHero.get(number - 1), enemy);
							int manaCost = abilitiesHero.get(number - 1).mana;
							damage = (int) (damage * (Math.pow(1.05, character.getLevel())));
							enemy.receiveDamage(damage);
							System.out.println("It costed " + manaCost + " mana");
							abilitiesHero.remove(number - 1);
						}
						if (enemy.getCurrentHealth() <= 0) {
							System.out.println("You have just killed it! A true warrior");
							abilitiesHero.clear();
							int health = character.getCurrentHealth();
							character.setCurrentHealth(health * 2);
							health = character.getCurrentHealth();
							if (health > character.getMaxHealth()) {
								character.setCurrentHealth(character.getMaxHealth());
							}
							character.setCurrentMana(character.getMaxMana());
							int experience = character.getExperience();
							character.setExperience(experience + random.nextInt(11) + 5);
							System.out.println("Current health: " + character.getCurrentHealth());
							System.out.println("Current experience: " + character.getExperience());
							break;
						}
						if (abilitiesEnemy.isEmpty()) {
							int damage = enemy.getDamage();
							damage = (int) (damage * (Math.pow(1.05, character.getLevel())));
							character.receiveDamage(damage);
						} else {
							int enemyAbility = random.nextInt(abilitiesEnemy.size());
							int damage = enemy.useAbility(abilitiesEnemy.get(enemyAbility), character);
							damage = (int) (damage * (Math.pow(1.05, character.getLevel())));
							int manaCost = abilitiesEnemy.get(enemyAbility).mana;
							character.receiveDamage(damage);
							System.out.println("It costed " + manaCost + " mana\n");
							abilitiesEnemy.remove(enemyAbility);

						}
						if(character.getCurrentHealth() <= 0){
							System.out.println("You have got slayed... GAME OVER");
							notAlive = true;
							break;
						}
					}
				}
				if (type == CellEntityType.SANCTUARY) {
					int health = (int) (Math.pow(1.1, character.getLevel()) * (random.nextInt(51) + 50));
					character.setCurrentHealth(character.getCurrentHealth() + health);
					if (character.getCurrentHealth() >= character.getMaxHealth()) {
						character.setCurrentHealth(character.getMaxHealth());
					}
					int mana = (int) (Math.pow(1.1, character.getLevel()) * (random.nextInt(51) + 50));
					character.setCurrentMana(character.getCurrentMana() + mana);
					if (character.getCurrentMana() >= character.getMaxMana()) {
						character.setCurrentMana(character.getMaxMana());
					}
					int experience = character.getExperience();
					int experienceGiven = random.nextInt(26) + 10;
					character.setExperience(experience + experienceGiven);
					System.out.println("The sanctuary offered " + health + " HP");
					System.out.println("Your HP is now " + character.getCurrentHealth());
					//Am ales sa schimb mana cu experienta, pentru ca oricum mana va fi mereu
					//in stare maxima atunci cand intalnim un sanctuar
					System.out.println("The sanctuary offered " + experienceGiven + " experience");
					System.out.println("Your experience is now " + character.getExperience() + "\n");
					System.out.println("The sanctuary offered " + mana + " mana");
					System.out.println("Your mana is now " + character.getCurrentMana());
				}
			}
		}
	}
	public int chooseAttack() throws InvalidCommand {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine().trim();
		if (input.equalsIgnoreCase("exit")) {
			return -100;
		}
		try {
			return Integer.parseInt(input);
		} catch (Exception e) {
			throw new InvalidCommand("Invalid command.\n");
		}
	}
	private Account authenticateUser () {
		Scanner scanner = new Scanner(System.in);

		while (true) {

			System.out.println("Please log in:");
			System.out.print("Email: ");
			String email = scanner.nextLine();
			System.out.print("Password: ");
			String password = scanner.nextLine();

			for (Account account : accounts) {
				Credentials credentials = account.getInformation().getCredentials();
				if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
					System.out.println("Authentication successful!");
					return account;
				}
			}

			System.out.println("Invalid email or password.");
			System.out.print("Do you want to try again? (yes/no): ");
			String response = scanner.nextLine().toLowerCase();

			if (!response.equalsIgnoreCase("yes")) {
				return null;
			}
		}
	}

	private Character selectCharacter (ArrayList<Character> characters) {
		System.out.println("Select a character from your account:");
		for (int i = 0; i < characters.size(); i++) {
			Character character = characters.get(i);
			System.out.println(character.getName() + ": " + character.getProfession() + " (Level " + character.getLevel() + ")");
		}
		Scanner scanner = new Scanner(System.in);
		String choice;
		while (true) {
			System.out.print("Enter the name of your character: ");
			choice = scanner.nextLine();
			for (int i = 0; i < characters.size(); i++) {
				if (characters.get(i).getName().equals(choice)) {
					return characters.get(i);
				}
			}
			System.out.println("Invalid choice.");
		}
	}

	public CellEntityType movement () {
		boolean okCommand = false;
		CellEntityType type = null;
		while (!okCommand) {
			System.out.println("Choose a direction to go: L, R, U, D");
			Scanner scanner = new Scanner(System.in);
			String direction = scanner.next().toUpperCase();
			try {
				switch (direction) {
					case "L":
						type = grid.goWest();
						okCommand = true;
						break;
					case "R":
						type = grid.goEast();
						okCommand = true;
						break;
					case "U":
						type = grid.goNorth();
						okCommand = true;
						break;
					case "D":
						type = grid.goSouth();
						okCommand = true;
						break;
					case "EXIT":
						okCommand = true;
						break;
					default:
						throw new InvalidCommand("Invalid command.\n");
				}
			} catch (ImpossibleMove | InvalidCommand e) {
				System.out.println(e.getMessage());
			}
		}
		return type;
	}


	public void runHard() {
		System.out.println("Welcome to League of Warriors!");

		Account account = authenticateUser();
		if (account == null) {
			System.out.println("Authentication failed");
			return;
		}
		characters = account.getCharacters();
		while (true) {

			Character character = selectCharacter(characters);
			if (character == null) {
				System.out.println("No character selected. Fail");
				return;
			}

			if (character.getFirstGame()) {
				System.out.println("FIRST GAME!");
				for (int k = 1; k < character.getLevel(); k++) {
					Random random = new Random();
					for (int i = 0; i < 3; i++) {
						int j = random.nextInt(3);
						if (j == 0) {
							character.incCharisma();
						}
						if (j == 1) {
							character.incDexterity();
						}
						if (j == 2) {
							character.incStrength();
						}
					}
				}
				character.setMaxHealth(character.getLevel());
				character.setMaxMana(character.getLevel());
			}
			character.setCurrentMana(character.getMaxMana());
			character.setCurrentHealth(character.getMaxHealth());
			character.setNotFirstGame();
			System.out.println("Start the adventure!");
			System.out.println("Current level: " + character.getLevel());
			System.out.println("Current experience: " + character.getExperience());
			Random random = new Random();
			int length;
			int width;
			grid = Grid.hardGrid();
			CellEntityType type;
			boolean notAlive = false;
			boolean exit_fight = false;
			while (true) {
				if (notAlive) {
					break;
				}
				if (exit_fight) {
					break;
				}
				grid.printGrid();
				type = movement();
				if (type == null) {
					break;
				}
				if (type == CellEntityType.PORTAL) {
					System.out.println("You have just finished a map!");
					account.incMapsCompleted();
					character.evolve();
					System.out.println("Current level: " + character.getLevel());
					System.out.println("Current experience: " + character.getExperience());
					length = 0;
					width = 0;
					while (length * width < 8) {
						length = random.nextInt(10) + 1;
						width = random.nextInt(10) + 1;
					}
					grid = Grid.randomGrid(length, width);
				}
				if (type == CellEntityType.ENEMY) {
					Enemy enemy = new Enemy();
					enemy.setMaxHealth(character.getLevel());
					enemy.setMaxMana(character.getLevel());
					enemy.setCurrentHealth(enemy.getMaxHealth());
					enemy.setCurrentMana(enemy.getMaxMana());
					enemy.setAbilities();
					List<Spell> abilitiesEnemy;
					abilitiesEnemy = enemy.getAbilities();
					character.setAbilities();
					List<Spell> abilitiesHero;
					abilitiesHero = character.getAbilities();
					System.out.println("You have encountered an enemy!\n");
					boolean firstAttack = true;
					while(true) {
						System.out.println("Enemy's stats:");
						System.out.println("Health: " + enemy.getCurrentHealth() + "  Mana: " + enemy.getCurrentMana());
						if (enemy.getImmunityIce()) {
							System.out.print("Immunity Ice: yes  ");
						} else {
							System.out.print("Immunity Ice: no  ");
						}
						if (enemy.getImmunityFire()) {
							System.out.print("Immunity Fire: yes  ");
						} else {
							System.out.print("Immunity Fire: no  ");
						}
						if (enemy.getImmunityEarth()) {
							System.out.print("Immunity Earth: yes  \n\n");
						} else {
							System.out.print("Immunity Earth: no  \n\n");
						}
						System.out.println("Enemy's abilities are: ");
						for (int i = 0; i < abilitiesEnemy.size(); i++) {
							Spell spell = abilitiesEnemy.get(i);
							if (firstAttack) {
								spell.setSpellDamage((int) (spell.damage * (Math.pow(1.05, character.getLevel()))));
								spell.setSpellMana((int) (spell.mana * (Math.pow(1.05, character.getLevel()))));
							}
							System.out.println((i + 1) + ". " + spell.toString());
						}
						System.out.println();
						System.out.println("Your stats:");
						System.out.println("Health: " + character.getCurrentHealth() + "  Mana: " + character.getCurrentMana());
						if (character.getImmunityIce()) {
							System.out.print("Immunity Ice: yes  ");
						} else {
							System.out.print("Immunity Ice: no  ");
						}
						if (character.getImmunityFire()) {
							System.out.print("Immunity Fire: yes  ");
						} else {
							System.out.print("Immunity Fire: no  ");
						}
						if (character.getImmunityEarth()) {
							System.out.print("Immunity Earth: yes  \n\n");
						} else {
							System.out.print("Immunity Earth: no  \n\n");
						}
						System.out.println("Your abilities are: ");
						for (int i = 0; i < abilitiesHero.size(); i++) {
							Spell spellHero = abilitiesHero.get(i);
							if (firstAttack) {
								spellHero.setSpellDamage((int) (spellHero.damage * (Math.pow(1.05, character.getLevel()))));
								spellHero.setSpellMana((int) (spellHero.mana * (Math.pow(1.05, character.getLevel()))));
							}
							System.out.println((i + 1) + ". " + spellHero.toString());
						}
						firstAttack = false;
						System.out.println();

						System.out.println("Choose how to attack, 0 as default");
						boolean okCommand = false;
						int number = 0;
						while(!okCommand) {
							try{
								number = chooseAttack();
								if (number == -100) {
									break;
								}
								if (number > abilitiesHero.size() || number < 0) {
									System.out.println("You are out of range, try again.");
								} else {
									okCommand = true;
								}
							} catch (InvalidCommand e) {
								System.out.println(e.getMessage());
							}
						}
						if (number == -100) {
							exit_fight = true;
							abilitiesHero.clear();
							break;
						}
						if (number == 0) {
							int damage = character.getDamage();
							damage = (int) (damage * (Math.pow(1.05, character.getLevel())));
							enemy.receiveDamage(damage);
						} else {
							int damage = character.useAbility(abilitiesHero.get(number - 1), enemy);
							int manaCost = abilitiesHero.get(number - 1).mana;
							damage = (int) (damage * (Math.pow(1.05, character.getLevel())));
							enemy.receiveDamage(damage);
							System.out.println("It costed " + manaCost + " mana");
							abilitiesHero.remove(number - 1);
						}
						if (enemy.getCurrentHealth() <= 0) {
							System.out.println("You have just killed it! A true warrior");
							abilitiesHero.clear();
							int health = character.getCurrentHealth();
							character.setCurrentHealth(health * 2);
							health = character.getCurrentHealth();
							if (health > character.getMaxHealth()) {
								character.setCurrentHealth(character.getMaxHealth());
							}
							character.setCurrentMana(character.getMaxMana());
							int experience = character.getExperience();
							character.setExperience(experience + random.nextInt(11) + 5);
							System.out.println("Current health: " + character.getCurrentHealth());
							System.out.println("Current experience: " + character.getExperience());
							break;
						}
						if (abilitiesEnemy.isEmpty()) {
							int damage = enemy.getDamage();
							damage = (int) (damage * (Math.pow(1.05, character.getLevel())));
							character.receiveDamage(damage);
						} else {
							int enemyAbility = random.nextInt(abilitiesEnemy.size());
							int damage = enemy.useAbility(abilitiesEnemy.get(enemyAbility), character);
							damage = (int) (damage * (Math.pow(1.05, character.getLevel())));
							int manaCost = abilitiesEnemy.get(enemyAbility).mana;
							character.receiveDamage(damage);
							System.out.println("It costed " + manaCost + " mana\n");
							abilitiesEnemy.remove(enemyAbility);

						}
						if(character.getCurrentHealth() <= 0){
							System.out.println("You have got slayed... GAME OVER");
							abilitiesHero.clear();
							notAlive = true;
							break;
						}
					}
				}
				if (type == CellEntityType.SANCTUARY) {
					int health = (int) (Math.pow(1.1, character.getLevel()) * (random.nextInt(51) + 50));
					character.setCurrentHealth(character.getCurrentHealth() + health);
					if (character.getCurrentHealth() >= character.getMaxHealth()) {
						character.setCurrentHealth(character.getMaxHealth());
					}
					int mana = (int) (Math.pow(1.1, character.getLevel()) * (random.nextInt(51) + 50));
					character.setCurrentMana(character.getCurrentMana() + mana);
					if (character.getCurrentMana() >= character.getMaxMana()) {
						character.setCurrentMana(character.getMaxMana());
					}
					int experience = character.getExperience();
					int experienceGiven = random.nextInt(26) + 10;
					character.setExperience(experience + experienceGiven);
					System.out.println("The sanctuary offered " + health + " HP");
					System.out.println("Your HP is now " + character.getCurrentHealth());
					//Am ales sa schimb mana cu experienta, pentru ca oricum mana va fi mereu
					//in stare maxima atunci cand intalnim un sanctuar
					System.out.println("The sanctuary offered " + experienceGiven + " experience");
					System.out.println("Your experience is now " + character.getExperience());
					System.out.println("The sanctuary offered " + mana + " mana");
					System.out.println("Your mana is now " + character.getCurrentMana() + "\n");
				}
			}
		}
	}
}














