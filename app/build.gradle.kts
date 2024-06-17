plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding=true
    }
}


dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.fragment:fragment:1.4.0")
    //implementation("com.android.support:appcompat-v7:28.0.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //mapsforge

    implementation("org.mapsforge:mapsforge-core:0.21.0")
    implementation("org.mapsforge:mapsforge-map:0.21.0")
    implementation("org.mapsforge:mapsforge-map-reader:0.21.0")
    implementation("org.mapsforge:mapsforge-themes:0.21.0")
    implementation("org.mapsforge:mapsforge-map-android:0.21.0")
    implementation("com.caverock:androidsvg:1.4")
    implementation("org.mapsforge:mapsforge-core:0.21.0")
    implementation("org.mapsforge:mapsforge-poi:0.21.0")
    implementation("org.mapsforge:mapsforge-poi-android:0.21.0")

    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.firebaseui:firebase-ui-database:7.1.1")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.0")

    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    //input type image
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //requete api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.7.1")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    //chargement image
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    //api("com.theartofdev.edmodo:android-image-cropper:2.7.0")
}
