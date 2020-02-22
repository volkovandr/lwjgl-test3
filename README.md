# lwjgl-test3

Trying to create an OpenGL simple game application with Scala and LWJGL

## How to start

Just execute `./gradlew run` in Linux

In Windows use `gradlew.bat` instead.

### How to start selecting a graphics processor

The app prints some OpenGL-related properties of your graphics card and it might be interesting
to run it with different GPU to see the difference. 
Since it's a Java application, it is tricky to make Windows run it with a selected GPU. 

The way how that could be done is as follows:
1. Build the application's distribution package using `.\gradlew.bat installDist`
1. Then navigate to `build\install\lwjgl-test3\bin`
1. Then right-click `lwjgl-test.bat` and select "Create shortcut"
1. And now right click the shortcut and select "Run with graphics processor..." and select the one you want.