#include <jni.h>

/*声明要调用的方法*/
extern "C" {
extern int bsdiff_main(int argc, char *argv[]);
extern int bspatch_main(int argc, char *argv[]);
}

/**
 * 生成补丁文件
 */
extern "C"
JNIEXPORT jint JNICALL
Java_com_playgame_havefun_hfybsdiff_BsDiffUtil_diff(JNIEnv *env, jobject thiz,
                                                    jstring new_file_path, jstring old_file_path,
                                                    jstring patch_file_path) {
    const char *newFile = env->GetStringUTFChars(new_file_path, nullptr);
    const char *oldFile = env->GetStringUTFChars(old_file_path, nullptr);
    const char *patchFile = env->GetStringUTFChars(patch_file_path, nullptr);

    char *argv[] = {"hfy_bs_diff", const_cast<char *>(oldFile), const_cast<char *>(newFile),
                    const_cast<char *>(patchFile)};
    /* 调用bsDiff的main方法，*/
    int res = bsdiff_main(4, argv);
    env->ReleaseStringUTFChars(old_file_path, oldFile);
    env->ReleaseStringUTFChars(new_file_path, newFile);
    env->ReleaseStringUTFChars(patch_file_path, patchFile);
    return res;
}



/**
 * 合并补丁文件
 */
extern "C"
JNIEXPORT jint JNICALL
Java_com_playgame_havefun_hfybsdiff_BsDiffUtil_patch(JNIEnv *env, jobject thiz,
                                                     jstring old_file_path, jstring patch_file_path,
                                                     jstring combine_file_path) {
    const char *oldFile = env->GetStringUTFChars(old_file_path, nullptr);
    const char *patchFile = env->GetStringUTFChars(patch_file_path, nullptr);
    const char *combineFile = env->GetStringUTFChars(combine_file_path, nullptr);
    char *argv[] = {"hfy_bs_patch", const_cast<char *>(oldFile), const_cast<char *>(combineFile),
                    const_cast<char *>(patchFile)};
    /*调bspatch的main方法*/
    int res = bspatch_main(4, argv);
    env->ReleaseStringUTFChars(old_file_path, oldFile);
    env->ReleaseStringUTFChars(combine_file_path, combineFile);
    env->ReleaseStringUTFChars(patch_file_path, patchFile);
    return res;
}