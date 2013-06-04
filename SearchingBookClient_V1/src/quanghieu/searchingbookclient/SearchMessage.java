package quanghieu.searchingbookclient;

import org.json.*;
import android.graphics.Bitmap;

public class SearchMessage {

	public String header = FRAME_HEADER.SEARCH_BOOK_CMD;
	public String datas;
	
	public SearchMessage(Bitmap img){
		datas = Base64Tools.encodeTobase64(img);
	}
	
	public String ToJsonString(){
		JSONObject jObj = new JSONObject();
		
		try {
			jObj.put("header", header);
			jObj.put("datas", datas);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jObj.toString();
	}
}
