package com.duynn.zahoo.presentation.base

/**
 *Created by duynn100198 on 10/04/21.
 */
/**
 * Immutable object which handle partial [MVIViewState] change
 * [reduce] is a function that take the current state and an [MVIIntent] as arguments,
and return a new [MVIViewState] result.
 * In other words, ([MVIViewState], [MVIIntent]) => new [MVIViewState] .
 */
interface MVIPartialStateChange<S : MVIViewState> {
    fun reduce(viewState: S): S
}
