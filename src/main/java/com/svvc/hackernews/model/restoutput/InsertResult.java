package com.svvc.hackernews.model.restoutput;

public class InsertResult<T>
{
    private T data;

    private boolean inserted;

    public static <T> InsertResult<T> successful(final T data)
    {
        InsertResult<T> result = new InsertResult<>();
        result.setData(data);
        result.setInserted(true);
        return result;
    }

    public static <T> InsertResult<T> failed(final T data)
    {
        InsertResult<T> result = new InsertResult<>();
        result.setData(data);
        result.setInserted(false);
        return result;
    }

    public T getData()
    {
        return data;
    }

    public void setData(final T data)
    {
        this.data = data;
    }

    public boolean isInserted()
    {
        return inserted;
    }

    public void setInserted(final boolean inserted)
    {
        this.inserted = inserted;
    }
}
