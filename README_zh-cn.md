# iOS-remote使用指南

## 简介
iOS-remote是结合[WebDriverAgent](https://github.com/facebook/WebDriverAgent) 和 [ios-minicap](https://github.com/openstf/ios-minicap) 开源项目做出来的基于JAVA的iOS远程真机控制的项目。

## 平台
仅限Mac使用

## 特点
- [x] 启动项目时运行 iproxy
- [x] 为WDA服务创建http代理
- [x] 添加缺失的索引页
- [x] 支持包管理API
- [x] 支持WDA运行
- [x] iOS远程真机控制
- [x] 基于Java开发

## 功能
- [x] iOS远程真机控制（点击拖拽）
- [x] HOME键功能
- [x] iPhone输入框添加文字（中英文--中文还在修复中）
- [x] 设备信息显示
- [x] 从本地安装ipa文件到iPhone真机里
- [x] 卸载已安装APP
- [x] 截图功能

## 安装要求
* 用brew安装libjpeg-turbo (要求版本1.5及以上)
* Xcode (要求版本8及以上,注：9有一定无法使用的风险)
* [cmake](https://cmake.org/)（最好通过brew安装）
* OS X Yosemite (要求版本10.9及以上)
* iOS（要求版本8及以上）
* [Lightning cable](https://en.wikipedia.org/wiki/Lightning_(connector)). 查看设备列表.
* 用[Carthage](https://github.com/Carthage/Carthage) 获取所有依赖项
* 用[npm](https://www.npmjs.com)建立Inspector bundle
* Eclipse IDE for Java EE Developers
* JavaSE (要求版本1.6及以上) 
* Tomcat (要求版本7及以上) 
* libimobiledevice
* ideviceinstaller
* usbmuxd

## 其他帮助文档
[How to install ios-minicap](http://blog.csdn.net/yxys01/article/details/76442135)</br></br>
[How to install WebDriverAgent](https://testerhome.com/topics/4904)</br></br>
[WebDriverAgent Q&A](https://testerhome.com/topics/9666)</br></br>
[Eclipse Import Maven Project](http://blog.csdn.net/yxys01/article/details/78111229)</br></br>
[Configure Tomcat9 In Mac](http://blog.csdn.net/yxys01/article/details/77715330)</br></br>

## 安装

### 1、安装Xcode
Xcode这个可以去官网安装或者去我的网盘下载Xcode8.3.3.xip
链接：http://pan.baidu.com/s/1hszRESW 密码：yogw

下载好Xcode，还要下载Command Line Tools

 1. 打开mac终端
 2. 在终端中输入以下命令：`xcode-select --install`  ，按回车。

然后一路点确定安装即可
详情可见：http://blog.csdn.net/yxys01/article/details/73456973


### 2、安装Homebrew
Homebrew的安装很简单，只需在终端下输入如下指令：

```bash
$ /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```
Homebrew安装成功后，会自动创建目录 /usr/local/Cellar 来存放Homebrew安装的程序。 这时你在命令行状态下面就可以使用 brew 命令了.

详情可见：http://blog.csdn.net/yxys01/article/details/77452318

### 3、安装node和npm

直接打开终端输入如下指令：

```
$ brew install node
```
执行完上面的命令，你就安装好了nodejs和npm 

### 4、安装相关依赖库
**(1)需要安装 [usbmuxd](https://github.com/libimobiledevice/usbmuxd) 以便于通过 USB 通道测试 iOS 真机，不需要测试真机则不用安装**
```
$ brew install usbmuxd
```

**(2)请安装 carthage 来构建 WebDriverAgent.**
```
$ brew install carthage
```
**(3)安装libimobiledevice 和 ideviceinstaller**

```
$ sudo brew update
$ sudo brew install libimobiledevice
$ sudo brew install ideviceinstaller
```

### 5、安装ios-minicap

**(1)打开终端，clone该项目：**

```
$ git clone https://github.com/openstf/ios-minicap 
```
**(2)安装libjpeg-turbo**

```
$ brew install libjpeg-turbo 
```
**(3)安装cmake**

```
$ brew install cmake
```
**(4)启动ios-minicap**

**详情可见：http://blog.csdn.net/yxys01/article/details/76442135 或者 https://testerhome.com/topics/10456**

### 6、安装 WebDriverAgent

安装步骤详情可见：https://testerhome.com/topics/10463 </br>

**(1)打开终端，clone该项目：**

```
$ git clone https://github.com/facebook/WebDriverAgent
```
**(2)运行初始化脚本**
```
$ cd /Users/yourname/WebDriverAgent

$ mkdir -p Resources/WebDriverAgent.bundle

$ sh ./Scripts/bootstrap.sh
```
该脚本会使用Carthage下载所有的依赖，使用npm打包响应的js文件
执行完成后，直接双击打开WebDriverAgent.xcodeproj这个文件。

**(3)安装中遇到一些问题，解决方案可见：**
http://blog.csdn.net/yxys01/article/details/77045359


### 7、安装iOS-remote

**(1)打开终端，clone该项目：**
```
$ git clone https://github.com/weamylady2/iOS_remote
```
or
```
$ git clone https://github.com/yxys01/iOS_remote
```
**(2)在 Eclipse中打开 iOS_remote**

打开Eclipse 
```
Import->Maven->Existing Maven Projects->Next->Browse(iOS_remote's path)->Finish
```

**更改 iOS_remote中的一些设置**

```
Java Resources->src/main/resource->config.properties
```

**在config.properties中改三个参数:`minicapPath、wdaPath、bashPath`**
```
minicapPath=/Users/yourname/ios-minicap-master
wdaPath=/Users/yourname/WebDriverAgent
bashPath=/Users/yourname/ios_remote/src/main/resources
wdaPort=8200
minicapPort=12345
```
**(3)重新构建 ios-minicap**
为了减少MAC的压力，我们需要减少从minicaps中发送imgs的频率。
在ios-minicap的文件夹中,编辑 `src/minicap.cpp`
添加一个方法：
```
static void sleep_ms(unsigned int secs)
{
struct timeval tval;
tval.tv_sec=secs/1000;
tval.tv_usec=(secs*1000)%1000000;
select(0,NULL,NULL,NULL,&tval);
}
```
然后在main中添加`sleep_ms(50);`

```
while (gWaiter.isRunning() and gWaiter.waitForFrame() > 0) {
client.lockFrame(&frame);
encoder.encode(&frame);
client.releaseFrame(&frame);
putUInt32LE(frameSize, encoder.getEncodedSize());
if ( pumps(socket, frameSize, 4) < 0 ) {
break;
}
if ( pumps(socket, encoder.getEncodedData(), encoder.getEncodedSize()) < 0 ) {
break;
}
sleep_ms(50);
}
```

重新构建 ios-minicap, 运行`build.sh`在ios-minicap文件夹中:
```
$ ./build.sh 
mkdir: build: File exists
-- Configuring done
-- Generating done
-- Build files have been written to: /Users/waterhuang/Downloads/ios-minicap-master/build
[100%] Built target ios_minicap
```

### 8、运行iOS_remote

**(1)新建一个终端，打开iproxy** 

```
$ iproxy 8200 8100
```
**(2)再打开一个终端**

```
$ cd /Users/yourname/iOS_remote
$ mvn tomcat7:run-war
```
**(3)打开浏览器，输入网址：http://localhost:8080/ios/ 即可**

### iOS-remote 安装篇

[iOS-remote 安装篇之 ios-minicap 安装使用完全指南](https://testerhome.com/topics/10456)

[iOS-remote 安装篇之 WebDriverAgent 安装使用完全指南](https://testerhome.com/topics/10463)

[iOS-remote 安装篇之 iOS-remote安装使用完全指南](https://testerhome.com/topics/10466)

### 参考文献
iOS-minicap + WDA 实现 ios 远程真机测试  https://testerhome.com/topics/10262
基于 WebDriverAgent 的 iOS 远程控制  https://testerhome.com/topics/8890
iOS 远程真机 (仅限屏幕查看)  https://testerhome.com/topics/6470
WebDriverAgent简介  https://testerhome.com/topics/4904
iOS 真机如何安装 WebDriverAgent  https://testerhome.com/topics/7220
WebDriverAgent天坑记  https://testerhome.com/topics/9666
STF 框架之 minicap 工具  https://testerhome.com/topics/3115

