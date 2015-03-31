/*
 * Copyright 2014 IBM Corp. All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package drawable.androidespoteldemo.Bluemix;

import com.ibm.mobile.services.data.IBMDataFile;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("DemoItem")
public class DemoItem extends IBMDataObject {
	public static final String CLASS_NAME = "DemoItem";
	private static final String NAME = "name";
    private static final String TEMPERATURE= "temperature";
    private static final String HUMIDITY = "humidity";
    private static final String TIMESTAMP= "timestamp";
    private static final String IMAGE="image";
    private static final String ADDRESS="address";
    private static final String THUMBNAILIMAGE= "thumbnailimage";

    /**
	 * Gets the name of the DemoItem.
	 * @return String itemName
	 */
	public String getName() {
		return (String) getObject(NAME);
	}

    public String getAddress() {
        return (String) getObject(ADDRESS);
    }


    public String getTemperature() {
        return (String) getObject(TEMPERATURE);
    }


    public String getHumidity() {
        return (String) getObject(HUMIDITY);
    }

    public String getDate() {
        return (String) getObject(TIMESTAMP);
    }


    public IBMDataFile getImage() {
        return (IBMDataFile) getObject(IMAGE);
    }

    public IBMDataFile getThumbnailImage() {
        return (IBMDataFile) getObject(IMAGE);
    }


	/**
	 * Sets the name of a list item, as well as calls setCreationTime().

	 */
    public void setItemObject(String name, Object object) {
        setObject(name, object);
    }


    public void setName(String itemName) {
		setObject(NAME, (itemName != null) ? itemName : "");
	}

    public void setAddress(String address) {
        setObject(ADDRESS, (address!= null) ? address: "");
    }


    public void setTemperature( String temperature) {
        setObject(TEMPERATURE, (temperature!= null) ? temperature: "");
    }

    public void setHumidity( String humidity) {
        setObject(HUMIDITY, (humidity!= null) ? humidity: "");
    }


    public void setDate( String timeStamp) {
        setObject(TIMESTAMP, (timeStamp != null) ? timeStamp: "");
    }


    public void setImage(IBMDataFile imageFile) {
        setObject(IMAGE, imageFile);
    }


    public void setThumbnailImage(IBMDataFile thumbnailImageFile) {
        setObject(THUMBNAILIMAGE,thumbnailImageFile);
    }

	
	/**
	 * When calling toString() for an item, we'd really only want the name.
	 * @return String theItemName
	 */
	public String toString() {
		String theItemName = "";
		theItemName = getName();
		return theItemName;
	}

}
