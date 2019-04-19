package com.buel.holyhelper.management.firestore

import com.buel.holyhelper.view.DataTypeListener

interface IManager<T, D> {
    /**
     *
     * @param dataModel
     */
    fun insert(dataModel: T, listener: DataTypeListener.OnCompleteListener<D>)

    /**
     *
     * @param dataModel
     */
     fun modify(dataModel: T, listener: DataTypeListener.OnCompleteListener<D>)

    /**
     *
     * @param dataModel
     */
    fun delete(dataModel: T, listener: DataTypeListener.OnCompleteListener<D>)


    /**
     *
     */
    fun getDocData(dataModel: T, listener: DataTypeListener.OnCompleteListener<D>)
    fun getColData(listener: DataTypeListener.OnCompleteListener<D>)
}