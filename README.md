# PocketCafe Application 🍰 ☕️ 🍵
카페 테이크아웃 사이렌오더 & 관리 안드로이드 애플리케이션입니다.<br>

- <strong>Admin Page</strong>

![image](https://user-images.githubusercontent.com/39934875/113481410-65649400-94d4-11eb-801f-4364b850ee2c.png)

- <strong>User Page</strong>

![image](https://user-images.githubusercontent.com/39934875/113482034-ba55d980-94d7-11eb-951d-7c1048e4feac.png)


### 1. 프로젝트 소개 & 환경
- Java
```java 15.0.2 2021-01-19
Java(TM) SE Runtime Environment (build 15.0.2+7-27)
Java HotSpot(TM) 64-Bit Server VM (build 15.0.2+7-27, mixed mode, sharing)
```
- Oracle Database
```
commons-collections-3.2.1.jar
commons-dbcp-1.2.2.jar
commons-pool.1.4.jar
ojdbc6.jar
```
- MyBatis
```
mybatis-3.1.1.jar
```
- Tomcat
```
tomcat v8.5
```
- etc
  - cos.jar
  - taglibs-standard-impl-1.2.5.jar
  - taglibs-standard-spec-1.2.5.jar

### 2. 파일 구조
- client
```
.
├── app
│   ├── build.gradle
│   ├── proguard-rules.pro
│   └── src
│       ├── main
│       │   ├── AndroidManifest.xml
│       │   ├── java
│       │   │     └── pocketcafe
│       │   │     │      │  
│       │   │     │      └── *.java
│       │   │     └── util
│       │   │          └── *.java
│       │   └── res
│       │       ├── drawable
│       │       ├── drawable-v24
│       │       ├── layout
│       │       └── values
├── build.gradle
├── gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle


```
- server
```
.
├── WebContent
│   ├── META-INF
│   └── WEB-INF
└── src
    ├── action
    │   ├── *.java
    ├── config
    │   └── mybatis
    │       ├── mapper
    │       └── sqlMapConfig.xml
    ├── dao
    │   ├── *DAO.java
    ├── service
    │   └── MyBatisConnector.java
    └── vo
        ├── .VO.java
```
 
 ### 3. 주요 기능 
 - <strong>매니저 ver</strong>
   ```
   1. 가게의 정보(가게명, 위치, 소개, 사진)를 등록할 수 있습니다.
   2. 가게의 메뉴를 관리(메뉴 사진, 메뉴 소개)할 수 있습니다.
   3. 실시간으로 주문 내역을 확인, 관리할 수 있습니다.
   ```
   
 - <strong>사용자 ver</strong>
   ```
   1. 원하는 가게를 검색 또는 현재 내 위치 기준으로 가게를 검색할 수 있습니다.
   2. 가게의 정보와 메뉴를 확인할 수 있습니다.
   3. 원하는 메뉴를 주문, 확인할 수 있습니다.
   4. 준비가 완료되면 알림을 받을 수 있습니다.
   ```

