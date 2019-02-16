package util;

import org.osbot.rs07.api.Skills;
import org.osbot.rs07.api.ui.Skill;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SkillTracker {

    private final Skills skills;
    private final Map<Skill, TrackedSkill> trackedSkills = new HashMap<>();

    public SkillTracker(final Skills skills) {
        this.skills = skills;
    }

    public Set<Skill> getTrackedSkills() {
        return trackedSkills.keySet();
    }

    public boolean isTracking(final Skill... skills) {
        for (Skill skill : skills) {
            if (!trackedSkills.containsKey(skill)) return false;
        }
        return true;
    }

    public void start(final Skill... skills) {
        for (Skill skill : skills) {
            trackedSkills.put(skill, new TrackedSkill(this.skills, skill));
        }
    }

    public void pause(Skill skill) {
        trackedSkills.get(skill).pause();
    }

    public void resume(Skill skill) {
        trackedSkills.get(skill).resume();
    }

    public void pauseAll() {
        trackedSkills.forEach((skill, trackedSkill) -> {
            trackedSkill.pause();
        });
    }

    public void resumeAll() {
        trackedSkills.forEach((skill, trackedSkill) -> {
            trackedSkill.resume();
        });
    }

    public void stop(Skill skill) {
        trackedSkills.remove(skill);
    }

    public void stopAll() {
        trackedSkills.clear();
    }

    public final int getLevel(final Skill skill) {
        return skills.getStatic(skill);
    }

    public final long getElapsedTime(final Skill skill) {
        TrackedSkill trackedSkill = trackedSkills.get(skill);
        return trackedSkill != null ? trackedSkill.getElapsedTime() : -1;
    }

    public final int getGainedXP(final Skill skill) {
        TrackedSkill trackedSkill = trackedSkills.get(skill);
        return trackedSkill != null ? trackedSkill.getGainedXP() : -1;
    }

    public final int getGainedXPPerHour(final Skill skill) {
        TrackedSkill trackedSkill = trackedSkills.get(skill);
        return trackedSkill != null ? trackedSkill.getGainedXPPerHour() : -1;
    }

    public final int getGainedLevels(final Skill skill) {
        TrackedSkill trackedSkill = trackedSkills.get(skill);
        return trackedSkill != null ? trackedSkill.getGainedLevels() : -1;
    }

    private class TrackedSkill {

        private final Skills skills;
        private final Skill skill;
        private final int startXP;
        private final int startLevel;

        private long lastCheckTime = -1;
        private long elapsedTime = -1;

        private boolean paused;

        TrackedSkill(final Skills skills, final Skill skill) {
            this.skills = skills;
            this.skill = skill;
            this.startXP = skills.getExperience(skill);
            this.startLevel = skills.getStatic(skill);
        }

        final void pause() {
            paused = true;
        }

        final void resume() {
            paused = false;
            lastCheckTime = System.currentTimeMillis();
        }

        final long getElapsedTime() {
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

            return elapsedTime;
        }

        final int getGainedXP() {
            return skills.getExperience(skill) - startXP;
        }

        final int getGainedXPPerHour() {
            long elapsedTimeMs = getElapsedTime();
            if (elapsedTimeMs <= 0) {
                return 0;
            }
            return (int) ((double) getGainedXP() / ((double) elapsedTimeMs / 3_600_000.0D));
        }

        int getGainedLevels() {
            return skills.getStatic(skill) - startLevel;
        }
    }
}
