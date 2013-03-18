package fr.eurecom.mobserv.arianna.drawerutil;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MenuEventHandler implements OnItemClickListener {

	private List<MenuItem> values;
	private Context context;
	
	
	public MenuEventHandler(List<MenuItem> values, Context context) {
		this.values = values; 
		this.context = context;
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (values.get(position).hasClickHandler()) {
			values.get(position).fireClick(context);
			
		} else {
			Toast.makeText(context,
					"Click ListItem Number " + position,
					Toast.LENGTH_SHORT).show();
		}
	}

}
