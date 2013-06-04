package quanghieu.searchingbookclient;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class CaptureAndLoadPicActivity extends Activity implements OnClickListener{

	final String[] CatalogIDDatas   = new String[] {	"noid",
														"all", 
														"giadinhnuoidaycon", 
														"quantrikinhdoanh", 
														"tamlyvagioitinh", 
														"thieunhi", 
														"truyenvatieuthuyet"	};
	final String[] CatalogNameDatas = new String[] {	"Hãy Chọn Chuyên Mục", 
														"Tất Cả", 
														"Sách Gia Đình & Nuôi Dạy Con", 
														"Quản Trị Kinh Doanh", 
														"Sách Tâm Lý & Giới TÍnh", 
														"Sách Thiếu Nhi", 
														"Truyện & Tiểu Thuyết"	};
	
	private List<String> CatalogIDs;
	private List<String> CatalogNames;
	
	final int CAMERA_CAPTURE = 1;
	final int PIC_CROP = 2;
	final int OPEN_PIC = 3;
	private Uri picUri;
	
	ImageView picbox;
	Spinner CbxCatalog;
	Button btnCapture, btnOpenPic, btnSearch;
	Bitmap capturedImage;
	String selectedCatalogID;
	AlertDialog alertDialog;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_and_load_pic);
		
		// handle Buttons, imageview
		btnCapture = (Button)findViewById(R.id.btnCapture);
		btnCapture.setOnClickListener(this);
		btnOpenPic = (Button)findViewById(R.id.btnOpenPic);
		btnOpenPic.setOnClickListener(this);
		btnSearch = (Button)findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);
		
		picbox = (ImageView)findViewById(R.id.picbox);
		
		alertDialog = new AlertDialog.Builder(CaptureAndLoadPicActivity.this).create();
		alertDialog.setTitle("Cảnh Báo");
		alertDialog.setMessage("Bạn chưa chọn chuyên mục");
		alertDialog.setIcon(R.drawable.tick);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {
				// TODO Auto-generated method stub
			}
		});
		
		CatalogIDs   = Arrays.asList(CatalogIDDatas);
		CatalogNames = Arrays.asList(CatalogNameDatas);
		
		// khoi tao adapter cho combobox
		ArrayAdapter<String> CbxCatalogAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CatalogNames);
		
		// dat style cho combobox dang radiobutton
		CbxCatalogAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// khoi tao combobox va gan adapter cho no
		CbxCatalog = (Spinner)findViewById(R.id.CbxCatalog);
		CbxCatalog.setAdapter(CbxCatalogAdapter);
		CbxCatalog.setSelection(0);
		CbxCatalog.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				selectedCatalogID = CatalogIDs.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.capture_and_load_pic, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnCapture) {
			// thuc hien cong viec chup anh
			try{
				// use standard intent to cature an image
				Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// handle giu lieu tra ve trong onActivityResult
				startActivityForResult(captureIntent, CAMERA_CAPTURE);
			}
			catch(ActivityNotFoundException ex){
				// hien thi loi
				String errorMessage = "Thiet bi cua ban khong ho tro chup anh";
				Toast toast = Toast.makeText(this,  errorMessage, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		else if (v.getId() == R.id.btnOpenPic) {
			// thuc hien cong viec mo anh
			Intent intent = new Intent();
			// goi gallery mac dinh cua android
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			try {
				//intent.putExtra("return-data", true);
				startActivityForResult(Intent.createChooser(intent, "Mở ảnh"), OPEN_PIC);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		else if (v.getId() == R.id.btnSearch) {
			// thuc hien cong viec tim kiem
			if (selectedCatalogID != null && selectedCatalogID != "noid" && capturedImage != null) {
				JSONObject MessageDatas  = new JSONObject();
				JSONObject MessageSearch = new JSONObject();
				try {
					MessageDatas.put("catalogid", selectedCatalogID);
					MessageDatas.put("base64img", Base64Tools.encodeTobase64(capturedImage));
					MessageSearch.put("header", FRAME_HEADER.SEARCH_BOOK_CMD);
					MessageSearch.put("datas", MessageDatas.toString());
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent ViewResultScreen = new Intent(getApplicationContext(), ViewResultsActivity.class);
				ViewResultScreen.putExtra("MessageSearch", MessageSearch.toString());
				startActivity(ViewResultScreen);
			}
			else{
				alertDialog.show();
			}
			
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(requestCode == CAMERA_CAPTURE){
				// get Uri cho buc anh vua chup dc
				picUri = data.getData();
				activeCrop();
			}
			else if (requestCode == OPEN_PIC) {
				// get Uri cho buc anh vua mo
				picUri = data.getData();
				activeCrop();
			}
			else if(requestCode == PIC_CROP){
				//get the returned data
				Bundle extras = data.getExtras();
				//get the cropped bitmap
				Bitmap orign = extras.getParcelable("data");
				// hien thi anh
				capturedImage = Bitmap.createScaledBitmap(orign, 223, 354, true);
				picbox.setImageBitmap(capturedImage);
			}
		}
	}
	
	private void activeCrop(){
		try {
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			cropIntent.setDataAndType(picUri, "image/*");
			cropIntent.putExtra("crop", "true");
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
		    //day du lieu vao data data
			cropIntent.putExtra("return-data", true);
			startActivityForResult(cropIntent, PIC_CROP);
		}
		catch(ActivityNotFoundException anfe){
		    String errorMessage = "Whoops - your device doesn't support the crop action!";
		    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
		    toast.show();
		}
	}

}
