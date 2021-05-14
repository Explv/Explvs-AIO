package activities.eating;

import org.osbot.rs07.api.ui.Skill;
import util.executable.Executable;
import util.Sleep;

public class Eating extends Executable {

    private final Food food;

    public Eating(final Food food) {
        this.food = food;
    }

    public float getHpPercent() {
        return getSkills().getDynamic(Skill.HITPOINTS) * 100 / getSkills().getStatic(Skill.HITPOINTS);
    }

    @Override
    public void run() throws InterruptedException {
        long foodCount = getInventory().getAmount(food.toString());
        getInventory().getItem(food.toString()).interact("Eat", "Drink");
        Sleep.sleepUntil(() -> getInventory().getAmount(food.toString()) < foodCount, 5000);
    }

    @Override
    public String toString() {
        return "Eating";
    }
}
