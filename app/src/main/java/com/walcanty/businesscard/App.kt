package com.walcanty.businesscard

import android.app.Application
import com.walcanty.businesscard.data.AppDataBase
import com.walcanty.businesscard.data.BusinessCardRepository

class App:Application() {

    val database by lazy { AppDataBase.getDatabase(this) }
    val repository by lazy { BusinessCardRepository(database.businessDao()) }

}