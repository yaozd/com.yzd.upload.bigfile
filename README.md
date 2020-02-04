# com.yzd.upload.bigfile
大文件上传：通过WebUploader实现大文件分片上传，支持2G的文件上传

## 参考
- [基于springboot做的大文件分段(片)上传，支持断点续传](https://www.jianshu.com/p/aa44eb96c7b6)
    - [https://github.com/superealboom/bigfile](https://github.com/superealboom/bigfile) -包含数据库
    - [https://github.com/yaozd/bigfile](https://github.com/yaozd/bigfile)
- [SpringBoot WebUploader大文件分片上传](https://blog.csdn.net/niugang0920/article/details/89387209) -测试同用byArvin (推荐此项目)可上传1G以上文件
    - [https://gitee.com/niugangxy/webuploader](https://gitee.com/niugangxy/webuploader)

## shell
```
Linux下校验下载文件的完整性
md5sum hyperspace-console-api.jar| grep ` cat -A hyperspace-console-api.jar.MD5 |cut -b 1-32`
PS: cut -b 1-32 通过文本截取的方式删除换行符
```