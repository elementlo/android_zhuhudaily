package com.qf.teach.project.zhihudaily.task;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.qf.teach.project.zhihudaily.activity.MainActivity;
import com.qf.teach.project.zhihudaily.c.API;
import com.qf.teach.project.zhihudaily.util.NetUtil;

/**
 * 欢迎界面的异步任务
 * @author Lusifer
 *
 * 2014年12月1日上午11:15:24
 */
@Deprecated
public class WelcomeTask extends AsyncTask<String, Void, Bitmap> implements AnimationListener {
	private Context context;
	private ImageView imgStart;

	public WelcomeTask(Context context, ImageView imgStart) {
		this.context = context;
		this.imgStart = imgStart;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bitmap = null;
		
		try {
			String json = NetUtil.getJson(API.getStartImageUrl());
			JSONObject jsonObject = new JSONObject(json);
			String imgUrl = jsonObject.getString("img");
			
			bitmap = NetUtil.getBitmap(imgUrl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) imgStart.setImageBitmap(result);
		
		// 图片动画
		Animation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); // 将图片放大1.2倍，从中心开始缩放
		animation.setDuration(2000); // 动画持续时间
		animation.setFillAfter(true); // 动画结束后停留在结束的位置
		animation.setAnimationListener(this); // 添加动画监听
		imgStart.startAnimation(animation); // 启动动画
	}
	
	// 动画监听

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// 动画结束时跳转至首页
		context.startActivity(new Intent(context, MainActivity.class));
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
	
}
