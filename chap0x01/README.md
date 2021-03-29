# H1 OpenWrt 虚拟机搭建

**目录**

* [实验目的](#10)
* [实验环境](#00)
* [实验要求](#01)
* [实验过程](#02)
  * [Part 0 复习VirtualBox的配置和使用](020)
  * [Part 1 下载安装OpenWrt](#021)
  * [Part 2 配置无线网卡使其正常工作](#022)
  * [023](#023)
  * [024](#024)
  * [025](#025)
  * [026](#026)
* [实验总结](#03)
* [问题和解决](#04)
* [课后作业](#05)
* [参考资料](#06)



## <span id = "10">实验目的</span>

- 熟悉基于 OpenWrt 的无线接入点（AP）配置
- 为第二章、第三章和第四章实验准备好「无线软 AP」环境

## <span id = "00">实验环境</span>

- 可以开启监听模式、AP 模式和数据帧注入功能的 USB 无线网卡
- Virtualbox 6.1.18
- Kali 2020.3

## <span id = "01">实验要求</span>

- [ ] 对照 [第一章 实验](https://c4pr1c3.github.io/cuc-mis/chap0x01/exp.html) `无线路由器/无线接入点（AP）配置` 列的功能清单，找到在 OpenWrt 中的配置界面并截图证明；
- [ ] 记录环境搭建步骤；
- [ ] 如果 USB 无线网卡能在 `OpenWrt` 中正常工作，则截图证明；
- [ ] 如果 USB 无线网卡不能在 `OpenWrt` 中正常工作，截图并分析可能的故障原因并给出可能的解决方法。



## <span id = "02">实验过程</span>

### <span id = "020">Part 0</span> 复习VirtualBox的配置和使用

* 虚拟机镜像列表

  ![虚拟镜像列表](img/虚拟镜像列表.png)

* 设置虚拟机和宿主机的文件共享，实现宿主机和虚拟机的双向文件共享。

  * 设置共享文件夹

  * 使用`sftp`协议实现双向文件共享。

    连接远程服务器（这里使用windows主机连接kali虚拟机）：`sftp user@ip`

    上传：`put [本地文件的地址] ([服务器上文件存储位置])`

    下载：`get [服务器上文件存储的位置] ([本地要存储的位置])`

    ![sftp文件共享](img/sftp文件共享.png)

* 配置`ssh`远程桌面连接

  参考上学期的实验报告配置：（上学期实验报告有小错误，已经修改）

  ![配置远程桌面](img/配置远程桌面.png)

* 虚拟机镜像备份和还原的方法

  * 选择`生成快照`,完成备份，之后可以通过`恢复备份`回到之前备份时的状态。

    ![快照备份方式](img/快照备份方式.png)

  * 刚创建好一个虚拟硬盘时，将虚拟硬盘更改为`多重加载`模式，之后如果想要获得一个全新的虚拟机即可选择`从已有虚拟硬盘创建`，也算是一种实现备份和还原的方法。

    

* 熟悉虚拟机基本网络配置，了解不同联网模式的典型应用场景

### <span id = "021">Part 1 </span>下载安装OpenWrt

* 在windows上执行老师提供的[bash脚本（稍微修改了一下）](code/setup.sh)【详细说明见[问题和解决——Q1](#041)】

  成功执行：（有一个报错是因为之前执行过一次，所以vdi文件已经创建好了）

  ![脚本执行成功](img/脚本执行成功.png)

* 执行完成后列表中出现自动创建好的虚拟机：

  ![openwrt安装成功](img/openwrt安装成功.png)

* 启动虚拟机：

  ![启动OpenWrt终端控制台](img\启动OpenWrt终端控制台.png)

* 修改`/etc/config/network` 配置文件来设置远程管理专用网卡的 IP 地址：

  将hostonly网卡的IP地址设为`192.168.56.11`（也可以修改成其他的IP，习惯性用这个IP）

  ```
  config interface 'lan'
      option type 'bridge'
      option ifname 'eth0'
      option proto 'static'
      option ipaddr '192.168.56.11' 
      option netmask '255.255.255.0'
      option ip6assign '60'
  ```

  执行命令 `ifdown eth0 && ifup eth0`，重启网卡，使配置生效。

* 输入`ip a`检查是否配置成功：

  如图，设置成功。

  ![网络配置成功](img/网络配置成功.png)

* 现在可以通过windows主机上访问`192.168.56.11`：

  ![可以访问](D:\Project-mis\2021-mis-public-yumlii33\chap0x01\img\可以访问.PNG)

* 安装`LuCi`软件包：

  对于路由器操作系统 `OpenWrt` 来说，通过 `SSH` 方式来管理，常见的远程管理方式是通过 `LuCi` 这个网页形式的管理界面来完成。

  ```
  # 更新 opkg 本地缓存,每次重启系统都要更新
  opkg update
  # 安装 luci
  opkg install luci
  ```

  安装成功的后访问：
  ![可以访问](img\可以访问.PNG)
  
* 此时还没有Wireless选项

  ![image-20210329154242628](C:\Users\mengli\AppData\Roaming\Typora\typora-user-images\image-20210329154242628.png)

### <span id = "022">Part 2</span> 配置无线网卡使其正常工作

* 插上无线网卡,检查能否识别设备：

  在显示窗口的右下角USB设备看到刚插入的设备，即可以识别，勾选上刚插上的无线网卡：

  ![插上无线网卡](img/插上无线网卡.png)


* 在OpenWrt中安装lsusb 用以查看无线网卡的信息：

  ```
  # 每次重启 OpenWrt 之后，安装软件包或使用搜索命令之前均需要执行一次 opkg update
  opkg update && opkg install usbutils
  ```

* 查看无线网卡的驱动是否安装好：

  ```bash
  # 查看 USB 外设的标识信息
  lsusb
  
  # 查看 USB 外设的驱动加载情况
  lsusb -t
  ```

  发现能识别到无线网卡设备，但是没有加载驱动:

  ![lsusb查看发现没有驱动](img/lsusb查看发现没有驱动.PNG)
  
  执行命令`ifconfig -a`或`ip link`也无法看到有效的无线网络。
  
  ![iplink没有wlan](img/iplink没有wlan.png)
  
* 安装对应驱动

  通过`lsusb`的执行结果可知：无线网卡的芯片名称为`RTL8811AU`。

  通过 `opkg find kmod-* | grep rtl88` 命令可以快速查找可能包含指定芯片名称的驱动程序包：

  发现只有`RTL8812au`型号的驱动，（由于在搜资料的时候，有博客说可以兼容，所以下载`RTL8812AU`型号的驱动

  ![下载kmod-rtl0012au-ct](img/下载kmod-rtl0012au-ct.png)

* 检查驱动是否安装成功

  再次执行`lsusb -t`,发现成功加载驱动：

  ![识别了驱动rtl8812au](img/识别了驱动rtl8812au.png)

  再次执行`ip link`，可以验证系统已经可以识别此块无线网卡：

  ![可以识别无线网卡网络](img/可以识别无线网卡网络.png)

* `lsusb -v`执行结果：[`lsusb -v`](code/lsusb-v-result.txt)

* `iw dev`执行结果：

  ```
  root@OpenWrt:~# iw dev
  phy#0
          Interface wlan0
                  ifindex 5
                  wdev 0x1
                  addr 40:a5:ef:de:ec:cd
                  type managed
                  txpower 12.00 dBm
  ```

* `iw phy`执行结果：[`iw phy`](code/iw-phy-result.txt)

  ![支持AP模式](img/支持AP模式.png)

  可以看到此款无线网卡支持AP模式。

* `reboot`重启OpenWrt，网页上`NetWork`里多了一个`Wireless`选项：

  ![image-20210329165628876](C:\Users\mengli\AppData\Roaming\Typora\typora-user-images\image-20210329165628876.png)

### <span id = "023">Part 3</span> 开启AP功能

* 配置更安全的无线完全机制

  默认情况下，OpenWrt 只支持 `WEP` 系列过时的无线安全机制。为了让 OpenWrt 支持 `WPA` 系列更安全的无线安全机制，还需要额外安装 2 个软件包：`wpa-supplicant` 和 `hostapd` 。

  ```
  # wpa-supplicant 提供 WPA 客户端认证
  # hostapd 提供 AP 或 ad-hoc 模式的 WPA 认证
  opkg install hostapd wpa-supplicant
  ```

  安装后重启。

为了使用其他无线客户端可以正确发现新创建的无线网络，以下还有 3 点需要额外注意的特殊配置注意事项：

* 无线网络的详细配置界面里的 `Interface Configuration` 表单里 `Network` 记得勾选 `wan` ；

  ![选择wan](img/选择wan.png)

* 虚拟机的 WAN 网卡对应的虚拟网络类型必须设置为 `NAT` 而不能使用 `NatNetwork` ，无线客户端连入无线网络后才可以正常上网。

  ![网卡NAT](img/网卡NAT.png)

* 不要使用 Auto 模式的信道选择和信号强度，[均手工指定](https://forum.archive.openwrt.org/viewtopic.php?id=37896) 才可以。

  ![手工指定](img/手工指定.png)

### <span id = "024">Part 4 </span>找到OpenWrt配置并截图

* 重置和恢复AP到出厂默认设置状态
* 设置AP的管理员用户名和密码
* 设置SSID广播和非广播模式
* 配置不同的加密方式
* 设置AP管理密码
* 配置无线路由器使用自定义的DNS解析服务器
* 配置DHCP和禁用DHCP
* 开启路由器/AP的日志记录功能（对指定事件记录）
* 配置AP隔离(WLAN划分)功能
* 设置MAC地址过滤规则（ACL地址过滤器）
* 查看WPS功能的支持情况
* 查看AP/无线路由器支持哪些工作模式

### <span id = "025">Part 5 </span>

* 

### <span id = "026">Part 6 </span>

## <span id = "03">实验总结</span>

## <span id = "04">问题和解决</span>

- [x] **Q0：在kali里面下载时，显示无法找到什么什么，但是能够ping通。上网搜博客，修改/etc/resolv.conf之后，不行，修改回来后，连ping都ping不通了。**

  A0：修改文件/etc/network/interfaces，设置dhcp，然后重启网络，sudo /etc/init.d/networking restart，就可以ping通了，并且可以访问之前不能下载的链接。

- [x] **<span id="041">Q1</span>：运行脚本安装openwrt时，修改脚本以及windows的配置，使得安装脚本在windows上成功运行。**

  * 运行环境：`Git Bash`可以提供bash脚本的运行环境，但是会有一些命令的缺失。

  * git bash没有wget命令：[下载wget安装包](https://eternallybored.org/misc/wget/)，并将wget.exe拷贝到C:\Program Files\Git\mingw64\bin\下面。

  * 找不到VBoxManage命令：在电脑上找到VBoxManage.exe，然后将其路径加到环境变量里。

    ![添加VBoxManage环境变量](img/添加VBoxManage环境变量.png)    

    

  * 创建虚拟机失败：删掉脚本里创建虚拟机是的分组选项，因为自己的电脑没有设置分组。       

    ![没找到分组报错](img/没找到分组报错.png)

  *    原始脚本中第 1 块网卡的界面名称为 vboxnet0，但是windows的virtualbox命名方式为VirtualBox Host-Only Ethernet Adapter  

    选择`192.168.56.x`的网段： 

    ![网段选择](img/网段选择.png) 

  解决完成上述问题后，setup-vm.sh运行成功，虚拟机可以正常运行。  

- [x] **Q2：执行`opkg install xx`卡住，一直不成功，也无返回结果；执行`opkg find`也无返回结果。**

  A2：在要安装驱动时，多次执行`opkg find`搜索对应驱动无果，想到源码编译，但是`opkg install git`也一直不成功，怀疑是脚本安装openwrt在我的机器上出错了。但是后来再次尝试**重启**后，`opkg`命令都能正常工作（除了有一点慢），不太清楚具体是什么原因导致之前一直失败。

- [x] **Q3：没有USB3.0选项。**

  ![没有usb3.0选项](img/没有usb3.0选项.png)

  A3：因为是新安装的VirtualBox，没有USB3.0的选项，所以要安装`virtualbox extension pack`。在[VirtalBox官网](https://www.virtualbox.org/wiki/Downloads)下载扩展包，双击安装包安装。

  安装成功：

  ![安装VirtualBoxExtensionPack](img/安装VirtualBoxExtensionPack.png)
  
- [ ] Q4：`iwconfig not found`

  A4：`opkg update && opkg install wireless-tools`

## <span id = "5">课后作业</span>

## <span id = "06">参考资料</span>

* [[OpenWrt Wiki] OpenWrt on VirtualBox HowTo](https://openwrt.org/docs/guide-user/virtualization/virtualbox-vm)
* [windows git bash wget: command not found](https://blog.csdn.net/eddy23513/article/details/106621754/)
* [OpenWrt Forum Archive](https://forum.archive.openwrt.org/viewtopic.php?id=37896)
* [reference](link)