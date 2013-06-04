package quanghieu.searchingbookclient;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewBookDetail extends Activity {

	private TextView txtTenSach;
	private TextView txtTenChuyenMuc;
	private TextView txtTenTacGia;
	private TextView txtNgayXB;
	private TextView txtNhaXB;
	private TextView txtSoTrang;
	private TextView txtGiaBan;
	private TextView txtGioiThieu;
	
	private ImageView BookDetailImage;
	
	private String BookDetailString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_book_detail);
		// lay du lieu sach gui sang tu activity truoc
		Intent i = getIntent();
		BookDetailString = i.getStringExtra("BookDetailString");
		
		//handle cac control tren activity
		txtTenSach = (TextView)findViewById(R.id.txtTenSach);
		txtTenChuyenMuc = (TextView)findViewById(R.id.txtTenChuyenMuc);
		txtTenTacGia = (TextView)findViewById(R.id.txtTenTacGia);
		txtNgayXB = (TextView)findViewById(R.id.txtNgayXB);
		txtNhaXB = (TextView)findViewById(R.id.txtNhaXB);
		txtSoTrang = (TextView)findViewById(R.id.txtSoTrang);
		txtGiaBan = (TextView)findViewById(R.id.txtGiaBan);
		txtGioiThieu = (TextView)findViewById(R.id.txtGioiThieu);
		BookDetailImage = (ImageView)findViewById(R.id.BookDetailImage);
		
		//nap du lieu
		try {
			JSONObject JObj = new JSONObject(BookDetailString);
			txtTenSach.setText(JObj.getString(BOOK_DETAIL_KEY.BOOK_NAME));
			txtTenChuyenMuc.setText(JObj.getString(BOOK_DETAIL_KEY.CATALOG));
			txtTenTacGia.setText(JObj.getString(BOOK_DETAIL_KEY.AUTHOR));
			txtNgayXB.setText(JObj.getString(BOOK_DETAIL_KEY.PUBLISHEDDATE));
			txtNhaXB.setText(JObj.getString(BOOK_DETAIL_KEY.PUBLISHER));
			txtSoTrang.setText(JObj.getString(BOOK_DETAIL_KEY.PAGES));
			txtGiaBan.setText(JObj.getString(BOOK_DETAIL_KEY.PRICE));
			txtGioiThieu.setText(JObj.getString(BOOK_DETAIL_KEY.INTRO));
			BookDetailImage.setImageBitmap(Base64Tools.decodeBase64(JObj.getString(BOOK_DETAIL_KEY.IMAGE)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_book_detail, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
        	case R.id.menu_back:
        		finish();
        		return true;
        	default:
                return super.onOptionsItemSelected(item);
        }
    }
	
}
