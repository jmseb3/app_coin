# 적용 방법

## APP Module
~~~kotlin
apply(from = rootProject.file("keystore/signing.gradle"))

...

android {
    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("필요한 이름")
        }
    }
}
~~~

## local.properties
~~~
KEYSTORE_FILE=keystore/myappkey.jks
STORE_PASSWORD=
KEY_PASSWORD=
~~~
