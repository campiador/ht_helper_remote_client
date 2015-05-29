package com.campiador.trojanhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NetListAdapter extends BaseAdapter {
	
	
	private Context mContext;
	private List<NetModel> mItems;
	private int mRowLayoutResourceId;
	private LayoutInflater mInflater;
	private IAdapter mControl;
	
	public interface IAdapter{
		
		int getSortMode();
		
	}
	
	public NetListAdapter(Context ctx, int resId, List<NetModel> items, IAdapter control) {
		mContext = ctx;
		mRowLayoutResourceId = resId;
		mItems = new ArrayList<NetModel>(items);
		mInflater = LayoutInflater.from(mContext);
		
		mControl = control;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if (view == null) {
			view = mInflater.inflate(mRowLayoutResourceId, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.textView_name);
			holder.tvZero = (TextView) view.findViewById(R.id.textView_zero_time);
			holder.tvOne = (TextView) view.findViewById(R.id.textView_one_time);
			holder.tvTransit = (TextView) view.findViewById(R.id.textView_transition_count);
			view.setTag(holder);
		} 
			
		ViewHolder holder = (ViewHolder) view.getTag();
		NetModel model = mItems.get(position);
		
		holder.tvName.setText(model.getName());
		holder.tvZero.setText(model.getZeroTime());
		if (mControl.getSortMode() == Constants.SORT_MODE_ZERO) {
			holder.tvZero.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_bright));
		} else {
			holder.tvZero.setTextColor(mContext.getResources().getColor(android.R.color.secondary_text_dark));
		}
		holder.tvOne.setText(model.getOneTime());
		if (mControl.getSortMode() == Constants.SORT_MODE_ONES) {
			holder.tvOne.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_bright));
		} else {
			holder.tvOne.setTextColor(mContext.getResources().getColor(android.R.color.secondary_text_dark));
		}
		holder.tvTransit.setText(model.getTransitionCount());
		if (mControl.getSortMode() == Constants.SORT_MODE_TRANSITIONS) {
			holder.tvTransit.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_bright));
		} else {
			holder.tvTransit.setTextColor(mContext.getResources().getColor(android.R.color.secondary_text_dark));
		}
			
		
		return view;
	}
	
	  static class ViewHolder {
		    public TextView tvName;
		    public TextView tvOne;
		    public TextView tvZero;
		    public TextView tvTransit;
		  }

	
    public void sortByTransitionCountAsc() {
     Comparator<NetModel> comparator = new Comparator<NetModel>() {

      @Override
      public int compare(NetModel object1, NetModel object2) {
  		int lfabric = 0;
  		int rfabric = 0; 
		lfabric = Integer.valueOf(object1.getTransitionCount());
		rfabric = Integer.valueOf(object2.getTransitionCount());
  		if (lfabric > rfabric) {
			return 1;
		} else if (lfabric < rfabric) {
			return -1;
		} else {
			return 0;
		}      }
     };
     Collections.sort(mItems, comparator);
        notifyDataSetChanged();
    }
    
    public void sortByZeroTimeAsc() {
    	Comparator<NetModel> comparator = new Comparator<NetModel>() {
    		
    		@Override
    		public int compare(NetModel object1, NetModel object2) {
    			int lfabric = 0;
    			int rfabric = 0; 
    			lfabric = Integer.valueOf(object1.getZeroTime());
    			rfabric = Integer.valueOf(object2.getZeroTime());
    			if (lfabric > rfabric) {
    				return 1;
    			} else if (lfabric < rfabric) {
    				return -1;
    			} else {
    				return 0;
    			}      }
    	};
    	Collections.sort(mItems, comparator);
    	notifyDataSetChanged();
    }
    
    public void sortByOneTimeAsc() {
    	Comparator<NetModel> comparator = new Comparator<NetModel>() {
    		
    		@Override
    		public int compare(NetModel object1, NetModel object2) {
    			int lfabric = 0;
    			int rfabric = 0; 
    			lfabric = Integer.valueOf(object1.getOneTime());
    			rfabric = Integer.valueOf(object2.getOneTime());
    			if (lfabric > rfabric) {
    				return 1;
    			} else if (lfabric < rfabric) {
    				return -1;
    			} else {
    				return 0;
    			}      }
    	};
    	Collections.sort(mItems, comparator);
    	notifyDataSetChanged();
    }

}
