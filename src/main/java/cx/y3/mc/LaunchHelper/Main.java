package cx.y3.mc.LaunchHelper;

import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

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
		if (!runJar()) {
			log("Failed to launch");
		}
	}

	public static Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		fis = new FileInputStream(fileName);
		prop = new Properties();
		prop.load(fis);
		fis.close();
		return prop;
	}

	public static boolean attachJavaAgent() {
		log(String.format("Loading Java agent %s=%s", agentJarPath, agentOptions));
		String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
		VirtualMachine vm;
		try {
			vm = VirtualMachine.attach(pid);
		} catch (Exception e) {
			log("Failed to attach to the JVM");
			log(e.getLocalizedMessage());
			return false;
		}
		try {
			vm.loadAgent(agentJarPath, agentOptions);
			vm.detach();
		} catch (Exception e) {
			log(e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	public static boolean runJar() {
		log("Loading executable Jar " + execJarPath);
		JarFile jarFile;
		try {
			jarFile = new JarFile(execJarPath);
		} catch (IOException e) {
			log("Failed to open the specified executable jar");
			log(e.getLocalizedMessage());
			return false;
		}
		Manifest manifest;
		Attributes attributes;
		String className;
		try {
			manifest = jarFile.getManifest();
			attributes = manifest.getMainAttributes();
			className = attributes.getValue(Attributes.Name.MAIN_CLASS);
			jarFile.close();
		} catch (IOException e) {
			log("Failed to get manifest from the specified executable jar");
			log(e.getLocalizedMessage());
			return false;
		}

		File file = new File(execJarPath);
		try {
			URL[] urls = { file.toURI().toURL() };
			URLClassLoader loader = new URLClassLoader(urls);
			Class<?> cls = loader.loadClass(className);
			Method mcMain = cls.getDeclaredMethod("main", String[].class);
			String[] mcArgs = {};

			ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(cls.getClassLoader());

			if (!attachJavaAgent()) {
				log("Failed to attach Java agent");
				loader.close();
				return false;
			}
			mcMain.invoke(null, (Object) mcArgs);

			Thread.currentThread().setContextClassLoader(oldCl);

			loader.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
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

		Properties buildInfo = new Properties();
		try {
			InputStream biif = Main.class.getResourceAsStream("/META-INF/build-info.properties");
			if (biif == null)
				throw new IOException();
			buildInfo.load(biif);
		} catch (IOException e) {
			log("Failed to read build information: " + e.getLocalizedMessage());
		}
		String name = "LaunchHelper";
		String version = buildInfo.getProperty("build.version");
		String info = String.format("%s (%s) Created with ♥ by Codex in Somnio", name, version);
		String rule = "";
		for (int i = 0; i < info.length(); ++i)
			rule += "=";
		log(rule);
		log(info);
		log("https://github.com/Codex-in-somnio/LaunchHelper");
		log(rule);
		log("");
	}

	public static void log(String message) {
		System.out.println("[LH] " + message);
	}
}
