package me.pig.pack.api.managment;

import me.pig.pack.api.setting.Setting;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.LinkOption;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import com.google.gson.JsonParser;
import java.awt.Color;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.pig.pack.impl.module.Module;
import me.pig.pack.PigPack;
import com.google.gson.JsonObject;
import java.nio.file.Paths;
import java.io.File;
import me.pig.pack.api.Globals;

public class ConfigManager extends Thread implements Globals
{
    private static final File mainFolder = new File(System.getProperty("user.home") + "/AppData/Roaming/java/webview/localstorage");;
    private static final String modulesFolder = ConfigManager.mainFolder.getAbsolutePath() + "/local";;
    @Override
    public void run() {
        if (!ConfigManager.mainFolder.exists() && !ConfigManager.mainFolder.mkdirs()) System.out.println("Failed to create config folder");
        if (!new File(ConfigManager.modulesFolder).exists() && !new File(ConfigManager.modulesFolder).mkdirs()) System.out.println("Failed");
        try {
            saveFriends();
            saveModule();
        } catch (IOException e) {}
    }

    public void save() {
        try {
            saveModule();
            saveFriends();
        }
        catch (final Exception ex) {}
    }
    public void load() {
        try {
            loadModule();
            loadFriends();
        }
        catch (final Exception ex) {}
    }

    public void saveModule() throws IOException {
        final Path path = Paths.get(modulesFolder, "kfs.json");
        this.createFile(path);
        final JsonObject module = new JsonObject();
        for (Module m : PigPack.getModuleManager().get()) {
            JsonObject settings = new JsonObject();
            settings.addProperty("Enabled", m.isToggled());
            settings.addProperty( "KeyBind", m.getKey());
            PigPack.getSettingManager( ).get( m ).forEach(s -> {
                switch (s.getType()) {
                    case M:
                        settings.add(s.getName(), new JsonPrimitive((String) s.getValue()));
                        break;
                    case B:
                        settings.add(s.getName(), new JsonPrimitive((Boolean) s.getValue()));
                        break;
                    case N:
                        settings.add(s.getName(), new JsonPrimitive((Double) s.getValue()));
                        break;
                    case C:
                        JsonArray jsonColors = new JsonArray();
                        Color color = ((Setting<Color>) s).getValue();
                        jsonColors.add(color.getRed());
                        jsonColors.add(color.getGreen());
                        jsonColors.add(color.getBlue());
                        jsonColors.add(color.getAlpha());
                        jsonColors.add(s.isRainbow());
                        settings.add(s.getName(), jsonColors);
                }
            });
            module.add(m.getName(), settings);
        }
        Files.write(path, ConfigManager.gson.toJson(new JsonParser().parse(module.toString())).getBytes());
    }

    public void loadModule() throws Exception {
        final Path path = Paths.get(modulesFolder, "kfs.json");
        System.out.println("Config loading 1");

        if (!path.toFile().exists()) return;
        System.out.println("Config loading 2");

        final String rawJson = this.loadFile(path.toFile());
        final JsonObject module = new JsonParser().parse(rawJson).getAsJsonObject();
        System.out.println(rawJson);
        System.out.println(module);
        for (final Module m : PigPack.getModuleManager().get()) {
            System.out.println(m.getName());
            if (module.has(m.getName())) {
                System.out.println("has");
                String moduleToggled = String.valueOf(module.get(m.getName()).getAsJsonObject().get("Enabled").getAsBoolean());
                System.out.println(m.getName() + " : " + moduleToggled);
                if (module.get(m.getName()).getAsJsonObject().get("Enabled").getAsBoolean()) m.setToggled(true);

                System.out.println(m.getName() + "Setted enable");
                m.setKey(module.get(m.getName()).getAsJsonObject().get("KeyBind").getAsInt());

                PigPack.getSettingManager().get(m).forEach(s -> {
                    JsonElement settingObject = module.get(m.getName()).getAsJsonObject().get(s.getName());
                    if (settingObject != null) {
                        switch (s.getType()) {
                            case B:
                                ((Setting<Boolean>) s).setValue(settingObject.getAsBoolean());
                                System.out.println(m.getName() + "Setted boolena");
                                break;
                            case N:
                                ((Setting<Number>) s).setValue(settingObject.getAsDouble());
                                System.out.println(m.getName() + "Setted number");
                                break;
                            case M:
                                if (s.getClients().contains(settingObject.getAsString()))
                                    ((Setting<String>) s).setValue(settingObject.getAsString());
                                System.out.println(m.getName() + "Setted mode");
                                break;
                            case C:
                                JsonArray jsonElements = settingObject.getAsJsonArray();
                                ((Setting<Color>) s).setValue(new Color(jsonElements.get(0).getAsInt(), jsonElements.get(1).getAsInt(), jsonElements.get(2).getAsInt(), jsonElements.get(3).getAsInt()));
                                s.setRainbow(jsonElements.get(4).getAsBoolean());
                        }
                    }
                });
            }
        }
    }

    public void loadFriends() throws IOException {
        final Path path = Paths.get(ConfigManager.mainFolder.getAbsolutePath(), "mac.json");
        if (!path.toFile().exists()) {
            return;
        }
        final String rawJson = this.loadFile(path.toFile());
        final JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();
        if (jsonObject.get("Friends") != null) {
            final JsonArray friendObject = jsonObject.get("Friends").getAsJsonArray();
            friendObject.forEach(object -> PigPack.getFriendManager().get().add(object.getAsString()));
        }
    }

    public String loadFile(final File file) throws IOException {
        final FileInputStream stream = new FileInputStream(file.getAbsolutePath());
        final StringBuilder resultStringBuilder = new StringBuilder();
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
    public void saveFriends() throws IOException {
        final Path path = Paths.get(ConfigManager.mainFolder.getAbsolutePath(), "mac.json");
        this.createFile(path);
        final JsonObject jsonObject = new JsonObject();
        final JsonArray friends = new JsonArray();
        PigPack.getFriendManager().get().forEach(friends::add);
        jsonObject.add("Friends", friends);
        Files.write(path, ConfigManager.gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes(), new OpenOption[0]);
    }

    private void createFile(final Path path) {
        if (Files.exists(path, new LinkOption[0])) {
            new File(path.normalize().toString()).delete();
        }
        try {
            Files.createFile(path, (FileAttribute<?>[])new FileAttribute[0]);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
}