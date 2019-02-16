package tasks;

public enum TaskType {

    LEVEL("Level Task"),
    LOOP("Loop Task"),
    RESOURCE("Resource Task"),
    TIMED("Timed task"),
    QUEST("Quest task"),
    GRAND_EXCHANGE("Grand Exchange Task"),
    TUTORIAL_ISLAND("Tutorial Island Task"),
    BREAK("Break Task");

    String name;

    TaskType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
