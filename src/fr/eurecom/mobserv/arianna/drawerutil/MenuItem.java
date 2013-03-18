package fr.eurecom.mobserv.arianna.drawerutil;

import fr.eurecom.mobserv.arianna.R;
import fr.eurecom.mobserv.arianna.R.drawable;
import fr.eurecom.mobserv.arianna.R.id;
import fr.eurecom.mobserv.arianna.R.layout;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuItem {

	private String title;
	private int iconResource;
	private boolean hasHandler = false;
	private ListItemEventListener listener;

	public MenuItem(String title, int iconResource, ListItemEventListener listener) {
		this.title = title;
		this.iconResource = iconResource;
		this.listener = listener;
		if ( listener != null){
			hasHandler = true;
		}
	}

	public MenuItem(String title, int iconResource) {
		this(title,iconResource,null);
	}
	public MenuItem(String title) {
		this(title, R.drawable.ic_action_generic);
	}
	

	public MenuItem addEventListener(ListItemEventListener listener){
		this.listener = listener;
		if ( listener != null){
			hasHandler = true;
		} else {
			hasHandler = false;
		}
		return this;
	}
	
	public boolean hasClickHandler(){
		return hasHandler;
	}
	
	public View getView(Context context, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(this.getLayout(), parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.lable);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		textView.setText(title);
		// Change the icon for Windows and iPhone
		imageView.setImageResource(iconResource);

		return rowView;
	}
	
	public int getLayout(){
		return R.layout.menu_item;
	}

	public void fireClick(Context context) {
		listener.onItemClick(context);
	}
}
