package com.buel.holyhelper.management;

/**
 * Created by 19001283 on 2018-06-11.
 */

public interface Management<T> {

    /**
     *
     * @param dataModel
     */
    public void insert(T dataModel, OnCompleteListener listener);

    /**
     *
     * @param dataSet
     */
    public void modify(T dataSet, OnCompleteListener listener);

    /**
     *
     * @param dataSet
     */
    public void delete(T dataSet, OnCompleteListener listener);

    /**
     *
     */
    public interface OnCompleteListener<D>{
        public void onComplete(D data);
    }
}
