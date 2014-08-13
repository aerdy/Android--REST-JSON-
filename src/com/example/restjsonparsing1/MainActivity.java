package com.example.restjsonparsing1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button ambildata = (Button)findViewById(R.id.buttonGetServerData);
		ambildata.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String serverURL = "http://androidexample.com/media/webservice/JsonReturn.php";
				
				new operasi().execute(serverURL);
			}
		});
		
		
	}
	
	public class operasi extends AsyncTask<String, Void, Void>{

		private final HttpClient client = new DefaultHttpClient();
		private String Contect;
		private String Error = null;
		private ProgressDialog progress = new ProgressDialog(MainActivity.this);
		String data = "";
		TextView update = (TextView)findViewById(R.id.textViewOutPut);
		TextView jsonparse = (TextView)findViewById(R.id.textViewJsonParsen);
		int sizeData = 0;
		EditText servertext = (EditText)findViewById(R.id.editTextServerText);

		
		
		protected void onPreExecute(){
			progress.setMessage("Tunggu...");
			progress.show();
			
			try {
				data +="&"+URLEncoder.encode("data","UTF-8")+servertext.getText();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			BufferedReader reader = null;
			
			try {
				URL url = new URL (params[0]);
				
				//mengirim data
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.flush();
				
				//mengambil data
				
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line =null;
				
				while ((line = reader.readLine()) != null) {
					sb.append(line+"");
				}
				
				Contect = sb.toString();
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
				Error = e.getMessage();
			}finally{
				try {
					reader.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			return null;
		}
		
		protected void onPostExecute(Void unused){
			progress.dismiss();
			if(Error !=null){
				update.setText("Output"+Error);
			}else{
				//menampilakan hasil dari respon JSON
				update.setText(Contect);
				String outputdata = "";
				JSONObject jsonrespon;
				
				try {
					jsonrespon = new JSONObject(Contect);
					JSONArray jsonmainnode = jsonrespon.optJSONArray("Android");
					
					int lengthJsonArr = jsonmainnode.length();
					
					for(int i = 0 ; i<lengthJsonArr;i++){
						JSONObject jsonchildnode = jsonmainnode.getJSONObject(i);
						
						String name = jsonchildnode.optString("name").toString();
						String number = jsonchildnode.optString("number").toString();
						String date = jsonchildnode.optString("date_added").toString();
						
						outputdata +="Nama :"+name+"Nomor :"+number+" Waktu :"+date;
					}
					
					jsonparse.setText(outputdata);
					
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	
}
