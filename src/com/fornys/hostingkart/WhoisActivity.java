package com.fornys.hostingkart;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;

public class WhoisActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;
	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();
	// url to make request
	private static String url = "http://api.fornystechnologies.com/whois/whois.php";
	// JSON Node names
	private static final String TAG_RESULT = "result";
	private static final String TAG_WHOIS = "whois";
	String domain, result, whois;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_whois);

		Intent intent = getIntent();
		domain = intent.getStringExtra(WhoisFragment.INTENT_DOMAIN);

		System.out.println("Domain: " + domain);

		// calling background thread
		new LoadWhois().execute();
	}

	class LoadWhois extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(WhoisActivity.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// post domain, tld as GET parameters
			params.add(new BasicNameValuePair("domain", domain));
			// params.add(new BasicNameValuePair("tld", tld));

			// getting JSON string from URL
			String json = jsonParser.makeHttpRequest(url, "GET", params);
			// System.out.println("JSON = " + json);

			try {
				JSONObject jObj = new JSONObject(json);
				result = jObj.getString(TAG_RESULT);
				whois = jObj.getString(TAG_WHOIS);
				System.out.println(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			// dismiss the dialog after getting domain information
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					if (result.equals("success")) {
						WebView wv = (WebView) findViewById(R.id.wv_whoisDetail);
						wv.loadData(whois, "text/html", "UTF-8");
					}
				}
			});
		}

	}

}
