package android.source.tuangou.framework.base;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

/*
 * Menu接口类
 * */
public abstract class MenuMediator{

	public MenuMediator(){
		
	}

	public abstract boolean createMenu(Activity activity, Menu menu);

	public abstract boolean optionsItemSelected(Activity activity, MenuItem menuitem);

	public abstract boolean prepareOptionsMenu(Activity activity, Menu menu);
}
