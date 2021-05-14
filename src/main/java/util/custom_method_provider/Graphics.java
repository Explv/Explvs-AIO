package util.custom_method_provider;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.script.MethodProvider;

import java.awt.*;

public class Graphics extends MethodProvider {

    public void drawEntity(final Entity entity, final Graphics2D graphics) {
        getDisplay().drawModel(
                graphics,
                entity.getGridX(),
                entity.getGridY(),
                entity.getZ(),
                entity.getModel()
        );
    }
}
