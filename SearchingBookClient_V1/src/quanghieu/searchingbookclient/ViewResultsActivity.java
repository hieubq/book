package quanghieu.searchingbookclient;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ViewResultsActivity extends Activity {

	private TCPClient mTcpClient;
	private Button btnBack;
	private Button btnStop;
	private ListView resultListView;
	private ListAdapter resultListAdapter;
	private String MessageSearch;
	private TextView txtStatus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_results);
		
		Intent i = getIntent();
		MessageSearch = i.getStringExtra("MessageSearch");
		resultListAdapter = new ListAdapter(this);
		resultListView = (ListView)findViewById(R.id.listResult);
		resultListView.setAdapter(resultListAdapter);
		btnBack = (Button)findViewById(R.id.btnBackToFirstActivity);
		btnStop = (Button)findViewById(R.id.btnStop);
		txtStatus = (TextView)findViewById(R.id.txtStatus);
		txtStatus.setText("Đang tìm ...");
		// connect to the server
        new connectTask().execute("");
        // tao su kiem bam nut back
        btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				JSONObject JObj = new JSONObject();
				try {
					JObj.put("header", FRAME_HEADER.DISCONNECTED_CMD);
					JObj.put("datas", "");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mTcpClient.sendMessage(JObj.toString());
			}
		});
        
        // tao su kien bam nut stop
        btnStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JSONObject JObj = new JSONObject();
				try {
					JObj.put("header", FRAME_HEADER.STOP_SEARCH_CMD);
					JObj.put("datas", "");
					mTcpClient.sendMessage(JObj.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        
        // tao su kiem bam vao 1 row cua list
        resultListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String SelectedBookID = resultListAdapter.getBookId(position);
				JSONObject JObj = new JSONObject();
				try {
					JObj.put("header", FRAME_HEADER.GET_DETAIL_CMD);
					JObj.put("datas", SelectedBookID);
					mTcpClient.sendMessage(JObj.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});     
	}

	
	public void setMessageSearch(String mes){
		this.MessageSearch = mes;
	}
	
	public class connectTask extends AsyncTask<String,String,TCPClient> {
   	 
        @Override
        protected TCPClient doInBackground(String... message) {
 
            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();
            
            return null;
        }
 
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            // in the arrayList we add the messaged received from server
            // arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            // ListAdapter.notifyDataSetChanged();
            //resultListAdapter.notifyDataSetChanged();
            // xu ly message nhan duoc o day, phan tich goi tin va nap du lieu
            try {
				JSONObject JObj = new JSONObject(values[0]);
				String frameHeader = JObj.getString("header");
				if (frameHeader.equalsIgnoreCase(FRAME_HEADER.SEND_RESULT_CMD)) {
					// phan tich datas thanh ket qua tim kiem duoc va hien thi len list
					resultListAdapter.add(JObj.getString("datas"));
					resultListAdapter.notifyDataSetChanged();
				}else if (frameHeader.equalsIgnoreCase(FRAME_HEADER.SEND_DETAIL_CMD)) {
					// phan tich datas thanh thong tin chi tiet 1 quyen sach va hien thi len activity detail
					Intent ViewResultScreen = new Intent(getApplicationContext(), ViewBookDetail.class);
					ViewResultScreen.putExtra("BookDetailString", JObj.getString("datas"));
					startActivity(ViewResultScreen);
				}else if (frameHeader.equalsIgnoreCase(FRAME_HEADER.DONE_SEARCH_CMD)) {
					// cap nhat trang thai activity
					txtStatus.setText("Hoàn Thành");
				}else if (frameHeader.equalsIgnoreCase(FRAME_HEADER.CL_CONNECTED_CMD)) {
					mTcpClient.sendMessage(MessageSearch);
				}else if (frameHeader.equalsIgnoreCase(FRAME_HEADER.ALLOW_DISCONNECTED_CMD)) {
					finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_results, menu);
		return true;
	}

}
