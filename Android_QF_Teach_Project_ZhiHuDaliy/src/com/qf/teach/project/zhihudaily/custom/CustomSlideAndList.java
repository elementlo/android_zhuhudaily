package com.qf.teach.project.zhihudaily.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.qf.teach.project.zhihudaily.R;
import com.qf.teach.project.zhihudaily.activity.ContentActivity;
import com.qf.teach.project.zhihudaily.c.API;
import com.qf.teach.project.zhihudaily.cache.BitmapCache;
import com.qf.teach.project.zhihudaily.entity.News;
import com.qf.teach.project.zhihudaily.entity.Story;
import com.qf.teach.project.zhihudaily.entity.TopStory;
import com.qf.teach.project.zhihudaily.util.date.DateStyle;
import com.qf.teach.project.zhihudaily.util.date.DateUtil;

/**
 * �Զ���õ�
 * @author Lusifer
 *
 * 2014��12��1������2:32:41
 */
public class CustomSlideAndList extends FrameLayout implements Listener<JSONObject>, OnPageChangeListener, OnRefreshListener2<ScrollView>, OnItemClickListener {
	private Context context;
	
	// �õ����
	private ViewPager vpSlide;
	private List<ImageView> imageViews;
	private LinearLayout dotsGroup;
	private TextView txTitle;
	private MyPagerAdapter pagerAdapter;
	private Timer timer;
	
	// �����б�
	private ListView lvNews;
	private MyBaseAdapter baseAdapter;
	private PullToRefreshScrollView svNews;
	private boolean isUp;
	
	// �������
	private RequestQueue mQueue;
	
	// �������
	private News news;
	
	public CustomSlideAndList(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.custom_slide_list, this);
		
