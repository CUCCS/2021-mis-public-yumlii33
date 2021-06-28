# H8 Android 缺陷应用漏洞攻击实验

**目录**

* [实验目的](#00)
* [实验环境](#01)
* [实验要求](#02)
* [实验过程](#03)
  * [Part 0 实验环境搭建](#030)
  * [Part 1 `Developer Backdoor`](#031)
  * [Part 2 `Insecure Logging`](#032)
  * [Part 3 `Android Application patching + Weak Auth`](#033)
  * [Part 4 `Exploiting Android Broadcast Receivers`](#034)
  * [Part 5 `Exploiting Android Content Provider`](#035)
* [实验总结](#04)
* [问题和解决](#05)
* [课后作业](#06)
* [参考资料](#07)



## <span id="00">实验目的</span>

- 理解 Android 经典的组件安全和数据安全相关代码缺陷原理和漏洞利用方法；
- 掌握 Android 模拟器运行环境搭建和 `ADB` 使用；

## <span id="01">实验环境</span>

- `Windows  10`

- [`Android-InsecureBankv2`](https://github.com/c4pr1c3/Android-InsecureBankv2)

- `python 2.7.18`

- `Android Studio 4.2.1`

- `apktool 5.2.1`

- `AVD`

  ![AVD信息](img/AVD信息.png)

## <span id="02">实验要求</span>

- 详细记录实验环境搭建过程；
- 至少完成以下实验：
  - `Developer Backdoor`
  - `Insecure Logging`
  - `Android Application patching + Weak Auth`
  - `Exploiting Android Broadcast Receivers`
  - `Exploiting Android Content Provider`
- （可选）使用不同于`Walkthroughs`中提供的工具或方法达到相同的漏洞利用攻击效果；
  - 推荐 [drozer](https://github.com/mwrlabs/drozer)

## <span id="03">实验过程</span>

### <span id="030">Part 0 实验环境搭建</span>

```
python2.7 环境
AndroLabServer 服务
InsecureBankv2.apk 应用安装
```

* 首先配置`python 2.7`环境

  * 在[官网](https://www.python.org/downloads/release/python-2718/)下载[python2.7.18](https://www.python.org/ftp/python/2.7.18/python-2.7.18.msi)并安装（一路`next`即可）

  * 添加环境变量`path`：`C:\Python27;C:\Python27\Scripts;`，并将这两条移动到最上面，然后在命令行使用`python`和`pip`命令时默认是`2.7`版本。

    ![python27环境配置成功](img/python27环境配置成功.png)

* 下载最新仓库到本地

  ```bash
  git clone https://github.com/c4pr1c3/Android-InsecureBankv2.git
  ```

* 安装必备软件

  ```python
  pip install install -r requirements.txt
  ```

* 运行 HTTP 服务器

  ```python 
  # 确保你已经安装了 python2。然后将当前目录更改为 AndroLabServer 文件夹
  # cd AndroLabServer
  
  python app.py
  ```

  ![AndroLabServer成功启动](img/AndroLabServer成功启动.png)

  `AndroLabServer`成功启动，也说明环境配置成功

* 查看可选参数

  ```python
  python app.py --help
  # InsecureBankv2 Backend-Server
  # Options:
  #   --host h    serve on host h (default 192.168.58.1)
  #   --port p    serve on port p (default 8888)
  #   --help      print this message
  ```

* 安装 `InsecureBankv2.apk` 到 `AVD`

  ```bash
  # 确保当前目录：...\Android-InsecureBankv2
  # 确保 avd已经启动
  
  adb install InsecureBankv2.apk
  ```

  ![InsecureBankv2-apk安装成功](img/InsecureBankv2-apk安装成功.png)

  安装成功

* 使用`...\Android-InsecureBankv2\Usage Guide.pdf`中的用户名密码测试登录

  ```bash
  User: dinesh 
  Password: Dinesh@123$ 
  User: jack 
  Password: Jack@123$
  ```

  ![测试登录成功](img/测试登录成功.png)

  登录成功

  


### <span id="031">Part 1`Developer Backdoor`</span>

#### 1.0 配置

* `Android-InsecureBankv2 apk` ——已经在`Part 0`中下载完成

* `JADX `

  ```bash
  # 由于上一个实验没有安装Jadx环境，所以先安装
  git clone https://github.com/skylot/jadx.git
  cd jadx
  gradlew.bat dist
  ```

  ![jadx安装成功](img/jadx安装成功.png)

* `dex2jar`——点击下载[当前最新版](https://sourceforge.net/projects/dex2jar/files/dex2jar-2.0.zip/download)到本地解压缩即可

#### 1.1 步骤

1. 解压缩`InsecureBankv2.apk`，直接`右键使用7zip解压缩`

   ![image-20210627144909435](img/解压缩apk.png)

2. 复制`classes.dex`文件到`dex2jar`目录下

   ![dex2jar目录](img/dex2jar目录.png)

   

3. 使用下面的命令将`.dex`文件转换为`.jar`文件

   ```bash
   d2j-dex2jar.bat classes.dex
   ```

   注意：本实验是在`windows`系统下操作，然而实验指南是`ubuntu`系统。`windows`系统下执行`.bat`文件而不是`.sh`文件。

   ![dex文件转jar文件](img/dex文件转jar文件.png)

   生成的`jar`文件为：`classes-dex2jar.jar`

4. 使用`JADX-GUI`打开`classes-dex2jar.jar`文件

   进入目录：`...jadx\build\jadx\bin\`，双击`jadx-gui.bat`，选择`classes-dex2jar.jar`文件：

   ![使用jadx打开jar文件](img/1-使用jadx打开jar文件.png)

   或者使用命令打开：

   ```bash
   jadx-gui.bat <path to classes-dex2jar.jar>
   ```

5. 下面的屏幕截图显示了`Android-InsecureBankv2`应用程序中开发人员后门的反编译代码，允许用户名为`devadmin`的用户与其他所有用户相比到达不同的端点。

   ![反编译代码中的dologin](img/1-反编译代码中的dologin.png)

   另外，如下图，左边为反编译结果，右边为仓库里的源代码，反编译结果和源代码有细微差别，但代码逻辑都一致

   ![反编译成功](img/反编译成功.png)

6. 进行测试

   ![录屏gif](img/1-预留后门测试录屏.gif)

   我们发现，任何用户都可以使用用户名`devadmin`，并使用任何密码登录应用程序，而不管密码是否有效。

#### 1.2 小结

开发者在开发应用的时候留了后门，如果知道了后门，非常容易绕过验证破解成功。



### <span id="032">Part 2 `Insecure Logging`</span>

#### 2.0 配置

* `Android-InsecureBankv2 apk` ——已经在`Part 0`中下载完成
* `Android SDK`——已在第五章实验中下载完成

#### 2.1 步骤

1. 将` InsecureBankv2.apk`文件复制到`Android SDK`的`platform-tools`文件夹中，然后使用`adb install  InsecureBankv2.apk`

   在`Part 0`中已经安装成功，可以直接使用，也可以卸载重装

2. 在命令行使用`adb logcat`查看日志

   直接输出在命令行不好分析。将输出到txt文件中，使用`ctrl+c`结束打印日志

   ![日志输出到文件](img/2-日志输出到文件.png)

3. 在模拟器上启动已安装的`InsecureBankv2`应用程序

4. 输入有效的凭据并点击`Login`

   ```
   User: dinesh
   Password: Dinesh@123$
   ```

   <img src="img/2-成功登录截图.png" alt="成功登录" width=300 /><img src="img/2-修改密码成功截图.png" alt="修改密码成功" width=300 />

   下面的屏幕截图显示了日志里记录的凭据：

   ![日志中SuccessfulLogin](img/2-日志中SuccessfulLogin.png)

5. 进入“更改密码”页面，输入新的凭据

   ```
   修改密钥为：Dinesh@321$
   ```

   下面的屏幕截图显示了在日志里记录的新凭据：

   ![2-日志中ChangePassword](img/2-日志中ChangePassword.png)

#### 2.2 小结

应用程序将用户的输入明文记录在日志中，如果被窃取到用户日志，即可获得登录过的账户和密钥。



### <span id="033">Part 3 `Android Application patching + Weak Auth`</span>

#### 3.0 配置

* `Android-InsecureBankv2 apk`——已下载

* `Android SDK`——已安装

* `apktool`——第七章实验已安装

* `SignApk`

  ```bash
  # 安装
  git clone https://github.com/appium-boneyard/sign.git
  ```

  但是在`windows`上安装好像有点问题，可以使用第七章的签名方法签名

#### 3.1 步骤

1. 安装`InsecureBankv2.apk`到模拟器（√ 前面的步骤中已经完成，不赘述）

2. 在模拟器中启动`InsecureBankv2.apk`（√ ）

3. 将`InsecureBankv2.apk`复制到`apktool`目录下，并输入命令反汇编

   因为第七章实验中已经将`apktool`的路径加入了环境变量，所以在任何目录下可以执行反编译命令

   ```bash
   # apktool 安装见第七章实验
   # 直接在"...\Android-InsecureBankv2\"目录下执行
   
   apktool d InsecureBankv2.apk -o InsecureBankv2-smali
   # -o <dir> 指定反汇编结果保存文件夹，默认为[ApkName]
   ```

   ![反汇编成功](img/反汇编成功.png)

   反汇编成功

4. 导航到`...\Android-InsecureBankv2\InsecureBankv2-smali\res\values\`，打开`String.xml`进行编辑

   ![修改string-xml-is-admin为yes](img/3-修改string-xml-is-admin为yes.png)

   修改好后保存

5. 导航回到`...\Android-InsecureBankv2`，重新编译：

   ```bash
   apktool b InsecureBankv2-smali
   ```

   ![重新生成apk文件](img/3-重新生成apk文件.png)

6. 对新生成的`apk`文件签名（不签名无法安装）

   ```bash
   cd InsecureBankv2-smali\dist
   C:\Users\mengli\AppData\Local\Android\Sdk\build-tools\30.0.2\apksigner sign --min-sdk-version 19 --ks D:\keystore0622.jks --out InsecureBankv2-signed.apk InsecureBankv2.apk
   ```

   ![重签名成功](img/3-重签名成功.png)

7. 安装`InsecureBankv2-signed.apk`到模拟器中

   ```bash
   # 先卸载
   adb install  InsecureBankv2-signed.apk
   ```

   ![重新安装](img/3-重新安装apk.png)

8. 在模拟器中启动新安装的`InsecureBankv2`，发现界面多了一个额外的`Create user`按钮。

   <img src="img/3-普通用户视角.png" alt="普通用户视角" width=300 /><img src="img/3-admin用户视角.png" alt="admin用户视角" width=300 />

   该按钮只对`admin`用户可见，所以之前看不到。

9. 尝试点击`Create user`按钮创建新用户，但并没有创建，这是假的创建用户按钮，并没有实现功能。

   <img src="img/3-点击创建用户按钮.png" alt="点击创建用户按钮" width=250 /><img src="img/3-点击创建用户服务器显示信息.png" alt="服务器显示信息" width=500 />

#### 3.2 小结

`apk`文件可以使用`apktool`等工具进行反编译，通过修改反编译出来的代码，再重打包重签名，可以轻易绕过脆弱认证，获取`admin`权限。

### <span id="034">Part 4 `Exploiting Android Broadcast Receivers`</span>

#### 4.0 配置

* `Android-InsecureBankv2 apk`——已在`Part 0`中安装
* `Android SDK`——已在`Part 0`中下载安装
* `apktool`——已在`实验七`中安装
* `JADX` ——已在`Part 1`中安装
* `dex2jar`——已在`Part 1`中安装

#### 4.1 步骤

1. 安装`InsecureBankv2.apk`到模拟器（ √ 前面的步骤中已经完成，不赘述）

2. 在模拟器中启动`InsecureBankv2.apk`（ √ ）

3. 将`InsecureBankv2.apk`复制到`apktool`目录下，并输入命令反编译（ √ ）

4. 打开解密的`AndroidManifest.xml`文件

   ![AndroidMainfest-xml](img/4-AndroidMainfest-xml.png)

5. 反编译`InsecureBankv2.apk`的步骤，已经在`Part 1 - 1`中完成

6. 反编译`InsecureBankv2.apk`的步骤，已经在`Part 1 - 2`中完成

7. 反编译`InsecureBankv2.apk`的步骤，已经在`Part 1 - 3`中完成

8. 反编译`InsecureBankv2.apk`的步骤，已经在`Part 1 - 4`中完成

9. 下面的屏幕截图显示了传递给前面显示的应用程序中声明的`Broadcast Receiver`的参数

   ![changepassword.xml](img/4-changepassword-xml.png)

   ![mybroadcastreceiver](img/4-mybroadcastreceiver.png)

10. 在模拟器上安装：`adb install InsecureBankv2.apk`（ √ )

11. 打开应用 （ √ ），注意并不需要输入用户名和密码登录

12. 在命令行中打开`adb shell`

    ![adb-shell](img/4-adb-shell.png)

13. 输入下面的命令（绕过登录直接修改密码）

    ```bash
    am broadcast -a theBroadcast -n com.android.insecurebankv2/com.android.insecurebankv2.MyBroadCastReceiver --es phonenumber 5554 --es newpass Dinesh@123!
    ```

    ![在命令行中输入am命令](img/4-在命令行中输入am命令.png)

14. 回到模拟器中，打开`Messages`应用，发现

    上述输入的命令自动发送短信联系上述广播接收器，并发送带有密码的短信文本（左图）

    <img src="img/4-messages应用发送修改请求和接收到修改成功的响应.png" alt="messages应用发送修改请求和接收到修改成功的响应" width=280 /><img src="img/4-修改密码会收到修改成功的短信提示.png" alt="修改密码会收到修改成功的短信提示" width=280 />

15. 手动修改，也会有`修改请求`和`修改成功`的短信记录（上面 右图）

#### 4.2 小结

可以使用工具绕过登录直接修改用户密钥。

测试还发现，在`app`中手动修改密码也会有`修改请求`和`修改成功`的短信记录，如果窃取到短信记录，就会泄露信息。

### <span id="035">Part 5 `Exploiting Android Content Provider`</span>

#### 5.0 配置

* `Android-InsecureBankv2 apk`——已在`Part 0`中安装
* `Android SDK`——已在`Part 0`中下载安装
* `apktool`——已在`实验七`中安装
* `JADX` ——已在`Part 1`中安装
* `dex2jar`——已在`Part 1`中安装

#### 5.1 步骤

1. 使用`adb install InsecureBankv2.apk`在模拟器中安装应用（ √ ）

2. 在模拟器上启动已安装的`InsecureBankv2`应用程序

3. 先使用`dinesh/Dinesh@123$`登录应用，再使用`jack/Jack@123$`登录应用

4. 使用`apktool`工具反汇编`InsecureBankv2.apk`（ √ ）

5. 打开`AndroidMainfest.xml`文件，找到`TrackUserContentProvider`

   ![TrackUserContentProvider](img/TrackUserContentProvider.png)

6. 反编译步骤 （√ ）

7. 反编译步骤 （√ ）

8. 反编译步骤 （√ ）

9. 反编译步骤 （√ ）

10. 在反编译文件中发现，传过来的数据会被保存到设备的`sql`数据库中：

    ![反编译的TrackUserContentProvider](img/5-TrackUserContentProvider.png)

11. 安装`InsecureBankv2.apk` （ √ ）

12. 启动`InsecureBankv2`应用

13. 打开命令行

14. 输入`adb shell`进入`adb命令行`

15. 输入下命令

    ```bash
    content query --uri content://com.android.insecurebankv2.TrackUserContentProvider/trackerusers
    ```

    ![登录记录](img/5-登录记录.png)

    发现登录记录`id= , name= `以明文的方式记录在设备上

#### 5.2 小结

应用程序以明文的方式保存`登录记录`，如果得到设备或者设备上的记录，就能得到存在的用户的用户名，再使用`Part 4`中的方法即可绕过登录修改用户密码。



## <span id="04">实验总结</span>

## <span id="05">问题和解决</span>

- [x] **Q0：** `sh d2j-dex2jar.sh classes.dex`命令执行失败

  ![sh文件执行失败](img/sh文件执行失败.png)

  **A0：** 在windows系统下要执行`.bat`命令：`d2j-dex2jar.bat classes.dex`

- [x] **Q1：** `mp4`转`gif`失败，原始工具不好用了。

  **A1：** 发现新的录屏和`GIF`制作软件[ScreenToGif](https://www.screentogif.com/)，非常好用。另外`腾讯QQ`自带的录屏功能也很好用。

## <span id="06">课后作业</span>

## <span id="07">参考资料</span>

* [课本 · 第八章实验 · 移动互联网安全](https://c4pr1c3.github.io/cuc-mis/chap0x08/exp.html)
* [课件 · 第八章  · 移动互联网安全](https://c4pr1c3.github.io/cuc-mis-ppt/chap0x08.md.html)
* [移动互联网安全（2021）_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1rr4y1A7nz?from=search&seid=6142859782746666446)
* []()
* []()
* []()



























































