# H1 OpenWrt 虚拟机搭建

**目录**

* [实验目的](#10)
* [实验环境](#00)
* [实验要求](#01)
* [实验过程](#02)
  * [020](020)
  * [021](#021)
  * [022](#022)
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

### <span id = "020">Part 0</span> 安装openWrt

#### 复习VirtualBox的配置和使用

* 重新安装了Kali2020，网卡选择：`NAT`+`Host-Only`。

* 将虚拟硬盘更改为`多重加载`。

* 配置`ssh`远程桌面连接。

  ![配置远程桌面](D:\Project-mis\2021-mis-public-yumlii33\chap0x01\img\配置远程桌面.png)

* 设置虚拟机和宿主机的文件共享，实现宿主机和虚拟机的双向文件共享。

  使用`sftp`协议实现双向文件共享。

  连接远程服务器（这里使用windows主机连接kali虚拟机）：`sftp user@ip`

  上传：`put [本地文件的地址] ([服务器上文件存储位置])`

  下载：`get [服务器上文件存储的位置] ([本地要存储的位置])`

  ![sftp文件共享](D:\Project-mis\2021-mis-public-yumlii33\chap0x01\img\sftp文件共享.png)

#### 下载安装`OpenWrt`

* 

https://downloads.openwrt.org/releases/19.07.2/targets/x86/64/openwrt-19.07.2-x86-64-combined-ext4.img.gz

https://downloads.openwrt.org/snapshots/targets/x86/64/openwrt-x86-64-combined-squashfs.img.gz

* 

### <span id = "021">Part 1 </span>

### <span id = "022">Part 2</span>

### <span id = "023">Part 3</span>

### <span id = "024">Part 4 </span>

### <span id = "025">Part 5 </span>

### <span id = "026">Part 6 </span>

## <span id = "03">实验总结</span>

## <span id = "04">问题和解决</span>

- [ ] Q0：在kali里面下载时，显示无法找到什么什么，但是能够ping通。上网搜博客，修改/etc/resolv.conf之后，不行，修改回来后，连ping都ping不通了。
- [ ] Q1：
- [ ] Q2：
- [ ] Q3：

## <span id = "5">课后作业</span>

## <span id = "06">参考资料</span>

* [[OpenWrt Wiki] OpenWrt on VirtualBox HowTo](https://openwrt.org/docs/guide-user/virtualization/virtualbox-vm)
* [reference](link)
* [reference](link)
* [reference](link)