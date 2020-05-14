# jrbbws
获取襄阳《今日播报》节目资源的WebService
<br><br><br><br>

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

1. `/` 域名根目录：提示项目功能

![image](https://user-images.githubusercontent.com/16408325/80949546-ca2bf000-8e26-11ea-87ba-276719fe6c61.png)

2. `/jrbb/{yyyyMMdd}` 尝试获取指定日期《今日播报》视频的信息

![image](https://user-images.githubusercontent.com/16408325/80950260-188dbe80-8e28-11ea-878d-da35d6fd909e.png)

3. `/jrbb/latest` 尝试获取最新《今日播报》视频的信息

![image](https://user-images.githubusercontent.com/16408325/80950334-38bd7d80-8e28-11ea-8e49-5c5c4f9fbe25.png)
<br><br><br><br>

### 启动方法：

1. 命令行下切到项目根目录；
2. mvn clean package，不想测试也可加上参数，即mvn clean package -Dmaven.test.skip=true
3. 在项目根目录下target文件夹下拷出jar包至服务器上项目运行目录（如/usr/local/jrbbws/）；
4. nohup jrbbws-x.x.x.jar &
