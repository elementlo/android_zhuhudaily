#include "com_qf_teach_project_zhihudaily_c_API.h"

JNIEXPORT jstring JNICALL Java_com_qf_teach_project_zhihudaily_c_API_getThemesUrl(
		JNIEnv *env, jclass thiz) {
	return (*env)->NewStringUTF(env, "http://news-at.zhihu.com/api/3/themes");
}

JNIEXPORT jstring JNICALL Java_com_qf_teach_project_zhihudaily_c_API_getStartImageUrl(
		JNIEnv *env, jclass thiz) {
	return (*env)->NewStringUTF(env,
			"http://news-at.zhihu.com/api/3/start-image/480*728");
}

JNIEXPORT jstring JNICALL Java_com_qf_teach_project_zhihudaily_c_API_getLatestUrl(
		JNIEnv *env, jclass thiz) {
	return (*env)->NewStringUTF(env,
			"http://news-at.zhihu.com/api/3/stories/latest");
}

JNIEXPORT jstring JNICALL Java_com_qf_teach_project_zhihudaily_c_API_getBefore(
		JNIEnv *env, jclass thiz) {
	return (*env)->NewStringUTF(env,
				"http://news-at.zhihu.com/api/3/stories/before/%s");
}

JNIEXPORT jstring JNICALL Java_com_qf_teach_project_zhihudaily_c_API_getTheme
  (JNIEnv *env, jclass thiz) {
	return (*env)->NewStringUTF(env,
					"http://news-at.zhihu.com/api/3/theme/%s");
}

JNIEXPORT jstring JNICALL Java_com_qf_teach_project_zhihudaily_c_API_getStory
  (JNIEnv *env, jclass thiz) {
	return (*env)->NewStringUTF(env,
						"http://news-at.zhihu.com/api/3/story/%s");
}

JNIEXPORT jstring JNICALL Java_com_qf_teach_project_zhihudaily_c_API_getStoryExtra
  (JNIEnv *env, jclass thiz) {
	return (*env)->NewStringUTF(env,
						"http://news-at.zhihu.com/api/3/story-extra/%s");
}
