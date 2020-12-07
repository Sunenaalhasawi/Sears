
package com.hasawi.sears.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sort {

    @SerializedName("unsorted")
    @Expose
    private boolean unsorted;
    @SerializedName("sorted")
    @Expose
    private boolean sorted;
    @SerializedName("empty")
    @Expose
    private boolean empty;

    /**
     * No args constructor for use in serialization
     */
    public Sort() {
    }

    /**
     * @param unsorted
     * @param sorted
     * @param empty
     */
    public Sort(boolean unsorted, boolean sorted, boolean empty) {
        super();
        this.unsorted = unsorted;
        this.sorted = sorted;
        this.empty = empty;
    }

    public boolean isUnsorted() {
        return unsorted;
    }

    public void setUnsorted(boolean unsorted) {
        this.unsorted = unsorted;
    }

    public Sort withUnsorted(boolean unsorted) {
        this.unsorted = unsorted;
        return this;
    }

    public boolean isSorted() {
        return sorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public Sort withSorted(boolean sorted) {
        this.sorted = sorted;
        return this;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public Sort withEmpty(boolean empty) {
        this.empty = empty;
        return this;
    }

}
