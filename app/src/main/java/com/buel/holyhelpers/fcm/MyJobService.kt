package com.buel.holyhelpers.fcm

import android.util.Log

class MyJobService : com.firebase.jobdispatcher.JobService() {

    override fun onStartJob(jobParameters: com.firebase.jobdispatcher.JobParameters): Boolean {
        Log.d(TAG, "Performing long running task in scheduled job")
        return false
    }

    override fun onStopJob(jobParameters: com.firebase.jobdispatcher.JobParameters): Boolean {
        return false
    }

    companion object {
        private const val TAG = "MyJobService"
    }
}
