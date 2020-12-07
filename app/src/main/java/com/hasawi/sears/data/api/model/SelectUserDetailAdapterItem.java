package com.hasawi.sears.data.api.model;

public class SelectUserDetailAdapterItem {
    private int unselectedImage, selectedImage;
    private Language languageItem;
    private boolean isSelected = false;

    public SelectUserDetailAdapterItem(int unselectedImage, int selectedImage, Language languageItem, boolean isSelected) {
        this.unselectedImage = unselectedImage;
        this.selectedImage = selectedImage;
        this.languageItem = languageItem;
    }

    public int getUnselectedImage() {
        return unselectedImage;
    }

    public void setUnselectedImage(int unselectedImage) {
        this.unselectedImage = unselectedImage;
    }

    public int getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(int selectedImage) {
        this.selectedImage = selectedImage;
    }

    public Language getLanguageItem() {
        return languageItem;
    }

    public void setLanguageItem(Language languageItem) {
        this.languageItem = languageItem;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
