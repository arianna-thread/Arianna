package fr.eurecom.mobserv.arianna.drawerutil;

import fr.eurecom.mobserv.arianna.R;
import fr.eurecom.mobserv.arianna.R.layout;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


public class MenuSeparator extends MenuItem {

	public MenuSeparator(String title) {
		super(title);
	}

	@Override
	public int getLayout() {
		return R.layout.menu_separator;
	}
	
	@Override
	public View getView(Context context, ViewGroup parent) {
		View v = super.getView(context, parent);
		v.setEnabled(false);
		return v;
	}

}
