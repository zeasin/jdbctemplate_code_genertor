package com.dianmoo.tools.db

import com.dianmoo.tools.utils.ConfigModel
import org.springframework.core.io.support.PropertiesLoaderUtils
import java.io.FileOutputStream
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import java.io.File
import java.io.FileInputStream


class ConnectionUtils {


    fun getConnection(): Connection {

        //val fo: FileOutputStream = FileOutputStream("config.properties")
//        val inStream = ConnectionUtils::class.java!!.getClassLoader().getResourceAsStream("config.properties")
        //单例调用配置文件
        val configModel: ConfigModel = com.dianmoo.tools.utils.Config.config()
//        val inStream = FileInputStream(File("config.properties"))
//        val properties = Properties();
//        properties.load(inStream)

//        val ip1 = propertie.getProperty("DB_IP")
//        val properties = PropertiesLoaderUtils.loadAllProperties("config.properties")
//        val JDBC_DRIVER = "com.mysql.jdbc.Driver"//properties.getProperty("JDBC_DRIVER")
        val ip = configModel.DB_IP//properties.getProperty("DB_IP")
        val port = configModel.DB_PORT//properties.getProperty("DB_PORT")
        val dbName = configModel.DB_NAME//properties.getProperty("DB_NAME")
//        val USER = properties.getProperty("DB_USER")
//        val PASS = properties.getProperty("DB_PASSWORD")

        val DB_URL = "jdbc:mysql://${ip}:${port}/${dbName}"


        val props = Properties()
        props.setProperty("user", configModel.DB_USER);
        props.setProperty("password", configModel.DB_PASS);
        props.setProperty("remarks", "true"); //设置可以获取remarks信息
        props.setProperty("useInformationSchema", "true");//设置可以获取tables remarks信息

        Class.forName(configModel.JDBC_DRIVER)
        print("连接数据库：" + DB_URL)
        return DriverManager.getConnection(DB_URL, props)
    }
}