package zucc.edu.cn.search;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by Administrator on 2016/3/27.
 */
public class CommonSuggestion  implements SearchSuggestion {



    private CommonWrapper commonWrapper;

    private String name;

    private boolean mIsHistory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean ismIsHistory() {
        return mIsHistory;
    }

    public void setmIsHistory(boolean mIsHistory) {
        this.mIsHistory = mIsHistory;
    }

    public CommonSuggestion(CommonWrapper label){
        this.commonWrapper = label;
        this.name = commonWrapper.getName();
    }

    public CommonSuggestion(Parcel source) {
        this.name = source.readString();
    }



    public CommonWrapper getCommon(){
        return commonWrapper;
    }



    @Override
    public String getBody() {
        return commonWrapper.getName();
    }

    @Override
    public Creator getCreator() {
        return CREATOR;
    }
    ///////

    public static final Creator<CommonSuggestion> CREATOR = new Creator<CommonSuggestion>() {
        @Override
        public CommonSuggestion createFromParcel(Parcel in) {
            return new CommonSuggestion(in);
        }

        @Override
        public CommonSuggestion[] newArray(int size) {
            return new CommonSuggestion[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

}
