>**⚠ Depreciated Project ⚠**  
>LaunchHelper-Revived is only made so that it works with later versions.  
>It is recommended to use **[Wrapidly](https://github.com/RayfieldMC/Wrapidly)** which has more features.

# LaunchHelper (Java 21 Fork 🚀)

This is a modernized fork of [Codex-in-somnio/LaunchHelper](https://github.com/Codex-in-somnio/LaunchHelper), updated to work on **Java 17+ / Java 21** and inside containerized environments (e.g. **Pterodactyl**, **Multicraft**).  

Unlike the original, this fork no longer uses `com.sun.tools.attach.VirtualMachine` (which fails on modern JVMs + panels). Instead, it cleanly spawns your target server process with `-javaagent`.

---

## ✨ Features
- ✅ Works on **Java 21** (tested)  
- ✅ Compatible with **Pterodactyl / Multicraft panels**  
- ✅ Automatic `launchhelper.properties` generation if missing  
- ✅ Passes `-javaagent` to your server jar without touching panel startup flags  
- ✅ Cross-platform (Windows / Linux, no `attach.dll` headaches)  

---

## ⚡ How to Use

1. Download `LaunchHelper-{version}.jar` from [Releases](../../releases).  

2. Place it in the **root of your server folder**.  

3. Configure `launchhelper.properties` (auto-created if missing):

   ```properties
   javaAgentJarPath=authlib-injector.jar
   javaAgentOptions=https://example.com/api/yggdrasil/
   execJarPath=paper.jar
   ```

4. Set your panel startup jar to `LaunchHelper-{version}.jar`.
   (Rename if needed to match what your host expects, e.g. `server.jar`.)

---

## 🔧 Building from Source

Requirements:

* Java 21+
* Maven

Build:

```bash
mvn clean package
```

The compiled jar will be at:

```
target/LaunchHelper-{version}.jar
```

---

## 📜 Original README.md (for reference)

> With LaunchHelper’s JAR, you can load a Java agent and launch another executable JAR **without adding any command-line arguments** — perfect for using [authlib-injector](https://github.com/yushijinhun/authlib-injector) on Multicraft panel servers.
>
> ### How to Use
>
> 1. Grab `LaunchHelper-{version}.jar` from the [Releases](https://github.com/Codex-in-somnio/LaunchHelper/releases).
>    If you’re not sure whether to use the Windows or Linux build, try Linux first — if it errors out, switch to Windows (see Notes below).
>
> 2. Drop `LaunchHelper-{version}.jar` into your Minecraft server’s root directory.
>
> 3. Create a file named `launchhelper.properties` to configure LaunchHelper. Add something like:
>
>    ```properties
>    javaAgentJarPath=<path to authlib-injector JAR>
>    javaAgentOptions=<Yggdrasil API URL>
>    execJarPath=<path to server JAR>
>    ```
>
> 4. On your panel server, set it up so it runs `LaunchHelper-{version}.jar`.
>
> ### Notes
>
> * LaunchHelper is **not cross-platform**. You need the right build for your OS, otherwise you’ll hit `java.lang.UnsatisfiedLinkError`.
> * On Windows, if you’re running with just the JRE, make sure `attach.dll` can be found. Some JRE builds don’t include it, which causes a `no providers installed` error.
> * Only tested on **Java 8**. Builds are Java 8–specific; other versions aren’t guaranteed to work.
>
> ### Building from Source
>
> Requirements:
>
> * Java JDK 1.8
> * Maven
>
> Run:
>
> ```bash
> mvn clean package
> ```
>
> The compiled JAR will be at `target/LaunchHelper-{version}.jar`.

