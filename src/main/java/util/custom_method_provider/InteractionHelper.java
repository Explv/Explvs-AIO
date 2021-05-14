package util.custom_method_provider;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.event.InteractionEvent;
import org.osbot.rs07.script.MethodProvider;

public class InteractionHelper extends MethodProvider {

    public boolean canInteract(final Entity entity) {
        return entity != null &&
               entity.isVisible() &&
               getMap().canReach(entity);
    }

    public boolean interactNoMovement(final Entity entity) {
        InteractionEvent event = new InteractionEvent(entity);
        event.setOperateCamera(false);
        event.setWalkTo(false);
        return execute(event).hasFinished();
    }
}
