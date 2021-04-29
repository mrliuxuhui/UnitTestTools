package com.flyingwillow.utt.extensionpoint;

import com.intellij.openapi.extensions.ExtensionPointName;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExtensionHolder<T> {

    private final CopyOnWriteArrayList<T> list;

    public ExtensionHolder(List<T> list) {
        this.list = new CopyOnWriteArrayList<>(list);
    }

    public ExtensionHolder() {
        Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            ExtensionPointName<T> extensionPointName = (ExtensionPointName<T>) type.getClass().getField("EXTENSION_POINT_NAME").get(null);
            this.list = new CopyOnWriteArrayList<>(extensionPointName.getExtensionList());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalTypeParameterException("Invalid Type Parameter!", e);
        }
    }

    public void remove(T e) {
        this.list.remove(e);
    }

    public void add(T e) {
        this.list.add(e);
    }
}
