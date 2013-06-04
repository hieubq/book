package quanghieu.searchingbookclient;

public class FRAME_HEADER {
	
	public static String CL_CONNECTED_CMD        = "0000";

    public static String SEARCH_BOOK_CMD         = "0001";
    public static String SEND_RESULT_CMD         = "0010";

    public static String GET_DETAIL_CMD          = "0100";
    public static String SEND_DETAIL_CMD         = "1000";

    public static String STOP_SEARCH_CMD         = "0011";
    public static String DONE_SEARCH_CMD         = "0101";

    public static String DISCONNECTED_CMD        = "1001";
    public static String ALLOW_DISCONNECTED_CMD  = "0111";
}
