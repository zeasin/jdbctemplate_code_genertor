package com.dianmoo.tools.utils

import org.springframework.core.io.support.PropertiesLoaderUtils
import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 * 配置文件单例
 */
object Config {
    fun config(): ConfigModel {
        val inStream = FileInputStream(File("config.properties"))
        val properties = Properties()
        properties.load(inStream)//加载配置文件
//        val properties = PropertiesLoaderUtils.loadAllProperties("config.properties")

        val model: ConfigModel = ConfigModel()

        model.JDBC_DRIVER = "com.mysql.jdbc.Driver"
        model.DB_IP = properties.getProperty("DB_IP")
        model.DB_PORT = properties.getProperty("DB_PORT")
        model.DB_NAME = properties.getProperty("DB_NAME")
        model.DB_USER = properties.getProperty("DB_USER")
        model.DB_PASS = properties.getProperty("DB_PASSWORD")

        model.AUTHOR = properties.getProperty("author")
        model.PACKAGE = properties.getProperty("package_name")


        return model
    }
}