		initView();
	}
	
	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		dotsGroup = (LinearLayout) findViewById(R.id.dots_group);
		txTitle = (TextView) findViewById(R.id.tx_title);
		
		pagerAdapter = new MyPagerAdapter();
		vpSlide = (ViewPager) findViewById(R.id.vp_slide);
		vpSlide.setOnPageChangeListener(this);
		vpSlide.setAdapter(pagerAdapter);
		
		baseAdapter = new MyBaseAdapter();
		lvNews = (ListView) findViewById(R.id.lv_news);
		lvNews.setOnItemClickListener(this);
		lvNews.setAdapter(baseAdapter);
		
		svNews = (PullToRefreshScrollView) findViewById(R.id.sv_news);
		svNews.setOnRefreshListener(this);
	}
	
	/**
	 * ��ʼ������
	 */
	public void init(String uri) {
		mQueue = Volley.newRequestQueue(context);
		mQueue.add(new JsonObjectRequest(Method.GET, uri, null, this, null));
	}

	/* -------------------- ���ݽ��� -------------------- */
	
	@Override
	public void onResponse(JSONObject response) {
		try {
			if (!isUp) {
				// ����Latest
				news = new News();
				news.setDate(response.getString("date"));
				
				// ����Stories�ڵ�
				parserStory(response);
				// ����TopStories�ڵ�
				parserTopStory(response);
				
				// ��ʼ���õ�
				initSlide();
				// ��ʼ��ListView;
				initListView();
				
				// ��ScrollView�ö�
				svNews.getRefreshableView().smoothScrollTo(0, 0);
			} else { // ��������
				news.setDate(response.getString("date"));
				
				// ����Stories�ڵ�
				parserStory(response);
				// ��ʼ��ListView;
				initListView();
				
				isUp = false;
			}
			
			// �ر�����ˢ��
			svNews.onRefreshComplete();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����Stories�ڵ�
	 * @throws JSONException 
	 */
	private void parserStory(JSONObject response) throws JSONException {
		JSONArray arrayStories = response.getJSONArray("stories");
		if (arrayStories != null && arrayStories.length() > 0) {
			List<Story> stories = new ArrayList<Story>();
			
			// ����
			Story storyTitle = new Story();
			String date = response.getString("date");
			if (date.equals(DateUtil.DateToString(new Date(), "yyyyMMdd"))) {
				storyTitle.setTitle("��������");
			} else {
				String mmdd = DateUtil.StringToString(date, DateStyle.MM_DD_CN);
				String week = DateUtil.getWeek(date).getChineseName();
				storyTitle.setTitle(String.format("%s  %s", mmdd, week));
			}
			stories.add(storyTitle);
			
			for (int i = 0 ; i < arrayStories.length() ; i++) {
				JSONObject obj = arrayStories.getJSONObject(i);
				
				if (obj.has("theme_name")) {
					
				} else {
					Story story = new Story();
					story.setGa_prefix(obj.getString("ga_prefix"));
					story.setId(obj.getLong("id"));
					
					// ͼƬ����
					JSONArray array = obj.getJSONArray("images");
					if (array != null && array.length() > 0) {
						String[] images = new String[array.length()];
						for (int x = 0 ; x < array.length() ; x++) {
							images[x] = array.getString(x);
						}
						story.setImages(images);
					}
					
					story.setShare_url(obj.getString("share_url"));
					story.setTitle(obj.getString("title"));
					story.setType(obj.getInt("type"));
					stories.add(story);
				}
			}
			
			// ��ֵ
			if (news.getStories() != null) {
				news.getStories().addAll(stories);
			} else {
				news.setStories(stories);
			}
		}
	}
	
	/**
	 * ����TopStories�ڵ�
	 * @throws JSONException 
	 */
	private void parserTopStory(JSONObject response) throws JSONException {
		JSONArray arrayTopStories = response.getJSONArray("top_stories");
		if (arrayTopStories != null && arrayTopStories.length() > 0) {
			List<TopStory> topStories = new ArrayList<TopStory>();
			for (int i = 0 ; i < arrayTopStories.length() ; i++) {
				JSONObject obj = arrayTopStories.getJSONObject(i);
				TopStory topStory = new TopStory();
				topStory.setGa_prefix(obj.getString("ga_prefix"));
				topStory.setId(obj.getLong("id"));
				topStory.setImage(obj.getString("image"));
				topStory.setShare_url(obj.getString("share_url"));
				topStory.setTitle(obj.getString("title"));
				topStory.setType(obj.getInt("type"));
				topStories.add(topStory);
			}
			
			// ��ֵ
			news.setTopStories(topStories);
		}
	}
	
	/* -------------------- ���ݽ��� -------------------- */
	
	/* -------------------- �õ�Ч�� -------------------- */
	
	private int item; // ViewPager��Postion
	private Handler pageChangeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			vpSlide.setCurrentItem(item);
			if (item == imageViews.size() - 1) {
				item = 0;
			} else {
				item++;
			}
		}
	};
	
	/**
	 * ��ʼ���õ�
	 */
	private void initSlide() {
		// ��ʼ��ImageViews
		imageViews = new ArrayList<ImageView>();
		
		for (int i = 0 ; i < news.getTopStories().size() ; i++) {
			TopStory topStory = news.getTopStories().get(i);
			NetworkImageView imageView = new NetworkImageView(context);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setImageUrl(topStory.getImage(), new ImageLoader(mQueue, new BitmapCache()));
			imageViews.add(imageView);
		}
		
		// ֪ͨ�õ����ݸı�
		pagerAdapter.notifyDataSetChanged();
		
		// �õ��л�Ч��
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				pageChangeHandler.sendEmptyMessage(0);
			}
		}, 3000, 3000);
		
		// ��ʼ��СԲ��
		initSmallDot(0);
		
		// ��ʼ������
		txTitle.setText(news.getTopStories().get(0).getTitle());
	}
	
	/**
	 * ��ʼ��СԲ��
	 * @param index
	 */
	private void initSmallDot(int index) {
		dotsGroup.removeAllViews();
		
		for (int i = 0 ; i < imageViews.size() ; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setImageResource(R.drawable.dot_default);
			imageView.setPadding(5, 0, 5, 0);
			
			dotsGroup.addView(imageView);
		}
		
		// ����ѡ����
		((ImageView)dotsGroup.getChildAt(index)).setImageResource(R.drawable.dot_selected);
	}
	
	/**
	 * ViewPager�����������õƣ�
	 * @author Lusifer
	 *
	 * 2014��12��1������3:36:20
	 */
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews == null ? 0 : imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imageViews.get(position));
			return imageViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViews.get(position));
		}
		
	}
	
	// ViewPager�л����� -- ��ʼ
	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		initSmallDot(position);
		item = position;
		
		txTitle.setText(news.getTopStories().get(position).getTitle());
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}
	
	// ViewPager�л����� -- ����
	
	/* -------------------- �õ�Ч�� -------------------- */
	
	/* -------------------- �����б� -------------------- */
	
	/**
	 * ��ʼ�������б�
	 */
	private void initListView() {
		// ֪ͨListView���ݸı�
		baseAdapter.notifyDataSetChanged();
	}
	
	class MyBaseAdapter extends BaseAdapter {
		private ViewHolder viewHolder;

		@Override
		public int getCount() {
			return news == null ? 0 : news.getStories().size();
		}

		@Override
		public Object getItem(int position) {
			return news.getStories().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Story story = news.getStories().get(position);
			
			// �ж��Ƿ�Ϊ����
			if (story.getId() == 0) {
				convertView = LayoutInflater.from(context).inflate(R.layout.list_news_titile, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.txTitle = (TextView) convertView.findViewById(R.id.tx_title);
				viewHolder.txTitle.setText(story.getTitle());
			} else { // ����
				if (convertView == null || story.getId() != 0) {
					convertView = LayoutInflater.from(context).inflate(R.layout.list_news, parent, false);
					
					viewHolder = new ViewHolder();
					viewHolder.txTitle = (TextView) convertView.findViewById(R.id.tx_title);
					viewHolder.imgThumb = (NetworkImageView) convertView.findViewById(R.id.img_thumb);
					
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				
				viewHolder.txTitle.setText(story.getTitle());
				if (story.getImages() != null && story.getImages().length > 0) {
					viewHolder.imgThumb.setImageUrl(story.getImages()[0], new ImageLoader(mQueue, new BitmapCache()));
				}
			}
			
			return convertView;
		}
		
		class ViewHolder {
			public TextView txTitle;
			public NetworkImageView imgThumb;
		}
		
	}

	/* -------------------- �����б� -------------------- */
	
	/* -------------------- ����ˢ�£��������� -------------------- */
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// �������
		timer.cancel();
		news.getStories().clear();
		news.getTopStories().clear();
		
		// ���³�ʼ������
		init(API.getLatestUrl());
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		isUp = true;
		mQueue.add(new JsonObjectRequest(Method.GET, String.format(API.getBefore(), news.getDate()), null, this, null));
	}

	/* -------------------- ����ˢ�£��������� -------------------- */
	
	/* -------------------- ListView���� -------------------- */
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Story story = news.getStories().get(position);
		Intent intent = new Intent(context, ContentActivity.class);
		intent.putExtra("id", story.getId());
		intent.putExtra("tag", "main");
		context.startActivity(intent);
	}
	
	/* -------------------- ListView���� -------------------- */
}
