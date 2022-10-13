package com.rca.RCA.type;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pagination<T> {
    private long countFilter = 0L;
    private int totalPages;
    private List<T> list;

    public int processAndGetTotalPages(int size) {
        if (this.countFilter > 0L) {
            this.totalPages = (int) this.countFilter / size;
            if (this.countFilter % size > 0) {
                this.totalPages++;
            }
        }
        return this.totalPages;
    }

    public long getCountFilter() {
        return countFilter;
    }

    public void setCountFilter(long countFilter) {
        this.countFilter = countFilter;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
