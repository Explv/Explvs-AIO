package org.aio.gui.conf_man;

import org.aio.gui.task_panels.TaskPanel;
import org.aio.gui.task_panels.TaskPanelFactory;
import org.aio.tasks.task.Task;
import org.aio.tasks.task.TaskType;
import org.aio.tasks.tutorial_island_task.TutorialIslandTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class ConfigManager {

    private static final String dataDirectory = Paths.get(System.getProperty("user.home"), "OSBot", "Data").toString();

    public void saveConfig(final JSONObject config) {
        JFileChooser fileChooser = getFileChooser();
        int option = fileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".config")) {
                selectedFile = new File(selectedFile + ".config");
            }
            try (FileWriter fileWriter = new FileWriter(selectedFile)) {
                fileWriter.write(config.toJSONString());
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Optional<JSONObject> readConfig() {
        try {
            JFileChooser fileChooser = getFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                return readConfig(fileChooser.getSelectedFile());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private JFileChooser getFileChooser() {
        JFileChooser jFileChooser = new JFileChooser(dataDirectory);
        jFileChooser.setFileSystemView(new FileSystemView() {
            @Override
            public File createNewFolder(File containingDir) throws IOException {
                return new File(dataDirectory);
            }
        });
        jFileChooser.setFileFilter(new FileNameExtensionFilter("Config files", "config"));
        return jFileChooser;
    }

    public Optional<JSONObject> readConfig(final File file) {
        if (!file.exists()) {
            return Optional.empty();
        }
        try (FileReader fileReader = new FileReader(file)) {
            JSONObject jsonObject = (JSONObject) (new JSONParser().parse(fileReader));
            return Optional.of(jsonObject);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Queue<Task> getTasksFromJSON(final JSONObject json) {
        Queue<Task> tasks = new LinkedList<>();
        tasks.add(new TutorialIslandTask());

        JSONArray taskJSONArray;

        if (json.containsKey("org/aio/tasks")) {
            taskJSONArray = (JSONArray) json.get("org/aio/tasks");
        } else {
            taskJSONArray = (JSONArray) json.get("tasks");
        }

        for (Object taskObj : taskJSONArray) {
            JSONObject taskJSON = (JSONObject) taskObj;
            TaskType taskType = TaskType.valueOf((String) taskJSON.get("type"));
            TaskPanel taskPanel = TaskPanelFactory.createTaskPanel(taskType);

            if (taskPanel == null) {
                continue;
            }

            taskPanel.fromJSON(taskJSON);
            Task task = taskPanel.toTask();
            tasks.add(task);
        }

        return tasks;
    }
}

