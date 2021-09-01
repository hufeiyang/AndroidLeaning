package com.viewopt.processor;

import javax.lang.model.element.TypeElement;

public class ClassValidator {
    static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen)
                .replace('.', '$');
    }
}
