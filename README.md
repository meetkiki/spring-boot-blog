> This repo is cloned from the repo: <br />
 [Tale Blog](https://github.com/otale/tale).
 
 # 安装
 > ${tale_dir}表示安装目录
 
 ## 原始安装
 1.下载要更新的版本的发布包
 
 2.解压缩发布包至安装目录
 ```$xslt
 tar -zxvf tale.tar.gz -C ${tale_dir}
 ```
 
 3.启动
 ```
 ${tale_dir}/tool start
 ```
 
 4.打开浏览器访问地址，按照提示输入配置信息，进行安装
 ```
 http://[服务器IP]:9000
 ```
 
 
 
 # 更新
 > ${tale_dir}表示安装目录
 
 1.停止原来的服务
 ```
 ${tale_dir}/tool stop  # 停止服务
 ```
 
 2.备份原来安装目录下的如下文件和文件夹
 ```
 ${tale_dir}/resources/tale.db
 ${tale_dir}/resources/install.lock
 ${tale_dir}/resources/upload/
 ```
 
 3.删除原来的文件
 ```
 rm -rf ${tale_dir}/*
 ```
 
 4.下载要更新的版本的发布包
 
 5.解压缩发布包至安装目录
 ```$xslt
 tar -zxvf tale.tar.gz -C ${tale_dir}
 ```
 
 6.将步骤2中备份的文件拷贝至对应路径
 
 7.启动
 ```
 ${tale_dir}/tool start
 ```