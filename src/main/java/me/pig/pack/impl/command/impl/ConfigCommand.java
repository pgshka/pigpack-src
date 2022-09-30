package me.pig.pack.impl.command.impl;
import java.awt.datatransfer.Clipboard;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Base64;
import com.google.gson.JsonParser;
import java.nio.file.Paths;
import me.pig.pack.PigPack;
import me.pig.pack.utils.ChatUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.File;
import me.pig.pack.impl.command.Command;

public class ConfigCommand extends Command
{
    private static File mainFolder = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\java\\webview\\localstorage");;
    private static String modulesFolder = ConfigCommand.mainFolder.getAbsolutePath() + "/local";

    public ConfigCommand() {
        super("config <save|load <pastebin url>|update|get>", "config");
    }

    public static String readURL(String pastebinURL) {
        String s = "null";
        try {
            URL url = new URL(pastebinURL);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String content;
            if ((content = bufferedReader.readLine()) != null) {
                return content;
            }
        }
        catch (Exception ex) {}
        return s;
    }

    @Override
    public void exec(String[] str) {
        str = Arrays.copyOfRange(str, 1, str.length);
        if (str.length < 0) {
            this.printUsage();
        }
        else {
            if (str[0].equalsIgnoreCase("save")) {
                ChatUtil.sendMessage(ChatFormatting.LIGHT_PURPLE + "Config saved");
                try {
                    PigPack.getConfigManager().saveModule();
                } catch (Exception io){}
            }
            if (str[0].equalsIgnoreCase("get")) {
                if (new File(ConfigCommand.modulesFolder).exists() || !new File(ConfigCommand.modulesFolder).mkdirs()){
                    PigPack.getConfigManager().save();
                }
                try {
                    Path path = Paths.get(ConfigCommand.modulesFolder, "kfs.json");
                    if (!path.toFile().exists()) {
                        return;
                    }
                    String rawJson = PigPack.getConfigManager().loadFile(path.toFile());
                    JsonObject module = new JsonParser().parse(rawJson).getAsJsonObject();
                    String toCode = module.toString();
                    String result = Base64.getEncoder().encodeToString(toCode.getBytes());
                    StringSelection stringSelection = new StringSelection(result);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    ChatUtil.sendMessage(ChatFormatting.LIGHT_PURPLE + "Configuration is received, and copied to your clipboard");
                }
                catch (Exception ex) {}
            }
            if (str[0].equalsIgnoreCase("load")) {
                if (new File(ConfigCommand.modulesFolder).exists() || !new File(ConfigCommand.modulesFolder).mkdirs()) {}
                if (str.length > 0) {
                    Path path = Paths.get(ConfigCommand.modulesFolder, "kfs.json");
                    byte[] decodedBytes = Base64.getDecoder().decode(readURL(str[1]));
                    ChatUtil.sendMessage(ChatFormatting.LIGHT_PURPLE + "Config loaded and saved.");
                    try {
                        Files.write(path, decodedBytes, new OpenOption[0]);
                    } catch (Exception io){}
                    PigPack.getConfigManager().load();
                    ChatUtil.sendMessage(new String(decodedBytes));
                }
                else {
                    ChatUtil.sendMessage(ChatFormatting.LIGHT_PURPLE + "Config updated");
                    PigPack.getConfigManager().load();
                }
            }
        }
    }


}
