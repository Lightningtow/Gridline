package com.lightningtow.gridline

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.lightningtow.gridline.auth.Model

class GridlineApplication : Application() {
    lateinit var model: Model

    override fun onCreate() {

        super.onCreate()
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        model = Model

    }

    companion object {
        val context: Context
            get() = instance!!.applicationContext

        @get:Synchronized
        var instance: GridlineApplication? = null
            private set
    }
}


//class GridlineApplication : Application() {
//    lateinit var model: Model
////    lateinit var context: Context
//    private var context: WeakReference<Context>? = null
//
//    //Private contructor
//
//
//    var mContext = context.getApplicationContext()
//
//    override fun onCreate() {
//        super.onCreate()
//
//
//        model = Model
////        GridlineApplication.Companion.context = applicationContext
////        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
////        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//
//
//    }
//
//    companion object { // todo memory leak? wtf
//////        lateinit var context: Context
////        @Synchronized
////        fun getInstance(context: Context): RestClient? {
////            if (mInstance == null) {
////                mInstance = RestClient(context.applicationContext)
////            }
////            return mInstance
////        }
//        private var context: WeakReference<Context?>? = null
//
//        //Private contructor
//        private fun WidgetManager(context: Context) {
//            this.context = WeakReference(context)
//        }
//
//        //Singleton
//        fun getInstance(context: Context): WidgetManager? {
//            if (null == widgetManager) {
//                widgetManager = WidgetManager(context)
//            }
//            return widgetManager
//        }
//    }
//
//}
