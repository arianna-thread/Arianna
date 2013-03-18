package fr.eurecom.mobserv.arianna;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.eurecom.mobserv.arianna.model.Path;

// TODO Try with a cursor????
	public class PathArrayAdapter extends ArrayAdapter<Path> {
		private final List<Path> list;
		private final LayoutInflater inflater;

		public PathArrayAdapter(Activity context, List<Path> list) {
			super(context, R.layout.path_list_row_layout, list);
			this.list = list;
			this.inflater=LayoutInflater.from(context);
		}

		static class ViewHolder {
			TextView pathName;
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

				convertView = inflater.inflate(R.layout.path_list_row_layout, null);

				// Creates a ViewHolder and store references to the two children views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.pathName = (TextView) convertView.findViewById(R.id.path_name);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			holder.pathName.setText(list.get(position).getTitle());

			return convertView;
		}
	}