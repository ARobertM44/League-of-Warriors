import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class Account {
    public static class Information {
        private Credentials credentials;
        private SortedSet<String> favoriteGames;
        private String country;
        private String name;

        private Information(Builder builder) {
            this.credentials = builder.credentials;
            this.favoriteGames = builder.favoriteGames;
            this.name = builder.name;
            this.country = builder.country;
        }

        public static class Builder {
            private Credentials credentials;
            private SortedSet<String> favoriteGames = new TreeSet<>();
            private String name;
            private String country;

            public Builder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }
            public Builder setFavoriteGames(SortedSet<String> favoriteGames) {
                this.favoriteGames = favoriteGames;
                return this;
            }
            public Builder setName(String name) {
                this.name = name;
                return this;
            }
            public Builder setCountry(String country) {
                this.country = country;
                return this;
            }
            public Information build() {
                return new Information(this);
            }
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public SortedSet<String> getFavoriteGames() {
            return favoriteGames;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }
    }

    private ArrayList<Character> characters;
    private int mapsCompleted;
    private Information information;

    public Account(ArrayList<Character> characters, int mapsCompleted, Information information) {
        this.characters = characters;
        this.mapsCompleted = mapsCompleted;
        this.information = information;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public int getMapsCompleted() {
        return mapsCompleted;
    }

    public void incMapsCompleted() {
        this.mapsCompleted++;
    }

    public Information getInformation() {
        return information;
    }
}
