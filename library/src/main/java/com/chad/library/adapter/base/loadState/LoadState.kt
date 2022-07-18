package com.chad.library.adapter.base.loadState

sealed class LoadState(
    val endOfPaginationReached: Boolean
) {


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
     * @param endOfPaginationReached
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
