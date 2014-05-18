package edu.tj.sse.runeveryday.ui;

import java.util.ArrayList;
import java.util.List;

import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.database.business.PlanBase;
import edu.tj.sse.runeveryday.database.entity.Training;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class PlanActivity extends Activity implements OnItemClickListener{

	private ListView mListView;
	private ListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_plan);

		mListView = (ListView)findViewById(R.id.planlist);
		PlanBase pb=new PlanBase(this);
        mAdapter = new  ListAdapter(this,pb.getCurrentPlan());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(PlanActivity.this);

	}
	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAdapter.changeImageVisable(view, position); 
    }
	
	private class ListAdapter extends BaseAdapter {
		private Context mContext;
		private View mLastView;
		private int mLastPosition;
		private int mLastVisibility;
		private List<Training> plan;
		
		public ListAdapter(Context context,List<Training> plan) {
			this.mContext = context;
			this.plan=plan;
			if(plan==null){
				this.plan=new ArrayList<Training>();
			}
			mLastPosition = -1;
		}

		@Override
		public int getCount() {
			return plan.size();
		}

		@Override
		public Object getItem(int position) {
			return plan.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if(convertView == null ) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				convertView = inflater.inflate(R.layout.plan_listitem, null);
				holder =new Holder();
				holder.textView = (TextView)convertView.findViewById(R.id.planlist_item);
				holder.itemContent = (TextView)convertView.findViewById(R.id.plan_hint_content);
				holder.isdone=(CheckBox)convertView.findViewById(R.id.plan_isdone);
				holder.hint = convertView.findViewById(R.id.plan_hint_part);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			if(mLastPosition == position){
				holder.hint.setVisibility(mLastVisibility);
			}else{
				holder.hint.setVisibility(View.GONE);
			}
			Training training=plan.get(position);
			holder.textView.setText(training.getWeek()+" "+training.getDay());
			//Log.d("PlanActivity",training.getWork());
			holder.itemContent.setText(training.getWork());
			Log.d("PlanActivity","position:"+position+" isdone:"+training.isIsdone());
			if(training.isIsdone()){
				holder.isdone.setChecked(true);
			}
			else{
				holder.isdone.setChecked(false);
			}
			return convertView;
		}
		
		class Holder {
			TextView textView;
			TextView itemContent;
			CheckBox isdone;
			View hint;
		}
		
		public void changeImageVisable(View view,int position) {
			if(mLastView != null && mLastPosition != position ) {
				Holder holder = (Holder) mLastView.getTag();
				switch(holder.hint.getVisibility()) {
				case View.VISIBLE:
					holder.hint.setVisibility(View.GONE);
					mLastVisibility = View.GONE;
					break;
				default :
					break;
				}
			}
			mLastPosition = position;
	        mLastView = view;
	        Holder holder = (Holder) view.getTag();
			switch(holder.hint.getVisibility()) {
			case View.GONE:
				holder.hint.setVisibility(View.VISIBLE);
				mLastVisibility = View.VISIBLE;
				break;
			case View.VISIBLE:
				holder.hint.setVisibility(View.GONE);
				mLastVisibility = View.GONE;
				break;
			}
		}
	}
}
