# H5 Android 模拟器环境搭建笔记

## 实验要求

* Android 模拟器环境搭建

## 实验环境

* windows 10 
* Android Studio 4.1.3

## 实验过程

### Part 0 安装和配置Java SDK

Android Studio 安装后已经内置了 Java 运行时环境，以 Android 3.2.1 为例，通过菜单 File -> Project Structure -> SDK Location 可以查看到 Android Studio 在编译当前项目时使用的 JDK 目录。

### Part 1 安装Android Studio

通过官方网站下载[Android Studio](https://developer.android.com/studio/) 最新版本并根据提示安装。安装 Android Studio 的过程中可能会提示联网下载更新 Android SDK。

初次启动提示找不到`Android SDK`，采用**跳过检测**的方法解决。（解决过程记录在[问题和解决 Q1](#jump1)）

安装成功：

![image-20210322110804821](C:\Users\mengli\AppData\Roaming\Typora\typora-user-images\image-20210322110804821.png)

### Part 2 下载安装Android SDK



### Part 3 配置Android模拟器运行环境

### Part 4 配置Gradle编译环境



## 实验总结

## 问题和解决

- [x] <span id= "jump1">Q1</span>：初次安装`Android Studio`，启动后报错：`Unable to access Android SDK add-on list`。

  ![image-20210322102536617](C:\Users\mengli\AppData\Roaming\Typora\typora-user-images\image-20210322102536617.png)

  原因：`Android Studio`启动后会在默认路径下检测是否有`Android SDK`，如果没有的话就会报上述错误。

  A1：因为还没有安装SDK，所以采取**跳过检测**的方法解决此问题。

  * 在`Android Studio`的安装目录下，找到`\bin\idea.properties`

  * 在尾行添加`disable.android.first.run=true`，表示初次启动不检测SDK

- [ ] Q2：

## 参考资料

* [移动网络安全第五章实验指南](https://c4pr1c3.github.io/cuc-mis/chap0x05/exp.html)
* [Android Studio报错unable to access android sdk add-on list解决方案](https://blog.csdn.net/u010358168/article/details/81535307)

