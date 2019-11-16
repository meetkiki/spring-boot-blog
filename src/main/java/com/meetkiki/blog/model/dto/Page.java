//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.meetkiki.blog.model.dto;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Page<T> {
    private int pageNum = 1;
    private int limit = 10;
    private int prevPage = 1;
    private int nextPage = 1;
    private int totalPages = 1;
    private long totalRows = 0L;
    private List<T> rows;
    private boolean isFirstPage = false;
    private boolean isLastPage = false;
    private boolean hasPrevPage = false;
    private boolean hasNextPage = false;
    private int navPages = 8;
    private int[] navPageNums;

    public <R> Page<R> map(Function<? super T, ? extends R> mapper) {
        Page<R> page = new Page(this.totalRows, this.pageNum, this.limit);
        if (null != this.rows) {
            page.setRows((List)this.rows.stream().map(mapper).collect(Collectors.toList()));
        }

        return page;
    }

    public Page<T> peek(Consumer<T> consumer) {
        if (null != this.rows) {
            this.rows = (List)this.rows.stream().peek(consumer).collect(Collectors.toList());
        }

        return this;
    }

    public Page<T> navPages(int navPages) {
        this.calcNavigatePageNumbers(navPages);
        return this;
    }

    public Page() {
    }

    public Page(long total, int page, int limit) {
        this.init(total, page, limit);
    }

    private void init(long total, int pageNum, int limit) {
        this.totalRows = total;
        this.limit = limit;
        this.totalPages = (int)((this.totalRows - 1L) / (long)this.limit + 1L);
        if (pageNum < 1) {
            this.pageNum = 1;
        } else if (pageNum > this.totalPages) {
            this.pageNum = this.totalPages;
        } else {
            this.pageNum = pageNum;
        }

        this.calcNavigatePageNumbers(this.navPages);
        this.judgePageBoudary();
    }

    private void calcNavigatePageNumbers(int navPages) {
        int i;
        if (this.totalPages <= navPages) {
            this.navPageNums = new int[this.totalPages];

            for(i = 0; i < this.totalPages; ++i) {
                this.navPageNums[i] = i + 1;
            }
        } else {
            this.navPageNums = new int[navPages];
            i = this.pageNum - navPages / 2;
            int endNum = this.pageNum + navPages / 2;
            if (i < 1) {
                i = 1;

                for(i = 0; i < navPages; ++i) {
                    this.navPageNums[i] = i++;
                }
            } else if (endNum > this.totalPages) {
                endNum = this.totalPages;

                for(i = navPages - 1; i >= 0; --i) {
                    this.navPageNums[i] = endNum--;
                }
            } else {
                for(i = 0; i < navPages; ++i) {
                    this.navPageNums[i] = i++;
                }
            }
        }

    }

    private void judgePageBoudary() {
        this.isFirstPage = this.pageNum == 1;
        this.isLastPage = this.pageNum == this.totalPages && this.pageNum != 1;
        this.hasPrevPage = this.pageNum != 1;
        this.hasNextPage = this.pageNum != this.totalPages;
        if (this.hasNextPage) {
            this.nextPage = this.pageNum + 1;
        }

        if (this.hasPrevPage) {
            this.prevPage = this.pageNum - 1;
        }

    }

    public int getPageNum() {
        return this.pageNum;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getPrevPage() {
        return this.prevPage;
    }

    public int getNextPage() {
        return this.nextPage;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public long getTotalRows() {
        return this.totalRows;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public boolean isFirstPage() {
        return this.isFirstPage;
    }

    public boolean isLastPage() {
        return this.isLastPage;
    }

    public boolean isHasPrevPage() {
        return this.hasPrevPage;
    }

    public boolean isHasNextPage() {
        return this.hasNextPage;
    }

    public int getNavPages() {
        return this.navPages;
    }

    public int[] getNavPageNums() {
        return this.navPageNums;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public void setFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public void setLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void setNavPages(int navPages) {
        this.navPages = navPages;
    }

    public void setNavPageNums(int[] navPageNums) {
        this.navPageNums = navPageNums;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Page)) {
            return false;
        } else {
            Page<?> other = (Page)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getPageNum() != other.getPageNum()) {
                return false;
            } else if (this.getLimit() != other.getLimit()) {
                return false;
            } else if (this.getPrevPage() != other.getPrevPage()) {
                return false;
            } else if (this.getNextPage() != other.getNextPage()) {
                return false;
            } else if (this.getTotalPages() != other.getTotalPages()) {
                return false;
            } else if (this.getTotalRows() != other.getTotalRows()) {
                return false;
            } else {
                label62: {
                    Object this$rows = this.getRows();
                    Object other$rows = other.getRows();
                    if (this$rows == null) {
                        if (other$rows == null) {
                            break label62;
                        }
                    } else if (this$rows.equals(other$rows)) {
                        break label62;
                    }

                    return false;
                }

                if (this.isFirstPage() != other.isFirstPage()) {
                    return false;
                } else if (this.isLastPage() != other.isLastPage()) {
                    return false;
                } else if (this.isHasPrevPage() != other.isHasPrevPage()) {
                    return false;
                } else if (this.isHasNextPage() != other.isHasNextPage()) {
                    return false;
                } else if (this.getNavPages() != other.getNavPages()) {
                    return false;
                } else {
                    return Arrays.equals(this.getNavPageNums(), other.getNavPageNums());
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Page;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getPageNum();
        result = result * 59 + this.getLimit();
        result = result * 59 + this.getPrevPage();
        result = result * 59 + this.getNextPage();
        result = result * 59 + this.getTotalPages();
        long $totalRows = this.getTotalRows();
        result = result * 59 + (int)($totalRows >>> 32 ^ $totalRows);
        Object $rows = this.getRows();
        result = result * 59 + ($rows == null ? 43 : $rows.hashCode());
        result = result * 59 + (this.isFirstPage() ? 79 : 97);
        result = result * 59 + (this.isLastPage() ? 79 : 97);
        result = result * 59 + (this.isHasPrevPage() ? 79 : 97);
        result = result * 59 + (this.isHasNextPage() ? 79 : 97);
        result = result * 59 + this.getNavPages();
        result = result * 59 + Arrays.hashCode(this.getNavPageNums());
        return result;
    }

    public String toString() {
        return "Page(pageNum=" + this.getPageNum() + ", limit=" + this.getLimit() + ", prevPage=" + this.getPrevPage() + ", nextPage=" + this.getNextPage() + ", totalPages=" + this.getTotalPages() + ", totalRows=" + this.getTotalRows() + ", rows=" + this.getRows() + ", isFirstPage=" + this.isFirstPage() + ", isLastPage=" + this.isLastPage() + ", hasPrevPage=" + this.isHasPrevPage() + ", hasNextPage=" + this.isHasNextPage() + ", navPages=" + this.getNavPages() + ", navPageNums=" + Arrays.toString(this.getNavPageNums()) + ")";
    }
}
