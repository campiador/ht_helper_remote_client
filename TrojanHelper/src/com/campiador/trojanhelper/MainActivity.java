package com.campiador.trojanhelper;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import com.campiador.trojanhelper.NetListAdapter.IAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class MainActivity extends Activity implements IAdapter{
	
	private String mServerIP;
	private int mServerPort;
	
	private Socket clientSocket;
	
	
	private ListView lvNetList;
	private List<NetModel> itemList;
	
	private NetListAdapter mAdapter;
	
	private Spinner spnrSort;
	
	private String[] spnrData = {
		"Sort by zero value duration",
		"Sort by one value duration",
		"Sort by total transition count"	
	};
	
	private int mSortmode = Constants.SORT_MODE_ZERO;
	
	boolean first  = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mServerIP = getIntent().getStringExtra(Constants.SELECTED_SERVER_IP);
		mServerPort = getIntent().getIntExtra(Constants.SELECTED_SERVER_PORT, Constants.DEFAULT_SERVER_PORT);
		
		
//	load Android listview from the above xml layout file into the NetModel Java object
		lvNetList = (ListView) findViewById(R.id.listView_nets);
		
		spnrSort = (Spinner) findViewById(R.id.spinner_mode);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spnrData);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnrSort.setAdapter(dataAdapter);
		spnrSort.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mSortmode = arg2;
				if (first) {
					first = false;
				} else {
					Toast.makeText(MainActivity.this, arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
					/*Collections.sort(itemList, mAdapter);
					mAdapter = new NetListAdapter(MainActivity.this, R.layout.net_list_row, itemList, MainActivity.this);
					mAdapter.notifyDataSetChanged();*/
					
					switch (arg2) {
					case Constants.SORT_MODE_ZERO:
						mAdapter.sortByZeroTimeAsc();
						break;
					case Constants.SORT_MODE_ONES:
						mAdapter.sortByOneTimeAsc();
						break;
					case Constants.SORT_MODE_TRANSITIONS:
						mAdapter.sortByTransitionCountAsc();
						break;

					default:
						break;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		spnrSort.setEnabled(false);
		
		new GameTask().execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class GameTask extends AsyncTask<Void, Void, Void>  {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			Log.i("TH", "in worker thread");
			
			try {
				
//				we need a 1D list of items
				itemList = new ArrayList<NetModel>();
				
//				This is GameClient main function
				Log.d("TH", "ip: " + mServerIP);
				Log.d("TH", "port: " + mServerPort);
				
				clientSocket = new Socket(mServerIP, mServerPort);
				Log.i("TH", "socket created");
				Scanner input = new Scanner(clientSocket.getInputStream());
				int linecount= 0;
				
				NetModel tempNet = null;
				int shouldbreak = 0;
				while (input.hasNextLine()) {
					linecount++;
					String line;
					
					line = input.nextLine();
					line = line.trim();
					Log.i("TH", line);
					
					if (linecount >= 15) {
						switch ((linecount - 15) % 4) {
						case 0:
							if ( line.equals(")") ) {
								shouldbreak = 2;
								break;
							}
							tempNet = new NetModel(line.replace("(", ""));
							break;
						case 1:
							
					        String[] ara = line.split("\\s+");
					        
					        String zero;
					        String one;
					        
					        for (int i = 0; i < ara.length; i++) {
					        	ara[i] = ara[i].replace("(", "");
					        	ara[i] = ara[i].replace(")", "");
							}
					        
					        zero = ara[1];
					        one = ara[3];
					        
					        tempNet.setZeroTime(zero);
							tempNet.setOneTime(one);
							
							break;
							
						case 2:
							String[] arb = line.split("\\s+");
							String transit;
							for (int i = 0; i < arb.length; i++) {
								arb[i] = arb[i].replace("(", "");
								arb[i] = arb[i].replace(")", "");
							}
							
							transit = arb[1];
							tempNet.setTransitionCount(transit);
							
							break;
						case 3:
							if (!tempNet.getTransitionCount().equals("0")) {
								itemList.add(tempNet);
							}
							break;

						default:
							break;
						}
						
					}
					
					if (shouldbreak == 2) {
						break;
					}
					
				}
				
//				for (NetModel model : itemList) {
//					Log.d("TH", "model: " + model.getName() + " - " + 
//							model.getZeroTime() + " - " + model.getOneTime() + " - " + model.getTransitionCount());
//				}
				input.close();
				
				mAdapter = new NetListAdapter(MainActivity.this, R.layout.net_list_row, itemList, MainActivity.this);
				MainActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						lvNetList.setAdapter(mAdapter);
						
					}
				});
				
				
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			spnrSort.setEnabled(true);
			super.onPostExecute(result);
		}

	}

	
	public void displayInformation(MenuItem item) {
		
		
		MainActivity.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
				.setTitle("Trojan Insert Helper")
				.setMessage("This app receives Switching Activity Interchange Format files" +
						" from a server, over a wireless conncetion." +
						" Then Trojan Insert Helper parses received data " +
						"and draws a list of nets that are prone to Trojan Insertion " +
						"in descending order on possibilty of trojan detection.\n\n" +
						"Developed by Behnam Heydarshahi, Summer 2014\n"
						)
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								
							}
						})
						.create();
				dialog.show();
				
			}
		});
		
	}
	
	@Override
	public int getSortMode() {
		return mSortmode;
	}
	
//	public static class MyComparator implements Comparator<NE>
	
}
