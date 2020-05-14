# 《今日播报》WebService

用以获取襄阳《今日播报》视频节目资源信息
<br><br><br>

### 程序运行结果结果预览

```json
{
    "status": 1,
    "errMsg": "success",
    "datestamp": "20200101",
    "title": "20200101今日播报 ",
    "poster": "http://img.cjyun.org/a/10125/202001/d12430cb3aee9d014f16491380385f2f.png",
    "url": "http://videoplus.cjyun.org/20200101/605546_605546_1577881046_transv.mp4"
}
```

### 现包含功能：

1. `/jrbb/latest` 尝试获取最新《今日播报》视频的信息

![image](https://user-images.githubusercontent.com/16408325/80950334-38bd7d80-8e28-11ea-8e49-5c5c4f9fbe25.png)

2. `/jrbb/{yyyyMMdd}` 尝试获取指定日期《今日播报》视频的信息

![image](https://user-images.githubusercontent.com/16408325/80950260-188dbe80-8e28-11ea-878d-da35d6fd909e.png)

### 启动方法：

1. 命令行下切到项目根目录；
2. 执行 `mvn clean package` ，不想测试也可加上参数，即 `mvn clean package -Dmaven.test.skip=true`
3. 在项目根目录下target文件夹下拷出jar包至服务器上项目运行目录（如/usr/local/jrbbws/）；
4. 执行 `nohup jrbbws-x.x.x.jar &` ，即可后台运行该程序且会在本目录下生成名为 `nohup.out` 的日志文件，内容是程序的console输出
