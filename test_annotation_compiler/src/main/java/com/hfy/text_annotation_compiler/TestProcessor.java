package com.hfy.text_annotation_compiler;

import com.google.auto.service.AutoService;
import com.hfy.test_annotations.TestAnnotation;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * 自定义注解处理器
 * 知识来源：https://juejin.cn/post/6844903701283340301#heading-0
 *
 * 编译流程： source(源代码) -> processor（处理器） -> generate （文件生成）-> javacompiler -> .class文件 -> .dex(只针对安卓)。
 * 所以，注解处理器的执行是在编译的初始阶段。并且会有多个processor（查看注册的processor：intermediates/annotation_processor_list/debug/annotationProcessors.json）。
 *
 * 1、注解@AutoService 把 TestProcessor注册到编译器中
 * 2、注解@SupportedAnnotationTypes 用于表示 TestProcessor 要处理的注解，使用全名
 * 3、注解@SupportedSourceVersion 设置jdk环境为java8
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.hfy.test_annotations.TestAnnotation"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedOptions() //一些支持的可选配置项
public class TestProcessor extends AbstractProcessor {

    public static final String ACTIVITY = "android.app.Activity";


    /**
     * Filer 就是文件流输出路径，当我们用AbstractProcess生成一个java类的时候，我们需要保存在Filer指定的目录下
     */
    Filer mFiler;

    /**
     * 类型 相关的工具类。当process执行的时候，由于并没有加载类信息，所以java文件中的类信息都是用element来代替了。
     * 类型相关的都被转化成了一个叫TypeMirror，其getKind方法返回类型信息，其中包含了基础类型以及引用类型。
     *
     */
    Types types;

    /**
     * Elements 获取元素信息的工具，比如说一些类信息继承关系等。
     */
    Elements elementUtils;

    /**
     * 来报告错误、警告以及提示信息
     * 用来写一些信息给使用此注解库的第三方开发者的
     */
    Messager messager;

