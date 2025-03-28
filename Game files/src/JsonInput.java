// import org.example.entities.Account;
// import org.example.entities.Credentials;
// import org.example.entities.characters.Character;
// import org.example.entities.characters.Mage;
// import org.example.entities.characters.Rogue;
// import org.example.entities.characters.Warrior;
import org.json.simple.JSONArray;
// import org.json.Exception;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class JsonInput {
    public static ArrayList<Account> deserializeAccounts() {
        String accountPath = "C:\\Users\\albur\\Desktop\\Tema_1\\src\\accounts.json";
        try {
            String content = new String((Files.readAllBytes(Paths.get(accountPath))));
            JSONObject obj;
            try{
                JSONParser parser = new JSONParser();
                obj = (JSONObject) parser.parse(content);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            JSONArray accountsArray = (JSONArray) obj.get("accounts");

            ArrayList<Account> accounts = new ArrayList<>();
            for (int i=0; i < accountsArray.size(); i++) {
                JSONObject accountJson = (JSONObject) accountsArray.get(i);
                // name, country, games_number
                String name = (String) accountJson.get("name");
                String country = (String) accountJson.get("country");
                int gamesNumber = Integer.parseInt((String)accountJson.get("maps_completed"));

                // Credentials
                Credentials credentials = null;
                try {
                    JSONObject credentialsJson = (JSONObject) accountJson.get("credentials");
                    String email = (String) credentialsJson.get("email");
                    String password = (String) credentialsJson.get("password");

                    credentials = new Credentials(email, password);
                } catch (Exception e) {
                    System.out.println("! This account doesn't have all credentials !");
                }

                // Favorite games
                SortedSet<String> favoriteGames = new TreeSet();
                try {
                    JSONArray games = (JSONArray) accountJson.get("favorite_games");
                    for (int j = 0; j < games.size(); j++) {
                        favoriteGames.add((String) games.get(j));
                    }
                } catch (Exception e) {
                    System.out.println("! This account doesn't have favorite games !");
                }

                // Characters
                ArrayList<Character> characters = new ArrayList<>();
                try {
                    JSONArray charactersListJson = (JSONArray) accountJson.get("characters");
                    for (int j = 0; j < charactersListJson.size(); j++) {
                        JSONObject charJson = (JSONObject) charactersListJson.get(j);
                        String cname = (String) charJson.get("name");
                        String profession = (String) charJson.get("profession");
                        String level = (String) charJson.get("level");
                        int lvl = Integer.parseInt(level);
//                        Integer experience = (Integer) charJson.get("experience");
                        int experience = 0;
                        Object experienceObj = charJson.get("experience");
                        if (experienceObj instanceof String) {
                            experience = Integer.parseInt((String) experienceObj);
                        } else if (experienceObj instanceof Long) {
                            experience = ((Long) experienceObj).intValue();
                        } else {
                            System.out.println("Invalid format for experience: " + experienceObj);
                        }
//                        Character newCharacter = null;
//                        if (profession.equals("Warrior"))
//                            newCharacter = new Warrior(cname, experience, lvl);
//                        if (profession.equals("Rogue"))
//                            newCharacter = new Rogue(cname, experience, lvl);
//                        if (profession.equals("Mage"))
//                            newCharacter = new Mage(cname, experience, lvl);
                        Character newCharacter = CharacterFactory.createCharacter(profession, cname, lvl, experience);
                        characters.add(newCharacter);
                    }
                } catch (Exception e) {
                    System.out.println("! This account doesn't have characters !");
                }
                // Account.Information information = new Account.Information(credentials, favoriteGames, name, country);
                Account.Information information = new Account.Information.Builder()
                        .setCredentials(credentials)
                        .setFavoriteGames(favoriteGames)
                        .setName(name)
                        .setCountry(country)
                        .build();
                Account account = new Account(characters, gamesNumber, information);
                accounts.add(account);
            }
            return accounts;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}