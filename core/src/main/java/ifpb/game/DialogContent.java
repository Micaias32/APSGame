package ifpb.game;

import java.util.Optional;

public class DialogContent {
    String[] names;
    Speech[] speeches;
    int currentIndex = 0;

    public DialogContent(String[] names, Speech[] speeches) {
        this.names = names;
        this.speeches = speeches;
    }

    public Optional<Speech> next(DialogContent this) {
        if (currentIndex >= speeches.length) {
            return Optional.empty();
        } else {
            return Optional.of(speeches[currentIndex]);
        }
    }

    public static Optional<DialogContent> fromJson(String json) {
        return Optional.empty();
    }
}
