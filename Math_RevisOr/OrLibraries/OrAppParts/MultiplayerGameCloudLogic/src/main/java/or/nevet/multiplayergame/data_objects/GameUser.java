package or.nevet.multiplayergame.data_objects;

public class GameUser extends User implements Comparable<GameUser> {
    private final long score;
    public GameUser(String email, long score) {
        super(email);
        this.score = score;
    }

    public long getScore() {
        return score;
    }



    @Override
    public int compareTo(GameUser o) {
        return Long.compare(score, o.score);
    }
}
