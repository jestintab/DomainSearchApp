package com.fornys.hostingkart;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;

public class SearchFragment extends Fragment {

	View view;

	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();
	// url to make request
	private static String url = "http://api.fornystechnologies.com/domainsearch/index.php";

	// JSON Node names
	private static final String TAG_STATUS = "status";
	// private static final String TAG_CLASSKEY = "classkey";

	// domain JSONArray
	// JSONArray domain = null;
	String domain = null;
	String tld = null;
	String get = "GET";
	String dom;
	String status;

	// Intent value
	public final static String INTENT_DOMAIN = "com.fornys.hostingkart.MESSAGE";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_search, container, false);

		cd = new ConnectionDetector(getActivity());
		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();
		// check for Internet status
		if (!isInternetPresent) {
			AppMsg.Style style = AppMsg.STYLE_ALERT;
			AppMsg appmsg = AppMsg.makeText(getActivity(), R.string.internet_msg, style);
			appmsg.show();
		}

		// Domain text
		final EditText tv_domain = (EditText) view
				.findViewById(R.id.et_domainName);

		// Spinner
		final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.tld,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		// Search button
		Button btn_search = (Button) view.findViewById(R.id.search);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Getting values domain and tld
				domain = tv_domain.getText().toString();
				tld = spinner.getSelectedItem().toString();

				// calling background thread
				new LoadSingleTrack().execute();
			}
		});

		return view;
	}

	/**
	 * Background Async Task to get domain information
	 * */
	class LoadSingleTrack extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading result ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting domain json and parsing
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// post domain, tld as GET parameters
			params.add(new BasicNameValuePair("domain", domain));
			params.add(new BasicNameValuePair("tld", tld));

			// getting JSON string from URL
			String json = jsonParser.makeHttpRequest(url, get, params);
			// System.out.println("JSON = " + json);

			dom = domain + "." + tld;
			// System.out.println(dom);
			try {
				JSONObject jObj = (new JSONObject(json)).getJSONObject(dom);
				// JSONObject jObj = json.getJSONObject(dom);
				status = jObj.getString(TAG_STATUS);
				// System.out.println(status);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting domain information
			pDialog.dismiss();

			// updating UI from Background Thread
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					TextView tv_status = (TextView) view
							.findViewById(R.id.tv_status);
					Button btn_whois = (Button) view
							.findViewById(R.id.btn_whois);
					if (status.equals("regthroughothers")
							|| status.equals("regthroughus")) {
						tv_status.setText("Unavailable");
						tv_status.setTextColor(Color.RED);
						btn_whois.setVisibility(View.VISIBLE);
						btn_whois.setText(R.string.whois);
						btn_whois.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(getActivity(),
										WhoisActivity.class);
								intent.putExtra(INTENT_DOMAIN, dom);
								startActivity(intent);
							}
						});
					} else if (status.equals("available")) {
						tv_status.setText("Available");
						tv_status.setTextColor(Color.GREEN);
						btn_whois.setVisibility(View.VISIBLE);
						btn_whois.setText(R.string.add_wishlist);
						btn_whois.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								DatabaseHandler db = new DatabaseHandler(
										getActivity());

								// Inserting Contacts
								Log.d("Insert: ", "Inserting ..");
								db.addDomain(new Domain(dom));
							}
						});

					}
				}
			});

		}

	}

}
