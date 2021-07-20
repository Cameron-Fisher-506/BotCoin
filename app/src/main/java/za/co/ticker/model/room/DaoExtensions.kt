package za.co.ticker.model.room

suspend fun <T> IBaseDao<T>.upsert(entity: T, baseDao: IBaseDao<T>) {
    val result = baseDao.insert(entity)
    if (result == -1L) {
        baseDao.update(entity)
    }
}

suspend fun <T> IBaseDao<T>.upsert(entities: List<T>, baseDao: IBaseDao<T>) {
    val results = baseDao.insert(entities)
    val updateList: MutableList<T> = mutableListOf()

    for (i in results.indices) {
        if (results[i] == -1L) {
            updateList.add(entities[i])
        }
    }

    if (updateList.isNotEmpty()) {
        baseDao.update(updateList)
    }
}