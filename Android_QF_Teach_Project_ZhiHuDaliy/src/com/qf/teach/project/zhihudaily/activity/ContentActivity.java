package com.qf.teach.project.zhihudaily.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.qf.teach.project.zhihudaily.R;
import com.qf.teach.project.zhihudaily.c.API;
import com.qf.teach.project.zhihudaily.entity.StoryInfo;
import com.qf.teach.project.zhihudaily.entity.ThemeStoryInfo;

public class ContentActivity extends Activity implements Listener<JSONObject>, OnClickListener {
	private static final String TAG = "ContentActivity";

	// 控件相关
	private Button btnShare;
	private WebView webContent;

	// 参数相关
	private long id;
	private String tag;

	// 网络相关
	private RequestQueue mQueue;

	// 数据相关
	private StoryInfo storyInfo;
	private ThemeStoryInfo themeStoryInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		initParams();
		initView();
		initData();
		initShareSDK();
	}

	/**
	 * 初始化参数
	 */
	private void initParams() {
		id = getIntent().getLongExtra("id", 0);
		tag = getIntent().getStringExtra("tag");

		Log.d(TAG, String.valueOf(id));
	}

	private void initView() {
		btnShare = (Button) findViewById(R.id.btn_share);
		btnShare.setOnClickListener(this);

		webContent = (WebView) findViewById(R.id.web_content);
	}

	private void initData() {
		mQueue = Volley.newRequestQueue(getApplicationContext());
		mQueue.add(new JsonObjectRequest(Method.GET, String.format(API.getStory(), id), null, this, null));
	}

	/**
	 * 初始化ShareSDK
	 */
	private void initShareSDK() {
		ShareSDK.initSDK(this);
	}

	/* -------------------- 网络请求 -------------------- */

	@Override
	public void onResponse(JSONObject response) {
		try {
			// 主题内容解析
			if (response.has("theme_name")) {
				themeStoryInfo = new ThemeStoryInfo();
				themeStoryInfo.setBody(response.getString("body"));
				themeStoryInfo.setEditor_avatar(response.getString("editor_avatar"));
				themeStoryInfo.setEditor_id(response.getLong("editor_id"));
				themeStoryInfo.setEditor_name(response.getString("editor_name"));
				themeStoryInfo.setGa_prefix(response.getString("ga_prefix"));
				themeStoryInfo.setId(response.getLong("id"));
				themeStoryInfo.setShare_url(response.getString("share_url"));
				themeStoryInfo.setTheme_id(response.getLong("theme_id"));
				themeStoryInfo.setTheme_image(response.getString("theme_image"));
				themeStoryInfo.setTheme_name(response.getString("theme_name"));
				themeStoryInfo.setTitle(response.getString("title"));
				themeStoryInfo.setType(response.getInt("type"));

				// JS
				JSONArray arrayJs = response.getJSONArray("js");
				if (arrayJs != null && arrayJs.length() > 0) {
					String[] js = new String[arrayJs.length()];
					for (int i = 0; i < arrayJs.length(); i++) {
						js[i] = arrayJs.getString(i);
					}
					themeStoryInfo.setJs(js);
				}

				// CSS
				JSONArray arrayCss = response.getJSONArray("css");
				if (arrayCss != null && arrayCss.length() > 0) {
					String[] css = new String[arrayCss.length()];
					for (int i = 0; i < arrayCss.length(); i++) {
						css[i] = arrayCss.getString(i);
					}
					themeStoryInfo.setCss(css);
				}

				// 加载HTML
				String body = null;
				if (themeStoryInfo.getCss() != null && themeStoryInfo.getCss().length > 0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < themeStoryInfo.getCss().length; i++) {
						sb.append("<link rel=\"stylesheet\" href=\"");
						sb.append(themeStoryInfo.getCss()[i]);
						sb.append("\">");
					}
					body = String.format("%s%s", sb.toString(), themeStoryInfo.getBody());
				} else {
					body = themeStoryInfo.getBody();
				}

				webContent.loadDataWithBaseURL(null, body, "text/html", "UTF-8", null);
			} else {
				// 首页内容解析
				storyInfo = new StoryInfo();
				storyInfo.setBody(response.getString("body"));
				storyInfo.setGa_prefix(response.getString("ga_prefix"));
				storyInfo.setId(response.getLong("id"));
				storyInfo.setImage(response.getString("image"));
				storyInfo.setImage_source(response.getString("image_source"));
				storyInfo.setShare_url(response.getString("share_url"));
				storyInfo.setTitle(response.getString("title"));
				storyInfo.setType(response.getInt("type"));

				// JS
				JSONArray arrayJs = response.getJSONArray("js");
				if (arrayJs != null && arrayJs.length() > 0) {
					String[] js = new String[arrayJs.length()];
					for (int i = 0; i < arrayJs.length(); i++) {
						js[i] = arrayJs.getString(i);
					}
					storyInfo.setJs(js);
				}

				// CSS
				JSONArray arrayCss = response.getJSONArray("css");
				if (arrayCss != null && arrayCss.length() > 0) {
					String[] css = new String[arrayCss.length()];
					for (int i = 0; i < arrayCss.length(); i++) {
						css[i] = arrayCss.getString(i);
					}
					storyInfo.setCss(css);
				}

				// 加载HTML
				String body = null;
				if (storyInfo.getCss() != null && storyInfo.getCss().length > 0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < storyInfo.getCss().length; i++) {
						sb.append("<link rel=\"stylesheet\" href=\"");
						sb.append(storyInfo.getCss()[i]);
						sb.append("\">");
					}
					body = String.format("%s%s", sb.toString(), storyInfo.getBody());
				} else {
					body = storyInfo.getBody();
				}

				// 设置顶部图片
				if (storyInfo.getImage() != null && storyInfo.getImage().length() > 0) {
					body = body.replace("<div class=\"img-place-holder\"></div>", "<div class=\"img-place-holder\" style=\"background-image: url(" + storyInfo.getImage() + "); background-position:center;\"></div>");
				}

				// webContent.getSettings().setJavaScriptEnabled(true);
				// webContent.loadUrl("file:///android_asset/index.html");

				webContent.loadDataWithBaseURL(null, body, "text/html", "UTF-8", null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/* -------------------- 网络请求 -------------------- */

	/* -------------------- 点击事件 -------------------- */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_share:
			OnekeyShare oks = new OnekeyShare();
			// 关闭sso授权
			oks.disableSSOWhenAuthorize();

			// 分享时Notification的图标和文字
			oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
			// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
			oks.setTitle(getString(R.string.share));
			// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
			oks.setTitleUrl("http://www.funtl.com");

			if (tag.equals("main")) {
				oks.setText(storyInfo.getTitle() + ":" + storyInfo.getShare_url());
			} else if (tag.equals("content")) {
				oks.setText(themeStoryInfo.getTitle() + ":" + themeStoryInfo.getShare_url());
			}

			// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
			oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
			// url仅在微信（包括好友和朋友圈）中使用
			oks.setUrl("http://www.funtl.com");
			// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//			oks.setComment("我是测试评论文本");
			// site是分享此内容的网站名称，仅在QQ空间使用
			oks.setSite(getString(R.string.app_name));
			// siteUrl是分享此内容的网站地址，仅在QQ空间使用
			oks.setSiteUrl("http://www.funtl.com");

			// 启动分享GUI
			oks.show(this);
			break;
		default:
			break;
		}
	}

	/* -------------------- 点击事件 -------------------- */

}
