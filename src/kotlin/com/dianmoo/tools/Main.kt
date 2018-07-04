package com.dianmoo.tools

import com.dianmoo.tools.core.CodeGenerator
import com.dianmoo.tools.db.ConnectionUtils
import com.dianmoo.tools.model.Table
import com.dianmoo.tools.utils.Config
import org.springframework.core.io.support.PropertiesLoaderUtils
import java.io.FileOutputStream
import java.util.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.sql.Connection
import java.sql.SQLException


fun main(args: Array<String>) {
//    val properties = PropertiesLoaderUtils.loadAllProperties("config.properties")
//    val JDBC_DRIVER = properties.getProperty("JDBC_DRIVER")
//    val DB_URL = properties.getProperty("DB_URL")
//    val USER = properties.getProperty("DB_USER")
//    val PASS = properties.getProperty("DB_PASSWORD")
//    val config = Config.config()
//
////    val dburl = "jdbc:mysql://${ip}:${port}/${dbName}"
//
//    println(config.DB_IP)

//    val directory = File("")
//    println(directory.getAbsolutePath())
    try {
        FileInputStream(File("config.properties"))
    } catch (e: FileNotFoundException){
        println("-------------没有配置文件，开始配置参数-------------")
        config()
    }

    println("-------------开始生成代码-------------")
    generator()
}

fun generator() {
    var conn: Connection? = null
    try {
        println("连接数据库...")

        conn = ConnectionUtils().getConnection()

        //读取库信息
        val databaseMetaData = conn.getMetaData()
        var tableList: MutableList<Table> = mutableListOf()


        //返回某个数据库的表名
        //参数解析: 第1和第2个都是数据库的名字(2个，是为兼容不同数据库), 第3个参数是查询表名的过滤模式(null为不过滤即查所有，"%a%"为表名中包含字母'a'),最后一个参数是表类型如"TABLE"、"VIEW"等(这些值可查看API中getTableTypes()方法)
        val rs = databaseMetaData.getTables("dbtest", "dbtest", null, arrayOf("TABLE", "VIEW"))
        println("-------------获取到以下表信息-------------")
        while (rs.next()) {
            val name = rs.getString("TABLE_NAME") //字符串参数的具体取值参看API中getTables()
            val name2 = rs.getString("TABLE_TYPE")
            val name3 = rs.getString("REMARKS")
            var table: Table = Table()
            table.tableName = name
            table.tableType = name2;
            val r7 = Regex("\\$\\{(.*)\\}")
            table.tableComment = name3.replace(r7, "")
            if (r7.find(name3)?.value != null) {
                table.tableAliasName = r7.find(name3)?.value!!.replace("\${", "").replace("}", "")
            }

            println("表名：${name}，类型：${name2}，备注：${name3}，别名：${table.tableAliasName}")
//            println(table.tableAliasName)
            tableList.add(table)
        }


        for (t in tableList) {
            println("开始生成表[表名：${t.tableName} 别名：${t.tableAliasName}]" )
            val resultSet = databaseMetaData.getColumns(null, "%", t.tableName, "%")

            //生成实体
            CodeGenerator().generateEntity(resultSet, t)
        }



        conn.close()


    } catch (se: SQLException) {
        // 处理 JDBC 错误
        se.printStackTrace()
    } catch (e: Exception) {
        // 处理 Class.forName 错误
        e.printStackTrace()
    } finally {
        // 关闭资源
        // 什么都不做
        try {
            if (conn != null) conn.close()
        } catch (se: SQLException) {
            se.printStackTrace()
        }
    }

    //
}

fun config() {
    val sc = Scanner(System.`in`)
    println("-------------------全局参数配置-------------------")
    val author: String
//    val output_path: String
    val package_name: String
    print("请输入作者：")
    author = sc.nextLine()

    print("请输入程序包名：")
    package_name = sc.nextLine()

//    print("请输入代码输出路径：")
//    output_path = sc.nextLine()


    println("-------------------MySQL配置-------------------")
    val ip: String
    val port: String
    val dbName: String
    val userName: String
    val pass: String


    print("请输入服务器IP：")
    ip = sc.nextLine()

    print("请输入MYSQL端口：")
    port = sc.nextLine()

    print("请输入数据库名：")
    dbName = sc.nextLine()

    print("请输入登录用户：")
    userName = sc.nextLine()

    print("请输入登录密码：")
    pass = sc.nextLine()


    val fo: FileOutputStream = FileOutputStream("config.properties")
    val properties = Properties()

    properties.setProperty("author", author)
    properties.setProperty("package_name", package_name)
//    properties.setProperty("OUTPUT_PATH", output_path)
    properties.setProperty("DB_IP", ip)
    properties.setProperty("DB_PORT", port)
    properties.setProperty("DB_NAME", dbName)
    properties.setProperty("DB_USER", userName)
    properties.setProperty("DB_PASSWORD", pass)

    properties.store(fo, "Properties");


    val dbUrl = "jdbc:mysql://${ip}:${port}/${dbName}"

    println("你输入MySQL连接地址是：" + dbUrl)
}