package moe.peanutmelonseedbigalmond.push.pushserverfcm.component

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.Serializable

class OffsetBasedPageRequest : Pageable, Serializable {
    private companion object {
        private const val serialVersionUID: Long = -25822477129613575L;
    }

    private var limit: Int = 0
    private var offset: Int = 0
    private var sort: Sort

    constructor(offset: Int, limit: Int, sort: Sort) {
        require(offset >= 0) { "Offset index must not be less than zero" }
        require(limit >= 1) { "Limit must not be less than one" }
        this.limit = limit
        this.offset = offset
        this.sort = sort
    }

    constructor(offset: Int, limit: Int, direction: Sort.Direction, vararg properties: String) : this(
        offset,
        limit,
        Sort.by(direction, *properties)
    )

    constructor(offset: Int, limit: Int) : this(offset, limit, Sort.unsorted())

    override fun getPageNumber(): Int = offset / limit

    override fun getPageSize(): Int = limit

    override fun getOffset(): Long = offset.toLong()

    override fun getSort(): Sort = sort

    override fun next(): Pageable = OffsetBasedPageRequest(offset + pageSize, pageSize, sort)

    fun previous(): Pageable = OffsetBasedPageRequest(offset - pageSize, pageSize, sort)

    override fun previousOrFirst(): Pageable = if (!hasPrevious()) first() else previous()

    override fun first(): Pageable = OffsetBasedPageRequest(0, pageSize, sort)

    override fun withPage(pageNumber: Int): Pageable =
        OffsetBasedPageRequest((pageNumber - 1) * pageSize, pageSize, sort)

    override fun hasPrevious(): Boolean = offset > limit
}