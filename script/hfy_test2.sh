#! /bin/bash

SCRIPT_DIR=$(cd "$(dirname "$0")";pwd)


#环境变量定义
#用户自定义变量只在当前的shell中生效，而环境变量会在当前shell和这个shell的所有子shell当中生效
#如果把环境变量写入相应的配置文件，那么这个环境变量就会在所有的shell中生效
#export 变量名=变量值 #申明变量
export TYPE="type_000"

sh "$SCRIPT_DIR/"hfy_test.sh