    /**
     * 每个注解处理器被初始化的时候都会被调用
     * @param processingEnvironment 能提供很多有用的工具类，Elements、Types和Filer
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mFiler = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    /**
     * 注解处理器实际处理方法，一般要求子类实现该抽象方法，
     * 你可以在在这里写你的扫描与处理注解的代码，以及生成Java文件。
     *
     * 在注解处理过程中，我们需要扫描所有的Java源文件。
     * 源代码的每一个部分都是一个特定类型的Element，也就是说Element代表源文件中的元素，例如包、类、字段、方法等。
     *
     * Parameterizable：表示混合类型的元素（不仅只有一种类型的Element)
     * TypeParameterElement：带有泛型参数的类、接口、方法或者构造器。
     * VariableElement：表示字段、常量、方法或构造函数。参数、局部变量、资源变量或异常参数。
     * QualifiedNameable：具有限定名称的元素
     * ExecutableElement：表示类或接口的方法、构造函数或初始化器（静态或实例），包括注释类型元素。
     * TypeElement :表示类和接口
     * PackageElement：表示包
     *
     * APT对整个源文件的扫描。有点类似于我们解析XML文件（这种结构化文本一样）。
     *
     * 例如：
     * package com.test.aaa.bbb.ccc;    //PackageElement
     * class Person {                   //TypeElement
     *     private String where;        //VariableElement
     *     public void doSomething() { } //ExecutableElement
     *     public void run() {          //ExecutableElement
     *         int runTime;             //VariableElement
     *     }
     * }
     *
     * @param annotations @SupportedAnnotationTypes声明的注解 和 未被消费的的注解的子集，也就是剩下的且是本Processor关注的 注解
     * @param roundEnvironment 有关当前和以前 round 的信息的环境，可以让你查询出包含特定注解的被注解元素
     * @return true，表示 @SupportedAnnotationTypes中声明的注解 由此 Processor 消费掉，不会传给下个Processor。
     * 在理解上，注解处理的流程 和 Android中的事件分发类似：{@link #process(Set, RoundEnvironment)} 和 onTouchEvent() 一样。
     * 所有注解不断向下分发，每个processor都可以决定是否消费掉自己声明的注解。
     *
     * 但是！！！！！！查看源码发现：
     * 即使这里annotations为空（本Processor关注的注解被处理且消耗了），但依然可以通过roundEnvironment 获取 某个注解 对应的所有Element。
     * 所以，通常主需要判断annotations非空，使用roundEnvironment.getElementsAnnotatedWith(XXX.class)获取 某注解 对应的所有Element即可。
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (annotations == null || annotations.size() == 0){
            return false;
        }

        //获取所有包含 @TestAnnotation 注解的元素
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(TestAnnotation.class);

        for (Element element : elements) {

            TypeMirror typeMirror = element.asType();

            //获取元素 具体 类别，例如是类还是接口（见ElementKind枚举类型）
            ElementKind kind = element.getKind();

            //0、元素是类(其实这个可以省去，下面)
            if (kind == ElementKind.CLASS){

                //1、检验可见性修饰符--public
                Set<Modifier> modifiers = element.getModifiers();//可见性修饰符
                if (!modifiers.contains(Modifier.PUBLIC)) { //见Modifier 枚举类型
                    messager.printMessage(Diagnostic.Kind.ERROR, "the class is not public");
                    return false;
                }

                //2、检验继承类型--Activity
                TypeMirror type_Activity = elementUtils.getTypeElement(ACTIVITY).asType();
                if (!types.isSubtype(typeMirror, type_Activity)) {
                    return false;
                }

                //3、拿到 注解信息、element信息，然后通过JavaPoet来构造源文件
                createFileByJavaPoet(element.getAnnotation(TestAnnotation.class), element);

            }else if (kind == ElementKind.INTERFACE){ //元素是接口
                //
            }else if (kind == ElementKind.METHOD){ //元素是方法
                //获取该方法的返回值类型、参数类型、参数名称
                ExecutableElement methodElement = (ExecutableElement) element;
                TypeMirror returnType = methodElement.getReturnType();//获取TypeMirror，能使我们在未经编译的源代码中查看方法、域以及类型信息
                TypeKind returnTypeKind = returnType.getKind();//获取返回值的类型（见TypeKind枚举类型）
                System.out.println("print return type----->" + returnTypeKind.toString());
            }
        }
        return true;
    }


    /**
     * 生成java源代码文件
     * todo 如何使用生成的java源代码文件？当然可以手动使用；但是后续有新增呢？那就考虑编译时 自动搜集这些生成的类，然后插桩织入代码-ASM
     * @param annotation 注解信息
     * @param element 使用annotation的元素
     */
    private void createFileByJavaPoet(TestAnnotation annotation, Element element) {

        String name = annotation.name();

        //获取子元素。例如 类-TypeElement 的子元素 可能有 方法-ExecutableElement、变量-VariableElement
        List<? extends Element> enclosedElements = element.getEnclosedElements();
        //获取父元素。例如 类-TypeElement 的父元素 可能是 包-PackageElement
        Element enclosingElement = element.getEnclosingElement();

        //可以获取子元素的注解（如果有）
        for (Element enclosedElement : enclosedElements) {
//            enclosedElement.getAnnotation()
        }

        //根据以上信息，使用JavaPoet生成java代码文件

        //创建main方法
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)//设置可见性修饰符public static
                .returns(void.class)//设置返回值为void
                .addParameter(String[].class, "args")//添加参数类型为String数组，且参数名称为args
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")//添加语句
                .build();
        //创建类
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)//将main方法添加到HelloWord类中
                .build();

        //创建文件，第一个参数是包名，第二个参数是相关类
        JavaFile javaFile = JavaFile.builder("com.hfy.demo01", helloWorld)
                .build();

        try {
            //创建文件
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            log(e.getMessage());
        }
    }
    /**
     * 调用打印语句而已
     */
    private void log(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

//    /**
//     * 用来指定你使用的Java版本，通常这里返回SourceVersion.latestSupported()。
//     * @return
//     */
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.latestSupported();
//    }
}