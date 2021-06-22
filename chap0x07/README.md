# H7 Hello World v2版逆向实验

**目录**

* [实验目的](#00)
* [实验环境](#01)
* [实验要求](#02)
* [实验过程](#03)
  * [Part 0 生成app-release.apk](#030)
  * [Part 1 smali代码分析](#031)
  * [Part 2 重打包](#032)
  * [Part 3 重签名](#033)
  * [Part 4 破解效果展示](#034)
* [实验总结](#04)
* [问题和解决](#05)
* [课后作业](#06)
* [参考资料](#07)



## <span id="00">实验目的</span>

## <span id="01">实验环境</span>

* Windows 10
* Android Studio 4.2.1(Andriod Studio 4.1.3导入项目失败，所以更新到最新版本 )

## <span id="02">实验要求</span>

- [✅]使用apktool反汇编上一章实验中我们开发的Hello World v2版程序，对比Java源代码和smali汇编代码之间的一一对应关系。
- [✅]对Hello World v2版程序生成的APK文件进行程序图标替换，并进行重打包，要求可以安装到一台未安装过Hello World v2版程序的Android模拟器中。
- [✅]尝试安装重打包版Hello World v2到一台已经安装过原版Hello World v2程序的模拟器中，观察出错信息并解释原因。
- [✅]去掉Hello World v2版程序中DisplayMessageActivity.java代码中的那2行日志打印语句后编译出一个新的apk文件，假设文件名是：misdemo-v3.apk，尝试使用课件中介绍的几种软件逆向分析方法来破解我们的认证算法。

## <span id="03">实验过程</span>

### <span id="030">Part 0 生成app-release.apk</span>

* 检出[Deliberately Vulnerable Android Hello World](https://github.com/c4pr1c3/DVAHW)最新版代码，在Android Studio中导入该项目；

* `Build` -> `Generate Signed Bundle or APK`

  ![生成发布版APK](img/生成发布版APK.png)

* 选择`APK`

* 选择密钥库路径和密钥别名，输入密钥库密码和`alias`密码(如果没有创建密钥库，则先`CreateNewKeyStore`)

* 生成发布版`apk`文件

  ```说明
  v1和v2的签名使用
  1）只勾选v1签名并不会影响什么，但是在7.0上不会使用更安全的验证方式
  2）只勾选V2签名7.0以下会直接安装完显示未安装，7.0以上则使用了V2的方式验证
  3）同时勾选V1和V2则所有机型都没问题
  ```

  <img alt="选择apk" src="img/选择apk.png" width=350 /><img src="img/选择密钥库和密钥.png" alt="选择密钥库和密钥" width=350 /><img src="img/选择release.png" alt="选择release" width=350 />

  生成成功：

  ![BUILDSUCCESSFUL](img/BUILDSUCCESSFUL.png)

* 生成的发布版`apk`文件位于项目根目录下相对路径：`app/app-release.apk`；

  ![生成的发布版apk](img/生成的发布版apk.png)

* 

### <span id="031">Part 1 smali代码分析</span>

### <span id="032">Part 2 重打包</span>

### <span id="033">Part 3 重签名</span>

### <span id="034">Part 4 破解效果展示</span>

## <span id="04">实验总结</span>



## <span id="05">问题和解决</span>

- [ ] **Q0：**[Deliberately Vulnerable Android Hello World](https://github.com/c4pr1c3/DVAHW)最新版代码，在Android Studio中导入该项目时报错：

  ```bash
  10:47	Gradle sync failed: This version of the Android Support plugin for IntelliJ IDEA (or Android Studio) cannot open this project, please retry with version 4.2 or newer.
  Consult IDE log for more details (Help | Show Log) (2 m 4 s 968 ms)
  ```

  **A0：**`Android Studio`版本太低，下载最新版`Android Studio 4.2.1`，然后打开项目，待`Android Studio`自动下载一些插件后，项目成功导入。

  ![项目成功导入](img/项目成功导入.png)

- [ ] **Q1：**创建密钥库和密钥时报错

  `Android studio 警告: PKCS12 密钥库不支持其他存储和密钥口令。正在忽略用户指定的-keypass值`
  
  **A1：**参考网上博客，在创建时，`key store`和`key`的`Password`必须一致，这可能是因为`keytool`版本的问题，后续可以通过`keytool`命令修改`Password`。
  
  <img src="img/newkeystore.png" alt="创建新密钥库" width=500 />
  
  另外不要勾选`Remember Password`，手动输入。
  
  <img src="img/不要勾选RememberPassword.png" alt="不要勾选RememberPassword" width=500  />

## <span id="06">课后作业</span>

## <span id="07">参考资料</span>

* [课本 · 第六章实验 · 移动互联网安全](https://c4pr1c3.github.io/cuc-mis/chap0x06/exp.html)
* [课件 · 第六章  · 移动互联网安全](https://c4pr1c3.github.io/cuc-mis-ppt/chap0x06.md.html)
* [移动互联网安全（2021）_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1rr4y1A7nz?from=search&seid=6142859782746666446)
* [android签名jks转pkcs12（JKS 密钥库使用专用格式。建议使用 “keytool -importkeystore xx pkcs12“ 迁移到行业标准格式 PKCS12）_xiaoerye的专栏-CSDN博客](https://blog.csdn.net/xiaoerye/article/details/114284426)
* []()
* []()





