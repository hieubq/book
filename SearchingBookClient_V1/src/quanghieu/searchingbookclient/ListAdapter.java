package quanghieu.searchingbookclient;

import java.util.ArrayList;
import java.util.HashMap;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.*;

import android.graphics.Bitmap;

public class ListAdapter extends BaseAdapter{

	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
	
    public ListAdapter(Activity a){
    	this.activity = a;
    	data = new ArrayList<HashMap<String, String>>();
    	inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public ListAdapter(Activity a, ArrayList<HashMap<String, String>> d){
    	//this.activity = a;
    	this.data = d;
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public String getBookId(int arg0) {
		// TODO Auto-generated method stub
		HashMap<String, String> book = data.get(arg0);
		return book.get(RESULT_KEY.BOOK_ID);
	}
	
	public void add(String jstr){
		try {
			JSONObject JObj = new JSONObject(jstr);
			HashMap<String, String> newItem = new HashMap<String, String>();
			newItem.put(RESULT_KEY.BOOK_ID, JObj.getString(RESULT_KEY.BOOK_ID));
			newItem.put(RESULT_KEY.BOOK_NAME, JObj.getString(RESULT_KEY.BOOK_NAME));
			newItem.put(RESULT_KEY.AUTHOR, JObj.getString(RESULT_KEY.AUTHOR));
			newItem.put(RESULT_KEY.PUBLISHER, JObj.getString(RESULT_KEY.PUBLISHER));
			newItem.put(RESULT_KEY.PRICE, JObj.getString(RESULT_KEY.PRICE));
			newItem.put(RESULT_KEY.IMAGE, JObj.getString(RESULT_KEY.IMAGE));
			data.add(newItem);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearData(){
		data.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi=convertView;
        if(convertView==null){
            vi = inflater.inflate(R.layout.list_row, null);
        }
        TextView txtBookTitle = (TextView)vi.findViewById(R.id.BookTitle);
        TextView txtAuthor = (TextView)vi.findViewById(R.id.author);
        TextView txtPublisher = (TextView)vi.findViewById(R.id.publisher);
        TextView txtPrice = (TextView)vi.findViewById(R.id.price);
        ImageView thumb_image = (ImageView)vi.findViewById(R.id.list_image);
 
        HashMap<String, String> book = data.get(position);
 
        // Setting all values in ListView
        txtBookTitle.setText(book.get(RESULT_KEY.BOOK_NAME));
        txtAuthor.setText("* Tác giả: " + book.get(RESULT_KEY.AUTHOR));
        txtPublisher.setText("* NXB: " + book.get(RESULT_KEY.PUBLISHER));
        txtPrice.setText("* Giá bán: " + book.get(RESULT_KEY.PRICE));
        
        Bitmap bm = Base64Tools.decodeBase64(book.get(RESULT_KEY.IMAGE));
        thumb_image.setImageBitmap(bm);
        return vi;
	}

	
}
