#!/usr/bin/env python3

# 在 python 用 import 或者 from...import 来导入相应的模块
# 模块是一个包含所有你定义的函数和变量的文件，其后缀名是.py。模块可以被别的程序引入
# import sys 就是导入 python 标准库中模块，即导入sys.py中的能力
import sys

# python 最具特色的就是用缩进来写模块。所有代码块语句必须包含相同的缩进空白数量，这个必须严格执行
if True:
    print("true")
else:
    print("false")

# word1='单引号'
# word2="双引号"
# word3_1='''三个单引号
# 单单单'''
# word3_3="""三个双引号
# 双双双"""
#
# print(word1)
# print(word2)
# print(word3_1)
# print(word3_3)

#我是单行注释
"""我是
多行注释"""

#键盘输入
# input("按下 enter 键退出，其他任意键显示...\n")


# str='123456789'
#
# print(str)                 # 输出字符串
# print(str[0:-1])           # 输出第一个到倒数第二个的所有字符
# print(str[0])              # 输出字符串第一个字符
# print(str[0:1])            # 输出从第一个开始到第二个的字符
# print(str[2:5])            # 输出从第三个开始到第五个的字符
# print(str[2:])             # 输出从第三个开始后的所有字符
# print(str[1:5:2])          # 输出从第二个开始到第五个且每隔一个的字符（步长为2）
# print(str * 2)             # 输出字符串两次
# print(str + '你好')         # 连接字符串
#
# print('------------------------------')
#
# print('hello\nrunoob')      # 使用反斜杠(\)+n转义特殊字符
# print(r'hello\nrunoob')     # 在字符串前面添加一个 r，表示原始字符串，不会发生转义，r 指 raw，即 raw string

sys.stdout.write(" nihao \n")

# isinstance 和 type 的区别在于：
# type()不会认为子类是一种父类类型。
# isinstance()会认为子类是一种父类类型。

class A:
    pass

print(type(A())==A) #True
print(isinstance(A(),A))#True

class B(A):
    pass

print(type(B())==A)#False
print(isinstance(B(),A))#True

print(type(A()))#<class '__main__.A'>
print(type(1.0))#<class 'float'>

# 显式类型转换,使用 int()、float()、str() 等预定义函数来执行显式类型转换
print("------------------------显式类型转换:")
v1 = float("1.11")
v2 = 1
v3 = v1 + v2
print(v1)
print(v2)
print(v3)

print("------------------------推导式：")
# 推导式是一种独特的数据处理方式，可以从一个数据序列构建另一个新的数据序列的结构体。

# 列表推导式
# [表达式 for 变量 in 列表 if 条件]
# [out_exp_res for out_exp in input_list if condition]
oldScoreList=[30,55,60,75,80,90]
goodScoreList = [score for score in oldScoreList if score >=60]
print(goodScoreList) #[60, 75, 80, 90]

oldNameList=["james","curry","durant","kobe","jordon","paul","harden"]
newNameList=[name.upper() for name in oldNameList if len(name)>4]
print(newNameList)#['JAMES', 'CURRY', 'DURANT', 'JORDON', 'HARDEN']

# 字典推导式
# { key_expr: value_expr for value in collection if condition }
list=["james","curry","durant","kobe","jordon","paul","harden"]
list.pop
newDic={name:len(name) for name in list if len(name)>4}
print(newDic)#{'james': 5, 'curry': 5, 'durant': 6, 'jordon': 6, 'harden': 6}

# 集合推导式，注意：string、list 和 tuple 都属于 sequence（序列）
# { expression for item in Sequence if conditional }
newSet1={num**2 for num in [1,2,3,4,5]}
print(newSet1)#{1, 4, 9, 16, 25}

newSet2={letter.upper() for letter in 'abcdef'}
print(newSet2)#{'B', 'F', 'E', 'A', 'C', 'D'}

# 元组推导式
# 可以利用 range 区间、元组、列表、字典和集合等数据类型，快速生成一个满足指定需求的元组。
# (expression for item in Sequence if conditional )
# 元组推导式和列表推导式的用法也完全相同，只是元组推导式是用 () 圆括号将各部分括起来，而列表推导式用的是中括号 []，另外元组推导式返回的结果是一个生成器对象。
# 例如，我们可以使用下面的代码生成一个包含数字 1~9 的元组：
a=(num for num in range(1,10))
print(a)#<generator object <genexpr> at 0x7f920808dc10>
b=tuple(a)
print(b)#(1, 2, 3, 4, 5, 6, 7, 8, 9)

print("------------------------运算符：")

#逻辑运算符

#a为非0数字或True，输出"是true！"，即 非0等价于True，（所有非零的数字或者非空对象都是真）
#a为0或False，输出"是false！"，即 0等价于False，("", [],  (),  {}, set() 都是假)
a=1
if(""):
    print("是true！")
else:
    print("是false！")

#成员运算符
#in，在；not in，不在
a=1
list=[1,2,3]
print("a in list:",a in list)#a in list: True

#身份运算符
# is 是判断两个标识符是不是引用自一个对象；is not，不是同一个对象
# is 与 == 区别：is 用于判断两个变量引用对象是否为同一个， == 用于判断引用变量的值是否相等。
a=A()
b=a
print("a is b:",a is b)#a is b: True

print("------------------------字符串：")

# 转义字符
# 在需要在字符中使用特殊字符时，python 用反斜杠 \ 转义字符

# 字符串格式化
# Python 支持格式化字符串的输出 。用法是将一个值插入到一个有字符串格式符 %s 的字符串中。
print("我是 %s, 年龄是 %d" % ('飞洋',30)) # 我是 飞洋, 年龄是 30

