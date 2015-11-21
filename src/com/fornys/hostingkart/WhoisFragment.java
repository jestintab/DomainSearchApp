package com.fornys.hostingkart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class WhoisFragment extends Fragment {

	View view;

	// Intent value
	public final static String INTENT_DOMAIN = "com.fornys.hostingkart.MESSAGE";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_whois, container, false);

		final EditText editText = (EditText) view.findViewById(R.id.domain);

		Button btn_whois = (Button) view.findViewById(R.id.whoisSearch);
		btn_whois.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String dom = editText.getText().toString();

				Intent intent = new Intent(getActivity(), WhoisActivity.class);
				intent.putExtra(INTENT_DOMAIN, dom);
				startActivity(intent);

			}
		});

		return view;
	}

}
