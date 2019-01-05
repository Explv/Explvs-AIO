package org.aio.paint;

import org.aio.tasks.Task;
import org.aio.util.SkillTracker;
import org.osbot.rs07.Bot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.canvas.paint.Painter;
import org.osbot.rs07.input.mouse.BotMouseListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class Paint implements Painter {

    private final Bot bot;
    private final SkillTracker skillTracker;
    private final Color holoBlueLight = Color.decode("#33b5e5");
    private final Font trebuchet = new Font("Trebuchet MS", Font.PLAIN, 14);

    private long lastCheckTime = -1;
    private long elapsedTime = -1;

    private Task currentTask;
    private boolean paintHidden;
    private final Rectangle toggleButton = new Rectangle(435, 344, 76, 24);
    private boolean toggleButtonHovered;
    private Image mouseImage;

    private boolean paused;

    public Paint(final Bot bot, final SkillTracker skillTracker) {
        this.bot = bot;
        this.skillTracker = skillTracker;

        try {
            mouseImage = ImageIO.read(getClass().getResourceAsStream("/resources/cursor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bot.addMouseListener(new BotMouseListener() {
            @Override
            public void checkMouseEvent(final MouseEvent e) {
                if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                    if (toggleButton.contains(e.getPoint())) {
                        paintHidden = !paintHidden;
                    }
                    e.consume();
                } else if (e.getID() == MouseEvent.MOUSE_MOVED) {
                    toggleButtonHovered = toggleButton.contains(e.getPoint());
                    e.consume();
                }
            }

            @Override
            public void mouseMoved(final MouseEvent e) {
                checkMouseEvent(e);
            }
        });
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        lastCheckTime = System.currentTimeMillis();
    }

    public void setCurrentTask(final Task currentTask) {
        this.currentTask = currentTask;
    }

    @Override
    public void onPaint(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        if (!paused) {
            drawMouse(g);
        }
        drawScriptInfo(g);
    }

    private void drawScriptInfo(Graphics2D g) {
        if (!paintHidden) {
            drawScriptInfoBackground(g);
            drawTitle(g);
            g.setFont(trebuchet);
            drawRunTime(g);
            drawTaskInfo(g);
            drawActivityInfo(g);
            drawSkillsInfo(g);
        }
        g.setFont(trebuchet);
        drawTogglePaintButton(g);
    }

    private void drawScriptInfoBackground(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 337, 518, 143);
        g.setColor(Color.decode("#1b1919"));
        g.fillRect(2, 339, 514, 139);
    }

    private void drawTitle(Graphics2D g) {
        g.setColor(holoBlueLight);
        g.setFont(g.getFont().deriveFont(20f));
        g.drawString("Explv", 10, 367);
        g.setColor(Color.WHITE);
        g.drawString("'s AIO", 10 + g.getFontMetrics().stringWidth("Explv"), 367);
    }

    private void drawRunTime(Graphics2D g) {
        if (lastCheckTime == -1) {
            lastCheckTime = System.currentTimeMillis();
            elapsedTime = 0;
        }

        if (!paused) {
            long currentTime = System.currentTimeMillis();
            elapsedTime += currentTime - lastCheckTime;
            lastCheckTime = currentTime;
            elapsedTime += System.currentTimeMillis() - lastCheckTime;
        }

        g.drawString("Run time: " + formatTime(elapsedTime), 10, 397);
    }

    private void drawTaskInfo(Graphics2D g) {
        if (currentTask != null) {
            g.drawString(currentTask.toString(), 10, 417);
        }
    }

    private void drawActivityInfo(Graphics2D g) {
        if (currentTask != null) {
            g.drawString("Activity: " + currentTask.getActivity().toString(), 10, 437);
        }
    }

    private void drawSkillsInfo(Graphics2D g) {
        final int x = 10;
        int y = 457;
        for (final Skill skill : skillTracker.getTrackedSkills()) {
            String output = String.format("%s lvl %d (+%d lvls) +%s xp (%s xp / hr)",
                    skill.toString(),
                    skillTracker.getLevel(skill),
                    skillTracker.getGainedLevels(skill),
                    formatValue(skillTracker.getGainedXP(skill)),
                    formatValue(skillTracker.getGainedXPPerHour(skill)));
            g.drawString(output, x, y);
            y += 20;
        }
    }

    private void drawTogglePaintButton(Graphics2D g) {
        String text = paintHidden ? "Maximise" : "Minimise";

        if (toggleButtonHovered) {
            g.setColor(holoBlueLight);
        } else {
            g.setColor(Color.WHITE);
        }

        g.fill(toggleButton);

        g.setColor(Color.decode("#1b1919"));
        g.fillRect(toggleButton.x + 1, toggleButton.y + 1, toggleButton.width - 2, toggleButton.height - 2);

        if (toggleButtonHovered) {
            g.setColor(holoBlueLight);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawString(text, toggleButton.x + 10, toggleButton.y + (toggleButton.height / 2) + 5);
    }

    private void drawMouse(Graphics2D g) {
        final Point mousePos = bot.getMethods().getMouse().getPosition();

        if (mouseImage == null) {
            g.setColor(Color.WHITE);
            g.drawLine(mousePos.x - 5, mousePos.y - 5, mousePos.x + 5, mousePos.y + 5);
            g.drawLine(mousePos.x - 5, mousePos.y + 5, mousePos.x + 5, mousePos.y - 5);
        } else {
            g.drawImage(mouseImage, mousePos.x, mousePos.y, null);
        }
    }

    private String formatTime(long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60;
        h %= 24;
        m %= 60;
        s %= 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    private String formatValue(long val) {
        return val > 1_000_000 ? String.format("%.2fm", ((float) val / 1_000_000)) :
                val > 1_000 ? String.format("%.2fk", ((float) val / 1_000)) :
                        Long.toString(val);
    }
}
