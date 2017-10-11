<center><font color=#000000 size=6 face="黑体">iOS-remote使用指南</font>
</center></br>

## <font color=#000000 size=5 face="黑体">1、安装Xcode</font>
Xcode这个可以去官网安装或者去我的网盘下载Xcode8.3.3.xip
链接：http://pan.baidu.com/s/1hszRESW 密码：yogw

下载好Xcode，还要下载Command Line Tools

 1. 打开mac终端
 2. 在终端中输入以下命令：`xcode-select --install`  ，按回车。

然后一路点确定安装即可
详情可见：http://blog.csdn.net/yxys01/article/details/73456973


## <font color=#000000 size=5 face="黑体">2、安装Homebrew</font>
Homebrew的安装很简单，只需在终端下输入如下指令：

```
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```
Homebrew安装成功后，会自动创建目录 /usr/local/Cellar 来存放Homebrew安装的程序。 这时你在命令行状态下面就可以使用 brew 命令了.

详情可见：http://blog.csdn.net/yxys01/article/details/77452318

## <font color=#000000 size=5 face="黑体">3、安装node和npm
</font>
直接打开终端输入如下指令：

```
brew install node
```
执行完上面的命令，你就安装好了nodejs和npm 

## <font color=#000000 size=5 face="黑体">4、安装相关依赖库</font>
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
sudo brew update
sudo brew install libimobiledevice
sudo brew install ideviceinstaller
```

## <font color=#000000 size=5 face="黑体">5、安装ios-minicap</font>

**(1)打开终端，clone该项目：**

```
$git clone https://github.com/openstf/ios-minicap 
```
**(2)安装libjpeg-turbo**

```
brew install libjpeg-turbo 
```
**(3)安装cmake**

```
brew install cmake
```
**(4)启动ios-minicap，详情可见：http://blog.csdn.net/yxys01/article/details/76442135**

## <font color=#000000 size=5 face="黑体">6、安装 WebDriverAgent</font>

**(1)打开终端，clone该项目：**

```
git clone https://github.com/facebook/WebDriverAgent
```
**(2)运行初始化脚本**
```
./Scripts/bootstrap.sh
```
该脚本会使用Carthage下载所有的依赖，使用npm打包响应的js文件
执行完成后，直接双击打开WebDriverAgent.xcodeproj这个文件。

**(3)安装中遇到一些问题，解决方案可见：**
http://blog.csdn.net/yxys01/article/details/77045359


## <font color=#000000 size=5 face="黑体">7、安装iOS-remote</font>

**(1)打开终端，clone该项目：**
```
$git clone https://github.com/weamylady2/iOS_remote
```
or
```
$git clone https://github.com/yxys01/iOS_remote
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
然后在main中添加一个sleep

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

## <font color=#000000 size=5 face="黑体">8、运行iOS_remote</font>

**(1)新建一个终端，打开iproxy** 

```
$iproxy 8200 8100
```
**(2)再打开一个终端**

```
$cd /Users/yourname/iOS_remote
$mvn tomcat7:run-war
```
**(3)打开浏览器，输入网址：http://localhost:8080/ios/ 即可**



