package com.duynn.zahoo.data.scheduler

import com.duynn.zahoo.domain.scheduler.DispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 *Created by duynn100198 on 10/04/21.
 */
class IODispatcher : DispatchersProvider {
    override fun dispatcher(): CoroutineContext {
        return Dispatchers.IO
    }
}
