package com.nui.multiphotopicker.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nui.multiphotopicker.R;
import com.nui.multiphotopicker.model.ImageItem;
import com.nui.multiphotopicker.util.ImageDisplayer;
import com.nui.multiphotopicker.util.IntentConstants;

public class ImageZoomActivity extends Activity
{

	private ViewPager pager;
	private MyPageAdapter adapter;
	private int currentPosition;
	private List<ImageItem> mDataList = new ArrayList<ImageItem>();

	private RelativeLayout photo_relativeLayout;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_zoom);

		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);

		initData();

		Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
		photo_bt_exit.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
		Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
		photo_bt_del.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				//假如有一张图片，点击删除之后退出该页面
				if (mDataList.size() == 1)
				{
					removeImgs();
					finish();
				}
				//加入多张，删除当前的图片
				else
				{
					removeImg(currentPosition);
					pager.removeAllViews();
					adapter.removeView(currentPosition);
					adapter.notifyDataSetChanged();
				}
			}
		});

		pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setOnPageChangeListener(pageChangeListener);

		adapter = new MyPageAdapter(mDataList);
		pager.setAdapter(adapter);
		pager.setCurrentItem(currentPosition);
	}

	private void initData()
	{
		currentPosition = getIntent().getIntExtra(
				IntentConstants.EXTRA_CURRENT_IMG_POSITION, 0);
		mDataList = PublishActivity.mDataList;
		Log.i("ly","8888888888888888"+mDataList.toString());
	}

	private void removeImgs()
	{
		mDataList.clear();
	}

	private void removeImg(int location)
	{
		if (location + 1 <= mDataList.size())
		{
			mDataList.remove(location);
		}
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener()
	{

		public void onPageSelected(int arg0)
		{
			currentPosition = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

		public void onPageScrollStateChanged(int arg0)
		{

		}
	};

	class MyPageAdapter extends PagerAdapter
	{
		private List<ImageItem> dataList = new ArrayList<ImageItem>();
		private ArrayList<ImageView> mViews = new ArrayList<ImageView>();

		public MyPageAdapter(List<ImageItem> dataList)
		{
			this.dataList = dataList;
			int size = dataList.size();
			for (int i = 0; i != size; i++)
			{
				ImageView iv = new ImageView(ImageZoomActivity.this);
				ImageDisplayer.getInstance(ImageZoomActivity.this).displayBmp(
						iv, null, dataList.get(i).sourcePath, false);
				iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
				mViews.add(iv);
			}
		}

		public int getItemPosition(Object object)
		{
			return POSITION_NONE;
		}

		public Object instantiateItem(View arg0, int arg1)
		{
			ImageView iv = mViews.get(arg1);
			((ViewPager) arg0).addView(iv);
			return iv;
		}

		public void destroyItem(View arg0, int arg1, Object arg2)
		{
			if (mViews.size() >= arg1 + 1)
			{
				((ViewPager) arg0).removeView(mViews.get(arg1));
			}
		}

		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

		@Override
		public int getCount()
		{
			return dataList.size();
		}

		public void removeView(int position)
		{
			if (position + 1 <= mViews.size())
			{
				mViews.remove(position);
			}
		}

	}
}