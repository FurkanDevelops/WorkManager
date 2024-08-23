package com.example.workmanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val data = Data.Builder().putInt("intKey",1).build()

        val constraints = Constraints.Builder()
            //Burada özellikleri belirliyorsun şarj olurken tekrar etsin falan vs gibi
            .setRequiresCharging(false)
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        /*
        val myWorkRequest : WorkRequest = OneTimeWorkRequestBuilder<RefreshDataBase>()
            // OneTime bu app açılınca 1 defa yapıyor farklı çeşitleri de mevcur
            //<> içine workManager ile çalıştığın sınıfı yaz
            .setConstraints(constraints)
            .setInputData(data)
            /*
            .setInitialDelay(5,TimeUnit.HOURS)
            .addTag("myTag")

             */
            .build()
        WorkManager.getInstance(this).enqueue(myWorkRequest)

         */
        val myWorkRequest : PeriodicWorkRequest = PeriodicWorkRequestBuilder<RefreshDataBase>(15,TimeUnit.MINUTES)
            // yukarıda parantez içinde 15 yazan süre , diğeri de dakika mı saat mi olacağı belirlenen yer
            // en az 15 dakika oluyor
            .setConstraints(constraints)
            .setInputData(data)
            .build()
        WorkManager.getInstance(this).enqueue(myWorkRequest)
        //workManager çalıştırma biçimi

        //Çalıştı mı çalışmadı mı başarısız mı oldu vs bunları öğrenmek için
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id).observe(this,
            Observer {
                if(it.state == WorkInfo.State.RUNNING){
                    println("Running")
                }else if(it.state == WorkInfo.State.FAILED){
                    println("Failed")
                }else if(it.state == WorkInfo.State.BLOCKED){
                    println("Blocked")
                }else if(it.state == WorkInfo.State.BLOCKED){
                    println("Succeded")
                }
            }
        )
        //İptal etmek için
        // WorkManager.getInstance(this).cancelAllWork()//hepsini iptal eder

        //Chaning
        //One Time Request de yapılır
        // Birbirine bağlama işlemi yapılır
        val oneTimeWorkRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDataBase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()
        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
            //beginde olan bitince aşağıda olanları yaoacak
            .then(oneTimeWorkRequest)
            .then(oneTimeWorkRequest)
            .enqueue()

    }
}