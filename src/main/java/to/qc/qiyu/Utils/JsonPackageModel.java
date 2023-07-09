package to.qc.qiyu.Utils;

import com.alibaba.fastjson.JSONObject;

public class JsonPackageModel {
    public static JSONObject model(String session,String qq){
        JSONObject Json = new JSONObject();
        Json.put("sessionKey",session);
        Json.put("qq",qq);
        return Json;
    }


}
