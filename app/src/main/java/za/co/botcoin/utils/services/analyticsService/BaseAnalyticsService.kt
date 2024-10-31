package za.co.botcoin.utils.services.analyticsService

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object BaseAnalyticsService {
    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun trackAnalytics(context: Context, tag: String) {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        }

    }
}