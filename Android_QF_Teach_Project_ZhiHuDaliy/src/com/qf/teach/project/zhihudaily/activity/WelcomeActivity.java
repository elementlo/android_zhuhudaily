package com.qf.teach.project.zhihudaily.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.qf.teach.project.zhihudaily.R;
import com.qf.teach.project.zhihudaily.c.API;
import com.qf.teach.project.zhihudaily.cache.BitmapCache;

public class WelcomeActivity extends Activity implements AnimationListener {
	private NetworkImageView imgStart;
	private RequestQueue mQueue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		initView();
		initData();
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		imgStart = (NetworkImageView) findViewById(R.id.img_start);
	}
	
	/**
	 * ��ʼ������
	 */
	private void initData() {
		mQueue = Volley.newRequestQueue(getApplicationContext());
		mQueue.add(new JsonObjectRequest(Method.GET, API.getStartImageUrl(), null, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String imgUrl = response.getString("img");
					imgStart.setImageUrl(imgUrl, new ImageLoader(mQueue, new BitmapCache()));
					
					// ͼƬ����
					Animation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); // ��ͼƬ�Ŵ�1.2���������Ŀ�ʼ����
					animation.setDuration(2000); // ��������ʱ��
					animation.setFillAfter(true); // ����������ͣ���ڽ�����λ��
					animation.setAnimationListener(WelcomeActivity.this); // ��Ӷ�������
					imgStart.startAnimation(animation); // ��������
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, null));
	}
	
	// ��������

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// ��������ʱ��ת����ҳ
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
}
