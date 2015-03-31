package drawable.androidespoteldemo.Bluemix;

import com.ibm.mobile.services.data.IBMDataObject;

import java.util.List;

/**
 * Created by AChojeck on 04/03/2015.
 */
public interface onDatOpInterface {

    void onSuccess(List<DemoItem> objects);

    void onSuccess(String result);

    void onSuccess(int res);

    void onSuccess(IBMDataObject myItem);

    void onFail();

    void onCancel();
}
