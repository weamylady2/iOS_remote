# iOS_remote
-iOS Remote Real Machine
-Combine [WebDriverAgent](https://github.com/facebook/WebDriverAgent) and [ios-minicap](https://github.com/openstf/ios-minicap) 

## Platform
Limited in Mac

## Features
- [x] Launch iproxy when start
- [x] Create http proxy for WDA server
- [x] add udid into `GET /status`
- [x] forward all url starts with `/origin/<url>` to `/<url>`
- [x] Add the missing Index page
- [x] Support Package management API
- [x] Support launch WDA
- [x] iOS device remote control

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
[How to install ios-minicap](http://blog.csdn.net/yxys01/article/details/76442135)
[How to install WebDriverAgent](https://testerhome.com/topics/4904)
[WebDriverAgent Q&A](https://testerhome.com/topics/9666)

## Install
1、Install support
(1).[usbmuxd](http://blog.csdn.net/yxys01/article/details/77188976)
```
brew install usbmuxd
```
(2).[libimobiledevice and ideviceinstaller](http://blog.csdn.net/yxys01/article/details/76868493)
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
5、Configure iOS_remote</br>
This is maven project.</br>
(1).Open iOS_remote in Eclipse</br>
Open Eclipse </br>
Import->Maven->Existing Maven Projects->Next->Browse(iOS_remote's path)->Finish</br>
(2).Change Config</br>
In iOS_remote</br>
Java Resources->src/main/resource->config.properties</br>
In config.properties</br>
change code(change three parameters:minicapPath、wdaPath、bashPath)</br>
```
minicapPath=/Users/yourname/ios-minicap-master
wdaPath=/Users/yourname/WebDriverAgent
bashPath=/Users/yourname/ios_remote/src/main/resources
wdaPort=8200
minicapPort=12345
```
Change the three parameters into your project path</br>
6、Run iOS_remote</br>
（1).Open iproxy</br>
Open one terminal window</br>
```
$iproxy 8200 8100
```
(2).Run iOS_remote</br>
```
$cd /Users/yourname/iOS_remote
$mvn tomcat7:run-war
```
(3).Show in browser</br>
Open the ios remote page by url::`http://localhost:8080/ios/`


