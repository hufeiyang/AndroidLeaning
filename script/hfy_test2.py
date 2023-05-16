#!/usr/bin/env python3


# 在 python 用 import 或者 from...import 来导入相应的模块
# 模块是一个包含所有你定义的函数和变量的文件，其后缀名是.py。模块可以被别的程序引入
# 1、import sys 引入 python 标准库中的 sys.py 模块；这是引入某一模块的方法。
# 2、sys.argv 是一个包含命令行参数的列表。
# 3、sys.path 包含了一个 Python 解释器自动查找所需模块的路径的列表。
import sys
print('命令行参数如下:')
for i in sys.argv:
   print(i)
print('\nPython 路径为：', sys.path, '\n')

import hfy_test # 在执行时，即 ./hfy_test2.py时，hfy_test.py中的主程序会执行，且hfy_test.py中的函数、属性都会被导入

print(hfy_test.oldNameList) #['james', 'curry', 'durant', 'kobe', 'jordon', 'paul', 'harden']

print("hh %s h" % ("nini"))
len({1:"a",2:"b"})

print(dir())#dir() 函数会罗列出当前定义的所有名称


import pickle #序列化、反序列化
import os

# file=open("PythonFiles/pickle.txt","wb")#wb
# dic={"a":1,"b":2,'c':3}
# pickle.dump(dic, file)
# file.close

# file=open("PythonFiles/pickle.txt","rb")#rb
# dic=pickle.load(file)
# print(dic)#{'a': 1, 'b': 2, 'c': 3}

#安装requests模块，因为不是Python内置的。用于http请求
#os模块system方法可直接 自系统上执行命令!!! 即shell命令，类似 os.system("cd /files/cache")
ret= os.system("pip3 install requests")
#安装后就可以导入requests模块
import requests
response=requests.get("https://www.baidu.com")
print(response.text)