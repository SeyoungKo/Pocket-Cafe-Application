# PocketCafe Application ☕️ 🍵
카페 메뉴 테이크아웃 주문 & 관리 안드로이드 애플리케이션입니다.

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
  
 ### 4. 실행 예시 화면

