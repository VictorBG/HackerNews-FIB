package com.svvc.hackernews.model.restoutput;

import static com.svvc.hackernews.service.contributions.impl.ContributionsServiceImpl.PAGE_QUANTITY;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageListResponse<Response>
{
    private int page;
    private int pageCount;
    private boolean hasNextPage;
    private List<Response> items;

    public static <T> PageListResponse<T> of(int page, List<T> list)
    {
        PageListResponse<T> response = new PageListResponse<>();
        response.setItems(list);
        response.setPage(page);
        response.setPageCount(response.getItems().size());
        response.setHasNextPage(list.size() == PAGE_QUANTITY);
        return response;
    }

    public List<Response> getItems()
    {
        return items;
    }

    public void setItems(final List<Response> items)
    {
        this.items = items;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(final int page)
    {
        this.page = page;
    }

    public int getPageCount()
    {
        return pageCount;
    }

    public void setPageCount(final int pageCount)
    {
        this.pageCount = pageCount;
    }

    public boolean isHasNextPage()
    {
        return hasNextPage;
    }

    public void setHasNextPage(final boolean hasNextPage)
    {
        this.hasNextPage = hasNextPage;
    }
}
