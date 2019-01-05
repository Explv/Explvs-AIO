package org.aio.script;

import org.aio.util.ScriptProperties;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class VersionChecker {

    public static final String GITHUB_RELEASES_URL = "https://github.com/Explv/Explvs-AIO/releases/";
    private static final String GITHUB_API_LATEST_RELEASE_URL = "https://api.github.com/repos/Explv/Explvs-AIO/releases/latest";

    public static boolean updateIsIgnored(final String currentVersion) {
        String ignoreUpdateScriptVerProp = ScriptProperties.getProperty(ScriptProperties.IGNORE_UPDATE_SCRIPT_VER);

        return currentVersion.equals(ignoreUpdateScriptVerProp);
    }

    public static void ignoreUpdate(final String currentVersion) {
        ScriptProperties.setProperty(ScriptProperties.IGNORE_UPDATE_SCRIPT_VER, currentVersion);
    }

    public static boolean isUpToDate(final String currentVersion) {
        Optional<String> latestVersionOpt = getLatestVersion();

        if (!latestVersionOpt.isPresent()) {
            return true;
        }

        String latestVersion = latestVersionOpt.get();

        return currentVersion.equals(latestVersion);
    }

    private static Optional<String> getLatestVersion() {
        Optional<String> latestGitHubReleaseTagOpt = getLatestGitHubReleaseTag();

        if (!latestGitHubReleaseTagOpt.isPresent()) {
            return Optional.empty();
        }

        String latestGitHubReleaseTag = latestGitHubReleaseTagOpt.get();
        String latestGitHubVersion = latestGitHubReleaseTag.replaceAll("[^\\d.]", "");
        return Optional.of(latestGitHubVersion);
    }

    private static Optional<String> getLatestGitHubReleaseTag() {
        Optional<JSONObject> latestGitHubReleaseOpt = getLatestGitHubReleaseJSON();

        if (!latestGitHubReleaseOpt.isPresent()) {
            return Optional.empty();
        }

        JSONObject latestGitHubRelease = latestGitHubReleaseOpt.get();

        String versionTag = (String) latestGitHubRelease.get("tag_name");

        return Optional.of(versionTag);
    }

    private static Optional<JSONObject> getLatestGitHubReleaseJSON() {
        try {
            URL url = new URL(GITHUB_API_LATEST_RELEASE_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            try (InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                return Optional.of((JSONObject) (new JSONParser().parse(bufferedReader)));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
