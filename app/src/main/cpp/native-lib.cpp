#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_bamabin_tv_1app_utils_AESHelper_getKey(JNIEnv* env, jobject obj /* this */) {
    jclass cls = (*env).GetObjectClass(obj);
    jmethodID mid = (*env).GetMethodID(cls,"getPackageManager"  ,"()Landroid/content/pm/PackageManager;");
    jobject packageManager = (*env).CallObjectMethod(obj, mid);

    // this.getPackageName()
    mid = (*env).GetMethodID(cls, "getPackageName", "()Ljava/lang/String;");//
    jstring packageName = (jstring) (*env).CallObjectMethod(obj, mid);

    // packageManager->getPackageInfo(packageName, GET_SIGNATURES);
    cls = (*env).GetObjectClass(packageManager);
    mid = (*env).GetMethodID(cls, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    jint flags = 0x00000040;
    jobject packageInfo = (*env).CallObjectMethod(packageManager, mid, packageName, flags);

    // packageInfo->signatures
    cls = (*env).GetObjectClass(packageInfo);
    jfieldID fid = (*env).GetFieldID(cls, "signatures", "[Landroid/content/pm/Signature;");
    jobjectArray signatures = (jobjectArray )(*env).GetObjectField(packageInfo, fid);

    // signatures[0]
    jobject signature = (*env).GetObjectArrayElement(signatures, 0);
    cls=(*env).GetObjectClass(signature);
    mid=(*env).GetMethodID(cls, "hashCode", "()I");

    jint hashInt=(*env).CallIntMethod(signature, mid);

    std::string hashStr="";

//    if (hashInt != -1757145615){
//        return env->NewStringUTF(hashStr.c_str());
//    }

    char buf[64];
    sprintf(buf, "%d", hashInt);
//    hashStr.append(buf);
    hashStr.append("-1757145615BAMABiNR1sTuVwXyZoiWE");

    return env->NewStringUTF(hashStr.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_bamabin_tv_1app_utils_AESHelper_getIV(JNIEnv* env, jobject obj /* this */) {
    std::string data = "aB7dEfGh1jKlMn0P";

    return env->NewStringUTF(data.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_bamabin_tv_1app_data_remote_RequestInterceptor_getURL(JNIEnv* env, jobject obj /* this */) {
    jclass cls = (*env).GetObjectClass(obj);
    jmethodID mid = (*env).GetMethodID(cls,"getPackageManager"  ,"()Landroid/content/pm/PackageManager;");
    jobject packageManager = (*env).CallObjectMethod(obj, mid);

    // this.getPackageName()
    mid = (*env).GetMethodID(cls, "getPackageName", "()Ljava/lang/String;");//
    jstring packageName = (jstring) (*env).CallObjectMethod(obj, mid);

    // packageManager->getPackageInfo(packageName, GET_SIGNATURES);
    cls = (*env).GetObjectClass(packageManager);
    mid = (*env).GetMethodID(cls, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    jint flags = 0x00000040;
    jobject packageInfo = (*env).CallObjectMethod(packageManager, mid, packageName, flags);

    // packageInfo->signatures
    cls = (*env).GetObjectClass(packageInfo);
    jfieldID fid = (*env).GetFieldID(cls, "signatures", "[Landroid/content/pm/Signature;");
    jobjectArray signatures = (jobjectArray )(*env).GetObjectField(packageInfo, fid);

    // signatures[0]
    jobject signature = (*env).GetObjectArrayElement(signatures, 0);
    cls=(*env).GetObjectClass(signature);
    mid=(*env).GetMethodID(cls, "hashCode", "()I");

    jint hashInt=(*env).CallIntMethod(signature, mid);

    std::string hashStr="";

//    if (hashInt != -1757145615){
//        return env->NewStringUTF(hashStr.c_str());
//    }

    std::string data = "https://drive.google.com";

    return env->NewStringUTF(data.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_bamabin_tv_1app_repo_AppRepository_getId(JNIEnv* env, jobject obj /* this */) {
    jclass cls = (*env).GetObjectClass(obj);
    jmethodID mid = (*env).GetMethodID(cls,"getPackageManager"  ,"()Landroid/content/pm/PackageManager;");
    jobject packageManager = (*env).CallObjectMethod(obj, mid);

    // this.getPackageName()
    mid = (*env).GetMethodID(cls, "getPackageName", "()Ljava/lang/String;");//
    jstring packageName = (jstring) (*env).CallObjectMethod(obj, mid);

    // packageManager->getPackageInfo(packageName, GET_SIGNATURES);
    cls = (*env).GetObjectClass(packageManager);
    mid = (*env).GetMethodID(cls, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    jint flags = 0x00000040;
    jobject packageInfo = (*env).CallObjectMethod(packageManager, mid, packageName, flags);

    // packageInfo->signatures
    cls = (*env).GetObjectClass(packageInfo);
    jfieldID fid = (*env).GetFieldID(cls, "signatures", "[Landroid/content/pm/Signature;");
    jobjectArray signatures = (jobjectArray )(*env).GetObjectField(packageInfo, fid);

    // signatures[0]
    jobject signature = (*env).GetObjectArrayElement(signatures, 0);
    cls=(*env).GetObjectClass(signature);
    mid=(*env).GetMethodID(cls, "hashCode", "()I");

    jint hashInt=(*env).CallIntMethod(signature, mid);

    std::string hashStr="";

//    if (hashInt != -1757145615){
//        return env->NewStringUTF(hashStr.c_str());
//    }

    std::string data = "1--IfXV33qVKgpWp1be5DUztWJiJgH-ZB";

    return env->NewStringUTF(data.c_str());
}