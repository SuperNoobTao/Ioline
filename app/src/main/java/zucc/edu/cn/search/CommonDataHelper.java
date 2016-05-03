package zucc.edu.cn.search;


import android.content.Context;
import android.widget.Filter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CommonDataHelper {
        public static String FILE_NAME = "";
        public static List<CommonWrapper> sCommonWrappers = new ArrayList<>();

        /**
         * 构造器 接受file_name
         * @author Administrator
         * @time 2016/3/27 20:07
         *
         */
        public interface OnFindResultsListener{
                void onResults(List<CommonSuggestion> results);
        }
        /**
         *      先加载数据作为 默认
         * */
        public static List<CommonSuggestion> getHistory(Context context, int count){
                initColorWrapperList(context);
                List<CommonSuggestion> suggestionList = new ArrayList<>();
                CommonSuggestion labelSuggestion;
                for(int i=0; i<count; i++){
                        labelSuggestion = new CommonSuggestion(sCommonWrappers.get(i));
                        labelSuggestion.setmIsHistory(true);
                        suggestionList.add(labelSuggestion);
                }
                return suggestionList;
        }
        /**
         *      加载所有， 选择类型必须加载所有
         * */
        public static List<CommonSuggestion> getHistory(Context context){
                initColorWrapperList(context);
                List<CommonSuggestion> suggestionList = new ArrayList<>();
                CommonSuggestion labelSuggestion;
                for(int i = 0; i< sCommonWrappers.size(); i++){
                        labelSuggestion = new CommonSuggestion(sCommonWrappers.get(i));
                        labelSuggestion.setmIsHistory(true);
                        suggestionList.add(labelSuggestion);
                }
                return suggestionList;
        }
        /**
         * find 查到内容并通过过滤
         * @author Administrator
         * @time 2016/3/27 16:55
         *
         */

        public static void find(Context context, String query, String file_name, final OnFindResultsListener listener){
                FILE_NAME = file_name;
                initColorWrapperList(context);
                new Filter(){
                        @Override
                        protected FilterResults performFiltering(CharSequence constraint) {
                                List<CommonSuggestion> suggestionList = new ArrayList<>();

                                if (!(constraint == null || constraint.length() == 0)) {

                                        for(CommonWrapper school: sCommonWrappers){

//                                                if(color.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
//                                                        suggestionList.add(new ColorSuggestion(color));
                                                if(school.getName().toUpperCase().contains(constraint.toString().toUpperCase()))
                                                        suggestionList.add(new CommonSuggestion(school));
                                        }
                                }
                                FilterResults results = new FilterResults();
                                results.values = suggestionList;
                                results.count = suggestionList.size();
                                return results;
                        }

                        @Override
                        protected void publishResults(CharSequence constraint, FilterResults results) {
                                if(listener!=null)
                                        listener.onResults((List<CommonSuggestion>)results.values);
                        }
                }.filter(query);

        }

        private static void initColorWrapperList(Context context){
                if(sCommonWrappers.isEmpty()) {
                        String jsonString = loadJson(context);
                        sCommonWrappers = deserializeColors(jsonString);
                }

        }

        private static String loadJson(Context context) {
                String jsonString;
                try {
                        InputStream is = context.getAssets().open(FILE_NAME);
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        jsonString = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                        ex.printStackTrace();
                        return null;
                }

                return jsonString;
        }

        private static List<CommonWrapper> deserializeColors(String jsonString){
                Gson gson = new Gson();

                Type collectionType = new TypeToken<List<CommonWrapper>>() {}.getType();
                return gson.fromJson(jsonString, collectionType);
        }

}