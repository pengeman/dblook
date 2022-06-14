package dbhelpx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * json的操作类
 *
 */
public class JsonUtil {

	/**
	 * 传过list来将list里面的map取出来进行再加工生成json格式字符串
	 * */
	public static String ListToJson(List list) {
		List newList = new ArrayList();
		int listsize = list.size();
		Map oldMap = null;
		Map newMap = null;
		for (int i = 0; i < listsize; i++) {
			oldMap = (Map) list.get(i);
			newMap = new HashMap();
			// 将集合放在map中
			Set set = oldMap.keySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Object key = iterator.next();
				Object value = oldMap.get(key);
				newMap.put("\"" + key + "\"", "\"" + value + "\"");
			}
			newList.add(newMap);
		}

		String str = newList.toString();
		str = str.replace('=', ':');
		return str;
	}
}