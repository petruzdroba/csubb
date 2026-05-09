package org.zdroba.entity;

import java.util.List;

public class Page<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long total;

    public Page(List<T> content, int pageNumber, int pageSize, long total) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.total = total;
    }

    public Page(List<T> content, int pageSize) {
        this.content = content;
        this.pageNumber = -1;
        this.pageSize = pageSize;
        this.total = -1;
    }

    public List<T> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotal() {
        return total;
    }
}
