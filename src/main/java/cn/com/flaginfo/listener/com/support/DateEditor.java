package cn.com.flaginfo.listener.com.support;

import cn.com.flaginfo.listener.utils.DateUtil;
import java.beans.PropertyEditorSupport;
import java.util.Date;

public class DateEditor extends PropertyEditorSupport {
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		super.setValue(DateUtil.parseDate(text));
	}
	
	@Override
	public String getAsText() {
		return DateUtil.fmtDate((Date)this.getValue());
	}

}
