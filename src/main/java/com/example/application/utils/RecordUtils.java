package com.example.application.utils;

import com.vaadin.flow.data.provider.hierarchy.TreeData;

import java.util.List;

public class RecordUtils {
    public static <T> void populateChildren(TreeData<T> treeData, List<T> records, T parent) {
        for (T child : records) {
            treeData.addItem(null, child);
            treeData.setParent(child, parent);
        }
    }
}
