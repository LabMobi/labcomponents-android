package mobi.lab.componentsdemo.infrastructure.common.http

interface ErrorTransformer {
    fun transform(error: Throwable): Throwable
}
