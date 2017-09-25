# iOS_remote
iOS Remote Real Machine
Combine [WebDriverAgent](https://github.com/facebook/WebDriverAgent) and [ios-minicap](https://github.com/openstf/ios-minicap) 

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
# Build Command:
# mvn tomcat7:run-war
# Then open the ios remote page by url:
# http://localhost:8080/ios/

