package com.example.android.materialdesigncodelab.utils;

import android.content.Context;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.poisearch.PoiSearch;

/**
 * Created by lixindong on 8/8/16.
 */
public class AmapUtils {
    public static void queryPOI(Context context, String keyWord, String cityCode, int page, PoiSearch.OnPoiSearchListener listener){
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", cityCode);
        // keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，
        //POI搜索类型共分为以下20种：汽车服务|汽车销售|
        //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //cityCode表示POI搜索区域的编码，是必须设置参数
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(page);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(context,query);//初始化poiSearch对象
        poiSearch.setOnPoiSearchListener(listener);//设置回调数据的监听器
        poiSearch.searchPOIAsyn();//开始搜索
    }

    public static void inputTips(Context context, String s1, String s2, Inputtips.InputtipsListener listener) {
        Inputtips inputtips = new Inputtips(context, listener);
        inputtips.setQuery(new InputtipsQuery(s1, s2));
        inputtips.requestInputtipsAsyn();
    }
}
