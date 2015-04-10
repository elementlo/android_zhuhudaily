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

	// �ؼ����
	private Button btnShare;
	private WebView webContent;

	// �������
	private long id;
	private String tag;

	// �������
	private RequestQueue mQueue;

	// �������
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
	 * ��ʼ������
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
	 * ��ʼ��ShareSDK
	 */
	private void initShareSDK() {
		ShareSDK.initSDK(this);
	}

	/* -------------------- �������� -------------------- */

	@Override
	public void onResponse(JSONObject response) {
		try {
			// �������ݽ���
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

				// ����HTML
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
				// ��ҳ���ݽ���
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

				// ����HTML
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

				// ���ö���ͼƬ
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

	/* -------------------- �������� -------------------- */

	/* -------------------- ����¼� -------------------- */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_share:
			OnekeyShare oks = new OnekeyShare();
			// �ر�sso��Ȩ
			oks.disableSSOWhenAuthorize();

			// ����ʱNotification��ͼ�������
			oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
			// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
			oks.setTitle(getString(R.string.share));
			// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
			oks.setTitleUrl("http://www.funtl.com");

			if (tag.equals("main")) {
				oks.setText(storyInfo.getTitle() + ":" + storyInfo.getShare_url());
			} else if (tag.equals("content")) {
				oks.setText(themeStoryInfo.getTitle() + ":" + themeStoryInfo.getShare_url());
			}

			// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
			oks.setImagePath("/sdcard/test.jpg");// ȷ��SDcard������ڴ���ͼƬ
			// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
			oks.setUrl("http://www.funtl.com");
			// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
//			oks.setComment("���ǲ��������ı�");
			// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
			oks.setSite(getString(R.string.app_name));
			// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
			oks.setSiteUrl("http://www.funtl.com");

			// ��������GUI
			oks.show(this);
			break;
		default:
			break;
		}
	}

	/* -------------------- ����¼� -------------------- */

}
