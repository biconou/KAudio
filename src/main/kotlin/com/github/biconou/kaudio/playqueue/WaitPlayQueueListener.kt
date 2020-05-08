package com.github.biconou.kaudio.playqueue

class WaitPlayQueueListener: PlayQueueListener {

    private val lock = Object()

    override fun endQueue() {
        synchronized(lock) {
            lock.notify()
        }
    }

    /**
     * Forces the current thread to wait until the playqueue is exhausted.
     */
    fun waitEndQueue() {
        synchronized(lock) {
            lock.wait()
        }
    }
}