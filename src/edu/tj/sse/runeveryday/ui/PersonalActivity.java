package edu.tj.sse.runeveryday.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import edu.tj.sse.runeveryday.R;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalActivity extends Activity {

	private int Screen_width;
	private int Screen_length;

	private List<Map<String, String>> mData;
	private SimpleAdapter listAdapter;
	private ListView mListView;
	private Map<String, String> map1 = new HashMap<String, String>();
	private Map<String, String> map2 = new HashMap<String, String>();
	private Map<String, String> map3 = new HashMap<String, String>();
	private Map<String, String> map4 = new HashMap<String, String>();
	private Map<String, String> map5 = new HashMap<String, String>();

	private float personal_height = 0;
	private float personal_weight = 0;
	private int personal_age = 0;
	private boolean personal_is_boy = true;
	private boolean personal_is_inChina = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personal);

		init_activity();

		init_listview();

	}

	private void Select_dialog(String Title, final int pos) {
		final String[] sex = new String[2];
		final String[] area = new String[2];
		switch (pos) {
		case 3:
			if (personal_is_boy) {
				sex[0] = "男  ";
				sex[1] = "女  ";
			} else {
				sex[0] = "女  ";
				sex[1] = "男  ";
			}
			new AlertDialog.Builder(this)
					.setTitle(Title)
					.setSingleChoiceItems(new String[] { sex[0], sex[1] }, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									map4.put("listname2", sex[which]);
									if (sex[which].equals("男  "))
										personal_is_boy = true;
									else
										personal_is_boy = false;
									listAdapter.notifyDataSetChanged();
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).show();
			break;
		case 4:
			if (personal_is_inChina) {
				area[0] = "中国大陆  ";
				area[1] = "其他地区  ";
			} else {
				area[0] = "其他地区  ";
				area[1] = "中国大陆  ";
			}
			new AlertDialog.Builder(this)
			.setTitle(Title)
			.setSingleChoiceItems(new String[] { area[0], area[1] }, 0,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							map5.put("listname2", area[which]);
							if (area[which].equals("中国大陆  "))
								personal_is_inChina = true;
							else
								personal_is_inChina = false;
							listAdapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					}).setNegativeButton("取消", null).show();
			break;
		}
	}

	private void Edit_dialog(String Title, final int pos) {
		final EditText view = new EditText(this);
		switch (pos) {
		case 0:
			view.setText(personal_height + "");
			break;
		case 1:
			view.setText(personal_weight + "");
			break;
		case 2:
			view.setText(personal_age + "");
			break;
		}
		new AlertDialog.Builder(this).setTitle(Title).setView(view)
				.setNegativeButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						try {
							switch (pos) {
							case 0:
								personal_height = Float.parseFloat(view
										.getText().toString());
								map1.put("listname2", personal_height + "cm  ");
								break;
							case 1:
								personal_weight = Float.parseFloat(view
										.getText().toString());
								map2.put("listname2", personal_weight + "kg  ");
								break;
							case 2:
								personal_age = Integer.parseInt(view.getText()
										.toString());
								map3.put("listname2", personal_age + "岁  ");
								break;
							}

							listAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO: handle exception
							Toast.makeText(PersonalActivity.this, "您的输入不规范",
									Toast.LENGTH_SHORT).show();
						}
					}
				})

				.setPositiveButton("取消", null).show();

	}

	private void init_activity() {
		GetScreen();
		TextView Layout = (TextView) findViewById(R.id.Personal_Title_name);
		Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
				Screen_length / 15));
		LinearLayout layout2 = (LinearLayout) findViewById(R.id.Personal_image);
		layout2.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
				Screen_length / 5));
	}

	private void init_listview() {

		mListView = (ListView) findViewById(R.id.personal_List);
		mData = new ArrayList<Map<String, String>>();
		map1.put("listname1", "  身高");
		map1.put("listname2", personal_height + "cm  ");
		map2.put("listname1", "  体重");
		map2.put("listname2", personal_weight + "kg  ");
		map3.put("listname1", "  年龄");
		map3.put("listname2", personal_age + "岁  ");
		map4.put("listname1", "  性别");
		if (personal_is_boy)
			map4.put("listname2", "男  ");
		else
			map4.put("listname2", "女  ");
		map5.put("listname1", "  所在地区");
		if (personal_is_inChina)
			map5.put("listname2", "中国大陆  ");
		else
			map5.put("listname2", "其他地区  ");
		mData.add(map1);
		mData.add(map2);
		mData.add(map3);
		mData.add(map4);
		mData.add(map5);
		listAdapter = new SimpleAdapter(this, mData,
				R.layout.personal_list_context, new String[] { "listname1",
						"listname2" }, new int[] { R.id.listname1,
						R.id.listname2 });
		mListView.setAdapter(listAdapter);
		setItemListener();
	}

	private void setItemListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			// 处理点击事件
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				switch (position) {
				case 0:
					Edit_dialog("请输入您的身高（cm）", position);
					break;

				case 1:
					Edit_dialog("请输入您的体重（kg）", position);
					break;
				case 2:
					Edit_dialog("请输入您的年龄（岁）", position);
					break;

				case 3:
					Select_dialog("请选择您的性别", position);
					break;
				case 4:
					Select_dialog("请选择您所在的地区", position);
					break;
				}
			}

		});
	}

	public void GetScreen() {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Screen_width = dm.widthPixels;
		Screen_length = dm.heightPixels;
	}
}
