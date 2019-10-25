# BioAuth - Mobile Backend

### Table of Contents

- [About the BioAuth project](#about-bioauth)
- [BioAuth implementations](#bioauth-implementations)

---

<a name="about-bioauth"/>

### About the BioAuth project

BioAuth is a remote biometric authentication system for mobile devices. The project was developed as part of a Master's thesis at the AGH University of Science and Technology in Cracow.

The BioAuth system is divided into three parts: **Mobile**, **Web** and **Server**:

![System architecture](https://github.com/jsamol/bioauth-backend-mobile/blob/master/docs/img/system_architecture.png)

#### Mobile

The Mobile part consists of a library which can be used to register a new biometric template or authenticate an already signed up user. 

The mobile library is responsible for acquiring a biometrics sample and sending it to the server. It is also able to perform a liveness detection.

Applications using the mobile BioAuth SDK must be registered in the system and assigned unique `AppId` and `AppSecret` keys.

#### Web

The Web part consists of a Mobile App Management Web Application which is used to register and manage mobile applications in the system.

#### Server

The Server part consists of Web and Mobile backends and biometric microservices. 

The Web backend is used for generating new `AppId` and `AppSecret` keys and registering new mobile applications in the database.

The Mobile backend receives biometric samples from the BioAuth mobile library and forwards them to a proper biometric microservice with any required additional data. It is also responsible for saving biometric templates in the database.

The biometric microservices perform a liveness detection, extract features from a biometric sample and compare the sample with saved templates.

<a name="bioauth-implementations"/>

### BioAuth implementations:
- Mobile:
  - [Android BioAuth SDK](https://github.com/jsamol/bioauth-android-sdk)
  - [Android BioAuth SDK Demo App](https://github.com/jsamol/bioauth-android-demo)
- Web:
  - [Mobile App Management Web Application](https://github.com/jsamol/bioauth-client-app-management)
- Server:
  - Backend:
    - [Web Backend](https://github.com/jsamol/bioauth-backend-app-management)
    - [Mobile Backend](https://github.com/jsamol/bioauth-backend-mobile) (*currently browsed*)
  - Microservice:
    - [Face Recognition Service](https://github.com/jsamol/bioauth-service-face-recognition)
  
## 

The authentication process in BioAuth is inspired by [FIDO protocols](https://fidoalliance.org/).
