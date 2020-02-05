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
wget http://localhost:9999/downloadLastFile
tar -xf  hyperspace-console-api.tar
md5sum hyperspace-console-api.jar| grep ` cat -A hyperspace-console-api.jar.MD5 |cut -b 1-32`
PS: cut -b 1-32 通过文本截取的方式删除换行符
rm hyperspace-console-api.tar
```
- 下载文件-通过MD5验证完整性
```
download.sh

#!/bin/sh
echo "下载文件"
filePath="/root/package"
fileNamePrefix="hyperspace-console-api"
tarFile="$filePath/$fileNamePrefix.tar"
jarFile="$filePath/$fileNamePrefix.jar"
md5File="$filePath/$fileNamePrefix.jar.MD5"
if [ ! -f $tarFile ];then
echo "文件不存在"
exit 0
fi
echo "解压文件"
tar -xf $tarFile
echo "验证文件是否完整?"
jarFileMd5Val=$(md5sum $jarFile)
echo $jarFileMd5Val
jarFileMd5ExpectVal=$(cat -A $md5File |cut -b 1-32)
echo $jarFileMd5ExpectVal
if [[ $jarFileMd5Val =~ $jarFileMd5ExpectVal ]]
then
    echo -e "\033[31m 验证文件完整性-成功 \033[0m"
	rm -f $md5File
	rm -f $jarFile
else
    echo -e "\033[33m 验证文件完整性-失败 \033[0m"
	rm -f $md5File
	rm -f $jarFile
fi
echo "显示文件列表"
ls -lh 

apply ./download
```