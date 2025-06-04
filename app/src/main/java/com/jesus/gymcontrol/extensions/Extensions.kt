package com.jesus.gymcontrol.extensions

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun Any.getJson(): String? {
	return this.let {
		GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(it)
	}
}

inline fun <reified T> String.fromJson(): T? {
	return Gson().fromJson(this, object : TypeToken<T>() {}.type)
}