package com.jesus.gymcontrol.infraestructure.di
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth

object SecondaryFirebase {

    private const val SECONDARY_APP_NAME = "SecondaryFirebaseApp"

    fun getSecondaryAuth(context: Context): FirebaseAuth {
        val existingApp = FirebaseApp.getApps(context).find { it.name == SECONDARY_APP_NAME }

        val app = existingApp ?: FirebaseApp.initializeApp(
            context,
            FirebaseOptions.Builder()
                .setApiKey("AIzaSyB14dPOLXpRTeYPXKNUcjeh1JI7nIP42x8")
                .setApplicationId("1:987033547327:android:a4d24e4496026a16393e8a")
                .setProjectId("gymcontrol-99701")
                .build(),
            SECONDARY_APP_NAME
        )

        return FirebaseAuth.getInstance(app)
    }
}