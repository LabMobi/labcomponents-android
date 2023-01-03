package mobi.lab.componentsdemo.common.eventbus

open class DataEvent<DATA>(val data: DATA) : Event() {
    override fun toString(): String {
        return "DataEvent(data=$data, retry=$error)"
    }
}
