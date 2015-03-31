package com.example.wizart.mytrips.ActiveAndroidDb;

import com.activeandroid.serializer.TypeSerializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class ArrayListLAtLngSerializer extends TypeSerializer {
    private static final Gson gson = new Gson();

    @Override
    public Class<?> getDeserializedType() {
        return ArrayList.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public Object serialize(final Object o) {
        if (null == o) return null;
        final String json = gson.toJson(o);
        return json;
    }

    @Override
    public Object deserialize(final Object o) {
        if (null == o) return null;
        final ArrayList<LatLng> myClassItems = gson.fromJson(o.toString(), ArrayList.class);
        return myClassItems;
    }
}
