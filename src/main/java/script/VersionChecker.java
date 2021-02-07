package script;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.ScriptProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class VersionChecker {

    public static final String GITHUB_RELEASES_URL = "https://github.com/Explv/Explvs-AIO/releases/";
    private static final String GITHUB_API_LATEST_RELEASE_URL = "https://api.github.com/repos/Explv/Explvs-AIO/releases/latest";

    private static String latestVersion;

    public static Optional<String> getLatestVersion() {
        if (latestVersion == null) {
            latestVersion = getLatestVersionFromGitHub().orElse(null);
        }

        return Optional.ofNullable(latestVersion);
    }

    public static boolean updateIsIgnored() {
        String ignoreUpdateScriptVerProp = ScriptProperties.getProperty(ScriptProperties.IGNORE_UPDATE_SCRIPT_VER);
        return latestVersion.equals(ignoreUpdateScriptVerProp);
    }

    public static void ignoreUpdate() {
        ScriptProperties.setProperty(ScriptProperties.IGNORE_UPDATE_SCRIPT_VER, latestVersion);
    }

    public static boolean isUpToDate(final String currentVersion) {
        return currentVersion.equals(getLatestVersion().orElse(currentVersion));
    }

    private static Optional<String> getLatestVersionFromGitHub() {
        Optional<String> latestGitHubReleaseTagOpt = getLatestGitHubReleaseTag();

        if (!latestGitHubReleaseTagOpt.isPresent()) {
            return Optional.empty();
        }

        String latestGitHubVersion = latestGitHubReleaseTagOpt.get();
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
