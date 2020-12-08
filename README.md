# ImageSharing - Android Library #

By Daniel Peña Vázquez

### Description ###

Android library for displaying a simple list of images. 
At same time it will give user buttons to share 1...N items with other apps. 

### Setup ###

1. Ensure to include jitpack.io in your /build.gradle

```
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
        // ...
    }
}
```

2. Add the right dependency in your main build.gradle (usually /app/build.gradle)

```
	dependencies {
                // ...
	        implementation 'com.github.blackdtools:imagesharing:X.Y.Z'
                // ...
	}


```
(Replace X.Y.Z with the current version of this library)

### Usage ###
1. Ensure you add our activity to your AndroidManifest.xml
```
        <activity
            android:name="net.blackdevelopers.imagesharing.ImageSharingActivity"
            android:theme="@style/Theme.ImageSharingStyle"
            />
```

2. Import this library at the activity that will launch the ImageSharingActivity
```
import net.blackdevelopers.imagesharing.ImageSharingActivity;
```

3. Call our public static function: ImageSharingActivity.start(activity,uriList).
If you are already in an activity then you can simply call this:
```
ImageSharingActivity.start(this, uriList);
```
Note: uriList is an ArrayList<Uri> containing the images you want to display, allowing your user to share them.


### Contact ###

Daniel Peña Vázquez
Email: daniel@blackdtools.com