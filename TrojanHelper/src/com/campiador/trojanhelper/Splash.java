package com.campiador.trojanhelper;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Splash extends Activity {
	
	private EditText etServerIP;
	private EditText etServerPort;
	private Button bConnect;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Log.i("TH", "in splash");
		
		etServerIP = (EditText) findViewById(R.id.server_ip_edit);
		etServerPort = (EditText) findViewById(R.id.server_port_edit);
		
		bConnect = (Button) findViewById(R.id.button_connect);
		
		bConnect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String ip = etServerIP.getText().toString();
				String portString = etServerPort.getText().toString();
				
				int port;
				
				if (portString == null || portString.equals("")) {
					port = Constants.DEFAULT_SERVER_PORT;
				} else {
					port = Integer.parseInt(portString);
				}
				
				if (ip == null || ip.equals("")) {
					ip = Constants.DEFAULT_SERVER_IP;
				} 
				
				{
					Intent intent = new Intent(Splash.this, MainActivity.class);
					intent.putExtra(Constants.SELECTED_SERVER_IP, ip);
					intent.putExtra(Constants.SELECTED_SERVER_PORT, port);
					startActivity(intent);
					finish();
						
				}
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
	public void displayInformation(MenuItem item) {
		
		
		Splash.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AlertDialog dialog = new AlertDialog.Builder(Splash.this)
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
}
