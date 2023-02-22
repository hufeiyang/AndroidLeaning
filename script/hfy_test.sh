#! /bin/bash

# 当前脚本名，即：hfy_test.sh/hfy_test.sh
echo "$0"
# dirname，取目录名，也就是 hfy_test.sh
echo "$(dirname "$0")"


#----- 以下是为了 cd到项目根目录 -----
#/Users/bytedance/AndroidStudioProjects/bd_v_app2/hfy_test.sh
SCRIPT_DIR=$(cd "$(dirname "$0")";pwd)
echo "script directory： "${SCRIPT_DIR}


# /Users/bytedance/AndroidStudioProjects/bd_v_app2
PRO_DIR=$(cd "${SCRIPT_DIR}/..";pwd)
echo "project directory： "${PRO_DIR}

cd ${PRO_DIR}
pwd

echo ---------------------------------

hfy=1
echo "hfy值为:${hfy}"
#read是获取从键盘输入的内容
#read -p "请输入一个值：" hfy
#echo "新的hfy的值为:${hfy}"
echo ---------------------------------

#同时从键盘读取多个值，中间用空格键
#read v1 v2
#echo "v1=${v1},v2=${v2}"
#echo ---------------------------------

# 预设变量， shell直接提供无需定义的变量。shell脚本支持从命令行传递参数，参数之间用空格隔开：例如 sh hfy_test.sh/hfy_test.sh 666 999
# $? 命令执行后返回的状态：返回0表示成功，非0表示失败
echo "输入的参数为:" $*
echo "参数数量为:" $#
echo "参数1:$1"
echo "参数2=:$2"

#脚本变量的特殊用法
#调用系统命令用``（反单引号）
#转义字符使用加-e”
echo "今天是`date`"
echo "我是\n大帅哥"

echo ---------------------------------

#命令序列
#“()”中由子shell执行，类似于子进程（“()”内定义的变量仅在此文件有效）
#“{}”由当前shell执行
v3=300
echo "v3=${v3}"
(
#  v3=301
  echo "子shell中的v3=${v3}"
)
echo "父shell中的v3=${v3}"

v4=400
echo "v4=${v4}"
{
  v4=401
  echo "子shell中的v4=${v4}"
}
echo "父shell中的v4=${v4}"

echo ---------------------------------

# 运算符
# expr 是一款表达式计算工具，使用它能完成表达式的求值操作：
echo `expr 1 + 1`
echo ---------------------------------
# 控制语句 if、case、for
if [ ${v4} == 401 ] ; then
   echo "true"
else
  echo "false"
fi

echo ---------------------------------
who > who.txt
echo ---------------------------------

echo "type=${TYPE}"
