package com.tab.wwa;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.NetworkUtility;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class WWAActivity extends Activity {
	final String url = "http://theappbusiness.net/people/";
	private ListView listView;
	private EmployeeAdapter adapter;
	private Document doc;
	private ArrayList<Employee> employeeList;
	private ProgressDialog progressDialog;
    private NetworkUtility nu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nu=new NetworkUtility(this);
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() // Universal
																				// Image
																				// Loader
																				// init
				.cacheInMemory(true).cacheOnDisc(true).build(); // turn caching
																// on
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(
				defaultOptions).build();
		ImageLoader.getInstance().init(config); // Do it on Application start
		if(!nu.isMobileDataEnabled()&!nu.isWifiConnected()){   //no network connectivity
			showCustomDialog(R.string.no_network_dialog_title, //show dialog
					R.string.no_network_dialog_msg);    
		}
		else{      //else get content from url
		new GetContentFromURLTask().execute(url);}
		setContentView(R.layout.main_activity);
		listView = (ListView) findViewById(R.id.listView1);
		listView.setTextFilterEnabled(true);

	}

		private void showCustomDialog(int title, int message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.dismiss();
							}
						});

		// create alert dialog
		AlertDialog alertDialog2 = alertDialogBuilder.create();

		// show it
		alertDialog2.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wwa, menu);
		return true;
	}

	public class GetContentFromURLTask extends
			AsyncTask<String, Void, ArrayList<Employee>> {
		// private ProgressDialog progressDialog;
		int gro;

		@Override
		protected void onPreExecute() {
			gro = WWAActivity.this.getResources().getConfiguration().orientation;
			// Create a new progress dialog
			progressDialog = new ProgressDialog(WWAActivity.this);
			progressDialog.setMessage("Fetching data.Please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		// The code to be executed in a background thread.
		@Override
		protected ArrayList<Employee> doInBackground(String... params) {
			employeeList = new ArrayList<Employee>();
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			try {
				doc = Jsoup.connect(params[0]).get();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Elements e = doc.getElementById("content").children();// parse html
			for (Element el : e) {

				Elements employees = el.children();
				for (Element emp : employees) {
					String src = url + (emp.select("img").first().attr("src"));// get
																				// image
																				// source
					Element name_element = emp.select("h3").first();
					String name = name_element.text();// get name
					String dep = emp.getElementsByTag("h3").text()
							.replace(name, "").trim();// crude but effective way
														// of getting department
					String bio = emp.select("p").first().text();// biography
					employeeList.add(new Employee(name, dep, bio, src));
				}
			}
			return employeeList;
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(ArrayList<Employee> employeeList) {

			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			adapter = new EmployeeAdapter(WWAActivity.this, R.id.listView1,
					employeeList);
			listView.setAdapter(adapter);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (progressDialog != null) {
			progressDialog = null;// make progressbar null to prevent activity from leaking on orientation change
		}

	};
}