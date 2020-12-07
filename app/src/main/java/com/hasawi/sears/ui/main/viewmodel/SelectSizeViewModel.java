package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SelectSizeViewModel extends ViewModel {
    HashMap<String, ArrayList<String>> productSizeHashmap;
    ArrayList<String> sizeList, shoesSizeList;
    ArrayList<String> productNameList;

    public ArrayList<String> getSizeList() {
        sizeList = new ArrayList<>();
        sizeList.add("XS");
        sizeList.add("S");
        sizeList.add("M");
        sizeList.add("L");
        sizeList.add("XL");
        sizeList.add("XXL");
        sizeList.add("3XL");
        sizeList.add("4XL");
        return sizeList;
    }

    public ArrayList<String> getShoesSizeList() {
        ArrayList<String> shoesSizeList = new ArrayList<>();
        shoesSizeList.add("6");
        shoesSizeList.add("7");
        shoesSizeList.add("8");
        shoesSizeList.add("9");
        shoesSizeList.add("10");
        return shoesSizeList;
    }

    public HashMap<String, ArrayList<String>> getProductSizeHashmap() {
        productSizeHashmap = new HashMap<>();
        productSizeHashmap.put("T-Shirt", sizeList);
        productSizeHashmap.put("Shirt", sizeList);
        productSizeHashmap.put("Kurta", sizeList);
        productSizeHashmap.put("Top", sizeList);
        productSizeHashmap.put("Jeans", sizeList);
        productSizeHashmap.put("Shoes", shoesSizeList);
        return productSizeHashmap;
    }

    public ArrayList<String> getProductNameList() {
        productNameList = new ArrayList<>();
        // Getting an iterator
        Iterator iterator = getProductSizeHashmap().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry element = (Map.Entry) iterator.next();
            productNameList.add(element.getKey().toString());
        }
        return productNameList;
    }


}
