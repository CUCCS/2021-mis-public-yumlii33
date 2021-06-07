# H6 Android Hello World 应用开发和 ADB 实验

**目录**

* 实验目的
* 实验环境
* 实验要求
* 实验过程
  * `ADB`实验
  * `Hello World v1`
  * `Hello World v2`
* 实验总结
* 问题和解决
* 参考资料



## 实验目的

* 
* 

## 实验环境

* 

## 实验要求

- ✅`ADB`实验
- ✅`Hello World v1`
- ✅`Hello World v2`

## 实验过程

### Part 0 `ADB`实验

#### 0.0 命令行

* 命令行连接模拟器

  ```bash
  # 查看开启的模拟器
  adb devices
  # 如果运行失败，提示没有adb命令，要手动将adb的路径加到环境变量中，详细见Q0
  
  # 连接模拟器终端
  adb -s emulator-5554 shell
  
  # 输出环境变量PATH
  echo $PATH
  
  # 查看当前系统版本（on Android 7.1.1 (Google APIs）x86_64 ，部分系统中没有提供 uname）
  uname -a
  ```

  ![连接模拟器命令](img/连接模拟器命令.png)

* 基本命令

  ```bash
  # 与linux基本相同。因为是Android模拟器是基于linux内核开发的。
  # cd / ls / pwd / ps / grep /kill / cat / chmod /chown / mkdir /echo /
  # touch / du / df / set / uptime / top / ifconfig / more
  ```

* 进阶命令

  ```bash
  # 与linux基本相同
  # su / iptables / iftop / lsof / mount / vmstat / wpa_cli / sqlite3
  
  # 将文件复制到设备/从设备复制文件
  # 因为只开启了一台Android设备，所以没有显示指定模拟器
  adb pull remote local
  adb push local remote
  
  # 安装应用
  adb install path_to_apk
  ```

  ![adb-pull-and-push](img/adb-pull-and-push.png)

#### 0.1 Activity Manger（am）

在Android中，除了从界面上启动程序之外，还可以从命令行启动程序，使用的是命令行工具am (activity manager ).

* 实例命令：

  ```shell
  # am 要在adb命令行中执行
  adb -s emulator-5554 shell
  
  # Camera（照相机）的启动方法为:
  am start -n com.android.camera/com.android.camera.Camera
  
  # Browser（浏览器）的启动方法为：
  am start -n com.android.browser/com.android.browser.BrowserActivity
  
  # 本台机器没有配置camera和Broswer功能，所以代码执行结果是 not exist
  
  # 启动浏览器 :
  am start -a android.intent.action.VIEW -d  http://sec.cuc.edu.cn/
  
  # 拨打电话 :
  am start -a android.intent.action.CALL -d tel:10086
  
  # 发短信：
  am start -a android.intent.action.SENDTO -d sms:10086 --es sms_body ye --ez exit_on_sent true
  ```

* 实例执行过程录屏：

  <img src="img/amExample-.gif" alt="amExample" width=400 />

  对应命令执行过程：

  ![amExample命令](img/amExample命令.png)

#### 0.2 软件包管理器（pm）

在 adb shell 中，可以使用[软件包管理器 (pm)](https://developer.android.com/studio/command-line/adb.html#pm) 工具发出命令，以对设备上安装的应用软件包进行操作和查询。在 shell 中，此语法为：

```bash
pm command
```

也可以直接从 adb 发出软件包管理器命令，无需进入远程 shell。例如：

```bash
adb shell pm command
adb shell pm uninstall com.example.MyApp
```

其他：

```shell
# 获取屏幕截图
screencap /sdcard/screen.png
adb shell screencap /sdcard/screen.png

# 屏幕录制
screenrecord /sdcard/demo.mp4
adb shell screenrecord /sdcard/demo.mp4
# 按Ctrl+c结束录屏
```

![image-20210607110739620](C:\Users\mengli\AppData\Roaming\Typora\typora-user-images\image-20210607110739620.png)

#### 0.3 其他adb实验

```bash
# 常用的按键对应的KEY_CODE
adb shell input keyevent 22 //焦点去到发送键
adb shell input keyevent 66 //回车按下

adb shell input keyevent 4 // 物理返回键
adb shell input keyevent 3 // 物理HOME键

# android 4.0+
$ input
usage: input ...
       input text <string>
       input keyevent <key code number or name>
       input tap <x> <y>
       input swipe <x1> <y1> <x2> <y2>
       
       
# 输入 hello
adb input
```

### Part 1 `Hello World v1`

#### 1.0 运行展示：

<img src="img/HelloWorldV.gif" alt="HelloWorldV1.gif" width=400 />

#### 1.1 代码编写

阅读Android官方给出的[使用Android Studio一步一步创建并运行的Hello World程序指南](https://developer.android.google.cn/training/basics/firstapp/creating-project.html)，按照官方指南编写。实际编写中要注意一下几点：

* 为了v2版的示例代码提供上下文环境的一个示范，要求以下设置：

  ```java
  Application Name设置为：MISDemo
  Company Domain设置为：cuc.edu.cn
  ```

* 由于实验中进行了删除和多次添加模块，所以模块id和默认不一致，本实验中为：

  ```java
  // MainActivity.java
  // EditText editText = (EditText) findViewById(R.id.editText);
  EditText editText = (EditText) findViewById(R.id.editTextTextPersonName3);
   
  // DisplayMessageActivity.java
  // TextView textView = findViewById(R.id.textView);
  TextView textView = findViewById(R.id.textView3);
  ```
  
  通过对应的`xml`文件查看使用的模块的`id`：
  
  ![ditTextPersonName3](img/editText-id.png)
  
  ![textView3](img/textView3-id.png)

#### 1.2 回答问题

- [✅] 按照向导创建的工程在模拟器里运行成功的前提下，生成的APK文件在哪儿保存的？

  ```bash
  \Users\[username]\AndroidStudioProjects\MISDemo\app\buid\outputs\apk\debug
  ```

  ![APK位置](img/APK位置.png)

- [✅] 使用adb shell是否可以绕过MainActivity页面直接“唤起”第二个DisplayMessageActivity页面？是否可以在直接唤起的这个DisplayMessageActivity页面上显示自定义的一段文字，比如：你好移动互联网安全

  可以

- [✅] 如何实现在真机上运行你开发的这个Hello World程序？

  [在硬件设备上运行应用  |  Android 开发者  |  Android Developers](https://developer.android.google.cn/studio/run/device)

  

- [✅] 如何修改代码实现通过 `adb shell am start -a android.intent.action.VIEW -d http://sec.cuc.edu.cn/` 可以让我们的cuc.edu.cn.misdemo程序出现在“用于打开浏览器的应用程序选择列表”？

  

- [✅] 如何修改应用程序默认图标？

  

- [✅] 如何修改代码使得应用程序图标在手机主屏幕上实现隐藏？



### Part 2 `Hello World v2`

## 实验总结

## 问题和解决

- [x] Q0：在命令行输入`adb devices`显示无此命令

  A0：将`C:\Users\mengli\AppData\Local\Android\Sdk\platform-tools`添加到环境变量`path`下。

- [x] Q1：

## 参考资料

* []()
* []()
* 







### Hello World v2



![image-20210606165029269](C:\Users\mengli\AppData\Roaming\Typora\typora-user-images\image-20210606165029269.png)



![image-20210606165118791](C:\Users\mengli\AppData\Roaming\Typora\typora-user-images\image-20210606165118791.png)





## Q&A

Q1：在命令行输入`adb devices`显示无此命令

A1：将`C:\Users\mengli\AppData\Local\Android\Sdk\platform-tools`添加到环境变量path下。



