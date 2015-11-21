package com.fornys.hostingkart;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

public class WishlistFragment extends Fragment {

	ArrayList<String> domain_names = new ArrayList<String>();

	// ListView lv;
	ActionSlideExpandableListView list;

	ArrayAdapter<String> files;

	DatabaseHandler db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wishlist, container,
				false);

		db = new DatabaseHandler(getActivity());

		// Getting all domain names
		List<Domain> domains = db.getAllContacts();

		for (Domain dm : domains) {
			// String log = "Id: "+dm.getID()+" ,Name: " + dm.getDomain();

			domain_names.add(dm.getDomain());
		}

		// lv = (ListView) view.findViewById(R.id.lv_domain);
		list = (ActionSlideExpandableListView) view
				.findViewById(R.id.lv_domain);

		// Expandable list view
		files = new ArrayAdapter<String>(getActivity(),
				R.layout.expandable_list_item, R.id.text, domain_names);
		// lv.setAdapter(new SlideExpandableListAdapter(files,
		// R.id.expandable_toggle_button, R.id.expandable));

		list.setAdapter(files);

		// Normal ListView
		// files = new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1, domain_names);
		// lv.setAdapter(files);

		list.setItemActionListener(
				new ActionSlideExpandableListView.OnActionClickListener() {

					@Override
					public void onClick(View listView, View buttonview,
							int position) {
						Toast.makeText(getActivity(),
								"list item = " + position, Toast.LENGTH_SHORT)
								.show();
						String domainFromList = (list
								.getItemAtPosition(position).toString());

						db.deleteContact(new Domain(domainFromList));

						updateList();
					}
				}, R.id.buttonDelete);

		return view;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void updateList() {

		List<Domain> domains = db.getAllContacts();

		for (Domain dm : domains) {
			String log = "Id: " + dm.getID() + " ,Name: " + dm.getDomain();
			System.out.println(log);
			domain_names.add(dm.getDomain());
		}

		files.clear();

		if (domain_names != null) {
			// If the platform supports it, use addAll, otherwise add in loop
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				files.addAll(domain_names);
			} else {
				for (String item : domain_names) {
					files.add(item);
				}
			}
		}

		files.notifyDataSetChanged();

	}

	@Override
	public void onPause() {
		super.onPause();
		files.clear();
	}

}
