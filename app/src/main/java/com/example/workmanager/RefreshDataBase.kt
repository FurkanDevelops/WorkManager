package com.example.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class RefreshDataBase(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
        override fun doWork(): Result {
            val getData = inputData
            val myNumber = getData.getInt("intKey",0)
            // yukarıda olan key mainActivity de olanla aynı olmak zorunda
            refreshDatabase(myNumber)
            return Result.success()
            // bir değer dönmesi lazım muhakkak
        }
    private fun refreshDatabase(myNumber : Int){
        val sharedPreferences = context.getSharedPreferences("com.example.workmanager",Context.MODE_PRIVATE)
        var mySavedNumber = sharedPreferences.getInt("myNumber",0)
        mySavedNumber = mySavedNumber +myNumber
        println(mySavedNumber)
        sharedPreferences.edit().putInt("myNumber",mySavedNumber).apply()//apply kaydetmeyi sağlıyor
        // bu app de her aılınca değer 2 defa artıyor
    }
}