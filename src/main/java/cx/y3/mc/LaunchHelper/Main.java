package cx.y3.mc.LaunchHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Main {

    public static String agentJarPath;
    public static String agentOptions;
    public static String execJarPath;

    public static void main(String[] args) {
        printSplash();
        String propsFileName = "launchhelper.properties";
        File propsFile = new File(propsFileName);

        if (!propsFile.exists()) {
            log("Properties file " + propsFileName + " does not exist");
            InputStream examplePropsFileIs = Main.class.getResourceAsStream("/" + propsFileName);
            try {
                Files.copy(examplePropsFileIs, Paths.get(propsFileName));
            } catch (IOException e) {
                log("Failed to generate " + propsFileName);
                log(e.getLocalizedMessage());
                return;
            }
            log("A sample version has been created");
            log("Please modify it as needed.");
            return;
        }

        try {
            Properties props = readPropertiesFile(propsFileName);
            agentJarPath = props.getProperty("javaAgentJarPath");
            agentOptions = props.getProperty("javaAgentOptions");
            execJarPath = props.getProperty("execJarPath");
        } catch (IOException e) {
            log("Failed to read " + propsFileName);
            log(e.getLocalizedMessage());
            return;
        }

        if (!runJarWithAgent()) {
            log("Failed to launch");
        }
    }

    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        Properties prop = new Properties();
        prop.load(fis);
        fis.close();
        return prop;
    }

    public static boolean runJarWithAgent() {
        log("Launching executable Jar with Java Agent…");
        try {
            ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-Xms128M",
                "-Xmx6144M",
                "-Dterminal.jline=false",
                "-Dterminal.ansi=true",
                "-javaagent:" + agentJarPath + "=" + agentOptions,
                "-jar", execJarPath
            );
            pb.inheritIO(); // forward console output
            Process process = pb.start();
            int exitCode = process.waitFor();
            log("Process exited with code " + exitCode);
            return exitCode == 0;
        } catch (Exception e) {
            log("Error launching jar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void printSplash() {
        log("");
        log("    //       ////   //  //  //  //   /////  //  //");
        log("   //      //  //  //  //  /// //  //      //  //  ___");
        log("  //      //////  //  //  // ///  //      //////  ____");
        log(" //      //  //  //  //  //  //  //      //  //  _____");
        log("//////  //  //   ////   //  //   /////  //  //  ______");
        log("");
        log("      //  //  //////  //      /////   //////  /////");
        log("     //  //  //      //      //  //  //      //  //  ___");
        log("    //////  /////   //      /////   /////   /////   ____");
        log("   //  //  //      //      //      //      //  //  _____");
        log("  //  //  //////  //////  //      //////  //  //  ______");
        log("");

        String info = "LaunchHelper (Java 21 fork) Created with ♥ by Codex in Somnio and RayfieldMC";
        String rule = "=".repeat(info.length());
        log(rule);
        log(info);
        log("(Original): https://github.com/Codex-in-somnio/LaunchHelper");
        log("(Java 21 Fork): https://github.com/RayfieldMC/LaunchHelper");
        log(rule);
        log("");
    }

    public static void log(String message) {
        System.out.println("[LHR] " + message);
    }
}
