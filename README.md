# iOS_remote
iOS Remote Real Machine</br></br>
Combine [WebDriverAgent](https://github.com/facebook/WebDriverAgent) and [ios-minicap](https://github.com/openstf/ios-minicap) 

## Language
[English](https://github.com/yxys01/iOS_remote/blob/master/README.md), [中文](https://github.com/yxys01/iOS_remote/blob/master/README_zh-cn.md)

## Platform
Limited in Mac

## Features
- [x] Launch iproxy when start
- [x] Create http proxy for WDA server
- [x] Add the missing Index page
- [x] Support Package management API
- [x] Support launch WDA
- [x] iOS device remote control
- [x] Based on Java

## Requirements
* brew install libjpeg-turbo (>=1.5 is required)
* Xcode (for the Frameworks)
* [cmake](https://cmake.org/)
* OS X Yosemite (10.9) or higher
* iOS 8 or higher
* [Lightning cable](https://en.wikipedia.org/wiki/Lightning_(connector)). See the list of devices.
* fetch all dependencies with [Carthage](https://github.com/Carthage/Carthage)
* build Inspector bundle using [npm](https://www.npmjs.com)
* Eclipse IDE for Java EE Developers
* JavaSE (1.6) or higher
* Tomcat (7) or higher
* libimobiledevice
* ideviceinstaller
* usbmuxd

## Other Help Document
[How to install ios-minicap](http://blog.csdn.net/yxys01/article/details/76442135)</br></br>
[How to install WebDriverAgent](https://testerhome.com/topics/4904)</br></br>
[WebDriverAgent Q&A](https://testerhome.com/topics/9666)</br></br>
[Eclipse Import Maven Project](http://blog.csdn.net/yxys01/article/details/78111229)</br></br>
[Configure Tomcat9 In Mac](http://blog.csdn.net/yxys01/article/details/77715330)</br></br>
## Install
1、Install support</br></br>
* [usbmuxd](http://blog.csdn.net/yxys01/article/details/77188976)
```
brew install usbmuxd
```
* [libimobiledevice and ideviceinstaller](http://blog.csdn.net/yxys01/article/details/76868493)
```
sudo brew update
sudo brew install libimobiledevice
sudo brew install ideviceinstaller
```

2、git clone 
```
$git clone https://github.com/openstf/ios-minicap.git
$git clone https://github.com/facebook/WebDriverAgent
$git clone https://github.com/weamylady2/iOS_remote
```
or
```
$git clone https://github.com/yxys01/iOS_remote
```
3、Configure ios-minicap
```
$cd /Users/yourname/ios-minicap-master
```
4、Configure WebDriverAgent
```
$cd /Users/yourname/WebDriverAgent
```
5、Configure iOS_remote</br></br>

iOS_remote is a maven project.</br></br>

* Open iOS_remote in Eclipse</br></br>
Open Eclipse </br></br>
**Import->Maven->Existing Maven Projects->Next->Browse(iOS_remote's path)->Finish**</br></br>

* Change Config</br></br>
In iOS_remote</br></br>
**Java Resources->src/main/resource->config.properties**</br></br>
In config.properties</br></br>
**change code(change three parameters:minicapPath、wdaPath、bashPath)**</br></br>

```
minicapPath=/Users/yourname/ios-minicap-master
wdaPath=/Users/yourname/WebDriverAgent
bashPath=/Users/yourname/ios_remote/src/main/resources
wdaPort=8200
minicapPort=12345
```
Change the three parameters into your project path</br></br>

6、Change and Rebuild ios-minicap</br></br>

In order to reduce the pressure of MAC, we need to reduce the frequency of sending imgs from minicaps.</br></br>
Go to the ios-minicap folder, and Edit the "src/minicap.cpp", Add a method:</br></br>
```
static void sleep_ms(unsigned int secs)
{
struct timeval tval;
tval.tv_sec=secs/1000;
tval.tv_usec=(secs*1000)%1000000;
select(0,NULL,NULL,NULL,&tval);
}
```
Then add a sleep in main:</br></br>
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
Next step is rebuilding ios-minicap, runing the build.sh in ios-minicap folder:</br></br>
```
$ ./build.sh 
mkdir: build: File exists
-- Configuring done
-- Generating done
-- Build files have been written to: /Users/waterhuang/Downloads/ios-minicap-master/build
[100%] Built target ios_minicap
```

7、Run iOS_remote</br></br>
* Open iproxy</br></br>
Open one terminal window</br></br>
```
$iproxy 8200 8100
```
* Run iOS_remote</br></br>
```
$cd /Users/yourname/iOS_remote
$mvn tomcat7:run-war
```
* Show in browser</br></br>
Open the ios remote page by url::`http://localhost:8080/ios/`


# LICENSE
Under [MIT](LICENSE)

