
# PocketCafe Application π° βοΈ π΅
μΉ΄ν μ¬μ΄λ μ€λ & κ΄λ¦¬ μλλ‘μ΄λ μ νλ¦¬μΌμ΄μμλλ€.<br>

- <strong>Admin Page</strong>

![image](https://user-images.githubusercontent.com/39934875/113481410-65649400-94d4-11eb-801f-4364b850ee2c.png)

- <strong>User Page</strong>

![image](https://user-images.githubusercontent.com/39934875/113482034-ba55d980-94d7-11eb-951d-7c1048e4feac.png)


### 1. νλ‘μ νΈ μκ° & νκ²½
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

### 2. νμΌ κ΅¬μ‘°
- client
```
.
βββ app
βΒ Β  βββ build.gradle
βΒ Β  βββ proguard-rules.pro
βΒ Β  βββ src
βΒ Β      βββ main
βΒ Β      βΒ Β  βββ AndroidManifest.xml
βΒ Β      βΒ Β  βββ java
βΒ Β      βΒ Β Β βΒ Β    βββ pocketcafe
βΒ Β      βΒ Β  βΒ Β    βΒ Β     βΒ Β 
βΒ Β      βΒ Β  βΒ Β    βΒ Β     βββ *.java
βΒ Β      βΒ Β  βΒ Β    βββ util
βΒ Β      βΒ Β Β βΒ Β  Β Β      βββ *.java
βΒ Β Β Β    βΒ Β Β βββ res
βΒ Β      βΒ Β      βββ drawable
βΒ Β      βΒ Β      βββ drawable-v24
βΒ Β      βΒ Β      βββ layout
βΒ Β      βΒ Β      βββ values
βββ build.gradle
βββ gradle
βββ gradle.properties
βββ gradlew
βββ gradlew.bat
βββ settings.gradle


```
- server
```
.
βββ WebContent
βΒ Β  βββ META-INF
βΒ Β  βββ WEB-INF
βββ src
    βββ action
    βΒ Β  βββ *.java
    βββ config
    βΒ Β  βββ mybatis
    βΒ Β      βββ mapper
    βΒ Β      βββ sqlMapConfig.xml
    βββ dao
    βΒ Β  βββ *DAO.java
    βββ service
    βΒ Β  βββ MyBatisConnector.java
    βββ vo
        βββ .VO.java
```
 
 ### 3. μ£Όμ κΈ°λ₯ 
 - <strong>λ§€λμ  ver</strong>
   ```
   1. κ°κ²μ μ λ³΄(κ°κ²λͺ, μμΉ, μκ°, μ¬μ§)λ₯Ό λ±λ‘ν  μ μμ΅λλ€.
   2. κ°κ²μ λ©λ΄λ₯Ό κ΄λ¦¬(λ©λ΄ μ¬μ§, λ©λ΄ μκ°)ν  μ μμ΅λλ€.
   3. μ€μκ°μΌλ‘ μ£Όλ¬Έ λ΄μ­μ νμΈ, κ΄λ¦¬ν  μ μμ΅λλ€.
   ```
   
 - <strong>μ¬μ©μ ver</strong>
   ```
   1. μνλ κ°κ²λ₯Ό κ²μ λλ νμ¬ λ΄ μμΉ κΈ°μ€μΌλ‘ κ°κ²λ₯Ό κ²μν  μ μμ΅λλ€.
   2. κ°κ²μ μ λ³΄μ λ©λ΄λ₯Ό νμΈν  μ μμ΅λλ€.
   3. μνλ λ©λ΄λ₯Ό μ£Όλ¬Έ, νμΈν  μ μμ΅λλ€.
   4. μ€λΉκ° μλ£λλ©΄ μλ¦Όμ λ°μ μ μμ΅λλ€.
   ```

