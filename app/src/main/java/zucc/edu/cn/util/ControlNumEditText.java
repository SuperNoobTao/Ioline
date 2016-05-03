package zucc.edu.cn.util;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;

public class ControlNumEditText extends EditText {
    private int MAX_NUM = 120;

    public interface onTextEditListener{
        public void textChanged(int cur_num);
    };

    private onTextEditListener listener;

    public void setOnTextEditListener(onTextEditListener listener) {
        this.listener = listener;
    }

    public ControlNumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlNumEditText(Context context) {
        super(context);
    }

    public int getMaxNum() {
        return MAX_NUM;
    }

    public void setMaxNum(int num) {
        this.MAX_NUM = num;
    }

    @Override
    public Editable getText() {
        Editable text = super.getText();
        int cur_num = (int) calculateLength(text);

        if (cur_num > MAX_NUM) {
            text.delete(MAX_NUM+1,cur_num);//限制输入 Text limits.
            cur_num = MAX_NUM;//返回最大值 Tell the listener current number.
        }

        if (listener != null) {
            listener.textChanged(cur_num);
        }

        return super.getText();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    /**
     * 判断字符数
     * Get the char number.
     * @param text
     * @return
     */
    private long calculateLength(CharSequence text) {
        double len = 0;
        for (int i = 0; i < text.length(); i++) {
            int tmp = (int) text.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }
}
