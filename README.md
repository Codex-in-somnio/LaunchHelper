# LaunchHelper

通过LaunchHelper的Jar可以实现不用添加任何命令行参数载入Java agent并启动另一个可执行Jar，用于在Multicraft面板服使用[authlib-injector](https://github.com/yushijinhun/authlib-injector)。

## 使用方法

1. 从[Release](https://github.com/Codex-in-somnio/LaunchHelper/releases)获取`LaunchHelper-{版本}.jar`，如果不确定用Windows还是Linux的版本，可以先尝试Linux版本，出错再尝试Windows版本（详见下面的注意事项）；

2. 将`LaunchHelper-{版本}.jar`放在Minecraft服务端根目录；

3. 创建一个命名为`launchhelper.properties`的文件，用于LaunchHelper的配置，填入：

   ```
   javaAgentJarPath=<authlib-injector的Jar路径>
   javaAgentOptions=<Yggdrasil API URL>
   execJarPath=<服务端Jar路径>
   ```

   例如：

   ```
   javaAgentJarPath=authlib-injector.jar
   javaAgentOptions=https://example.com/api/yggdrasil/
   execJarPath=paper.jar
   ```

   也可以直接先启动一次（参考下一步），使样例配置文件自动生成；

4. 在面板服上指定由`LaunchHelper-{版本}.jar`启动，或者重命名成自定义服务端需要的特定的文件名，具体需参考面板服方面的说明或咨询面板服客服。

## 注意事项

* LaunchHelper不能跨平台使用，需要使用和平台对应的Jar，跨平台会出现`java.lang.UnsatisfiedLinkError`；
* 如果在Windows环境下用JRE运行，相关动态链接库（`attach.dll`）需要确保能被链接到，经测试发现Windows环境下JRE可能不包含此DLL；遇到这种情况时会出现`no providers installed`的错误消息，可以从JDK安装目录下找到`jre/bin/attach.dll`复制一份到工作目录（服务端根目录）然后启动；
* 目前只测试过Java 8，只有针对Java 8的版本，其他Java版本不保证可用。
* Java9及以上版本需添加-Djdk.attach.allowAttachSelf=true参数

## 从源码构建

可以用Maven构建，环境要求：

- Java JDK 1.8
- Maven

执行以下命令构建：

```
mvn clean package
```

构建结果位于`target/LaunchHelper-{版本}.jar`