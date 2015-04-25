package Server;
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonTrans {

	public static String buildJson(String key, Object obj) {
		// JSON��ʽ���ݽ�������
		JSONObject jo = new JSONObject();
		jo.put(key, obj);
		return jo.toString();
	}

	public static Object parseJson(String jsonString, String key) {
		JSONObject jsonObj = JSONObject.fromObject(jsonString);

		return jsonObj.get(key);
	}

}
