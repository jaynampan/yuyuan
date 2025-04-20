package meow.softer.yuyuan.utils

import android.util.Log
import kotlin.reflect.KClass

fun debug(msg: String, tagClass:String? = null) {
    if (tagClass != null) {
        Log.d("MyTest $tagClass", msg)
    } else {
        Log.d("MyTest ", msg)
    }
}

fun error(tagClass: KClass<out Any>, msg: String) {
    Log.e("MyTest $tagClass", msg)
}