package com.hasawi.sears_outlet.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchProductListResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public class Data {

        @SerializedName("page")
        @Expose
        private Integer page;
        @SerializedName("nbHits")
        @Expose
        private Integer nbHits;
        @SerializedName("nbPages")
        @Expose
        private Integer nbPages;
        @SerializedName("hitsPerPage")
        @Expose
        private Integer hitsPerPage;
        @SerializedName("productSearchs")
        @Expose
        private List<ProductSearch> productSearchs = null;

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getNbHits() {
            return nbHits;
        }

        public void setNbHits(Integer nbHits) {
            this.nbHits = nbHits;
        }

        public Integer getNbPages() {
            return nbPages;
        }

        public void setNbPages(Integer nbPages) {
            this.nbPages = nbPages;
        }

        public Integer getHitsPerPage() {
            return hitsPerPage;
        }

        public void setHitsPerPage(Integer hitsPerPage) {
            this.hitsPerPage = hitsPerPage;
        }

        public List<ProductSearch> getProductSearchs() {
            return productSearchs;
        }

        public void setProductSearchs(List<ProductSearch> productSearchs) {
            this.productSearchs = productSearchs;
        }

    }
}
