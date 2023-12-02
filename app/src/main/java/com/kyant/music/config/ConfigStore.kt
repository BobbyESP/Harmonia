package com.kyant.music.config

import java.io.File
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

var configStore: ConfigStore? = null

class ConfigStore(
    private val filesDir: String,
    private val name: String
) {

    private val file by lazy {
        File(filesDir, "$name.toml")
    }

    private val lock = ReentrantReadWriteLock()

    init {
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    fun edit(key: String, value: Any?) {
        try {
            lock.write {
                val regex = Regex("$key = .*")
                val lines = file.readLines()
                if (value == null) {
                    file.writeText(
                        lines.filterNot { it.matches(regex) }.joinToString("\n")
                    )
                } else {
                    val line = lines.firstOrNull { it.matches(regex) }
                    if (line == null) {
                        file.appendText("$key = $value\n")
                    } else {
                        file.writeText(
                            lines.joinToString("\n") {
                                if (it.matches(regex)) {
                                    "$key = $value"
                                } else {
                                    it
                                }
                            }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun read(key: String): String? {
        try {
            lock.write {
                val regex = Regex("$key = (.*)")
                val lines = file.readLines()
                return lines.firstOrNull { it.matches(regex) }?.let {
                    regex.find(it)?.groupValues?.get(1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
