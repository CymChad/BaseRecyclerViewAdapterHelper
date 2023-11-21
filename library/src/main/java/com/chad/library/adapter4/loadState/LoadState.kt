package com.chad.library.adapter4.loadState

/**
 * Load state
 *
 * 加载状态
 *
 * @property endOfPaginationReached 是否已到达分页末尾
 */
sealed class LoadState(
    val endOfPaginationReached: Boolean
) {

    /**
     * There is currently no status.
     *
     * 当前没有任何状态（例如：初始化时，刷新数据时）
     */
    object None: LoadState(false) {
        override fun equals(other: Any?): Boolean {
            return other is None &&
                    endOfPaginationReached == other.endOfPaginationReached
        }

        override fun hashCode(): Int {
            return endOfPaginationReached.hashCode()
        }

        override fun toString(): String {
            return "None(endOfPaginationReached=$endOfPaginationReached)"
        }
    }

    /**
     * Is not currently loading, and no error currently observed.
     *
     * 当前未加载，并且当前未发生错误。
     *
     * @param endOfPaginationReached 是否已到达分页末尾
     */
    class NotLoading(
        endOfPaginationReached: Boolean
    ) : LoadState(endOfPaginationReached) {
        override fun toString(): String {
            return "NotLoading(endOfPaginationReached=$endOfPaginationReached)"
        }

        override fun equals(other: Any?): Boolean {
            return other is NotLoading &&
                    endOfPaginationReached == other.endOfPaginationReached
        }

        override fun hashCode(): Int {
            return endOfPaginationReached.hashCode()
        }

        companion object {
            val Complete = NotLoading(endOfPaginationReached = true)
            val Incomplete = NotLoading(endOfPaginationReached = false)
        }
    }

    /**
     * Loading is in progress.
     *
     * 正在加载
     */
    object Loading : LoadState(false) {
        override fun toString(): String {
            return "Loading(endOfPaginationReached=$endOfPaginationReached)"
        }

        override fun equals(other: Any?): Boolean {
            return other is Loading &&
                    endOfPaginationReached == other.endOfPaginationReached
        }

        override fun hashCode(): Int {
            return endOfPaginationReached.hashCode()
        }
    }

    /**
     * Loading hit an error.
     *
     * 加载时出错
     *
     * @param error [Throwable] that caused the load operation to generate this error state.
     */
    class Error(
        val error: Throwable
    ) : LoadState(false) {
        override fun equals(other: Any?): Boolean {
            return other is Error &&
                    endOfPaginationReached == other.endOfPaginationReached &&
                    error == other.error
        }

        override fun hashCode(): Int {
            return endOfPaginationReached.hashCode() + error.hashCode()
        }

        override fun toString(): String {
            return "Error(endOfPaginationReached=$endOfPaginationReached, error=$error)"
        }
    }
}
