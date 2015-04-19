Here's a pom.xml provided by Tiago Cardoso:

```
<?xml version='1.0' encoding='UTF-8'?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <modelVersion>4.0.0</modelVersion>
    <groupId>net.kogics</groupId>
    <artifactId>jiva-ng</artifactId>
    <packaging>jar</packaging>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <scala.version>2.8.0</scala.version>
    </properties>
    <dependencies>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.4</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.jmock</groupId>
            <artifactId>jmock</artifactId>
            <version>2.4.0</version>
        </dependency>
        <dependency>
            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
            <version>2.1_3</version>
        </dependency>
        <dependency>
            <groupId>org.objenesis</groupId>
            <artifactId>objenesis</artifactId>
            <version>1.0</version>
        </dependency>
        <dependency>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala-library</artifactId>
            <version>${scala.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala-compiler</artifactId>
            <version>${scala.version}</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>ScalaToolsMaven2Repository</id>
            <name>Scala-Tools Maven2 Repository</name>
            <url>http://scala-tools.org/repo-releases/</url>
        </repository>
    </repositories>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.5</source>
                    <target>1.5</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.scala-tools</groupId>
                <artifactId>maven-scala-plugin</artifactId>
                <version>2.14.1</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>testCompile</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <jvmArgs>
                        <jvmArg>-Xmx1024m</jvmArg>
                    </jvmArgs>
                    <args>
                        <!-- arg>-unchecked</arg -->
                        <arg>-deprecation</arg>
                    </args>
                    <scala-version>${scala.version}</scala-version>
                </configuration>
            </plugin>

            <plugin>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.5</version>
            </plugin>


        </plugins>
    </build>
</project>

```