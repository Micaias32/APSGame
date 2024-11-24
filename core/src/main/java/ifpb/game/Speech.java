package ifpb.game;

public class Speech {
    private String talking;
    private String talked;

    public Speech(String talking, String talked) {
        this.talking = talking;
        this.talked = talked;
    }

    public String getTalking() {
        return talking;
    }

    public String getTalked() {
        return talked;
    }
}
