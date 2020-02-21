package cx.y3.mc.LaunchHelper;

import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Main {
	public static void main(String[] args) throws Exception {
		Properties props = readPropertiesFile("launchhelper.properties");
		attachJavaAgent(props.getProperty("javaAgentJarPath"), props.getProperty("javaAgentOptions"));
		runJar(props.getProperty("execJarPath"));
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

	public static void attachJavaAgent(String jarPath, String options) throws Exception {
		String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
		VirtualMachine vm = VirtualMachine.attach(pid);
		vm.loadAgent(jarPath, options);
		vm.detach();
	}

	public static void runJar(String jarPath) throws Exception {
		JarFile jarFile = new JarFile(jarPath);
		Manifest manifest = jarFile.getManifest();
		Attributes attributes = manifest.getMainAttributes();
		String className = attributes.getValue(Attributes.Name.MAIN_CLASS);
		jarFile.close();

		File file = new File(jarPath);
		URL[] urls = { file.toURI().toURL() };
		URLClassLoader loader = new URLClassLoader(urls);
		Class<?> cls = loader.loadClass(className);
		Method mcMain = cls.getDeclaredMethod("main", String[].class);
		String[] mcArgs = {};

		ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cls.getClassLoader());
		mcMain.invoke(null, (Object) mcArgs);
		Thread.currentThread().setContextClassLoader(oldCl);

		loader.close();
	}
}
