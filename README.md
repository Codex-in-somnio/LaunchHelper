# LaunchHelper

With LaunchHelper’s JAR, you can load a Java agent and launch another executable JAR **without adding any command-line arguments** — perfect for using [authlib-injector](https://github.com/yushijinhun/authlib-injector) on Multicraft panel servers.

## How to Use

1. Grab `LaunchHelper-{version}.jar` from the [Releases](https://github.com/Codex-in-somnio/LaunchHelper/releases).
   If you’re not sure whether to use the Windows or Linux build, try Linux first — if it errors out, switch to Windows (see Notes below).

2. Drop `LaunchHelper-{version}.jar` into your Minecraft server’s root directory.

3. Create a file named `launchhelper.properties` to configure LaunchHelper. Add something like:

   ```
   javaAgentJarPath=<path to authlib-injector JAR>
   javaAgentOptions=<Yggdrasil API URL>
   execJarPath=<path to server JAR>
   ```

   Example:

   ```
   javaAgentJarPath=authlib-injector.jar
   javaAgentOptions=https://example.com/api/yggdrasil/
   execJarPath=paper.jar
   ```

   (Alternatively, just start it once — a sample config file will be generated automatically.)

4. On your panel server, set it up so it runs `LaunchHelper-{version}.jar`.
   Or rename it to match whatever filename your panel expects. Check your panel’s docs or ask their support if you’re unsure.

## Notes

* LaunchHelper is **not cross-platform**. You need the right build for your OS, otherwise you’ll hit `java.lang.UnsatisfiedLinkError`.
* On Windows, if you’re running with just the JRE, make sure `attach.dll` can be found. Some JRE builds don’t include it, which causes a `no providers installed` error. If that happens, copy `attach.dll` from your JDK install (`jre/bin/attach.dll`) into your server’s root directory.
* Only tested on **Java 8**. Builds are Java 8–specific; other versions aren’t guaranteed to work.

## Building from Source

Requirements:

* Java JDK 1.8
* Maven

Run:

```
mvn clean package
```

The compiled JAR will be at `target/LaunchHelper-{version}.jar`.
