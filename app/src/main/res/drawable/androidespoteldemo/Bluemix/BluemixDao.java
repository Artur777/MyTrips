package drawable.androidespoteldemo.Bluemix;

import android.util.Log;

import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by AChojeck on 04/03/2015.
 */
public class BluemixDao {


    private String CLASS_NAME="Bluemix.BluemixDao";

    public BluemixDao() {

    }

    public static void saveItem(IBMDataObject item, final onDaoBluemixSuccessFaillListener listener) {
//        DemoItem item = new DemoItem();
        item.save().continueWith(new Continuation<IBMDataObject, Void>() {

            @Override
            public Void then(Task<IBMDataObject> task) throws Exception {
                if (task.isFaulted()) {
                    listener.onFail();
                    // Handle errors
                } else {
                    IBMDataObject myItem = (DemoItem) task.getResult();
                    // Do more work
                    listener.onSuccess(myItem);
                }
                return null;
            }
        });
    }

    /*

class.readItem(item, new onDatOpListener() {
    public void onSuccess() {
        // Handle your response
    }
    public void onFail() {
        // Handle your response
    }

});

     */

public static void readItem(IBMDataObject item, final onDaoBluemixSuccessFaillListener listener) {

    IBMQuery<DemoItem> queryByClass=null;
    // Find a set of objects by class
    try {
        queryByClass = IBMQuery.queryForClass(DemoItem.class);
    } catch (IBMDataException e) {
        e.printStackTrace();
    }

    // Find a specific object
    IBMQuery<DemoItem> queryForObject = item.getQuery();

    queryByClass.find().continueWith(new Continuation<List<DemoItem>, Void>() {

        @Override
        public Void then(Task<List<DemoItem>> task) throws Exception {
            if (task.isFaulted()) {
                // Handle errors
                listener.onFail();
            } else {
                // do more work
                List<DemoItem> objects = task.getResult();
                listener.onSuccess(objects);
            }
            return null;
        }
    });
 }


    public void deleteItem(DemoItem item, final onDaoBluemixCancelListener listener )  {
        // This will attempt to delete the item on the server.
        item.delete().continueWith(new Continuation<IBMDataObject, Void>() {

            @Override
            public Void then(Task<IBMDataObject> task) throws Exception {
                // Log if the delete was cancelled.
                if (task.isCancelled()){
                    Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                    listener.onCancel();
                }

                // Log error message, if the delete task fails.
                else if (task.isFaulted()) {
                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                    listener.onFail();
                }
                // If the result succeeds do something on success
                else {
                    listener.onSuccess("Deleted");
//                    lvArrayAdapter.notifyDataSetChanged();

                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);

  //      lvArrayAdapter.notifyDataSetChanged();
    }



    private void sortItems(List<DemoItem> theList) {
        // Sort collection by case insensitive alphabetical order.
        Collections.sort(theList, new Comparator<DemoItem>() {
            public int compare(DemoItem lhs,DemoItem rhs) {
                String lhsName = lhs.getName();
                String rhsName = rhs.getName();
                return lhsName.compareToIgnoreCase(rhsName);
            }
        });
    }


    public void htttpGet(String item) {
//        cloudCodeService.get(item).continueWith(new Continuation<IBMHttpResponse, Void>() {
//
//            @Override
//            public Void then(Task<IBMHttpResponse> task) throws Exception {
//                if (task.isFaulted()) {
//                    // error handling code here
//                } else {
//                    IBMHttpResponse response = task.getResult();
//                    if (response.getHttpResponseCode() == 200) {
//                        // take action on success
//                    }
//                }
//                return null;
//            }
//        });

    }
}

