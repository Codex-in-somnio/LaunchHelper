# LaunchHelper

通过此Jar可以实现不用添加任何命令行参数载入Java agent并启动另一个可执行Jar，用于在Multicraft面板服使用[authlib-injector](https://github.com/yushijinhun/authlib-injector)。

## 构建

可以用Maven构建，环境要求：

* Java JDK 1.8
* Maven

```
mvn clean package
```

## 使用

1. 将LaunchHelper的Jar以及`lib`文件夹一起放在Minecraft服务端根目录

2. 创建一个命名为`launchhelper.properties`的文件，用于LaunchHelper的配置，填入：

   ```
   javaAgentJarPath=<authlib-injector的Jar路径>
   javaAgentOptions=<Yggdrasil API URL>
   execJarPath=<服务端Jar路径>
   ```

   例如：

   ```
   javaAgentJarPath=authlib-injector-1.1.26-41a7a47.jar
   javaAgentOptions=https://example.com/api/yggdrasil/
   execJarPath=paper-105.jar
   ```

3. 从LaunchHelper的Jar启动，例如将LaunchHelper的Jar重命名成自定义服务端需要的特定的文件名，具体需参考面板服方面的说明或咨询面板服客服。

## 注意事项

如果出现`java.lang.UnsatisfiedLinkError`的异常：

* 依赖的`tools.jar`不能跨平台使用，确保`tools.jar`是来自同一平台的；
* 如果用JRE运行，相关动态链接库需要能被链接到。