package fr.eurecom.mobserv.arianna;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.eurecom.mobserv.arianna.model.PointOfInterest;

//TODO Try with a cursor????
	public class PointOfInterestArrayAdapter extends ArrayAdapter<PointOfInterest> {
		private final List<PointOfInterest> list;
		private final LayoutInflater inflater;

		public PointOfInterestArrayAdapter(Activity context, List<PointOfInterest> list) {
			super(context, R.layout.point_of_interest_list_row_layout, list);
			this.list = list;
			this.inflater=LayoutInflater.from(context);
		}

		static class ViewHolder {
			ImageView pointOfInterestThumb;
			TextView pointOfInterestName;
		}

		/**
		 * Make a view to hold each row.
		 *
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is no need
			// to reinflate it. We only inflate a new View when the convertView supplied
			// by ListView is null.
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.point_of_interest_list_row_layout, null);

				// Creates a ViewHolder and store references to the two children views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.pointOfInterestName = (TextView) convertView.findViewById(R.id.point_of_interest_list_row_name);
				holder.pointOfInterestThumb = (ImageView) convertView.findViewById(R.id.point_of_interest_list_row_thumbnail);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			holder.pointOfInterestName.setText(list.get(position).getTitle());
			String name=list.get(position).getCategory().getIcon();
			int res=getContext().getResources().getIdentifier(name, "drawable", getContext().getPackageName());
			if (res != 0)
				holder.pointOfInterestThumb.setImageResource(res);
			return convertView;
		}
	}